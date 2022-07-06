from extensions.common.base import *
from extensions.serializers import *
from extensions.exceptions import *
from apps.data.models import *
from apps.system.models import *


class DepartmentSerializer(BaseSerializer):

    class Meta:
        model = Department
        read_only_fields = ['id']
        fields = ['number', 'name', 'remark', *read_only_fields]

    def validate_number(self, value):
        self.validate_unique({'number': value}, message='编号已存在')
        return value

    def validate_name(self, value):
        self.validate_unique({'name': value}, message='名称已存在')
        return value


class PersonSerializer(BaseSerializer):

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    department_item = DepartmentItemSerializer(source='department', read_only=True, label='部门')

    class Meta:
        model = Person
        read_only_fields = ['id', 'department_item']
        fields = ['number', 'name', 'department', 'remark', *read_only_fields]

    def validate_number(self, value):
        self.validate_unique({'number': value}, message='编号已存在')
        return value

    def validate_department(self, instance):
        instance = self.validate_foreign_key(Department, instance, message='部门不存在')
        return instance

    @transaction.atomic
    def update(self, instance, validated_data):
        if instance.department != validated_data['department']:
            instance.asset_set.all().update(department=validated_data['department'])
            instance.asset_flow_set.all().update(department=validated_data['department'])
        return super().update(instance, validated_data)


class CategorySerializer(BaseSerializer):

    class SubCategorySerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name', 'parent', 'origin', 'property_unit', 'unit',
                      'depreciation_life', 'useful_life', 'description']

    sub_category_items = SubCategorySerializer(source='sub_category_set', many=True, read_only=True, label='子类')

    class Meta:
        model = Category
        read_only_fields = ['id', 'sub_category_items']
        fields = ['number', 'name', 'parent', 'origin', 'property_unit', 'unit',
                  'depreciation_life', 'useful_life', 'description', *read_only_fields]

    def validate_number(self, value):
        self.validate_unique({'number': value}, message='编号已存在')
        return value

    def validate_name(self, value):
        self.validate_unique({'name': value}, message='名称已存在')
        return value

    def validate_parent(self, instance):
        instance = self.validate_foreign_key(Category, instance, message='一级分类不存在')
        return instance


class SubLocationSerializer(BaseSerializer):

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    department = IntegerField(source='manager.department.id', read_only=True, label='部门')
    department_item = DepartmentItemSerializer(source='manager.department', read_only=True, label='部门Item')
    manager_item = PersonItemSerializer(source='manager', read_only=True, label='负责人Item')

    class Meta:
        model = Location
        fields = ['id', 'name', 'parent', 'department', 'department_item', 'manager',
                  'manager_item', 'description']


class LocationSerializer(BaseSerializer):

    class DepartmentItemSerializer(BaseSerializer):

        class Meta:
            model = Department
            fields = ['id', 'number', 'name']

    class PersonItemSerializer(BaseSerializer):

        class Meta:
            model = Person
            fields = ['id', 'number', 'name']

    department = IntegerField(source='manager.department.id', read_only=True, label='部门')
    department_item = DepartmentItemSerializer(source='manager.department', read_only=True, label='部门Item')
    manager_item = PersonItemSerializer(source='manager', read_only=True, label='负责人Item')
    sub_location_items = SubLocationSerializer(source='sub_location_set', many=True, read_only=True, label='子类Items')

    class Meta:
        model = Location
        read_only_fields = ['id', 'department', 'department_item', 'manager_item', 'sub_location_items']
        fields = ['name', 'parent', 'manager', 'description', *read_only_fields]

    def validate_name(self, value):
        self.validate_unique({'name': value}, message='名称已存在')
        return value

    def validate_parent(self, instance):
        instance = self.validate_foreign_key(Location, instance, message='一级地点不存在')
        return instance

    def validate_manager(self, instance):
        instance = self.validate_foreign_key(Person, instance, message='负责人不存在')
        return instance


__all__ = [
    'DepartmentSerializer', 'PersonSerializer', 'CategorySerializer', 'LocationSerializer',
]
