from apps.asset.models import Asset, AssetFlow
from extensions.common.schema import *
from extensions.common.base import *
from extensions.permissions import *
from extensions.exceptions import *
from extensions.viewsets import *
from apps.stock_check.serializers import *
from apps.stock_check.permissions import *
from apps.stock_check.filters import *
from apps.stock_check.schemas import *
from apps.stock_check.models import *
from apps.data.models import *


class StockCheckViewSet(BaseViewSet):
    """盘点"""

    permission_classes = [IsAuthenticated]
    queryset = Location.objects.all()

    @extend_schema(request=StockCheckValidate)
    @action(detail=True, methods=['post'])
    def validate(self, request, *args, **kwargs):
        serializer = StockCheckValidate(data=request.data)
        serializer.is_valid(raise_exception=True)
        number_list = serializer.validated_data['number_list']

        location = self.get_object()
        check_assets = Asset.objects.filter(team=self.team, number__in=number_list,
                                            status__in=(Asset.Status.IN_USE, Asset.Status.LOSE))
        local_assets = location.asset_set.filter(status=Asset.Status.IN_USE)

        local_asset_set = set(local_assets)
        check_asset_set = set(check_assets)
        lose_assets_set = local_asset_set - check_asset_set
        surplus_asset_set = check_asset_set - local_asset_set

        data = {
            'surplus_assets': [{
                'id': asset.id,
                'number': asset.number,
                'name': asset.name,
                'location': asset.location.id if asset.location else None,
                'location_name': asset.location.name if asset.location else '',
            } for asset in surplus_asset_set],
            'lose_assets': [{
                'id': asset.id,
                'number': asset.number,
                'name': asset.name,
                'location': asset.location.id if asset.location else None,
                'location_name': asset.location.name if asset.location else '',
            } for asset in lose_assets_set], }
        return Response(data=data, status=status.HTTP_200_OK)

    @extend_schema(request=StockCheckConfirm, responses={204: None})
    @transaction.atomic
    @action(detail=True, methods=['post'])
    def confirm(self, request, *args, **kwargs):
        serializer = StockCheckConfirm(data=request.data)
        serializer.is_valid(raise_exception=True)
        surplus_assets = serializer.validated_data.get('surplus_assets', [])
        lose_assets = serializer.validated_data.get('lose_assets', [])

        location = self.get_object()
        manager = location.manager
        surplus_assets = Asset.objects.filter(team=self.team, id__in=surplus_assets,
                                              status__in=(Asset.Status.IN_USE, Asset.Status.LOSE))
        lose_assets = Asset.objects.filter(team=self.team, id__in=lose_assets,
                                           status__in=(Asset.Status.IN_USE, Asset.Status.LOSE))
        normal_assets = location.asset_set.filter(status=Asset.Status.IN_USE) \
            .exclude(id__in=[*[asset.id for asset in surplus_assets], *[asset.id for asset in lose_assets]])

        stock_check_record = StockCheckRecord.objects.create(location=location, team=self.team)
        stock_check_assets = [StockCheckAsset(stock_check_record=stock_check_record, asset=asset,
                                              status=StockCheckAsset.Status.NORMAL, team=self.team)
                              for asset in normal_assets]

        surplus_asset_flow_set = []
        for surplus_asset in surplus_assets:
            stock_check_assets.append(StockCheckAsset(team=self.team, stock_check_record=stock_check_record,
                                                      asset=surplus_asset, status=StockCheckAsset.Status.SURPLUS))
            surplus_asset_flow_set.append(AssetFlow(
                asset=surplus_asset, type=AssetFlow.Type.SURPLUS, location=location,
                department=manager.department, person=manager, team=self.team))
        surplus_assets.update(location=location, department=manager.department,
                              person=manager, status=Asset.Status.IN_USE)

        lose_asset_flow_set = []
        for lose_asset in lose_assets:
            stock_check_assets.append(StockCheckAsset(team=self.team, stock_check_record=stock_check_record,
                                                      asset=lose_asset, status=StockCheckAsset.Status.LOSE))
            lose_asset_flow_set.append(AssetFlow(
                asset=lose_asset, type=AssetFlow.Type.LOSE, location=location,
                department=lose_asset.department, person=lose_asset.person, team=self.team))
        lose_assets.update(location=None, department=None, person=None, status=Asset.Status.LOSE)

        StockCheckAsset.objects.bulk_create(stock_check_assets)
        AssetFlow.objects.bulk_create([*surplus_asset_flow_set, *lose_asset_flow_set])
        return Response(status=status.HTTP_204_NO_CONTENT)


class StockCheckRecordViewSet(ReadOnlyViewSet):
    """盘点记录"""

    serializer_class = StockCheckRecordSerializer
    permission_classes = [IsAuthenticated]
    filterset_class = StockCheckRecordFilter
    select_related_fields = ['location']
    prefetch_related_fields = ['stock_check_assets', 'stock_check_assets__asset']
    queryset = StockCheckRecord.objects.all()


__all__ = [
    'StockCheckViewSet', 'StockCheckRecordViewSet',
]
