package com.looker.market.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.ShoppingCart;
import com.looker.market.util.CartProvider;
import com.looker.market.widget.NumAddSubView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by looker on 2017/3/29.
 */

public class CartAdapter extends SimpleRecyclerAdapter<ShoppingCart> implements BaseRecyclerAdapter.OnItemClickListener{

    private static final String TAG = "CartAdapter";
    private CheckBox mCheckBox;
    private TextView totalPrice;
    private CartProvider mProvider;

    public CartAdapter(List<ShoppingCart> datas, Context context, int layoutResId, CheckBox checkBox, TextView total) {
        super(datas, context, layoutResId);

        mProvider = new CartProvider(context);

        mCheckBox = checkBox;
        setCheckBox(mCheckBox);

        totalPrice = total;
        showTotalPrice();
        setOnItemClickListener(this);
    }


    @Override
    protected void convert(BaseViewHolder holder, final ShoppingCart item) {

        Glide.with(mContext).load(item.getImgUrl()).into(holder.getImageView(R.id.image_view));
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥" + item.getPrice());

        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        final NumAddSubView numAddSubView = (NumAddSubView) holder.getView(R.id.num_control);
        numAddSubView.setValue(item.getCount());
        numAddSubView.setButtonCliclListener(new NumAddSubView.OnButtonCliclListener() {
            @Override
            public void onAddClick(View view, int value) {
                item.setCount(value);
                mProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onSubClick(View view, int value) {
                item.setCount(value);
                mProvider.update(item);
                showTotalPrice();
            }
        });
    }

    private float getTotalPrice(){
        float sum = 0;
        if (!isNull()){
            for (ShoppingCart data : mDatas) {
                if (data.isChecked()){
                    sum += data.getPrice() * data.getCount();
                }

            }
        }
        return sum;
    }

    public void showTotalPrice(){
        float total = getTotalPrice();
        totalPrice.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    private boolean isNull(){
        return !(mDatas != null && mDatas.size() > 0);
    }

    @Override
    public void onItemClick(View view, int position) {

        ShoppingCart cart = getItem(position);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(position);

        checkedListener();
        showTotalPrice();
    }

    public void checkedListener(){
        int count = 0;
        int checkedNum = 0;

        if (mDatas != null){
            count = mDatas.size();
            for (ShoppingCart data : mDatas) {
                if (data.isChecked()){
                    checkedNum = checkedNum + 1;
                }else {
                    mCheckBox.setChecked(false);
                    break;
                }
            }

            if (count == checkedNum){
                mCheckBox.setChecked(true);
            }
        }
    }

    public void checkedAll_None(boolean isChecked){

        if (isNull()){
            return;
        }

        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setChecked(isChecked);
            notifyItemChanged(i);
        }
    }

    private void setCheckBox(CheckBox checkBox) {
        final CheckBox ck = checkBox;
        ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedAll_None(ck.isChecked());
                showTotalPrice();
            }
        });
    }

    public void delCart(){

        if (isNull()){
            return;
        }

        Iterator<ShoppingCart> iterator = mDatas.iterator();
        while (iterator.hasNext()) {
            ShoppingCart cart = iterator.next();
            if (cart.isChecked()){
                int position = mDatas.indexOf(cart);
                mProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }
}
