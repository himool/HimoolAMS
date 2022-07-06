from extensions.common.base import *
from extensions.serializers import *
from extensions.exceptions import *
from apps.asset.models import *
from apps.data.models import *


class AssetSerializer(BaseSerializer):

    class CategoryItemSerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name', 'origin', 'property_unit', 'unit',
                      'depreciation_life', 'useful_life']

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    category_item = CategoryItemSerializer(source='category', read_only=True, label='分类')
    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')
    person_item = PersonItemSerializer(source='person', read_only=True, label='人员')
    status_display = CharField(source='get_status_display', read_only=True, label='状态')

    class Meta:
        model = Asset
        read_only_fields = ['id', 'category_item', 'location', 'location_item', 'department',
                            'department_item', 'person', 'person_item', 'status', 'status_display',
                            'collect_date', 'collect_description', 'borrow_date', 'give_back_date',
                            'repair_date', 'repair_description']
        fields = ['number', 'name', 'barcode', 'category', 'spec', 'model', 'brand', 'original_value',
                  'enable_borrow', *read_only_fields]

    def validate_number(self, value):
        self.validate_unique({'number': value}, message='编号已存在')
        return value

    def validate_category(self, instance):
        instance = self.validate_foreign_key(Category, instance, message='分类不存在')
        return instance


class AssetImportExportSerializer(BaseSerializer):
    number = CharField(label='资产编号(必填唯一)', error_messages={'required': '资产编号不能为空'})
    name = CharField(label='资产名称(必填)', error_messages={'required': '资产名称不能为空'})
    barcode = CharField(required=False, default=None, label='资产条码')
    category = CharField(source='category.name', label='资产分类(必填)', error_messages={'required': '资产分类不能为空'})
    spec = CharField(required=False, default=None, label='规格')
    model = CharField(required=False, default=None, label='型号')
    brand = CharField(required=False, default=None, label='品牌')
    original_value = FloatField(label='资产原值(必填)', error_messages={'required': '资产原值不能为空', 'invalid': '资产原值输入错误'})
    enable_borrow = BooleanField(required=False, default=True, label='借用状态[TRUE/FALSE]', error_messages={'invalid': '借用状态输入错误'})

    class Meta:
        model = Asset
        fields = ['number', 'name', 'barcode', 'category', 'spec', 'model', 'brand',
                  'original_value', 'enable_borrow']


class AssetSummaeyExportSerializer(BaseSerializer):

    number = CharField(label='资产编号')
    name = CharField(label='资产名称')
    barcode = CharField(required=False, label='资产条码')
    category_name = CharField(source='category.name', label='资产分类')
    spec = CharField(required=False, label='规格')
    model = CharField(required=False, label='型号')
    brand = CharField(required=False, label='品牌')
    location_name = CharField(source='location.name', default=None, label='地点')
    department_name = CharField(source='department.name', default=None, label='部门')
    person_number = CharField(source='person.number', default=None, label='人员编号')
    person_name = CharField(source='person.name', default=None, label='人员名称')
    status_display = CharField(source='get_status_display', read_only=True, label='状态')

    class Meta:
        model = Asset
        fields = ['number', 'name', 'barcode', 'category_name', 'spec', 'model', 'brand',
                  'location_name', 'department_name', 'person_number', 'person_name', 'status_display']


class AssetFlowSerializer(BaseSerializer):

    class AssetItemSerializer(BaseSerializer):

        class Meta:
            model = Asset
            fields = ['id', 'number', 'name', 'barcode', 'spec', 'model', 'brand']

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    asset_item = AssetItemSerializer(source='asset', read_only=True, label='资产')
    type_display = CharField(source='get_type_display', read_only=True, label='类型')
    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')
    person_item = PersonItemSerializer(source='person', read_only=True, label='人员')

    class Meta:
        model = AssetFlow
        fields = ['asset', 'asset_item', 'type', 'type_display', 'location', 'location_item',
                  'department', 'department_item', 'person', 'person_item', 'create_time']


class AssetFlowExportSerializer(BaseSerializer):
    asset_number = CharField(source='asset.number', label='编号')
    asset_name = CharField(source='asset.name', label='名称')
    asset_barcode = CharField(source='asset.barcode', label='条码')
    asset_spec = CharField(source='asset.spec', label='规格')
    asset_model = CharField(source='asset.model', label='型号')
    asset_brand = CharField(source='asset.brand', label='品牌')
    location_name = CharField(source='location.name', default=None, label='地点')
    department_name = CharField(source='department.name', default=None, label='部门')
    person_number = CharField(source='person.number', default=None, label='人员编号')
    person_name = CharField(source='person.name', default=None, label='人员名称')
    type_display = CharField(source='get_type_display', label='类型')
    create_time = DateTimeField(label='记录时间')

    class Meta:
        model = AssetFlow
        fields = ['asset_number', 'asset_name', 'asset_barcode', 'asset_spec', 'asset_model',
                  'asset_brand', 'location_name', 'department_name', 'person_number', 'person_name',
                  'type_display', 'create_time']


