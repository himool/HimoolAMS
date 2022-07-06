package com.pyramid.witmat.net.retorfit;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.gson.Gson;
import com.pyramid.witmat.BuildConfig;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author : xinlong.lv
 */
public abstract class ResCallback<T> implements Callback<T>, LifecycleObserver {

    private static final String TAG = ResCallback.class.getSimpleName();

    private boolean isDestroy = false;

    /**
     * 生命周期 onDestroy
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy(LifecycleOwner owner) {
        isDestroy = true;
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (isDestroy) {
            onFinish();
            return;
        }
        if (response.isSuccessful()) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onResponse: response======" + new Gson().toJson(response.body()));
            }
            successT(response.body());

        } else {
            responseError(response);
        }
        onFinish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        onError(t.getMessage());
        onFinish();
    }


    private void responseError(Response<T> response) {
        try {
            ResponseBody body = response.errorBody();
            if (body == null) {
                onError("Unknown Error");
            } else {
                onError(body.string());
            }
//            onError(body == null ? "Unknown Error" : body.string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public abstract void onSuccess(T result);

    private void successT(T result) {
        onSuccess(result);

    }


    public void onError(String errorStr) {

    }



    public void onFinish() {

    }

}