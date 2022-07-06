package com.pyramid.witmat.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pyramid.witmat.R;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Pattern;


/**
 * 状态栏透明
 * Created by SCWANG on 2016/10/26.
 */

@SuppressWarnings("unused")
public class StatusBarUtil {

    public static int DEFAULT_COLOR = 0;
    public static float DEFAULT_ALPHA = 0;//Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 0.2f : 0.3f;
    public static final int MIN_API = 19;
    //<editor-fold desc="沉侵">
    public static void immersive(Activity activity) {
        immersive(activity, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static void immersive(Activity activity, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        immersive(activity.getWindow(), color, alpha);
    }

    public static void immersive(Activity activity, int color) {
        immersive(activity.getWindow(), color, 1f);
    }

    public static void immersive(Window window) {
        immersive(window, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static void immersive(Window window, int color) {
        immersive(window, color, 1f);
    }

    public static void immersive(Window window, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mixtureColor(color, alpha));

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        } else if (Build.VERSION.SDK_INT >= MIN_API && Build.VERSION.SDK_INT > 16) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }
    //</editor-fold>

    //<editor-fold desc="DarkMode">
    public static void darkMode(Activity activity, boolean dark) {
        if (isFlyme4Later()) {
            Log.w("lxl", "darkMode 4");
            darkModeForFlyme4(activity.getWindow(), dark);
        } else if (isMIUI6Later()) {
            Log.w("lxl", "darkMode 6");
            darkModeForMIUI6(activity.getWindow(), dark);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.w("lxl", "darkMode m");
            darkModeForM(activity.getWindow(), dark);
        } else {
            Log.w("lxl", "darkMode no");
        }
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void darkMode(Activity activity) {
        darkMode(activity.getWindow(), DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static void darkMode(Activity activity, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        darkMode(activity.getWindow(), color, alpha);
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void darkMode(Window window, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (isFlyme4Later()) {
            darkModeForFlyme4(window, true);
            immersive(window, color, alpha);
        } else if (isMIUI6Later()) {
            darkModeForMIUI6(window, true);
            immersive(window, color, alpha);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            darkModeForM(window, true);
            immersive(window, color, alpha);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        } else {
            immersive(window, color, alpha);
        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

//        setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
    }

    //------------------------->

    /**
     * android 6.0设置字体颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private static void darkModeForM(Window window, boolean dark) {
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        if (dark) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    public static boolean darkModeForFlyme4(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams e = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(e);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(e, value);
                window.setAttributes(e);
                result = true;
            } catch (Exception var8) {
                Log.e("StatusBar", "darkIcon: failed");
            }
        }

        return result;
    }

    /**
     * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
     * http://dev.xiaomi.com/doc/p=4769/
     */
    public static boolean darkModeForMIUI6(Window window, boolean darkmode) {
        boolean result = false;
        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (darkmode) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
            }
            result = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isMiUIV7OrAbove()) {
                Log.w("lxl", "isMiUIV7OrAbove");
                //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                if (darkmode) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";

    public static boolean isMiUIV7OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 5;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }

    }

    /**
     * 判断是否Flyme4以上
     */
    public static boolean isFlyme4Later() {
        return Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }

    /**
     * 判断是否为MIUI6以上
     */
    public static boolean isMIUI6Later() {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, "ro.miui.ui.version.name");
            val = val.replaceAll("[vV]", "");
            int version = Integer.parseInt(val);
            return version >= 6;
        } catch (Exception e) {
            return false;
        }
    }
    //</editor-fold>


    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     */
    public static void setPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     */
    public static void setPaddingSmart(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context);//增高
            }
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static void setPaddingSmart(Context context, View view, boolean onlyH) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context);//增高
            }
            if (onlyH) {
                view.setPadding(0, view.getPaddingTop() + getStatusBarHeight(context),
                        0, 0);
            } else {
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                        view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     */
    public static void setHeightAndPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);//增高
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
     */
    public static void setMargin(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).topMargin += getStatusBarHeight(context);//增高
            }
            view.setLayoutParams(lp);
        }
    }

    /**
     * 创建假的透明栏
     */
    public static void setTranslucentView(ViewGroup container, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            int mixtureColor = mixtureColor(color, alpha);
            View translucentView = container.findViewById(android.R.id.custom);
            if (translucentView == null && mixtureColor != 0) {
                translucentView = new View(container.getContext());
                translucentView.setId(android.R.id.custom);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                container.addView(translucentView, lp);
            }
            if (translucentView != null) {
                translucentView.setBackgroundColor(mixtureColor);
            }
        }
    }

    public static int mixtureColor(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        int a = (color & 0xff000000) == 0 ? 0xff : color >>> 24;
        return (color & 0x00ffffff) | (((int) (a * alpha)) << 24);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }


    //显示状态栏和导航栏
    public static void showSystemUI(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //导航栏背景颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Log.w("lxl","setNavigationBarColor");
//            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.black));
//        }
//        String model = Build.MODEL; // 手机型号 暂发现Redmi 6A 不兼容，以后继续加吧
//        if(model.contains("Redmi 6A")||model.contains("H9493")||model.contains("play6")){
//            StatusBarUtil.darkMode(activity,true);
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        if (!NotchUtils.hasNotchScreen(activity)) {
            StatusBarUtil.darkMode(activity, true);
            Log.w("lxl", "setSystemUiVisibility");
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.w("lxl", "setNavigationBarColor");
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.black));
            }
        }
    }

    //影藏状态栏和导航栏
    public static void hideSystemUI(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }
    /**
     * 设置状态栏颜色（自定义颜色)
     *
     * @param activity 目标activity
     * @param color    状态栏颜色值
     */
    public static void setColor(@NonNull Activity activity, @ColorInt int color) {
        setColor(activity, color, (int) DEFAULT_ALPHA);
    }

    /**
     * 设置纯色状态栏（自定义颜色，alpha）
     *
     * @param activity 目标activity
     * @param color    状态栏颜色值
     * @param alpha    状态栏透明度
     */
    public static void setColor(@NonNull Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(cipherColor(color, alpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) activity.getWindow().getDecorView(), color, alpha);
            setRootView(activity, true);
        }
    }
    /**
     * 计算alpha色值
     *
     * @param color 状态栏颜色值
     * @param alpha 状态栏透明度
     */
    private static int cipherColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
    /**
     * 设置根布局参数
     *
     * @param activity         目标activity
     * @param fitSystemWindows 是否预留toolbar的高度
     */
    private static void setRootView(Activity activity, boolean fitSystemWindows) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fitSystemWindows);
                ((ViewGroup) childView).setClipToPadding(fitSystemWindows);
            }
        }
    }
    /**
     * 设置状态栏渐变颜色
     *
     * @param activity 目标activity
     * @param view     目标View
     */
    public static void setGradientColor(@NonNull Activity activity, View view) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(android.R.id.custom);
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView);
        }
        setRootView(activity, false);
        setTransparentForWindow(activity);
        setPaddingTop(activity, view);
    }

    /**
     * 设置透明状态栏
     *
     * @param activity 目标界面
     */
    public static void setTransparentForWindow(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    } /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     *
     * @param context 目标Context
     * @param view    需要增高的View
     */
    public static void setPaddingTop(Context context, @NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0 && view.getPaddingTop() == 0) {
                lp.height += getStatusBarHeight(context);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                        view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

}