class CollectSerializer(BaseSerializer):

    class CategoryItemSerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name', 'origin', 'property_unit', 'unit',
                      'depreciation_life', 'useful_life']

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    category_item = CategoryItemSerializer(source='category', read_only=True, label='分类')
    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')
    person_item = PersonItemSerializer(source='person', read_only=True, label='人员')
    status_display = CharField(source='get_status_display', read_only=True, label='状态')

    class Meta:
        model = Asset
        read_only_fields = ['id', 'number', 'name', 'barcode', 'category', 'category_item', 'location_item',
                            'department_item', 'person_item', 'spec', 'model', 'brand', 'original_value',
                            'status', 'status_display',  'enable_borrow']
        fields = ['location', 'department', 'person', 'collect_date', 'collect_description', *read_only_fields]

    def validate_location(self, instance):
        instance = self.validate_foreign_key(Location, instance, message='存放地点不存在')
        return instance

    def validate_department(self, instance):
        instance = self.validate_foreign_key(Department, instance, message='部门不存在')
        return instance

    def validate_person(self, instance):
        instance = self.validate_foreign_key(Person, instance, message='使用人不存在')
        return instance

    def validate(self, attrs):
        if person := attrs.get('person'):
            department = attrs['department']
            if person.department != department:
                raise ValidationError(f'人员[{person.number}]非部门[{department.name}]')
        return super().validate(attrs)


class BorrowSerializer(BaseSerializer):

    class CategoryItemSerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name', 'origin', 'property_unit', 'unit',
                      'depreciation_life', 'useful_life']

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    category_item = CategoryItemSerializer(source='category', read_only=True, label='分类')
    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')
    person_item = PersonItemSerializer(source='person', read_only=True, label='人员')
    status_display = CharField(source='get_status_display', read_only=True, label='状态')

    class Meta:
        model = Asset
        read_only_fields = ['id', 'number', 'name', 'barcode', 'category', 'category_item', 'location_item',
                            'department_item', 'person_item', 'spec', 'model', 'brand', 'original_value',
                            'status', 'status_display',  'enable_borrow']
        fields = ['department', 'person', 'borrow_date', 'give_back_date', *read_only_fields]

    def validate_department(self, instance):
        instance = self.validate_foreign_key(Department, instance, message='部门不存在')
        return instance

    def validate_person(self, instance):
        instance = self.validate_foreign_key(Person, instance, message='借用人不存在')
        return instance

    def validate(self, attrs):
        if person := attrs.get('person'):
            department = attrs['department']
            if person.department != department:
                raise ValidationError(f'人员[{person.number}]非部门[{department.name}]')
        return super().validate(attrs)


class TransferSerializer(BaseSerializer):

    class CategoryItemSerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name', 'origin', 'property_unit', 'unit',
                      'depreciation_life', 'useful_life']

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    category_item = CategoryItemSerializer(source='category', read_only=True, label='分类')
    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')
    person_item = PersonItemSerializer(source='person', read_only=True, label='人员')
    status_display = CharField(source='get_status_display', read_only=True, label='状态')

    class Meta:
        model = Asset
        read_only_fields = ['id', 'number', 'name', 'barcode', 'category', 'category_item', 'location_item',
                            'department_item', 'person_item', 'spec', 'model', 'brand', 'original_value',
                            'status', 'status_display',  'enable_borrow']
        fields = ['location', 'department', 'person', *read_only_fields]

    def validate_location(self, instance):
        instance = self.validate_foreign_key(Location, instance, message='存放地点不存在')
        return instance

    def validate_department(self, instance):
        instance = self.validate_foreign_key(Department, instance, message='部门不存在')
        return instance

    def validate_person(self, instance):
        instance = self.validate_foreign_key(Person, instance, message='使用人不存在')
        return instance

    def validate(self, attrs):
        if person := attrs.get('person'):
            department = attrs['department']
            if person.department != department:
                raise ValidationError(f'人员[{person.number}]非部门[{department.name}]')
        return super().validate(attrs)


class TransferImportExportSerializer(BaseSerializer):
    number = CharField(label='资产编号', error_messages={'required': '资产编号不能为空'})
    status_display = CharField(source='get_status_display', read_only=True, label='状态')
    location_old = CharField(source='location.name', read_only=True, label='原地点')
    department_old = CharField(source='department.name', read_only=True, label='原部门')
    person_old = CharField(source='person.number', read_only=True, label='原人员编号')
    location = CharField(source='location.name', write_only=True, required=False, label='新地点')
    department = CharField(source='department.name', write_only=True, required=False, label='新部门')
    person = CharField(source='person.number', write_only=True, required=False, label='新人员编号')

    class Meta:
        model = Asset
        fields = ['number', 'status_display', 'location_old', 'department_old', 'person_old', 'location', 'department', 'person']


class RepairSerializer(BaseSerializer):

    class CategoryItemSerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name', 'origin', 'property_unit', 'unit',
                      'depreciation_life', 'useful_life']

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    category_item = CategoryItemSerializer(source='category', read_only=True, label='分类')
    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')
    person_item = PersonItemSerializer(source='person', read_only=True, label='人员')
    status_display = CharField(source='get_status_display', read_only=True, label='状态')

    class Meta:
        model = Asset
        read_only_fields = ['id', 'number', 'name', 'barcode', 'category', 'category_item', 'location_item',
                            'department_item', 'person_item', 'spec', 'model', 'brand', 'original_value',
                            'status', 'status_display',  'enable_borrow']
        fields = ['repair_date', 'repair_description', *read_only_fields]


__all__ = [
    'AssetSerializer', 'AssetImportExportSerializer', 'AssetSummaeyExportSerializer',
    'AssetFlowSerializer', 'AssetFlowExportSerializer',
    'CollectSerializer', 'BorrowSerializer', 'TransferSerializer',
    'TransferImportExportSerializer', 'RepairSerializer',
]
