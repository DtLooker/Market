package com.looker.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.Wares;

import java.util.List;

/**
 * Created by looker on 2017/3/16.
 */

public class HotWarsAdapter extends RecyclerView.Adapter<HotWarsAdapter.ViewHolder>{

    private List<Wares> mDatas;
    private Context mContext;
    private View mView;

    public HotWarsAdapter(List<Wares> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mView == null){
            mView = LayoutInflater.from(mContext).inflate(R.layout.template_hot_wares, null);
        }

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wares wares = mDatas.get(position);

        Glide.with(mContext).load(wares.getImgUrl()).into(holder.warsImg);
        holder.warsTitle.setText(wares.getName());
        holder.warsPrice.setText("￥" + wares.getPrice());
        holder.buyBtn.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView warsImg;
        TextView warsTitle;
        TextView warsPrice;
        Button buyBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            warsImg = (ImageView) itemView.findViewById(R.id.wars_image);
            warsTitle = (TextView) itemView.findViewById(R.id.text_title);
            warsPrice = (TextView) itemView.findViewById(R.id.text_price);
            buyBtn = (Button) itemView.findViewById(R.id.button_buy);
        }
    }
}
