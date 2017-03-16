package com.looker.market.okhttp;

import android.content.Context;

import com.looker.market.widget.MWaitDialog;

import okhttp3.Request;

/**
 * Created by looker on 2017/3/16.
 */

public abstract class LoadCallback<T> extends BaseCallback<T> {

    private MWaitDialog mDialog;

    public LoadCallback(Context context) {
        mDialog = new MWaitDialog(context);
    }

    @Override
    public void onResponseBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        closeDialog();
    }

    public void showDialog(){
        mDialog.show();
    }

    public void closeDialog(){
        mDialog.dismiss();
    }
}
