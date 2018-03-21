/*
 * Created by 向定权 on 17-12-8 上午11:04
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-12-8 上午11:04
 */

package org.xdq.xdqnews.http;


import org.xdq.xdqnews.http.api.ApiService;

/**
 * Created by  xiangdingquan  on 2017/12/8.
 *
 * retrofi实例的工厂类
 */

public class ApiFactory {


    private static final Object monitor = new Object();

    /**
     * ganweish的service
     */
    private static ApiService apiService;


    public static ApiService getApiService() {
        if (apiService ==null){
            synchronized (monitor){
                if (apiService ==null)
                    apiService = RetrofitManager.getInstance().create(ApiService.class);
            }
        }
        return apiService;
    }


}
