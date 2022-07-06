package com.pyramid.witmat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pyramid.witmat.R;
import com.pyramid.witmat.bean.AddressBean;
import com.pyramid.witmat.bean.ComListBody;
import com.pyramid.witmat.net.NetApi;
import com.pyramid.witmat.net.RetrofitUtil;
import com.pyramid.witmat.net.retorfit.ResCallback;
import com.pyramid.witmat.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.sofia.Sofia;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地点选择
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
public class AddressListActivity extends BaseActivity {


    @BindView(R.id.ivTLeft)
    ImageView ivTLeft;
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivTRight)
    ImageView ivTRight;
    @BindView(R.id.tvTRight)
    TextView tvTRight;
    @BindView(R.id.rlTRight)
    RelativeLayout rlTRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etKeyword)
    EditText etKeyword;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private Context context;
    BaseQuickAdapter<AddressBean, BaseViewHolder> adapter;
    private int page = 1;
    private int pagesize = 10;
    List<AddressBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresslist);
        ButterKnife.bind(this);
        //状态栏全透明全屏显示页面，布局能够延伸到状态栏和导航栏下方
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarLightFont()
                .statusBarBackgroundAlpha(0)
        ;

        context = this;
        tvTitle.setText("地点列表");

        adapter = new BaseQuickAdapter<AddressBean, BaseViewHolder>(R.layout.item_address) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, AddressBean bean) {
            }


            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
                AddressBean item = adapter.getItem(position);
                baseViewHolder.setText(R.id.tv, item.name);

            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("data",list.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rcv.setAdapter(adapter);
        iniSmartRefreshLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    private void iniSmartRefreshLayout() {
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                updateData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getLocationsList();
            }
        });

    }

    private void updateData() {
        page = 1;
        getLocationsList();
    }


    public void getLocationsList() {
        String loc=getIntent().getStringExtra("loc");
        String lock="";
        switch (loc){
            case "幼儿园":
                lock="kindergarten";
                break;
            case "小学":
                lock="primary_school";
                break;
            case "初中":
                lock="junior_school";
                break;
            case "高中":
                lock="high_school";
                break;
        }
        showProgressDialog("加载中...");
        RetrofitUtil.getInstance(this).create(NetApi.class)
                .getLocationsList(page, pagesize,lock).enqueue(new ResCallback<ComListBody<AddressBean>>() {
            @Override
            public void onSuccess(ComListBody<AddressBean> result) {
                hideProgressDialog();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();

                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(result.results);
                    adapter.setNewData(list);
                    adapter.setEnableLoadMore(list.size()<result.count);
                    page++;
                    if (adapter.getData().isEmpty()) {
                        llNodata.setVisibility(View.VISIBLE);
                    } else {
                        llNodata.setVisibility(View.GONE);
                    }

            }

            @Override
            public void onError(String errorStr) {
                super.onError(errorStr);
                hideProgressDialog();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                ToastUtil.showToast(AddressListActivity.this, errorStr);
            }

        });

    }


    @OnClick({R.id.rlBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                finish();
                break;
        }
    }
}
