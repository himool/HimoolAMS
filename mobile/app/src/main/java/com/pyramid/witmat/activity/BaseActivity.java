package com.pyramid.witmat.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.pyramid.witmat.R;
import com.pyramid.witmat.bean.AlertCallback;
import com.pyramid.witmat.bean.EventBusBean;
import com.pyramid.witmat.util.MyUtils;
import com.pyramid.witmat.view.CustomCommonDialog;
import com.pyramid.witmat.view.LoadingDialog;

import okhttp3.MediaType;
import okhttp3.RequestBody;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @Author : xinlong.lv
 */
public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "lxl";

    private LoadingDialog loadingDialog;
    CustomCommonDialog customCommonDialog;
    int DECIMAL_DIGITS = 2;
    public static final int REQUEST_CODE_SCAN = 0X01;
    private float volumnRatio;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    protected void showProgressDialog(String showMessage, boolean canceledOnTouchOutside) {
        if (isDestroyed()) return;
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(showMessage);
        loadingDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        loadingDialog.show();
    }

    public void showProgressDialog(String showMessage) {
        showProgressDialog(showMessage, true);
    }


    public void hideProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.w("lxl", this.getClass().getSimpleName());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // 停止识别
//        stopInventory();
    }


    @Override
    protected void onDestroy() {
        closeKeybord(null);
        super.onDestroy();

    }




    /**
     * 判断当前软键盘是否打开
     *
     * @param
     * @return
     */
    public boolean isSoftInputShow() {

        // 虚拟键盘隐藏 判断view是否为空
        View view = this.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
//

            return inputmanger.isActive() && this.getWindow().getCurrentFocus() != null;
        }
        return false;
    }

    /**
     * 关闭软键盘
     *
     * @param
     * @param
     */
    public void closeKeybord(View view) {
        if (isSoftInputShow()) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view == null) {
                View v = getWindow().peekDecorView();
                if (null != v) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
    }

    @Subscribe
    public void onEvent(EventBusBean event) {
    }





}
