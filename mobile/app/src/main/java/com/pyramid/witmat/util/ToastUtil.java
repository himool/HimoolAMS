package com.pyramid.witmat.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;



/**
 * @author ouyangbo
 * @date 2014-11-13
 * @email ouyangbo@kingnode.com
 * @remark
 * @modify by
 */
public class ToastUtil {
    protected static Toast toast = null;
    private static Handler handler = new Handler();

    public static void showToast(Context context, String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(context!=null&& !TextUtils.isEmpty(s))
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void showToast(Context context, String errorCode, String errorMsg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
