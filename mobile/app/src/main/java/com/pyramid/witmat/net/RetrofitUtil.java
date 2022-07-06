package com.pyramid.witmat.net;

import android.content.Context;
import android.text.TextUtils;


import com.pyramid.witmat.App;
import com.pyramid.witmat.BuildConfig;
import com.pyramid.witmat.net.retorfit.CookiesPersistent;
import com.pyramid.witmat.net.retorfit.InterceptorHeader;
import com.pyramid.witmat.net.retorfit.InterceptorLog;
import com.pyramid.witmat.util.ACache;

import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author : xinlong.lv
 */
public class RetrofitUtil {

    private static final long TIME_OUT = 60 * 2;
      public static final String HOST = "http://114.218.158.78:14445/api/";

    Context context;
    private volatile static RetrofitUtil instance = null;

    private RetrofitUtil(Context context) {
        this.context = context;
        if (null != instance) {
            throw new RuntimeException("Don't construct more!");
        }
    }

    public static RetrofitUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil(context);
                }
            }
        }
        return instance;
    }

    public String getHOST() {
        return HOST;
    }


    public synchronized <T> T create(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }


    /**
     * 请求头
     */
    private static Headers addHeaders() {
        Headers.Builder builder = new Headers.Builder();
        builder.add("Content-Type", "application/json");
        if(!TextUtils.isEmpty(App.getInstance().token))
            builder.add("Authorization", "Bearer "+App.getInstance().token);
//        String modelVersionInfo = String.format("%s %s; Android %s; %s",
//                Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, BuildConfig.VERSION_NAME);
//        builder.add("User-Agent", modelVersionInfo);
        return builder.build();
    }

    private Retrofit getRetrofit() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        OkHttpClient.Builder client = getOkHttpClientBuilder();

        if (BuildConfig.DEBUG) {
            client.addInterceptor(new InterceptorLog());
        }

        client.addInterceptor(new InterceptorHeader(addHeaders()));
        client.cookieJar(CookiesPersistent.getInstance());

        client.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        client.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        client.readTimeout(TIME_OUT, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getHOST());
//        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create());
        //添加自定义的转换器
        builder.addConverterFactory(CustomGsonConverterFactory.create());
        builder.client(client.build());

        return builder.build();
    }




}
