package com.looker.market.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.looker.market.R;
import com.looker.market.bean.ShoppingCart;
import com.looker.market.bean.Wares;
import com.looker.market.util.CartProvider;

import java.util.List;

/**
 * Created by looker on 2017/3/18.
 */

public class HotWRecyclerAdapter extends SimpleRecyclerAdapter<Wares> {

    private CartProvider provider;
    private Context mContext;

    public HotWRecyclerAdapter(List<Wares> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);

        provider = new CartProvider(context);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder holder, final Wares item) {

        Glide.with(mContext).load(item.getImgUrl()).into(holder.getImageView(R.id.wars_image));
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥" + item.getPrice());

        Button button = holder.getButton(R.id.button_buy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider.put(convertData(item));
                Toast.makeText(mContext, "已添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ShoppingCart convertData(Wares item){
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.setId(item.getId());
        shoppingCart.setDescription(item.getDescription());
        shoppingCart.setImgUrl(item.getImgUrl());
        shoppingCart.setName(item.getName());
        shoppingCart.setPrice(item.getPrice());

        return shoppingCart;
    }

    public void resetLayout(int layoutId){
        this.mLayoutResId = layoutId;

        notifyItemRangeChanged(0, getDatas().size());
    }
}
