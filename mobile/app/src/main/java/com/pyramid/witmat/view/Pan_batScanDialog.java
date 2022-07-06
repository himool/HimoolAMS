package com.pyramid.witmat.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pyramid.witmat.R;
import com.pyramid.witmat.activity.BomListActivity;
import com.pyramid.witmat.util.MyDividerDecoration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Pan_batScanDialog extends Dialog {

    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.llBat)
    LinearLayout llBat;
    private View rootView;
    private Context context;
    DialogOnClickListener dialogOnClickListener;
    List<String> list = new ArrayList<>();
    BaseQuickAdapter<String, BaseViewHolder> adapter;
    Set<String> scancodes = new HashSet<String>();
    private Disposable mDisposable;
    private static final int PERIOD = 3 * 1000;
    private static final int DELAY = 500;

    public Pan_batScanDialog(Context context, DialogOnClickListener dialogOnClickListener) {
        super(context, R.style.dialog);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.dialog_panbatscan, null);
        ButterKnife.bind(this, rootView);

        this.context = context;
        this.dialogOnClickListener = dialogOnClickListener;
        initView();

        this.setCanceledOnTouchOutside(true);
        setContentView(rootView);

    }

    private void startLoop() {
        mDisposable = Observable.interval(DELAY, PERIOD, TimeUnit.MILLISECONDS)
                .map((aLong -> aLong + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getList());//getUnreadCount()执行的任务
    }

    private void cancelLoop() {
        if (mDisposable != null) mDisposable.dispose();
    }


    private void initView() {
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_pdtablebatrow) {


            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
                baseViewHolder.setText(R.id.tv1, position + 1 + "")
                        .setText(R.id.tv2, list.get(position));
            }

            @Override
            protected void convert(@NonNull BaseViewHolder helper, String item) {

            }
        };

        rcv.addItemDecoration(new MyDividerDecoration(context, LinearLayoutManager.HORIZONTAL, 2, context.getResources().getColor(R.color.cdef)));
        adapter.setNewData(list);
        rcv.setAdapter(adapter);


    }

    public void getList() {
        list.clear();
        if (!scancodes.isEmpty())
            list.addAll(new ArrayList<>(scancodes));
        adapter.setNewData(list);

    }


    /**
     * 显示
     */
    public void showPickerDialog() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setSoftInputMode(layoutParams.SOFT_INPUT_ADJUST_NOTHING);
        this.show();
        ((BomListActivity)context).startFlag=true;
        startLoop();
    }

    @Override
    public void dismiss() {
        cancelLoop();
        ((BomListActivity)context).startFlag=false;
        super.dismiss();
    }

//    private void finishScan(String ids) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("infoId", id);
//        jsonObject.addProperty("ids", ids);
//        RequestBody body = ((BaseActivity) context).getRequestBody(jsonObject);
//
//        RetrofitUtil.getInstance(context).create(NetApi.class)
//                .finishScan(body).enqueue(new NetCallback<Result>() {
//            @Override
//            public void onSuccess(Result result) {
//                ToastUtil.showToast(context, result.getMessage());
//                dialogOnClickListener.dialogOnClickListener();
//                dismiss();
//
//            }
//
//            @Override
//            public void onLoginFail() {
//                super.onLoginFail();
//            }
//
//            @Override
//            public void onError(String errorStr) {
//                super.onError(errorStr);
//                ToastUtil.showToast(context, errorStr);
//            }
//
//        });
//    }

    @OnClick({R.id.llBat})
    public void onClick(View view) {

        String ids;
        switch (view.getId()) {
            case R.id.llBat:
                dialogOnClickListener.dialogOnClickListener(scancodes);
                dismiss();
                break;
        }
    }


    public void addRfid(String epcStr) {
        scancodes.add(epcStr);
    }


    public interface DialogOnClickListener {

        void dialogOnClickListener(Set<String> scancodes);

    }


    public void setDialogOnClickListener(DialogOnClickListener dialogOnClickListener) {
        this.dialogOnClickListener = dialogOnClickListener;
    }
}
