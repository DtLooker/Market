package com.looker.market.bean;

import java.io.Serializable;

/**
 * Created by looker on 2017/3/23.
 */

public class Category implements Serializable {

    private long id;
    private String name;
    private String imgUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
