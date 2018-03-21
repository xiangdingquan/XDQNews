/*
 * Created by 向定权 on 17-12-8 上午11:14
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-12-8 上午11:14
 */

package org.xdq.xdqnews.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xdq.xdqnews.app.NewsApp;
import org.xdq.xdqnews.http.api.ApiService;
import org.xdq.xdqnews.util.FileUtil;
import org.xdq.xdqnews.util.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by  xiangdingquan  on 2017/12/8.
 * <p>
 * 构建初始化 Retrofit
 */

public class RetrofitManager {

    private static RetrofitManager instance;
    private static Retrofit retrofit;

    private HttpLoggingInterceptor mLogInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.i("xdq-okhttp", "log: " + message);
        }
    });


    private RetrofitManager() {
        Gson gson = new GsonBuilder().serializeNulls().setLenient().create();
        mLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }


    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }


    private OkHttpClient httpClient() {
        File cacheFile = new File(FileUtil.getAppCacheDir(NewsApp.getContext()), "/XDQNewsCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(mLogInterceptor)
                .addInterceptor(new HeadInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .build();
    }


    //缓存拦截器
    private class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtils.isNetworkReachable(NewsApp.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetUtils.isNetworkReachable(NewsApp.getContext())) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                Log.i("xdq-okhttp", "intercept: " + "有网络时候设置缓存");
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                // 无网络时，设置超时为1周
                int maxStale = 60 * 60 * 24 * 28;
                Log.i("xdq-okhttp", "intercept: " + "无网络时候设置缓存");
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }


    private class HeadInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Long timestamp = System.currentTimeMillis();
            String uuid = UUID.randomUUID().toString();
            Request originalRequest = chain.request();
            Request authRequest = originalRequest.newBuilder()
                    .header("Timestamp", timestamp + "")
                    .header("UUID", uuid)
                    .build();
            return chain.proceed(authRequest);
        }
    }

    public void reset() {
        instance = null;
    }

}
