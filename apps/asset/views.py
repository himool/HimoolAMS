from apps.data.models import Category, Department, Location, Person
from extensions.common.schema import *
from extensions.common.base import *
from extensions.permissions import *
from extensions.exceptions import *
from extensions.viewsets import *
from apps.asset.serializers import *
from apps.asset.permissions import *
from apps.asset.filters import *
from apps.asset.schemas import *
from apps.asset.models import *
import re


class AssetViewSet(ModelViewSet, ExportMixin, ImportMixin):
    """资产"""

    serializer_class = AssetSerializer
    permission_classes = [IsAuthenticated]
    filterset_class = AssetFilter
    search_fields = ['number', 'name', 'barcode']
    ordering_fields = ['id', 'number', 'name']
    select_related_fields = ['category', 'location', 'department', 'person']
    queryset = Asset.objects.all()

    @transaction.atomic
    def perform_create(self, serializer):
        super().perform_create(serializer)
        AssetFlow.objects.create(asset=serializer.instance, type=AssetFlow.Type.CREATE, team=self.team)

    @extend_schema(responses={200: DownloadResponse})
    @action(detail=False, methods=['get'])
    def export(self, request, *args, **kwargs):
        """导出"""

        return self.get_export_response(AssetImportExportSerializer)

    @extend_schema(responses={200: DownloadResponse})
    @action(detail=False, methods=['get'])
    def summary_export(self, request, *args, **kwargs):
        """汇总导出"""

        return self.get_export_response(AssetSummaeyExportSerializer)

    @extend_schema(responses={200: DownloadResponse})
    @action(detail=False, methods=['get'])
    def import_template(self, request, *args, **kwargs):
        """导入模板"""

        return self.get_template_response(AssetImportExportSerializer)

    @extend_schema(request=UploadRequest, responses={204: None})
    @action(detail=False, methods=['post'])
    @transaction.atomic
    def import_data(self, request, *args, **kwargs):
        """导入数据"""

        request_serializer = UploadRequest(data=request.data)
        request_serializer.is_valid(raise_exception=True)
        validated_data = request_serializer.validated_data

        import_serializer = self.load_data(validated_data['file'], AssetImportExportSerializer)
        if not import_serializer.is_valid(raise_exception=False):
            message_list = []
            for index, error in enumerate(import_serializer.errors, 2):
                if error:
                    result = re.match("^{.*string='(.*?)'.*}$", str(error))
                    message_list.append(f'数据第{index}行错误, {result[1]}')
            raise ValidationError('\n'.join(message_list))

        asset_items = import_serializer.validated_data

        category_names = {item['category']['name'] for item in asset_items if 'category' in item}
        category_set = Category.objects.filter(name__in=category_names, team=self.team)

        asset_numbers = {item['number'] for item in asset_items}
        asset_set = Asset.objects.filter(number__in=asset_numbers, team=self.team)

        create_asset_set = []
        update_asset_set = []
        for asset_item in asset_items:
            asset_item['team'] = self.team

            if 'category' in asset_item:
                category_item = asset_item.pop('category')
                for category in category_set:
                    if category.name == category_item['name']:
                        asset_item['category'] = category
                        break
                else:
                    raise ValidationError(f'分类缺失[{category_item["name"]}]')

            for asset in asset_set:

                if asset.number == asset_item['number']:
                    update_asset_set.append(asset)
                    print(asset_item.items())
                    for key, value in asset_item.items():
                        setattr(asset, key, value)
                    break
            else:
                create_asset_set.append(Asset(**asset_item))

        try:
            Asset.objects.bulk_create(create_asset_set)
            Asset.objects.bulk_update(update_asset_set, AssetImportExportSerializer.Meta.fields)
        except IntegrityError as e:
            raise ValidationError('编号重复') from e

        return Response(status=status.HTTP_204_NO_CONTENT)

    @extend_schema(request=CollectSerializer, responses={200: CollectSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def collect(self, request, *args, **kwargs):
        """领用"""

        instance = self.get_object()
        if instance.status != Asset.Status.IDLE:
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法领用')

        serializer = CollectSerializer(instance=instance, data=request.data, context=self.context)
        serializer.is_valid(raise_exception=True)
        serializer.save(status=Asset.Status.IN_USE)

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.COLLECT, location=instance.location,
                                 department=instance.department, person=instance.person, team=self.team)

        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(responses={200: CollectSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def return_stock(self, request, *args, **kwargs):
        """退库"""

        instance = self.get_object()
        if instance.status != Asset.Status.IN_USE:
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法退库')

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.RETURN_STOCK, location=instance.location,
                                 department=instance.department, person=instance.person, team=self.team)

        instance.status = Asset.Status.IDLE
        instance.location = None
        instance.department = None
        instance.person = None
        instance.collect_date = None
        instance.collect_description = None

        instance.save(update_fields=['status', 'location', 'department', 'person', 'collect_date',
                                     'collect_description'])

        serializer = CollectSerializer(instance=instance, context=self.context)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(request=BorrowSerializer, responses={200: BorrowSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def borrow(self, request, *args, **kwargs):
        """借用"""

        instance = self.get_object()
        if not instance.enable_borrow:
            raise ValidationError('资产未开启借用功能')

        if instance.status != Asset.Status.IDLE:
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法借用')

        serializer = BorrowSerializer(instance=instance, data=request.data, context=self.context)
        serializer.is_valid(raise_exception=True)
        serializer.save(status=Asset.Status.BORROWING)

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.BORROW, department=instance.department,
                                 person=instance.person, team=self.team)

        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(responses={200: BorrowSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def give_back(self, request, *args, **kwargs):
        """归还"""

        instance = self.get_object()
        if instance.status != Asset.Status.BORROWING:
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法归还')

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.GIVE_BACK,
                                 department=instance.department, person=instance.person, team=self.team)

        instance.department = None
        instance.person = None
        instance.borrow_date = None
        instance.give_back_date = None
        instance.status = Asset.Status.IDLE
        instance.save(update_fields=['department', 'person', 'borrow_date', 'give_back_date', 'status'])

        serializer = BorrowSerializer(instance=instance, context=self.context)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(request=TransferSerializer, responses={200: TransferSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def transfer(self, request, *args, **kwargs):
        """调拨"""

        instance = self.get_object()
        if instance.status not in (Asset.Status.IN_USE, Asset.Status.BORROWING):
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法调拨')

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.TRANSFER_OUT, location=instance.location,
                                 department=instance.department, person=instance.person, team=self.team)

        serializer = TransferSerializer(instance=instance, data=request.data, context=self.context)
        serializer.is_valid(raise_exception=True)

        validated_data = serializer.validated_data
        if all((instance.department == validated_data['department'],
               instance.person == validated_data.get('person'),
               instance.location == validated_data.get('location'))):
            raise ValidationError('调拨使用人和存放地点一致, 无法调拨')

        serializer.save()

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.TRANSFER_IN, location=instance.location,
                                 department=instance.department, person=instance.person, team=self.team)

        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(request=RepairSerializer, responses={200: RepairSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def need_repair(self, request, *args, **kwargs):
        """需要维修"""

        instance = self.get_object()
        if instance.status not in (Asset.Status.IDLE, Asset.Status.IN_USE, Asset.Status.BORROWING):
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法维修')

        serializer = RepairSerializer(instance=instance, data=request.data, context=self.context)
        serializer.is_valid(raise_exception=True)
        serializer.save(status=Asset.Status.NEED_REPAIR)

        AssetFlow.objects.create(asset=instance, location=instance.location, department=instance.department,
                                 person=instance.person, type=AssetFlow.Type.NEED_REPAIR, team=self.team)

        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(responses={200: RepairSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def under_repair(self, request, *args, **kwargs):
        """维修中"""

        instance = self.get_object()
        if instance.status != Asset.Status.NEED_REPAIR:
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法处理')

        instance.status = Asset.Status.UNDER_REPAIR
        instance.location = None
        instance.department = None
        instance.person = None
        instance.collect_date = None
        instance.collect_description = None
        instance.borrow_date = None
        instance.give_back_date = None
        instance.save(update_fields=['status', 'location', 'department', 'person', 'collect_date',
                                     'collect_description', 'borrow_date', 'give_back_date'])

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.UNDER_REPAIR, team=self.team)

        serializer = RepairSerializer(instance=instance, context=self.context)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(responses={200: RepairSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def fixed(self, request, *args, **kwargs):
        """已修复"""

        instance = self.get_object()
        if instance.status not in (Asset.Status.NEED_REPAIR, Asset.Status.UNDER_REPAIR):
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法修复')

        instance.status = Asset.Status.IDLE
        instance.repair_date = None
        instance.repair_description = None
        instance.location = None
        instance.department = None
        instance.person = None
        instance.collect_date = None
        instance.collect_description = None
        instance.borrow_date = None
        instance.give_back_date = None
        instance.save(update_fields=['status', 'repair_date', 'repair_description', 'location',
                                     'department', 'person', 'collect_date', 'collect_description',
                                     'borrow_date', 'give_back_date'])

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.FIXED, team=self.team)

        serializer = RepairSerializer(instance=instance, context=self.context)
        return Response(data=serializer.data, status=status.HTTP_200_OK)

    @extend_schema(responses={200: RepairSerializer})
    @action(detail=True, methods=['post'])
    @transaction.atomic
    def scrapped(self, request, *args, **kwargs):
        """已报废"""

        instance = self.get_object()
        if instance.status not in (Asset.Status.NEED_REPAIR, Asset.Status.UNDER_REPAIR):
            raise ValidationError(f'资产[{instance.get_status_display()}], 无法报废')

        instance.status = Asset.Status.SCRAPPED
        instance.location = None
        instance.department = None
        instance.person = None
        instance.collect_date = None
        instance.collect_description = None
        instance.borrow_date = None
        instance.give_back_date = None
        instance.save(update_fields=['status', 'location', 'department', 'person', 'collect_date',
                                     'collect_description', 'borrow_date', 'give_back_date'])

        AssetFlow.objects.create(asset=instance, type=AssetFlow.Type.SCRAPPED, team=self.team)

        serializer = RepairSerializer(instance=instance, context=self.context)
        return Response(data=serializer.data, status=status.HTTP_200_OK)


class AssetTransferViewSet(BaseViewSet, ImportMixin, ExportMixin):
    """资产调拨"""

    serializer_class = AssetSerializer
    permission_classes = [IsAuthenticated]
    filterset_class = AssetFilter
    search_fields = ['number', 'name', 'barcode']
    ordering_fields = ['id', 'number', 'name']
    select_related_fields = ['category', 'location', 'department', 'person']
    queryset = Asset.objects.all()

    def get_queryset(self):
        return super().get_queryset().filter(status__in=(Asset.Status.IN_USE, Asset.Status.BORROWING))

    @extend_schema(responses={200: DownloadResponse})
    @action(detail=False, methods=['get'])
    def export(self, request, *args, **kwargs):
        """导出"""

        return self.get_export_response(TransferImportExportSerializer)

    @extend_schema(request=UploadRequest, responses={204: None})
    @action(detail=False, methods=['post'])
    @transaction.atomic
    def import_data(self, request, *args, **kwargs):
        """导入数据"""

        request_serializer = UploadRequest(data=request.data)
        request_serializer.is_valid(raise_exception=True)
        validated_data = request_serializer.validated_data

        import_serializer = self.load_data(validated_data['file'], TransferImportExportSerializer)
        if not import_serializer.is_valid(raise_exception=False):
            message_list = []
            for index, error in enumerate(import_serializer.errors, 2):
                if error:
                    result = re.match("^{.*string='(.*?)'.*}$", str(error))
                    message_list.append(f'数据第{index}行错误, {result[1]}')
            raise ValidationError('\n'.join(message_list))

        import_items = import_serializer.validated_data

        location_names = {item['location']['name'] for item in import_items if 'location' in item}
        location_set = Location.objects.filter(name__in=location_names, team=self.team)

        department_names = {item['department']['name'] for item in import_items if 'department' in item}
        department_set = Department.objects.filter(name__in=department_names, team=self.team)

        person_numbers = {item['person']['number'] for item in import_items if 'person' in item}
        person_set = Person.objects.filter(number__in=person_numbers, team=self.team)

        asset_numbers = {item['number'] for item in import_items}
        asset_set = Asset.objects.filter(number__in=asset_numbers, team=self.team)

        update_asset_set = []
        create_asset_flow_set = []
        for index, item in enumerate(import_items, 2):
            location = None
            department = None
            person = None

            for asset in asset_set:
                if asset.number == item['number']:
                    update_asset_set.append(asset)
                    break
            else:
                raise ValidationError(f'第{index}行, 资产缺失[{item["number"]}]')

            if asset.status == Asset.Status.IN_USE:
                if 'location' not in item:
                    raise ValidationError(f'第{index}行, 资产[{asset.number}]调拨地点缺失')

                if 'department' not in item:
                    raise ValidationError(f'第{index}行, 资产[{asset.number}]调拨部门缺失')
            elif asset.status == Asset.Status.BORROWING:
                if 'department' not in item:
                    raise ValidationError(f'第{index}行, 资产[{asset.number}]调拨部门缺失')

                if 'person' not in item:
                    raise ValidationError(f'第{index}行, 资产[{asset.number}]调拨人员缺失')

            create_asset_flow_set.append(AssetFlow(
                asset=asset, type=AssetFlow.Type.TRANSFER_OUT, location=asset.location,
                department=asset.department, person=asset.person, team=self.team))

            if asset.status == Asset.Status.IN_USE and 'location' in item:
                location_item = item.pop('location')
                for location in location_set:
                    if location.name == location_item['name']:
                        break
                else:
                    raise ValidationError(f'第{index}行, 地点缺失[{location_item["name"]}]')

            department_item = item.pop('department')
            for department in department_set:
                if department.name == department_item['name']:
                    break
            else:
                raise ValidationError(f'第{index}行, 部门缺失[{department_item["name"]}]')

            if 'person' in item:
                person_item = item.pop('person')
                for person in person_set:
                    if person.number == person_item['number']:
                        if person.department != department:
                            raise ValidationError(f'第{index}行, 人员[{person.number}]非部门[{department.name}]')
                        break
                else:
                    raise ValidationError(f'第{index}行, 人员缺失[{person_item["number"]}]')

            if all((asset.department == department, asset.person == person, asset.location == location)):
                continue

            asset.department = department
            asset.person = person
            asset.location = location

            create_asset_flow_set.append(AssetFlow(
                asset=asset, type=AssetFlow.Type.TRANSFER_IN, location=asset.location,
                department=asset.department, person=asset.person, team=self.team))

        Asset.objects.bulk_update(update_asset_set, ['location', 'department', 'person'])
        AssetFlow.objects.bulk_create(create_asset_flow_set)

        return Response(status=status.HTTP_204_NO_CONTENT)


class AssetFlowViewSet(ReadOnlyViewSet, ExportMixin):
    """资产流水"""

    serializer_class = AssetFlowSerializer
    permission_classes = [IsAuthenticated]
    filterset_class = AssetFlowFilter
    search_fields = ['asset__number', 'asset__name', 'asset__barcode']
    ordering_fields = ['id', 'create_time']
    select_related_fields = ['asset', 'location', 'department', 'person']
    queryset = AssetFlow.objects.all()

    @extend_schema(responses={200: DownloadResponse})
    @action(detail=False, methods=['get'])
    def export(self, request, *args, **kwargs):
        """导出"""

        return self.get_export_response(AssetFlowExportSerializer)


__all__ = [
    'AssetViewSet', 'AssetTransferViewSet', 'AssetFlowViewSet',
]
