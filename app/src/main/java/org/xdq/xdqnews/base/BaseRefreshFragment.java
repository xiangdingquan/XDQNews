package org.xdq.xdqnews.base;

import android.support.v4.widget.SwipeRefreshLayout;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.util.ThemeUtil;


/**
 * Created by 向定权 on 2017/1/9.
 * 带加载进度条的fragment
 */

public abstract class BaseRefreshFragment extends BaseFragment {


    protected SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void initViews() {
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshLayout = mRootView.findViewById(R.id.swipe_container);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lazyFetchData();
            }
        });
        mRefreshLayout.setColorSchemeResources(ThemeUtil.getCurrentColorPrimary(getActivity()));
    }


    /**
     * 是否显示刷新控件儿
     *
     * @param refresh true 表示显示  false表示不显示！
     */
    protected void showRefreshing(final boolean refresh) {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(refresh);
            }
        });
    }
}
