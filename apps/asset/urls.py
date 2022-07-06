from extensions.routers import *
from apps.asset.views import *


router = BaseRouter()
router.register('assets', AssetViewSet, 'asset')
router.register('asset_transfers', AssetTransferViewSet, 'asset_transfer')
router.register('asset_flows', AssetFlowViewSet, 'asset_flow')
urlpatterns = router.urls
