from django.contrib.auth.hashers import make_password
from django.db import transaction
from apps.system.models import *


def run(*args):
    number = input('编号: ')
    username = input('用户名: ')
    password = input('密码: ')
    name = input('名称: ')

    with transaction.atomic():
        team = Team.objects.create(number=number)
        User.objects.create(team=team, username=username, password=make_password(password),
                            name=name, is_manager=True)
