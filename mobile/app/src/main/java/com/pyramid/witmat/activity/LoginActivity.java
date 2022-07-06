package com.pyramid.witmat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.pyramid.witmat.App;
import com.pyramid.witmat.BuildConfig;
import com.pyramid.witmat.R;
import com.pyramid.witmat.bean.UserData;
import com.pyramid.witmat.net.NetApi;
import com.pyramid.witmat.net.RetrofitUtil;
import com.pyramid.witmat.net.retorfit.ResCallback;
import com.pyramid.witmat.util.ACache;
import com.pyramid.witmat.util.NoFastClickUtils;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 登录页面
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //         佛祖保佑       永无BUG     永不修改                        //
 * //         无锡金字塔网络科技有限公司                                  //
 * //         229608356@qq.com                                       //
 * //         18961768060                                            //
 * ////////////////////////////////////////////////////////////////////
 **/
public class LoginActivity extends BaseActivity {


    @BindView(R.id.etZh)
    EditText etZh;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.etCom)
    EditText etCom;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //状态栏全透明全屏显示页面，布局能够延伸到状态栏和导航栏下方
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarDarkFont()
                .statusBarBackgroundAlpha(0)
//                .invasionNavigationBar()
                .navigationBarBackgroundAlpha(0)
        ;

        context = this;
//        etZh.setText(ACache.get(LoginActivity.this).getAsString("user"));
//        etPass.setText(ACache.get(LoginActivity.this).getAsString("pass"));
        etCom.setText("admin");
        if (BuildConfig.DEBUG) {
//            etZh.setText("wzkaadmin");
//            etPass.setText("Pass@123456");

            etCom.setText("admin");
            etZh.setText("admin");
            etPass.setText("admin");
        }


    }


    private void login() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number", etCom.getText().toString());
        jsonObject.addProperty("username", etZh.getText().toString());
        jsonObject.addProperty("password", etPass.getText().toString());
        showProgressDialog("加载中...");
        RetrofitUtil.getInstance(this).create(NetApi.class)
                .login(jsonObject).enqueue(new ResCallback<UserData>() {
            @Override
            public void onSuccess(UserData result) {
                hideProgressDialog();
//                    ACache.get(LoginActivity.this).put("login", result);
//                    ACache.get(LoginActivity.this).put("user", etZh.getText().toString());
//                    ACache.get(LoginActivity.this).put("pass", etPass.getText().toString());
                    App.getInstance().token = result.access;
                    startActivity(new Intent(LoginActivity.this, BomListActivity.class));

                    finish();

            }

            @Override
            public void onError(String errorStr) {
                super.onError(errorStr);

                hideProgressDialog();
            }

        });
    }


    @OnClick({R.id.tvLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                if (NoFastClickUtils.isFastClick()) {
                    return;
                }
                login();

                break;
        }
    }
}
