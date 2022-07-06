from extensions.routers import *
from apps.data.views import *


router = BaseRouter()
router.register('departments', DepartmentViewSet, 'department')
router.register('persons', PersonViewSet, 'person')
router.register('categories', CategoryViewSet, 'category')
router.register('locations', LocationViewSet, 'location')
urlpatterns = router.urls
