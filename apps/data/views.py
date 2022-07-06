from extensions.common.schema import *
from extensions.common.base import *
from extensions.paginations import *
from extensions.permissions import *
from extensions.exceptions import *
from extensions.viewsets import *
from apps.data.serializers import *
from apps.data.permissions import *
from apps.data.filters import *
from apps.data.schemas import *
from apps.data.models import *


class DepartmentViewSet(ModelViewSet):
    """部门"""

    serializer_class = DepartmentSerializer
    permission_classes = [IsAuthenticated]
    search_fields = ['number', 'name']
    ordering_fields = ['id', 'number', 'name']
    queryset = Department.objects.all()


class PersonViewSet(ModelViewSet):
    """人员"""

    serializer_class = PersonSerializer
    permission_classes = [IsAuthenticated]
    filterset_fields = ['department']
    search_fields = ['number', 'name']
    ordering_fields = ['id', 'number', 'name']
    select_related_fields = ['department']
    queryset = Person.objects.all()


class CategoryViewSet(ModelViewSet):
    """资产分类"""

    serializer_class = CategorySerializer
    permission_classes = [IsAuthenticated]
    search_fields = ['number', 'name', 'description']
    ordering_fields = ['id', 'number', 'name']
    prefetch_related_fields = ['sub_category_set']
    queryset = Category.objects.all()

    def get_queryset(self):
        queryset = super().get_queryset()
        if self.request.method == 'GET':
            queryset = queryset.filter(parent__isnull=True)
        return queryset


class LocationViewSet(ModelViewSet):
    """存放地点"""

    serializer_class = LocationSerializer
    permission_classes = [IsAuthenticated]
    search_fields = ['name', 'description']
    ordering_fields = ['id', 'name']
    prefetch_related_fields = ['sub_location_set']
    queryset = Location.objects.all()

    def get_queryset(self):
        queryset = super().get_queryset()
        if self.request.method == 'GET':
            queryset = queryset.filter(parent__isnull=True)
        return queryset


__all__ = [
    'DepartmentViewSet', 'PersonViewSet', 'CategoryViewSet', 'LocationViewSet',
]
