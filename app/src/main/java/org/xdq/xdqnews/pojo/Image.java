package org.xdq.xdqnews.pojo;

import java.io.Serializable;

/**
 * Created by  xiangdingquan  on 2018/3/19.
 */

public class Image implements Serializable {

    private String url;
    private int width = 0;
    private int height = 0;
    private String link;

    public Image(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
