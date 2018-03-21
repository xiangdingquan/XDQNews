package org.xdq.xdqnews.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.base.BaseFragment;
import org.xdq.xdqnews.ui.activity.HomeActivity;
import org.xdq.xdqnews.ui.fragment.fuli.BaiduFragment;
import org.xdq.xdqnews.ui.fragment.fuli.GankFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 */

public class FuliFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mToolbarTitle.setText("图片中心");
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).initDrawer(mToolbar);
        }
        initTabLayout();
    }

    private void initTabLayout() {
        setUpViewpager();
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());
        mTabs.setupWithViewPager(mViewPager);
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setUpViewpager() {
        Bundle data;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Fragment fragment = new GankFragment();
        adapter.addFrag(fragment, "Gank");

        fragment = new BaiduFragment();
        data = new Bundle();
        data.putString("tag1","美女");
        fragment.setArguments(data);
        adapter.addFrag(fragment,"百度美女");

        fragment = new BaiduFragment();
        data = new Bundle();
        data.putString("tag1","明星");
        data.putString("tag2","刘德华");
        fragment.setArguments(data);
        adapter.addFrag(fragment,"百度明星刘德华");


        fragment = new BaiduFragment();
        data = new Bundle();
        data.putString("tag1","明星");
        data.putString("tag2","杨幂");
        fragment.setArguments(data);
        adapter.addFrag(fragment,"百度明星杨幂");

        fragment = new BaiduFragment();
        data = new Bundle();
        data.putString("tag1","明星");
        data.putString("tag2","张学友");
        fragment.setArguments(data);
        adapter.addFrag(fragment,"百度明星张学友");


        fragment = new BaiduFragment();
        data = new Bundle();
        data.putString("tag1","明星");
        data.putString("tag2","周星驰");
        fragment.setArguments(data);
        adapter.addFrag(fragment,"百度明星周星驰");


        fragment = new BaiduFragment();
        data = new Bundle();
        data.putString("tag1","明星");
        data.putString("tag2","张国荣");
        fragment.setArguments(data);
        adapter.addFrag(fragment,"百度明星张国荣");

        mViewPager.setAdapter(adapter);
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    protected void resetMemory() {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
