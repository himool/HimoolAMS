from extensions.serializers import *


class HomeSummaryResponse(Serializer):
    total_quantity = IntegerField(label='资产总数')
    total_value = IntegerField(label='资产总值')
    idle_quantity = IntegerField(label='空闲数量')
    in_use_quantity = IntegerField(label='在用数量')
    borrow_quantity = IntegerField(label='借出数量')
    under_repair_quantity = IntegerField(label='维修数量')


class CategorySummaryResponse(Serializer):
    category = IntegerField(label='资产分类ID')
    category_number = CharField(label='资产分类编号')
    category_name = CharField(label='资产分类名称')
    total_quantity = IntegerField(label='资产数量')
    total_amount = FloatField(label='资产原值合计')


class LocationSummaryResponse(Serializer):
    location = IntegerField(label='存放地点ID')
    location_name = CharField(label='存放地点名称')
    total_quantity = IntegerField(label='资产数量')
    total_amount = FloatField(label='资产原值合计')


class DepartmentSummaryResponse(Serializer):
    department = CharField(label='使用部门')
    total_quantity = IntegerField(label='资产数量')
    total_amount = FloatField(label='资产原值合计')


class PersonSummaryResponse(Serializer):
    user = CharField(label='使用人')
    total_quantity = IntegerField(label='资产数量')
    total_amount = FloatField(label='资产原值合计')


class StatusSummaryResponse(Serializer):
    status = CharField(label='状态')
    total_quantity = IntegerField(label='资产数量')
    total_amount = FloatField(label='资产原值合计')


__all__ = [
    'HomeSummaryResponse', 'CategorySummaryResponse', 'LocationSummaryResponse', 'DepartmentSummaryResponse',
    'PersonSummaryResponse', 'StatusSummaryResponse',
]
