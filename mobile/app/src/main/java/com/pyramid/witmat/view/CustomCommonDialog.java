package com.pyramid.witmat.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pyramid.witmat.R;
import com.pyramid.witmat.bean.AlertCallback;
import com.pyramid.witmat.util.ScreenUtils;


/**
 * 自定义对话框
 */
public class CustomCommonDialog extends Dialog {

    public CustomCommonDialog(Context context) {
        super(context);
    }

    public CustomCommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public static void closeDialog(Dialog mWeiboDialog) {
        if (mWeiboDialog != null && mWeiboDialog.isShowing()) {
            mWeiboDialog.dismiss();
            mWeiboDialog = null;
        }
    }


    public static class Builder {
        public static Context context;
        private static String title;
        private static String message;
        private static int messageTextColor;
        private static String LeftText;
        private static int LeftTextColor;
        private static String RightText;
        private static int RightTextColor;
        private static Boolean LeftTextShow;
        private static Boolean RightTextShow;
        private static Boolean Cancelable;
        private static Boolean CanceledOnTouchOutside;

        private static String positiveButtonText;
        private static String negativeButtonText;
        private String positiveButtonTextColor;
        private String negativeButtonTextColor;
        private OnCancelListener cancelListener;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public String getTitle() {
            return title;
        }

        public static String getMessage() {
            return message;
        }

        public static int getMessageTextColor() {
            return messageTextColor;
        }

        public Builder setMessageTextColor(int messageTextColo) {
            messageTextColor = messageTextColo;
            return this;
        }

        public static String getLeftText() {
            return LeftText;
        }

        public Builder setLeftText(String leftText) {
            LeftText = leftText;
            return this;
        }

        public static int getLeftTextColor() {
            return LeftTextColor;
        }

        public Builder setLeftTextColor(int leftTextColor) {
            LeftTextColor = leftTextColor;
            return this;
        }

        public static String getRightText() {
            return RightText;
        }

        public Builder setRightText(String rightText) {
            RightText = rightText;
            return this;
        }

        public static int getRightTextColor() {
            return RightTextColor;
        }

        public Builder setRightTextColor(int rightTextColor) {
            RightTextColor = rightTextColor;
            return this;
        }

        public static Boolean getLeftTextShow() {
            return LeftTextShow;
        }

        public Builder setLeftTextShow(Boolean leftTextShow) {
            LeftTextShow = leftTextShow;
            return this;
        }

        public static Boolean getRightTextShow() {
            return RightTextShow;
        }

        public Builder setRightTextShow(Boolean rightTextShow) {
            RightTextShow = rightTextShow;
            return this;
        }

        public static String getPositiveButtonText() {
            return positiveButtonText;
        }

        public Builder setPositiveButtonText(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public static String getNegativeButtonText() {
            return negativeButtonText;
        }

        public Builder setNegativeButtonText(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public String getPositiveButtonTextColor() {
            return positiveButtonTextColor;
        }

        public String getNegativeButtonTextColor() {
            return negativeButtonTextColor;
        }

        public OnCancelListener getCancelListener() {
            return cancelListener;
        }

        public OnClickListener getPositiveButtonClickListener() {
            return positiveButtonClickListener;
        }

        public void setPositiveButtonClickListener(OnClickListener positiveButtonClickListener) {
            this.positiveButtonClickListener = positiveButtonClickListener;
        }

        public OnClickListener getNegativeButtonClickListener() {
            return negativeButtonClickListener;
        }

        public Builder setNegativeButtonClickListener(OnClickListener negativeButtonClickListener) {
            this.negativeButtonClickListener = negativeButtonClickListener;
            return this;
        }

        public static Boolean getCanceledOnTouchOutside() {
            return CanceledOnTouchOutside;
        }

        public static Boolean getCancelable() {
            return Cancelable;
        }

        public static int getGravity() {
            return gravity;
        }


        public static int gravity;
        public static boolean fullScreen;

        //        public Builder(Context context, boolean fullScreen) {
//            this.context = context;
//            this.fullScreen = fullScreen;
//        }
//        public Builder(Context context, boolean fullScreen,boolean cancelable) {
//            this.context = context;
//            this.fullScreen = fullScreen;
//            this.Cancelable=cancelable;
//        }
        public Builder(Context context, boolean fullScreen, boolean cancelable, boolean canceledOnTouchOutside, int gravity) {
            this.context = context;
            this.fullScreen = fullScreen;
            this.Cancelable = cancelable;
            this.CanceledOnTouchOutside = canceledOnTouchOutside;
            this.gravity = gravity;
        }

//        public Builder(Context context, int gravity) {
//            this.context = context;
//            this.gravity = gravity;
//        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设定 dialog 外围是否可点击
         */
        public void setCanceledOnTouchOutside(Boolean canceledOnTouchOutside) {
            CanceledOnTouchOutside = canceledOnTouchOutside;
        }

        /**
         * 设定 dialog 外围是否可点击
         */
        public void setCancelable(Boolean Cancelable) {
            Cancelable = Cancelable;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            return this;
        }


        /**
         * 取消监听
         *
         * @param listener
         */

        public Builder setCancelListener(OnCancelListener listener) {
            this.cancelListener = listener;
            return this;
        }
        // 3

        public void setPositiveButton(String positiveButtonText, OnClickListener listener,
                                      boolean visiblePositive) {
            setPositiveButton(positiveButtonText, listener, true);
        }

        public void setNegativeButton(String negativeButtonText, OnClickListener listener,
                                      boolean visibleNegative) {
            setNegativeButton(negativeButtonText, listener, visibleNegative);
        }


        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButtonTextColor(String positiveButtonTextColor) {
            this.positiveButtonTextColor = positiveButtonTextColor;
            return this;
        }

        public Builder setNegativeButtonTextColor(String negativeButtonTextColor) {
            this.negativeButtonTextColor = negativeButtonTextColor;
            return this;
        }


        public CustomCommonDialog setAnimatorSet(CustomCommonDialog commonDialog, int styleId) {
            commonDialog.getWindow().setWindowAnimations(styleId);
            return commonDialog;
        }

        /**
         * 提示带button
         **/
        public static CustomCommonDialog hintwithButtonDialog(String hint, AlertCallback alertCallback) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomCommonDialog dialog = new CustomCommonDialog(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.popwindow_hintwithbutton, null);
            TextView tvTip = layout.findViewById(R.id.tvTip);
            TextView tvButton = layout.findViewById(R.id.tvButton);
            tvTip.setText(hint);
            tvButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertCallback.doConfirm();
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(getCancelable());


            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(getCanceledOnTouchOutside());
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(getGravity());

            android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();  //获取对话框当前的参数值、
            params.width = ScreenUtils.getScreenWidth() - 40;
            dialogWindow.setAttributes(params);

            return dialog;
        }
        public static CustomCommonDialog hintwithTwoButtonDialog(String hint, AlertCallback alertCallback) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomCommonDialog dialog = new CustomCommonDialog(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.popwindow_hintwithtwobutton, null);
            TextView tvTip = layout.findViewById(R.id.tvTip);
            TextView tvCancel = layout.findViewById(R.id.tvCancel);
            TextView tvCommit = layout.findViewById(R.id.tvCommit);
            tvTip.setText(hint);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertCallback.doCancel();
                    dialog.dismiss();
                }
            });
            tvCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertCallback.doConfirm();
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(getCancelable());


            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(getCanceledOnTouchOutside());
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(getGravity());

            android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();  //获取对话框当前的参数值、
            params.width = ScreenUtils.getScreenWidth() - 80;
            dialogWindow.setAttributes(params);

            return dialog;
        }


    }
}
