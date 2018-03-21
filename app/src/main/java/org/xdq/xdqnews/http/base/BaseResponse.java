/*
 * Created by 向定权 on 17-12-8 下午2:57
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-12-8 下午2:57
 */

package org.xdq.xdqnews.http.base;

/**
 * Created by  xiangdingquan  on 2017/12/8.
 * <p>
 * 响应数据基类
 */

public class BaseResponse<T> {

    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
