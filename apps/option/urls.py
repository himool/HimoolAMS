from extensions.routers import *
from apps.option.views import *


router = BaseRouter()
router.register('departments/options', DepartmentOptionViewSet, 'department_option')
router.register('persons/options', PersonOptionViewSet, 'person_option')
router.register('categories/options', CategoryOptionViewSet, 'category_option')
router.register('locations/options', LocationOptionViewSet, 'location_option')
urlpatterns = router.urls
