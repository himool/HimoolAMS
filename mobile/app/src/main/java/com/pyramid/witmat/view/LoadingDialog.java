package com.pyramid.witmat.view;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.pyramid.witmat.R;
import com.pyramid.witmat.activity.BaseActivity;
import com.pyramid.witmat.util.ScreenUtils;


/**
 * 加载中Dialog
 * Created by Alwasy on 2017/3/17.
 */

public class LoadingDialog extends Dialog {
    private BaseActivity context;
    private TextView tv_message;
    private ProgressBar progress;

    public LoadingDialog(BaseActivity context) {
        super(context, R.style.dialog_style);
        this.context = context;
        init();
    }


    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        tv_message = view.findViewById(R.id.tv_message);
        progress = view.findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        doubleBounce.setColor(R.color.colorMain);
        progress.setIndeterminateDrawable(doubleBounce);

        this.setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = ScreenUtils.dp2px(context, 180);//宽高可设置具体大小
        lp.height = ScreenUtils.dp2px(context, 180);
        window.setAttributes(lp);
        //默认不可以点击外面
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setOnDismissListener(dialog -> dismiss());
    }


    public void setMessage(String message) {
        tv_message.setText(message);
    }
}
