package org.xdq.xdqnews.util;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 *
 *
 */

public class DoubleClickUtil {

    public static long mLastClick = 0L;

    private static final int THRESHOLD = 2000;

    public static boolean check() {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick < THRESHOLD;
        mLastClick = now;
        return b;
    }
}
