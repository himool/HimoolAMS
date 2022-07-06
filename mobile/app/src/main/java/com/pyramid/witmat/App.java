package com.pyramid.witmat;

import android.app.Application;
import android.content.Context;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.tencent.bugly.Bugly;


public class App extends Application {

    static App instance = null;
    public String token;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public App() {
        super.onCreate();
        instance = this;
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Bugly.init(getApplicationContext(), "334078fe38", false);



    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.cffffff, R.color.colorMain);//全局设置主题颜色
                return new BezierRadarHeader(context);
//                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new BallPulseFooter(context);
//                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

}
