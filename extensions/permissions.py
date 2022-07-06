from rest_framework.permissions import BasePermission
from apps.system.models import User


class IsAuthenticated(BasePermission):
    message = '未登陆验证'

    def has_permission(self, request, view):
        if not isinstance(request.user, User):
            return False

        return True


class ModelPermission(BasePermission):
    message = '未添加操作权限'


__all__ = [
    'IsAuthenticated', 'ModelPermission',
]
