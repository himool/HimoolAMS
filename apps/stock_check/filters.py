from django_filters.rest_framework import FilterSet
from django_filters.filters import *
from apps.stock_check.models import *
from django.db.models import Q


class StockCheckRecordFilter(FilterSet):
    fuzzy_location = NumberFilter(method='filter_fuzzy_location', label='模糊地点')
    start_date = DateFilter(field_name='create_time', lookup_expr='gte', label='开始日期')
    end_date = DateFilter(field_name='create_time', lookup_expr='lt', label='结束日期')

    class Meta:
        model = StockCheckRecord
        fields = ['location', 'fuzzy_location', 'start_date', 'end_date']

    def filter_fuzzy_location(self, queryset, name, value):
        if value:
            queryset = queryset.filter(Q(location=value) | Q(location__parent=value))
        return queryset


__all__ = [
    'StockCheckRecordFilter',
]
