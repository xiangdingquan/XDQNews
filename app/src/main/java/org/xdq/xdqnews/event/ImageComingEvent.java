package org.xdq.xdqnews.event;

import org.xdq.xdqnews.pojo.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  xiangdingquan  on 2018/3/19.
 */

public class ImageComingEvent {


    private List<Image> images;


    private String fromName;

    //    public ImageComingEvent(String from, List<Image> images) {
    //        this.images = images;
    //        this.fromName = from;
    //    }


    public ImageComingEvent(String from, Image girl) {
        this.images = new ArrayList<>();
        images.add(girl);
        this.fromName = from;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
