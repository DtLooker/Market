package com.looker.market.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.HomeCampaign;

import java.util.List;

/**
 * Created by looker on 2017/3/18.
 */

public class HomeCampRecyclerAdapter extends SimpleRecyclerAdapter<HomeCampaign> {

    public static final int VIEW_TYPE_L = 0;
    public static final int VIEW_TYPE_R = 1;

    private Context mContext;

    public HomeCampRecyclerAdapter(List<HomeCampaign> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);

        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0){
            return VIEW_TYPE_R;
        }else{
            return VIEW_TYPE_L;
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeCampaign item) {

        holder.getTextView(R.id.text_title).setText(item.getTitle());
        Glide.with(mContext).load(item.getCpOne().getImgUrl()).into(holder.getImageView(R.id.img_view_big));
        Glide.with(mContext).load(item.getCpTwo().getImgUrl()).into(holder.getImageView(R.id.img_view_small_top));
        Glide.with(mContext).load(item.getCpThree().getImgUrl()).into(holder.getImageView(R.id.img_view_small_bottom));
    }
}
