package com.looker.market.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.Wares;

import java.util.List;

/**
 * Created by looker on 2017/3/23.
 */

public class WCategoryAdapter extends SimpleRecyclerAdapter<Wares> {

    private Context context;

    public WCategoryAdapter(List<Wares> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Wares item) {

        ImageView waresImage = holder.getImageView(R.id.image_view);
        Glide.with(context).load(item.getImgUrl()).into(waresImage);

        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
    }
}
