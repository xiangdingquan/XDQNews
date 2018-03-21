/*
 * Created by 向定权 on 17-12-8 下午1:11
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-12-8 下午1:11
 */

package org.xdq.xdqnews.http.api;

import org.xdq.xdqnews.http.base.BaseGankResponse;
import org.xdq.xdqnews.pojo.BaiduBean;
import org.xdq.xdqnews.pojo.Gank;
import org.xdq.xdqnews.pojo.Weather;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by  xiangdingquan  on 2017/12/8.
 * ganweish interface
 */

public interface ApiService {

    String BASE_URL = "http://www.jcodecraeer.com/";

    @GET("http://tj.nineton.cn/Heart/index/all?city=CHSH000000")
    Observable<Weather> getWeather();


    @GET("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/{page}")
    Observable<BaseGankResponse<List<Gank>>> getGank(@Path("page") String page);

    /**
     * http://image.baidu.com/channel/listjson?pn=0&rn=30&tag1=%E6%98%8E%E6%98%9F
     */
    @GET("http://image.baidu.com/channel/listjson")
    Observable<BaiduBean> getBaiduIMG(@Query("pn") String pn, @Query("rn") String rn, @Query("tag1") String tag1,@Query("tag2")String tag2);

}
