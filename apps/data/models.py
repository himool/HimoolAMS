from extensions.models import *


class Department(Model):
    """部门"""

    number = CharField(max_length=32, verbose_name='编号')
    name = CharField(max_length=64, verbose_name='名称')
    remark = CharField(max_length=256, blank=True, null=True, verbose_name='备注')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='department_set')

    class Meta:
        unique_together = [('number', 'team'), ('name', 'team')]


class Person(Model):
    """人员"""

    number = CharField(max_length=32, verbose_name='编号')
    name = CharField(max_length=64, verbose_name='名称')
    department = ForeignKey('data.Department', on_delete=PROTECT, related_name='person_set', verbose_name='部门')
    remark = CharField(max_length=256, blank=True, null=True, verbose_name='备注')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='person_set')

    class Meta:
        unique_together = [('number', 'team')]


class Category(Model):
    """资产分类"""

    number = CharField(max_length=32, verbose_name='编号')
    parent = ForeignKey('data.Category', on_delete=CASCADE, null=True, related_name='sub_category_set', verbose_name='一级分类')
    name = CharField(max_length=64, verbose_name='名称')
    origin = CharField(max_length=32, blank=True, null=True, verbose_name='产地')
    property_unit = CharField(max_length=32, blank=True, null=True, verbose_name='产权单位')
    unit = CharField(max_length=32, blank=True, null=True, verbose_name='计量单位')
    depreciation_life = IntegerField(null=True, verbose_name='默认折旧年限')
    useful_life = IntegerField(null=True, verbose_name='默认使用年限')
    description = CharField(max_length=256, blank=True, null=True, verbose_name='分类描述')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='category_set')

    class Meta:
        unique_together = [('number', 'team'), ('name', 'team')]


class Location(Model):
    """存放地点"""

    name = CharField(max_length=64, verbose_name='名称')
    parent = ForeignKey('data.Location', on_delete=CASCADE, null=True, related_name='sub_location_set', verbose_name='一级地点')
    manager = ForeignKey('data.Person', on_delete=CASCADE, null=True, related_name='location_set', verbose_name='负责人')
    description = CharField(max_length=256, blank=True, null=True, verbose_name='地点描述')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='location_set')

    class Meta:
        unique_together = [('name', 'team')]


__all__ = [
    'Department', 'Person', 'Category', 'Location',
]
