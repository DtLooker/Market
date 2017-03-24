package com.looker.market.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by looker on 2017/3/16.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> viewList;

    private BaseRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);

        mOnItemClickListener = onItemClickListener;
        viewList = new SparseArray<>();
    }

    private <T extends View> T retrieveView(int id){

        View view = viewList.get(id);
        if (view == null){
            view = itemView.findViewById(id);
            viewList.put(id, view);
        }
        return (T) view;
    }

    public TextView getTextView(int id){
        return retrieveView(id);
    }
    public Button getButton(int id){
        return retrieveView(id);
    }
    public EditText getEditText(int id){
        return retrieveView(id);
    }
    public ImageView getImageView(int id){
        return retrieveView(id);
    }
    public View getView(int id){
        return retrieveView(id);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }
}
