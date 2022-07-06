from extensions.common.schema import *
from extensions.common.base import *
from extensions.permissions import *
from extensions.exceptions import *
from extensions.viewsets import *
from apps.option.serializers import *
from apps.data.models import *


class DepartmentOptionViewSet(InfiniteOptionViewSet):
    serializer_class = DepartmentOptionSerializer
    search_fields = ['number', 'name']
    queryset = Department.objects.all()


class PersonOptionViewSet(InfiniteOptionViewSet):
    serializer_class = PersonOptionSerializer
    filterset_fields = ['department']
    search_fields = ['number', 'name']
    queryset = Person.objects.all()


class CategoryOptionViewSet(InfiniteOptionViewSet):
    serializer_class = CategoryOptionSerializer
    search_fields = ['number', 'name']
    prefetch_related_fields = ['sub_category_set']
    queryset = Category.objects.filter(parent__isnull=True)


class LocationOptionViewSet(InfiniteOptionViewSet):
    serializer_class = LocationOptionSerializer
    search_fields = ['name']
    prefetch_related_fields = ['sub_location_set']
    queryset = Location.objects.filter(parent__isnull=True)


__all__ = [
    'DepartmentOptionViewSet', 'PersonOptionViewSet', 'CategoryOptionViewSet', 'LocationOptionViewSet',
]
