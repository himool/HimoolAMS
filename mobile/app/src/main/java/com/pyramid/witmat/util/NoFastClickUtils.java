package com.pyramid.witmat.util;

/**
 * Create by lvxinlong  229608356@qq.com
 * desc
 * on 2020年6月17日21:05:52
 **/
public class NoFastClickUtils {
    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 500;//时间间隔

    public NoFastClickUtils() {
    }
    public NoFastClickUtils(int spaceTime) {
        this.spaceTime=spaceTime;
    }

    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {

            isAllowClick = false;

        } else {
            isAllowClick = true;

        }

        lastClickTime = currentTime;
        return isAllowClick;
    }
}
