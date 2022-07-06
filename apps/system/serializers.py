from django.contrib.auth.hashers import make_password
from extensions.common.base import *
from extensions.serializers import *
from extensions.exceptions import *
from apps.system.models import *


class UserSerializer(BaseSerializer):

    class Meta:
        model = User
        read_only_fields = ['id', 'create_time']
        fields = ['username', 'name', 'phone', 'email', *read_only_fields]

    def validate_username(self, value):
        self.validate_unique({'username': value}, message=f'用户名[{value}]已存在')
        return value

    def validate_name(self, value):
        self.validate_unique({'name': value}, message=f'名称[{value}]已存在')
        return value

    def create(self, validated_data):
        validated_data['password'] = make_password('123456')
        return super().create(validated_data)


__all__ = [
    'UserSerializer',
]
