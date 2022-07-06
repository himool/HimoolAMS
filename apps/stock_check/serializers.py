from extensions.common.base import *
from extensions.serializers import *
from extensions.exceptions import *
from apps.stock_check.models import *
from apps.data.models import *
from apps.asset.models import *


class StockCheckRecordSerializer(BaseSerializer):

    class LocationItemSerializer(BaseSerializer):

        class Meta:
            model = Location
            fields = ['id', 'name']

    class StockCheckAssetSerializer(BaseSerializer):

        class AssetItemSerializer(BaseSerializer):

            class Meta:
                model = Asset
                fields = ['id', 'number', 'name', 'barcode',  'spec', 'model', 'brand']

        asset_item = AssetItemSerializer(source='asset', read_only=True, label='资产')
        status_display = CharField(source='get_status_display', default=None, read_only=True, label='状态')

        class Meta:
            model = StockCheckAsset
            fields = ['id', 'asset', 'asset_item', 'status', 'status_display']

    location_item = LocationItemSerializer(source='location', read_only=True, label='地点')
    stock_check_asset_items = StockCheckAssetSerializer(source='stock_check_assets', many=True,
                                                        read_only=True, label='盘点资产')

    class Meta:
        model = StockCheckRecord
        fields = ['id', 'location', 'location_item', 'create_time', 'stock_check_asset_items']


__all__ = [
    'StockCheckRecordSerializer',
]
