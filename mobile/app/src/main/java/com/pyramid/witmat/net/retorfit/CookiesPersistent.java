package com.pyramid.witmat.net.retorfit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @Author : xinlong.lv
 */
public class CookiesPersistent implements CookieJar {


    private static CookiesPersistent instance;
    private final Map<String, List<Cookie>> cookieSet = new HashMap<>();


    public static CookiesPersistent getInstance() {
        if (instance == null) {
            synchronized (CookiesPersistent.class) {
                if (instance == null) {
                    instance = new CookiesPersistent();
                }
            }
        }
        return instance;
    }

    private CookiesPersistent() {
    }


    public void cleanCookies() {
        cookieSet.clear();
    }


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null) {
            cookieSet.put(url.host(), cookies);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookie = cookieSet.get(url.host());
        return cookie == null ? new ArrayList<>() : cookie;
    }
}
