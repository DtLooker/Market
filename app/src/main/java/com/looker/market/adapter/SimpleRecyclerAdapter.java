package com.looker.market.adapter;

import android.content.Context;

import java.util.List;


/**
 * Created by looker on 2017/3/18.
 */

public abstract   class SimpleRecyclerAdapter<T> extends BaseRecyclerAdapter<T, BaseViewHolder> {


    public SimpleRecyclerAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleRecyclerAdapter(List<T> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);
    }

}
