from extensions.serializers import *


class DownloadResponse(Serializer):
    file = FileField(label='Excel文件')


class UploadRequest(Serializer):
    file = FileField(label='Excel文件')


__all__ = [
    'DownloadResponse', 'UploadRequest',
]
