/*
 * Created by 向定权 on 17-12-8 上午11:32
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-12-8 上午11:32
 */

package org.xdq.xdqnews.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }
}