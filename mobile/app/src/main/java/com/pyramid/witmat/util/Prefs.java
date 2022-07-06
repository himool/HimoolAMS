package com.pyramid.witmat.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.pyramid.witmat.App;

/**
 * Author: Edward
 * Date: 2018/5/19
 * Description:
 */

public class Prefs {

    private static SharedPreferences getPrefs() {
        return App.getInstance().getSharedPreferences("DATA_CACHES", Context.MODE_PRIVATE);
    }
    public static void savePre(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }
    public static void savePre(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }

    public static void savePre(String key, String value) {
//        临时测试
//        if (key.equals(Constant.CityUrl)) {
//            getPrefs().edit().putString(key, " http://122.114.94.174:8091").apply();
//        } else
            getPrefs().edit().putString(key, value).apply();
    }

    public static int getIntValue(String key) {
        return getPrefs().getInt(key, 0);
    }

    public static String getStringValue(String key) {
        return getPrefs().getString(key, "");
    }


    public static boolean getBooleanValue(String key) {
        return getPrefs().getBoolean(key, false);
    }
}
