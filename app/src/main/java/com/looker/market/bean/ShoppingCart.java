package com.looker.market.bean;

import java.io.Serializable;

/**
 * Created by looker on 2017/3/26.
 */

public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
