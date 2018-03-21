package org.xdq.xdqnews.http.base;

import android.util.Log;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by  xiangdingquan  on 2018/3/15.
 */

public  abstract  class SimpleDisposableObserver<T> extends DisposableObserver<T> {

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.i("xdq", "onComplete: sds");
    }
}
