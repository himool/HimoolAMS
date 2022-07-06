from extensions.models import *


class Team(Model):

    number = CharField(max_length=32, unique=True, verbose_name='编号')
    create_time = DateTimeField(auto_now_add=True, verbose_name='创建时间')


class User(Model):
    """用户"""

    username = CharField(max_length=32, verbose_name='用户名')
    password = CharField(max_length=256, verbose_name='密码')
    name = CharField(max_length=64, verbose_name='名称')
    phone = CharField(max_length=32, null=True, blank=True, verbose_name='手机号')
    email = CharField(max_length=256, null=True, blank=True, verbose_name='邮箱')
    is_manager = BooleanField(default=False, verbose_name='管理员状态')
    create_time = DateTimeField(auto_now_add=True, verbose_name='创建时间')
    team = ForeignKey('system.Team', on_delete=CASCADE, related_name='users')

    class Meta:
        unique_together = [('username', 'team'), ('name', 'team')]


__all__ = [
    'Team', 'User',
]
