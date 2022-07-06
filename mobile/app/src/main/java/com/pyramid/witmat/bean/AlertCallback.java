package com.pyramid.witmat.bean;

import android.content.Context;

/**
 * Create by lxl 2019/05/24.
 */

public class AlertCallback {

    public Context context;

    public AlertCallback() {
    }

    public AlertCallback(Context context) {
        this.context = context;
    }

    public void showAlert() {
    }

    public void doConfirm() {
    }

    public void doConfirm(Object... args) {

    }
    public void error(Object... args) {

    }

    public void doCancel() {
    }

    public void doCustomAction() {
    }
}