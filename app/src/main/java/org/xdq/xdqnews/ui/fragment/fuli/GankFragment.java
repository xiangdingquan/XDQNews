package org.xdq.xdqnews.ui.fragment.fuli;

import android.support.design.widget.Snackbar;
import android.view.View;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.base.BaseImageFragment;
import org.xdq.xdqnews.http.ApiFactory;
import org.xdq.xdqnews.http.base.BaseGankResponse;
import org.xdq.xdqnews.pojo.Gank;
import org.xdq.xdqnews.pojo.Image;
import org.xdq.xdqnews.service.ImageService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  xiangdingquan  on 2018/3/19.
 */

public class GankFragment extends BaseImageFragment {


    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void getImageFromServer() {
        showRefreshing(true);
        DisposableObserver<BaseGankResponse<List<Gank>>> observer = new DisposableObserver<BaseGankResponse<List<Gank>>>() {
            @Override
            public void onNext(BaseGankResponse<List<Gank>> listBaseGankResponse) {
                mCurrentPage++;
                List<Image> list = new ArrayList<>();
                for (Gank gank : listBaseGankResponse.results) {
                    list.add(new Image(gank.getUrl()));
                }
                ImageService.start(getActivity(), GankFragment.class.getName(), list);
            }

            @Override
            public void onError(Throwable e) {
                showRefreshing(false);
                Snackbar.make(getView(), "获取Gank妹纸失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getImageFromServer();
                    }
                }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();
            }

            @Override
            public void onComplete() {

            }
        };

        ApiFactory.getApiService().getGank(String.valueOf(mCurrentPage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mDisposable.add(observer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
