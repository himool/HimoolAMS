package com.pyramid.witmat.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author : xinlong.lv
 */
public class MyUtils {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String md5(String input) {
        byte[] bytes = new byte[0];
        try {
            bytes = MessageDigest.getInstance("MD5").digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return printHexBinary(bytes);
    }

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString().toLowerCase();
    }

    /**
     * Short Toast
     *
     * @param msg message
     */
    public static void shortToastMsg(Context context, String msg) {
        if(context!=null&&!TextUtils.isEmpty(msg)){
            Toast toast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
            toast.setText(msg);
            toast.show();
        }
    }



    /**
     * 显示键盘
     *
     * @param editText 要获取光标并显示键盘的控件
     */
    public static void showKeyboard(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }


}
