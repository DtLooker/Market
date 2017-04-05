package com.looker.market.util;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.looker.market.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by looker on 2017/3/29.
 */

public class CartProvider {

    private static final String JSON_CART = "json_cart";

    private SparseArray<ShoppingCart> datas = null;
    private Context mContext;

    public CartProvider(Context context){

        mContext = context;
        datas = new SparseArray<>(10);
        listToSparse();
    }

    public void put(ShoppingCart cart){

        ShoppingCart temp = datas.get(cart.getId().intValue());
        if (temp != null){
            temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(temp.getId().intValue(), temp);
        commit();
    }

    public void update(ShoppingCart cart){
        datas.put(cart.getId().intValue(), cart);
        commit();
    }

    public void delete(ShoppingCart cart){
        datas.remove(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll(){
        return getDatsFromLocal();
    }

    public void commit(){
        List<ShoppingCart> carts = SparseToList();
        PreferencesUtils.putString(mContext, JSON_CART, JSONUtil.toJSON(carts));
    }

    public List<ShoppingCart> SparseToList(){

        int size = datas.size();
        List<ShoppingCart> carts = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ShoppingCart cart = datas.valueAt(i);
            carts.add(cart);
        }
        return carts;
    }

    public void listToSparse(){
        List<ShoppingCart> carts = getDatsFromLocal();
        if (carts != null && carts.size() > 0){
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        }
    }

    public List<ShoppingCart> getDatsFromLocal(){

        List<ShoppingCart> carts = null;
        String json = PreferencesUtils.getString(mContext, JSON_CART);
        if (json != null){
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return carts;
    }
}
