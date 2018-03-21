package org.xdq.xdqnews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xdq.xdqnews.R;
import org.xdq.xdqnews.app.NewsGlobal;
import org.xdq.xdqnews.base.BaseActivity;
import org.xdq.xdqnews.event.ThemeChangedEvent;
import org.xdq.xdqnews.ui.fragment.FuliFragment;
import org.xdq.xdqnews.ui.fragment.NewsFragment;
import org.xdq.xdqnews.ui.fragment.WeatherFragment;
import org.xdq.xdqnews.util.DoubleClickUtil;

import butterknife.BindView;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.contentLayout)
    FrameLayout mContentLayout;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private FragmentManager mFragmentManager;

    private String mCurrentFragmentTag;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getMenuResId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initNavigationViewHeader();
        mFragmentManager = getSupportFragmentManager();
        setDefaultFragment(savedInstanceState);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_weather:
                        item.setChecked(true);
                        switchContent(NewsGlobal.FRAGMENT_TAG_WEATHER);
                        break;
                    case R.id.navigation_item_paowang:
                        item.setChecked(true);
                        switchContent(NewsGlobal.FRAGMENT_TAG_NEWS);
                        break;
                    case R.id.navigation_item_fuli:
                        item.setChecked(true);
                        switchContent(NewsGlobal.FRAGMENT_TAG_FULI);
                        break;
                    case R.id.navigation_item_settings:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                    case R.id.navigation_item_about:
                        startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.START);
                return false;
            }
        });
    }


    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            toggle.syncState();
            mDrawerLayout.addDrawerListener(toggle);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThemeChanged(ThemeChangedEvent event) {
        this.recreate();
    }


    /**
     * 设置默认的fragment
     */
    private void setDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            switchContent(NewsGlobal.FRAGMENT_TAG_WEATHER);
        } else {
            mCurrentFragmentTag = savedInstanceState.getString(NewsGlobal.CURRENT_TAG);
            switchContent(mCurrentFragmentTag);
        }
    }

    private void initNavigationViewHeader() {
        mNavigation.inflateHeaderView(R.layout.drawer_header);

    }

    @Override
    protected void loadData() {

    }


    /**
     * 切换内容fragment
     *
     * @param name 内容部分的tag！
     */
    public void switchContent(String name) {
        if (mCurrentFragmentTag != null && mCurrentFragmentTag.equals(name))
            return;
        FragmentTransaction transaction =
                mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
        if (currentFragment != null)
            transaction.hide(currentFragment);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment == null) {
            switch (name) {
                case NewsGlobal.FRAGMENT_TAG_WEATHER:
                    fragment = new WeatherFragment();
                    break;
                case NewsGlobal.FRAGMENT_TAG_NEWS:
                    fragment = new NewsFragment();
                    break;
                case NewsGlobal.FRAGMENT_TAG_FULI:
                    fragment = new FuliFragment();
                    break;
            }
        }
        assert fragment != null;
        if (fragment.isAdded())
            transaction.show(fragment);
        else
            transaction.add(R.id.contentLayout, fragment, name);
        transaction.commit();
        mCurrentFragmentTag = name;
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NewsGlobal.CURRENT_TAG, mCurrentFragmentTag);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }


    @Override
    protected void resetMemory() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickUtil.check()) {
                Snackbar.make(HomeActivity.this.getWindow().getDecorView().findViewById(android.R.id.content),
                        "再按一次退出！",
                        Snackbar.LENGTH_SHORT).
                        show();
            } else {
                super.onBackPressed();
            }
        }
    }

}
