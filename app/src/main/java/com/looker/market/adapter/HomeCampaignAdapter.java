package com.looker.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.Campaign;
import com.looker.market.bean.HomeCampaign;

import java.util.List;

/**
 * Created by looker on 2017/3/16.
 */

public class HomeCampaignAdapter extends RecyclerView.Adapter<HomeCampaignAdapter.ViewHolder> {

    private static final int VIEW_TYPE_L = 0;
    private static final int VIEW_TYPE_R = 1;

    private List<HomeCampaign> mDatas;
    private Context context;

    private CampaignClickListener mCampaignClickListener;

    public void setCampaignClickListener(CampaignClickListener campaignClickListener) {
        mCampaignClickListener = campaignClickListener;
    }

    public HomeCampaignAdapter(List<HomeCampaign> datas, Context context) {
        mDatas = datas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(context);
            if (viewType == VIEW_TYPE_L){
                return new ViewHolder(inflater.inflate(R.layout.template_home_left, null));

            }else if (viewType == VIEW_TYPE_R){
                return new ViewHolder(inflater.inflate(R.layout.template_home_right, null));
            }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textTitle.setText(mDatas.get(position).getTitle());

        Glide.with(context).load(mDatas.get(position).getCpOne().getImgUrl()).into(holder.imgbig);
        Glide.with(context).load(mDatas.get(position).getCpTwo().getImgUrl()).into(holder.imgSmallTop);
        Glide.with(context).load(mDatas.get(position).getCpThree().getImgUrl()).into(holder.imgSmallBottom);


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position % 2 ==0){
            return VIEW_TYPE_R;
        }else {
            return VIEW_TYPE_L;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitle;
        ImageView imgbig;
        ImageView imgSmallTop;
        ImageView imgSmallBottom;


        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imgbig = (ImageView) itemView.findViewById(R.id.img_view_big);
            imgSmallTop = (ImageView) itemView.findViewById(R.id.img_view_small_top);
            imgSmallBottom = (ImageView) itemView.findViewById(R.id.img_view_small_bottom);


            imgbig.setOnClickListener(this);
            imgSmallTop.setOnClickListener(this);
            imgSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());

            switch (view.getId()){
                case R.id.img_view_big:
                    mCampaignClickListener.onClickListener(view, homeCampaign.getCpOne());
                    break;
                case R.id.img_view_small_top:
                    mCampaignClickListener.onClickListener(view, homeCampaign.getCpTwo());
                    break;
                case R.id.img_view_small_bottom:
                    mCampaignClickListener.onClickListener(view, homeCampaign.getCpThree());
                    break;
            }
        }
    }

    public interface CampaignClickListener{
        void onClickListener(View view, Campaign campaign);
    }
}
