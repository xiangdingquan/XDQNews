package org.xdq.xdqnews.base;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xdq.xdqnews.R;
import org.xdq.xdqnews.adapter.ImageAdapter;
import org.xdq.xdqnews.event.ImageComingEvent;
import org.xdq.xdqnews.ui.activity.fuli.ImageActivity;

import butterknife.BindView;

/**
 * Created by  xiangdingquan  on 2018/3/19.
 *
 *
 */

public abstract class BaseImageFragment extends BaseRefreshFragment {

    @BindView(R.id.rv_image)
    RecyclerView mRecyclerView;
    protected ImageAdapter mAdapter;
    protected int mCurrentPage = 1;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAdapter = new ImageAdapter(getActivity(), null);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRefreshLayout.setEnabled(false);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getImageFromServer();
                        mRefreshLayout.setEnabled(true);
                        mAdapter.loadMoreComplete();
                    }
                }, 1000);
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ImageActivity.newIntent(getActivity(), adapter.getData(), position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        mCurrentPage = 1;
        mAdapter.setNewData(null);
        getImageFromServer();
    }

    protected abstract void getImageFromServer();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageIsComing(ImageComingEvent event) {
        if (!event.getFromName().equals(this.getClass().getName()))
            return;
        showRefreshing(false);
        if (mAdapter.getData().size() == 0) {
            mAdapter.setNewData(event.getImages());
        } else {
            mAdapter.addData(mAdapter.getData().size(), event.getImages());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void resetMemory() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


}
