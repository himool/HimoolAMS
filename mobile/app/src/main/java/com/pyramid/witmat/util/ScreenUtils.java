/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pyramid.witmat.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

import com.pyramid.witmat.App;
import com.pyramid.witmat.activity.BaseActivity;


/**
 * 屏幕工具类
 *
 */
public class ScreenUtils {

    public static int dp2px(BaseActivity context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public enum EScreenDensity {
        XXHDPI,    //超高分辨率    1080×1920
        XHDPI,    //超高分辨率    720×1280
        HDPI,    //高分辨率         480×800
        MDPI,    //中分辨率         320×480
    }

    public static EScreenDensity getDisply(Context context) {
        EScreenDensity eScreenDensity;
        //初始化屏幕密度
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        if (densityDpi <= 160) {
            eScreenDensity = EScreenDensity.MDPI;
        } else if (densityDpi <= 240) {
            eScreenDensity = EScreenDensity.HDPI;
        } else if (densityDpi < 400) {
            eScreenDensity = EScreenDensity.XHDPI;
        } else {
            eScreenDensity = EScreenDensity.XXHDPI;
        }
        Log.w("lxl","escreenDensity   "+eScreenDensity);
        return eScreenDensity;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return App.getInstance().getResources().getDisplayMetrics().widthPixels;
    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }
    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return App.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 将dp转换成px
     *
     * @param dp
     * @return
     */
    public static float dpToPx(float dp) {
        return dp * App.getInstance().getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(float dp) {
        return (int) (dpToPx(dp) + 0.5f);
    }

    /**
     * 将px转换成dp
     *
     * @param px
     * @return
     */
    public static float pxToDp(float px) {
        return px / App.getInstance().getResources().getDisplayMetrics().density;
    }

    public static int pxToDpInt(float px) {
        return (int) (pxToDp(px) + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue
     * @return
     */
    public static float pxToSp(float pxValue) {
        return pxValue / App.getInstance().getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue
     * @return
     */
    public static float spToPx(float spValue) {
        return spValue *App.getInstance().getResources().getDisplayMetrics().scaledDensity;
    }



    /**
     * 当前是否是横屏
     *
     * @param context context
     * @return boolean
     */
    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 当前是否是竖屏
     *
     * @param context context
     * @return boolean
     */
    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


}
