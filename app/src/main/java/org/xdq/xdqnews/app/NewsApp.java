package org.xdq.xdqnews.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 */

public class NewsApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
