from extensions.models import *


class Asset(Model):
    """资产"""

    class Status(TextChoices):
        """状态"""

        IDLE = ('idle', '闲置')
        IN_USE = ('in_use', '在用')
        BORROWING = ('borrowing', '借用中')
        NEED_REPAIR = ('need_repair', '需要维修')
        UNDER_REPAIR = ('under_repair', '维修中')
        SCRAPPED = ('scrapped', '已报废')
        LOSE = ('lose', '丢失')

    number = CharField(max_length=32, verbose_name='rfid编号')
    name = CharField(max_length=256, verbose_name='名称')
    barcode = CharField(max_length=32, blank=True, null=True, verbose_name='资产条码')
    category = ForeignKey('data.Category', on_delete=PROTECT,
                          related_name='asset_set', verbose_name='资产分类')
    spec = CharField(max_length=32, blank=True, null=True, verbose_name='规格')
    model = CharField(max_length=32, blank=True, null=True, verbose_name='型号')
    brand = CharField(max_length=32, blank=True, null=True, verbose_name='品牌')
    original_value = FloatField(verbose_name='资产原值')
    enable_borrow = BooleanField(default=True, verbose_name='开启借用状态')
    status = CharField(max_length=32, choices=Status.choices, default=Status.IDLE, verbose_name='状态')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='asset_set')

    location = ForeignKey('data.Location', on_delete=PROTECT, null=True,
                          related_name='asset_set', verbose_name='存放地点')
    department = ForeignKey('data.Department', on_delete=PROTECT, null=True,
                            related_name='asset_set', verbose_name='部门')
    person = ForeignKey('data.Person', on_delete=PROTECT, null=True,
                        related_name='asset_set', verbose_name='人员')
    collect_date = DateField(null=True, verbose_name='领用日期')
    collect_description = CharField(max_length=256, blank=True, null=True, verbose_name='领用描述')
    borrow_date = DateField(null=True, verbose_name='借用日期')
    give_back_date = DateField(null=True, verbose_name='归还日期')
    repair_date = DateField(null=True, verbose_name='维修日期')
    repair_description = CharField(max_length=256, blank=True, null=True, verbose_name='维修说明')

    class Meta:
        unique_together = [('number', 'team')]


class AssetFlow(Model):
    """资产流水"""

    class Type(TextChoices):
        """类型"""

        CREATE = ('create', '创建')
        COLLECT = ('collect', '领用')
        RETURN_STOCK = ('return_stock', '退库')
        BORROW = ('borrow', '借用')
        GIVE_BACK = ('give_back', '归还')
        TRANSFER_OUT = ('transfer_out', '调拨出')
        TRANSFER_IN = ('transfer_in', '调拨入')
        NEED_REPAIR = ('need_repair', '需要维修')
        UNDER_REPAIR = ('under_repair', '维修中')
        FIXED = ('fixed', '已修复')
        SCRAPPED = ('scrapped', '已报废')
        LOSE = ('lose', '丢失')
        SURPLUS = ('surplus', '盘盈')

    asset = ForeignKey('asset.Asset', on_delete=CASCADE, related_name='asset_flow_set', verbose_name='资产')
    type = CharField(max_length=64, choices=Type.choices, verbose_name='类型')
    location = ForeignKey('data.Location', on_delete=CASCADE, null=True,
                          related_name='asset_flow_set', verbose_name='存放地点')
    department = ForeignKey('data.Department', on_delete=CASCADE, null=True,
                            related_name='asset_flow_set', verbose_name='部门')
    person = ForeignKey('data.Person', on_delete=CASCADE, null=True,
                        related_name='asset_flow_set', verbose_name='人员')
    create_time = DateTimeField(auto_now_add=True, verbose_name='创建时间')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='asset_flow_set')


__all__ = [
    'Asset', 'AssetFlow',
]
