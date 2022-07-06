from extensions.routers import *
from apps.summary.views import *


router = BaseRouter()
router.register('home_summaries', HomeSummaryViewSet, 'home_summary')
router.register('category_summaries', CategorySummaryViewSet, 'category_summary')
router.register('location_summaries', LocationSummaryViewSet, 'location_summary')
router.register('department_summaries', DepartmentSummaryViewSet, 'department_summary')
router.register('person_summaries',PersonSummaryViewSet, 'person_summary')
router.register('status_summaries', StatusSummaryViewSet, 'status_summary')
urlpatterns = router.urls
