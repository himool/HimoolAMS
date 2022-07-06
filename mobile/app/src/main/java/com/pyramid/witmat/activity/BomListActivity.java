package com.pyramid.witmat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.zyapi.CommonApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pyramid.witmat.R;
import com.pyramid.witmat.bean.AddressBean;
import com.pyramid.witmat.bean.BomBean;
import com.pyramid.witmat.net.NetApi;
import com.pyramid.witmat.net.RetrofitUtil;
import com.pyramid.witmat.net.retorfit.ResCallback;
import com.pyramid.witmat.util.NoFastClickUtils;
import com.pyramid.witmat.util.ToastUtil;
import com.pyramid.witmat.view.Pan_batScanDialog;
import com.qs.uhf.uhfreaderlib.reader.Tools;
import com.qs.uhf.uhfreaderlib.reader.UhfReader;
import com.qs.uhf.uhfreaderlib.reader.UhfReaderDevice;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.sofia.Sofia;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物资列表
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
public class BomListActivity extends BaseActivity {


    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ivTLeft)
    ImageView ivTLeft;
    @BindView(R.id.ivTRight)
    ImageView ivTRight;
    @BindView(R.id.tvTRight)
    TextView tvTRight;
    @BindView(R.id.rlTRight)
    RelativeLayout rlTRight;
    @BindView(R.id.tvCustF)
    TextView tvCustF;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.tvPan)
    TextView tvPan;
    @BindView(R.id.llAddress)
    LinearLayout llAddress;
    @BindView(R.id.tvLoc)
    TextView tvLoc;
    @BindView(R.id.llLoc)
    LinearLayout llLoc;
    private Context context;
    BaseQuickAdapter<BomBean, BaseViewHolder> adapter;
    List<BomBean> list = new ArrayList<>();
    Pan_batScanDialog pan_batScanDialog;
    Set<String> scancodes = new HashSet<String>();
    AddressBean addressBean;


    private boolean runFlag = true;
    public boolean startFlag = false;
    private boolean connectFlag = false;
    private boolean powerOn = false;
    private static String serialPortPath = "/dev/ttyS1";
    public UhfReader reader; // 超高频读写器
    private UhfReaderDevice readerDevice; // 读写器设备，抓哟操作读写器电源

    static CommonApi mCommonApi;
    private static int mComFd = -1;
    MediaPlayer player;

    String[] locs = new String[]{"幼儿园", "小学", "初中", "高中"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomlist);
        ButterKnife.bind(this);
        //状态栏全透明全屏显示页面，布局能够延伸到状态栏和导航栏下方
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarLightFont()
                .statusBarBackgroundAlpha(0)
        ;

        context = this;
        tvTitle.setText("盘点列表");
        rlTRight.setVisibility(View.VISIBLE);
        tvTRight.setText("数据上传");
        serialPortPath = "/dev/ttyS1";


        try {
            mCommonApi = new CommonApi();

            openGPIO();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            UhfReader.setPortPath(serialPortPath);

            reader = UhfReader.getInstance();
            // 获取读写器设备示例，若返回null，则设备电源打开失败
            readerDevice = UhfReaderDevice.getInstance();
            if (reader == null) {
                return;
            }
            if (readerDevice == null) {
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            reader.setOutputPower(26);


            Thread thread = new InventoryThread();
            thread.start();
        } catch (Exception e) {

        }

        // 初始化声音播放器
        player = MediaPlayer.create(this, R.raw.msg);


        adapter = new BaseQuickAdapter<BomBean, BaseViewHolder>(R.layout.item_static) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, BomBean bean) {
            }


            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
                BomBean item = adapter.getItem(position);
                baseViewHolder.setText(R.id.tvName, item.name)
                        .setText(R.id.tv1, item.number)
                        .setText(R.id.tv2, item.property_unit)
                        .setText(R.id.tv3, item.barcode)
                        .setText(R.id.tv4, item.category_name)
                        .setText(R.id.tv5, item.spec)
                        .setText(R.id.tv6, item.model)
                        .setText(R.id.tv7, item.brand)
                        .setText(R.id.tv8, item.origin)
                        .setText(R.id.tv9, item.unit)
                        .setText(R.id.tv10, item.status_display)
                ;
                TextView tvButton = baseViewHolder.getView(R.id.tvButton);
                if (scancodes.isEmpty()) {
                    tvButton.setVisibility(View.GONE);
                } else {
                    tvButton.setVisibility(View.VISIBLE);
                }
                if (scancodes.contains(item.number)) {
                    tvButton.setText("匹配成功");
                } else {
                    tvButton.setText("匹配失败");
                }
                baseViewHolder.addOnClickListener(R.id.tvButton);
            }
        };
        rcv.setAdapter(adapter);
        iniSmartRefreshLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    @Override
    protected void onDestroy() {
        runFlag = false;
        if (reader != null) {
            reader.close();
        }
        if (readerDevice != null) {
            readerDevice.powerOff();
        }
        closeGPIO();
        mCommonApi.closeCom(mComFd);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_F1) {
            dialogAction();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void stock_check() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArrays = new JsonArray();
        for (String code : scancodes) {
            jsonArrays.add(code);
        }
        jsonObject.add("numbers", jsonArrays);
        showProgressDialog("加载中...");
        RetrofitUtil.getInstance(this).create(NetApi.class)
                .stock_check(addressBean.id, jsonObject).enqueue(new ResCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                hideProgressDialog();
                ToastUtil.showToast(BomListActivity.this, "数据上报成功");
                scancodes.clear();
                getList();


            }

            @Override
            public void onError(String errorStr) {
                super.onError(errorStr);
                ToastUtil.showToast(BomListActivity.this, errorStr);
                hideProgressDialog();
            }

        });
    }


    private void iniSmartRefreshLayout() {
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getList();
            }
        });

    }


    public void getList() {
        if (addressBean == null) {
//            ToastUtil.showToast(this, "请选择地点");
            llNodata.setVisibility(View.VISIBLE);
            return;
        }
        showProgressDialog("加载中...");
        RetrofitUtil.getInstance(this).create(NetApi.class)
                .getAssetsList(addressBean.id).enqueue(new ResCallback<List<BomBean>>() {
            @Override
            public void onSuccess(List<BomBean> result) {
                hideProgressDialog();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();

                list.clear();
                list.addAll(result);
                adapter.setNewData(result);

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
                ToastUtil.showToast(BomListActivity.this, errorStr);
                startActivity(new Intent(BomListActivity.this,LoginActivity.class));
            }

        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            addressBean = (AddressBean) data.getSerializableExtra("data");
            if (addressBean != null) {
                tvCustF.setText(addressBean.name);
                getList();
            }
        }
    }

    @OnClick({R.id.rlBack, R.id.llAddress, R.id.llLoc, R.id.tvPan, R.id.rlTRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                finish();
                break;
            case R.id.rlTRight:
                if (scancodes.isEmpty()) {
                    ToastUtil.showToast(this, "没有数据可以上传");
                    return;
                }
                stock_check();
                break;
            case R.id.tvPan:
                dialogAction();
                break;
            case R.id.llAddress:
                if (TextUtils.isEmpty(tvLoc.getText().toString())) {
                    ToastUtil.showToast(this, "请选择位置");
                    return;
                }
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.putExtra("loc",tvLoc.getText().toString());
                startActivityForResult(intent, 100);
                break;
            case R.id.llLoc:
                OptionsPickerView devtypepvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tvLoc.setText(locs[options1]);
                        addressBean = null;
                        tvCustF.setText("");
                        getList();
                    }
                })
                        .setTitleText("选择位置")
                        .setContentTextSize(20)//设置滚轮文字大小
                        .setDividerColor(getResources().getColor(R.color.c63))//设置分割线的颜色
                        .setSelectOptions(0, 0)//默认选中项
                        .setBgColor(getResources().getColor(R.color.cffffff))
                        .setTitleBgColor(getResources().getColor(R.color.cffffff))
                        .setTitleColor(getResources().getColor(R.color.black))
                        .setCancelColor(getResources().getColor(R.color.colorMain))
                        .setSubmitColor(getResources().getColor(R.color.colorMain))
                        .setTextColorCenter(getResources().getColor(R.color.black))
                        .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                        .setOutSideColor(0x00000000) //设置外部遮罩颜色
                        .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                            @Override
                            public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(LoginOrRegestActivity.this, str, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

                devtypepvOptions.setPicker(Arrays.asList(locs));//一级选择器
                devtypepvOptions.show();
                break;
        }
    }

    private void dialogAction() {
        if (NoFastClickUtils.isFastClick()) {
            return;
        }
        if (!startFlag) {
            pan_batScanDialog = new Pan_batScanDialog(BomListActivity.this, new Pan_batScanDialog.DialogOnClickListener() {
                @Override
                public void dialogOnClickListener(Set<String> data) {
                    Log.w("lxl", new Gson().toJson(data));

                    tvCustF.post(new Runnable() {
                        @Override
                        public void run() {
                            scancodes.clear();
                            scancodes.addAll(data);
                            adapter.setNewData(list);
                        }
                    });


                }
            });
            pan_batScanDialog.showPickerDialog();
        } else {
            pan_batScanDialog.dismiss();
        }
    }

    /**
     * 盘存线程
     *
     * @author Administrator
     */
    class InventoryThread extends Thread {
        private List<byte[]> epcList;

        @Override
        public void run() {
            super.run();
            while (runFlag) {
                if (startFlag) {
                    epcList = reader.inventoryRealTime(); // 实时盘存
                    if (epcList != null && !epcList.isEmpty()) {
                        // 播放提示音
                        player.start();
                        for (byte[] epc : epcList) {
                            if (epc != null) {
                                String epcStr = Tools.Bytes2HexString(epc,
                                        epc.length);
                                tvCustF.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (pan_batScanDialog != null && pan_batScanDialog.isShowing()) {
                                            pan_batScanDialog.addRfid(epcStr);
                                        }
                                    }
                                });
                            }
                        }
                    }
                    epcList = null;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    // 打开gpio
    public static void openGPIO() {
        // TODO Auto-generated method stub

        mCommonApi.setGpioDir(25, 1);
        mCommonApi.setGpioOut(25, 1);

        mCommonApi.setGpioDir(27, 1);
        mCommonApi.setGpioOut(27, 1);


    }

    // 关闭gpio
    public static void closeGPIO() {

        mCommonApi.setGpioDir(25, 1);
        mCommonApi.setGpioOut(25, 0);

        mCommonApi.setGpioDir(27, 1);
        mCommonApi.setGpioOut(27, 0);

    }

}
