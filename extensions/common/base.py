from django.db import transaction
from django.conf import settings
from functools import reduce
import pendulum
import re


def get_next_number(last_number: str, default_number: str) -> str:
    """获取编号"""

    if result := re.findall('[0-9]+', last_number):
        current_number = result[-1]
        next_number = str(int(current_number) + 1)

        if len(current_number) > len(next_number):
            next_number = next_number.zfill(len(current_number))
    else:
        return default_number

    result = re.match(f'^(.*){current_number}(.*?)$', last_number)
    prefix, suffix = result.group(1), result.group(2)

    return prefix + next_number + suffix


__all__ = [
    'transaction', 'settings', 'pendulum', 'reduce', 'get_next_number',
]
