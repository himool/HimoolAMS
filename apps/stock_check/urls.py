from extensions.routers import *
from apps.stock_check.views import *


router = BaseRouter()
router.register('locations', StockCheckViewSet, 'stock_check')
router.register('stock_check_records', StockCheckRecordViewSet, 'stock_check_record')
urlpatterns = router.urls
