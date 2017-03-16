package com.looker.market.bean;

import java.io.Serializable;

/**
 * Created by looker on 2017/3/16.
 */

public class BaseBean implements Serializable {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
