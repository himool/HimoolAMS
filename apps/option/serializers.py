from extensions.common.base import *
from extensions.serializers import *
from extensions.exceptions import *
from apps.data.models import *


class DepartmentOptionSerializer(ModelSerializer):

    class Meta:
        model = Department
        fields = ['id', 'number', 'name']


class PersonOptionSerializer(ModelSerializer):

    class Meta:
        model = Person
        fields = ['id', 'number', 'name', 'department']


class CategoryOptionSerializer(ModelSerializer):

    class SubCategorySerializer(BaseSerializer):

        class Meta:
            model = Category
            fields = ['id', 'number', 'name']

    sub_category_items = SubCategorySerializer(source='sub_category_set', many=True, read_only=True, label='子类')

    class Meta:
        model = Category
        fields = ['id', 'number', 'name', 'sub_category_items']


class LocationOptionSerializer(ModelSerializer):

    class SubLocationSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    sub_location_items = SubLocationSerializer(source='sub_location_set', many=True, read_only=True, label='子类')

    class Meta:
        model = Location
        fields = ['id', 'name', 'sub_location_items']


__all__ = [
    'DepartmentOptionSerializer', 'PersonOptionSerializer', 'CategoryOptionSerializer', 'LocationOptionSerializer',
]
