package com.looker.market.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
 * Created by looker on 2017/3/18.
 */

public class HomeCampRecyclerAdapter extends RecyclerView.Adapter<HomeCampRecyclerAdapter.ViewHolder>{

    public static final int VIEW_TYPE_L = 0;
    public static final int VIEW_TYPE_R = 1;

    private List<HomeCampaign> mDatas;

    private LayoutInflater mInflater;

    private Context mContext;

    private OnCampaignClickListener mClickListener;

    public HomeCampRecyclerAdapter(List<HomeCampaign> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    public void setOnCampaignClickListener(OnCampaignClickListener listener){
        mClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mInflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_R){
            return new ViewHolder(mInflater.inflate(R.layout.template_home_right, parent, false));
        }
        return new ViewHolder(mInflater.inflate(R.layout.template_home_left, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCampaign homeCampaign = mDatas.get(position);

        holder.textTitle.setText(homeCampaign.getTitle());
        Glide.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Glide.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into((holder.imageViewSmallTop));
        Glide.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into((holder.imageViewSmallBottom));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0){
            return VIEW_TYPE_R;
        }else {
            return VIEW_TYPE_L;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.img_view_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.img_view_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.img_view_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                anim(view);
            }
        }

        private void anim(final View view) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());

                    switch (view.getId()){

                        case R.id.img_view_big:
                            mClickListener.onClick(view,homeCampaign.getCpOne());
                            break;

                        case R.id.img_view_small_top:
                            mClickListener.onClick(view, homeCampaign.getCpTwo());
                            break;

                        case R.id.img_view_small_bottom:
                            mClickListener.onClick(view, homeCampaign.getCpThree());
                            break;
                    }
                }
            });
            animator.start();
        }
    }

    public interface OnCampaignClickListener{
        void onClick(View view, Campaign campaign);
    }

}
