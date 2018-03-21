/*
 * Created by 向定权 on 17-12-8 下午3:14
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-12-8 下午3:14
 */

package org.xdq.xdqnews.http.base;


import android.app.Activity;
import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.xdq.xdqnews.util.ToastUtils;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by  xiangdingquan  on 2017/12/8.
 * <p>
 * 默认的观察者 处理服务器响应
 */

public abstract class DefaultObserver<T extends BaseResponse> implements Observer<T> {

    private Activity mActivity;

    //  Activity 是否在执行onStop()时取消订阅

    private boolean isAddInStop = false;

    public DefaultObserver(Activity activity) {
        this.mActivity = activity;
        //dialogUtils.showProgress(activity);
    }


    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onNext(T value) {
        // dismissProgress();
        onSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        //请求异常的情况
        //LogUtils.e("Retrofit", e.getMessage());
        //dismissProgress();

        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {

    }


    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    public abstract void onSuccess(T response);


    /**
     * 服务器返回数据，但响应码不为200
     *
     * @param response 服务器返回的数据
     */
    public void onFail(T response) {
        ToastUtils.show("服务器返回数据失败");
    }


    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.show("网络连接失败,请检查网络", Toast.LENGTH_SHORT);
                break;

            case CONNECT_TIMEOUT:
                ToastUtils.show("连接超时,请稍后再试", Toast.LENGTH_SHORT);
                break;

            case BAD_NETWORK:
                ToastUtils.show("服务器异常", Toast.LENGTH_SHORT);
                break;

            case PARSE_ERROR:
                ToastUtils.show("解析服务器响应数据失败", Toast.LENGTH_SHORT);
                break;

            case UNKNOWN_ERROR:
            default:
                ToastUtils.show("未知错误", Toast.LENGTH_SHORT);
                break;
        }
    }


    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }

}
