package com.looker.market.adapter;


import android.content.Context;

import com.looker.market.R;
import com.looker.market.bean.Category;

import java.util.List;

/**
 * Created by looker on 2017/3/23.
 */

public class CategoryAdapter extends SimpleRecyclerAdapter<Category> {


    public CategoryAdapter(List<Category> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, Category item) {
        holder.getTextView(R.id.first_title).setText(item.getName());
    }
}
