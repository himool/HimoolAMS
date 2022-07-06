from django_filters.rest_framework import FilterSet
from django_filters.filters import *
from apps.asset.models import *
from django.db.models import Q


class AssetFilter(FilterSet):
    fuzzy_category = NumberFilter(method='filter_fuzzy_category', label='模糊分类')
    fuzzy_location = NumberFilter(method='filter_fuzzy_location', label='模糊地点')
    status = MultipleChoiceFilter(choices=Asset.Status.choices, label='等级')

    class Meta:
        model = Asset
        fields = ['fuzzy_category', 'category', 'enable_borrow', 'status', 'fuzzy_location',
                  'location', 'department', 'person']

    def filter_fuzzy_category(self, queryset, name, value):
        if value:
            queryset = queryset.filter(Q(category=value) | Q(category__parent=value))
        return queryset

    def filter_fuzzy_location(self, queryset, name, value):
        if value:
            queryset = queryset.filter(Q(location=value) | Q(location__parent=value))
        return queryset


class AssetFlowFilter(FilterSet):
    category = NumberFilter(field_name='asset__category', label='分类')
    fuzzy_category = NumberFilter(method='filter_fuzzy_category', label='模糊分类')
    fuzzy_location = NumberFilter(method='filter_fuzzy_location', label='模糊地点')
    type = MultipleChoiceFilter(choices=AssetFlow.Type.choices, label='类型')
    start_date = DateFilter(field_name='create_time', lookup_expr='gte', label='开始日期')
    end_date = DateFilter(field_name='create_time', lookup_expr='lt', label='结束日期')

    class Meta:
        model = AssetFlow
        fields = ['asset', 'category', 'fuzzy_category', 'location', 'fuzzy_location',
                  'department', 'person', 'type', 'start_date', 'end_date']

    def filter_fuzzy_category(self, queryset, name, value):
        if value:
            queryset = queryset.filter(Q(asset__category=value) | Q(asset__category__parent=value))
        return queryset

    def filter_fuzzy_location(self, queryset, name, value):
        if value:
            queryset = queryset.filter(Q(location=value) | Q(location__parent=value))
        return queryset


__all__ = [
    'AssetFilter', 'AssetFlowFilter',
]
