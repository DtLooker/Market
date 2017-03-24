package com.looker.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by looker on 2017/3/16.
 */

public abstract class BaseRecyclerAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas;
    protected Context mContext;
    protected int mLayoutResId;

    protected static int viewType;

    protected LayoutInflater inflater;

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public BaseRecyclerAdapter(Context context, int layoutResId) {
        this(null, context, layoutResId);
    }

    public BaseRecyclerAdapter(List<T> datas, Context context, int layoutResId) {
        mDatas = datas == null ? new ArrayList<T>() : datas;
        mContext = context;
        mLayoutResId = layoutResId;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(mLayoutResId, null, false);
        setViewType(viewType);
        return new BaseViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        T item = getItem(position);

       convert((H)holder, item);

    }

    public void setViewType(int viewType){
        this.viewType = viewType;
    }

    public static int getViewType(){
        return viewType;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() <= 0){
            return 0;
        }
        return mDatas.size();
    }

    public T getItem(int position){
        if (position >= mDatas.size()){
            return null;
        }
        return mDatas.get(position);
    }
    /**
     * 清空数据
     */
    public void clear(){
        int itemCount = mDatas.size();
        mDatas.clear();
        this.notifyItemRangeRemoved(0, itemCount);
    }

    /***
     * 获取数据
     */
    public List<T> getDatas(){
        return mDatas;
    }

    public void addData(List<T> datas){
        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {
        if (datas != null && datas.size() > 0){
            mDatas.addAll(datas);
            this.notifyItemRangeChanged(position, mDatas.size());
        }
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H holder, T item);

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
