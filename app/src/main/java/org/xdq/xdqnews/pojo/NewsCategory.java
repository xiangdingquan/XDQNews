package org.xdq.xdqnews.pojo;

import java.io.Serializable;

/**
 * Created by  xiangdingquan  on 2018/3/15.
 */

public class NewsCategory implements Serializable {

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
