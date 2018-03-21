package org.xdq.xdqnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xdq.xdqnews.R;
import org.xdq.xdqnews.app.NewsGlobal;
import org.xdq.xdqnews.base.BaseFragment;
import org.xdq.xdqnews.http.api.ApiService;
import org.xdq.xdqnews.http.base.SimpleDisposableObserver;
import org.xdq.xdqnews.pojo.NewsCategory;
import org.xdq.xdqnews.ui.activity.HomeActivity;
import org.xdq.xdqnews.ui.fragment.news.NewsCategoryFragment;
import org.xdq.xdqnews.util.ACache;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 */

public class NewsFragment extends BaseFragment {


    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private ACache mACache;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mToolbarTitle.setText("泡网专栏");
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).initDrawer(mToolbar);
        }
        mACache = ACache.get(getActivity());
    }

    @Override
    protected void lazyFetchData() {
        List<NewsCategory> cache =
                (List<NewsCategory>) mACache.getAsObject(NewsGlobal.CATE_PAOWANG_CATEGORY);
        if (cache != null && cache.size() > 0) {
            initTabLayout(cache);
            return;
        }

        DisposableObserver<List<NewsCategory>> observer = new SimpleDisposableObserver<List<NewsCategory>>() {
            @Override
            public void onNext(List<NewsCategory> newsCategories) {
                initTabLayout(newsCategories);
                mACache.put(NewsGlobal.CATE_PAOWANG_CATEGORY, (Serializable) newsCategories);
            }
        };
        Observable.just(ApiService.BASE_URL).map(new Function<String, List<NewsCategory>>() {
            @Override
            public List<NewsCategory> apply(String s) throws Exception {
                List<NewsCategory> list = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(s).timeout(10000).get();
                    Element cate = doc.select("div#navMenu").first();
                    Elements links = cate.select("a[href]");
                    for (Element element : links) {
                        NewsCategory news = new NewsCategory();
                        news.setName(element.text());
                        news.setUrl(element.attr("abs:href"));
                        list.add(news);
                    }
                } catch (IOException e) {
                    Snackbar.make(mRootView, "获取分类失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lazyFetchData();
                        }
                    }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();
                }
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mDisposable.add(observer);

    }

    private void initTabLayout(List<NewsCategory> list) {
        setupViewPager(list);
        mViewPager.setOffscreenPageLimit(list.size());
        mTabs.setupWithViewPager(mViewPager);
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(List<NewsCategory> list) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (NewsCategory news : list) {
            Fragment fragment = new NewsCategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", news.getUrl());
            fragment.setArguments(bundle);
            adapter.addFrag(fragment, news.getName());
        }
        mViewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

    }


    @Override
    protected void resetMemory() {
        mDisposable.clear();
    }


}
