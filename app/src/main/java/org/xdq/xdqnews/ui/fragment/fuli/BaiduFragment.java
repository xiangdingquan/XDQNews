package org.xdq.xdqnews.ui.fragment.fuli;

import org.xdq.xdqnews.base.BaseImageFragment;
import org.xdq.xdqnews.http.ApiFactory;
import org.xdq.xdqnews.http.base.SimpleDisposableObserver;
import org.xdq.xdqnews.pojo.BaiduBean;
import org.xdq.xdqnews.pojo.Image;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  xiangdingquan  on 2018/3/20.
 */

public class BaiduFragment extends BaseImageFragment {
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void getImageFromServer() {
        showRefreshing(true);
        String tag1 = getArguments().getString("tag1");
        String tag2 = getArguments().getString("tag2");
        DisposableObserver<BaiduBean> observer = new SimpleDisposableObserver<BaiduBean>() {
            @Override
            public void onNext(BaiduBean baiduBean) {
                mCurrentPage++;
                List<Image> list = new ArrayList<>();
                for (BaiduBean.DataBean dataBean : baiduBean.getData()) {
                    list.add(new Image(dataBean.getImage_url()));
                }
                if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
                    mAdapter.setNewData(list);
                } else {
                    mAdapter.addData(mAdapter.getData().size(), list);
                }
                showRefreshing(false);
            }
        };
        ApiFactory.getApiService().getBaiduIMG(String.valueOf(mCurrentPage), "30", tag1, tag2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        mDisposable.add(mDisposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
