from extensions.routers import *
from apps.system.views import *


router = BaseRouter()
router.register('users', UserViewSet, 'user')
router.register('user', UserActionViewSet, 'user_action')
urlpatterns = router.urls
