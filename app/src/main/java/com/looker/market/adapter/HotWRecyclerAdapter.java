package com.looker.market.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.Wares;

import java.util.List;

/**
 * Created by looker on 2017/3/18.
 */

public class HotWRecyclerAdapter extends SimpleRecyclerAdapter<Wares> {

    public HotWRecyclerAdapter(List<Wares> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, Wares item) {

        Glide.with(mContext).load(item.getImgUrl()).into(holder.getImageView(R.id.wars_image));
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
        holder.getButton(R.id.button_buy).setEnabled(true);
    }
}
