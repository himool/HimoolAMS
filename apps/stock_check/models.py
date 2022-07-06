from extensions.models import *


class StockCheckRecord(Model):
    """盘点记录"""

    location = ForeignKey('data.Location', on_delete=CASCADE, related_name='stock_check_records', verbose_name='地点')
    create_time = DateTimeField(auto_now_add=True, verbose_name='创建时间')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='stock_check_records')


class StockCheckAsset(Model):
    """盘点资产"""

    class Status(TextChoices):
        """状态"""

        SURPLUS = ('surplus', '盘盈')
        NORMAL = ('normal', '正常')
        LOSE = ('lose', '丢失')

    stock_check_record = ForeignKey('stock_check.StockCheckRecord', on_delete=CASCADE, related_name='stock_check_assets', verbose_name='盘点记录')
    asset = ForeignKey('asset.Asset', on_delete=CASCADE, related_name='stock_check_assets', verbose_name='资产')
    status = CharField(max_length=32, choices=Status.choices, verbose_name='状态')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='stock_check_assets')


__all__ = [
    'StockCheckRecord', 'StockCheckAsset',
]
