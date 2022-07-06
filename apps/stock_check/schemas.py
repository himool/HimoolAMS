from extensions.serializers import *
from rest_framework.serializers import ListField


class StockCheckValidate(Serializer):
    number_list = ListField(label='资产编号列表')


class StockCheckConfirm(Serializer):
    surplus_assets = ListField(label='盘盈资产')
    lose_assets = ListField(label='盘亏资产')


__all__ = [
    'StockCheckValidate', 'StockCheckConfirm',
]
