/*
 * Created by 向定权 on 18-3-2 上午11:52
 *
 * Copyright (c) 2018.  All rights reserved.
 *
 * Last modified 18-3-2 上午11:52
 */

package org.xdq.xdqnews.widght;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by  xiangdingquan  on 2018/3/2.
 * <p>
 * 自定义webView
 */

public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 解决适配问题
     *
     * @param mode
     */
    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);
    }


    /**
     * 对html进行修改  让他可以自动适应窗口大小
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void loadHtmlDetail(String htmlBody) {
        this.getSettings().setJavaScriptEnabled(true);
        loadData(getHtmlData(htmlBody), "text/html;charset=utf-8", "utf-8");

    }


    /**
     * 拼接body部分
     *
     * @param htmlBody body部分
     * @return 拼接后的body部分
     */
    private String getHtmlData(String htmlBody) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width:100% !important; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body style:'height:auto;max-width: 100%; width:auto;'>" + htmlBody + "</body></html>";
    }


}
