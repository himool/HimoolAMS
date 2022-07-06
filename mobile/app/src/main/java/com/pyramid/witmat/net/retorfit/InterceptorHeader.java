package com.pyramid.witmat.net.retorfit;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author : xinlong.lv
 */
public class InterceptorHeader implements Interceptor {

    private Headers headers;

    public InterceptorHeader(Headers headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.method(request.method(), request.body());
        builder.url(request.url());
        if (headers != null) {
            builder.headers(headers);
        }
        return chain.proceed(builder.build());
    }
}
