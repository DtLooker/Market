package com.looker.market.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by looker on 2017/3/16.
 */

public class MWaitDialog extends ProgressDialog {

    public MWaitDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage("正在拼命加载....");
    }

    public MWaitDialog(Context context, int theme) {
        super(context, theme);

    }
}
