package org.xdq.xdqnews.ui.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.adapter.WeatherAdapter;
import org.xdq.xdqnews.app.NewsGlobal;
import org.xdq.xdqnews.base.BaseRefreshFragment;
import org.xdq.xdqnews.http.ApiFactory;
import org.xdq.xdqnews.pojo.Weather;
import org.xdq.xdqnews.ui.activity.HomeActivity;
import org.xdq.xdqnews.util.ACache;
import org.xdq.xdqnews.util.TimeUtils;
import org.xdq.xdqnews.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 * <p>
 * 天气界面
 */

public class WeatherFragment extends BaseRefreshFragment {


    @BindView(R.id.bannner)
    ImageView mBannner;
    @BindView(R.id.tv_city_name)
    TextView mTvCityName;
    @BindView(R.id.tv_weather_string)
    TextView mTvWeatherString;
    @BindView(R.id.tv_weather_aqi)
    TextView mTvWeatherAqi;
    @BindView(R.id.tv_temp)
    TextView mTvTemp;
    @BindView(R.id.tv_update_time)
    TextView mTvUpdateTime;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.rv_weather)
    RecyclerView mRvWeather;
    private WeatherAdapter mWeatherAdapter;

    private ACache mACache;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToolbar.setTitle("天气情况");
        ((HomeActivity) getActivity()).initDrawer(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_weather);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        shareWeather();
                        return true;
                }
                return false;
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvWeather.setLayoutManager(manager);
        mWeatherAdapter = new WeatherAdapter(null);
        mWeatherAdapter.openLoadAnimation();
        mRvWeather.setAdapter(mWeatherAdapter);
        mACache = ACache.get(getActivity());
    }


    private void shareWeather() {
        ToastUtils.show("开发中。。");
    }


    @Override
    protected void lazyFetchData() {
        showRefreshing(true);
        Weather cacheWeather = (Weather) mACache.getAsObject(NewsGlobal.CACHE_WEAHTHER_NAME);
        if (cacheWeather != null) {
            showWeather(cacheWeather.getWeather().get(0));
            showRefreshing(false);
            return;
        }
        DisposableObserver<Weather> observer = new DisposableObserver<Weather>() {
            @Override
            public void onNext(Weather weather) {
                showWeather(weather.getWeather().get(0));
                mACache.put(NewsGlobal.CACHE_WEAHTHER_NAME, weather);
            }

            @Override
            public void onError(Throwable e) {
                showRefreshing(false);
                Snackbar.make(mRootView, "获取天气失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lazyFetchData();
                    }
                }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();

            }

            @Override
            public void onComplete() {
                showRefreshing(false);
            }
        };
        ApiFactory.getApiService()
                .getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mDisposable.add(observer);
    }

    private void showWeather(Weather.WeatherBean weather) {
        mTvCityName.setText(weather.getCity_name());
        mTvWeatherString.setText(weather.getNow().getText());
        mTvWeatherAqi.setText(String.format("空气质量:%s", weather.getNow().getAir_quality().getCity().getQuality()));
        mTvTemp.setText(String.format("%s℃", weather.getNow().getTemperature()));
        String updateTime = TimeUtils.string2String(weather.getLast_update(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()));
        mTvUpdateTime.setText(String.format("更新时间 -- %s", updateTime));
        List<MultiItemEntity> weathers = new ArrayList<>();

        Weather.WeatherBean nowWeather = (Weather.WeatherBean) weather.clone();
        nowWeather.setItemType(NewsGlobal.TYPE_NOW);
        weathers.add(nowWeather);

        Weather.WeatherBean suggestionsWeather = (Weather.WeatherBean) weather.clone();
        suggestionsWeather.setItemType(NewsGlobal.TYPE_SUGGESTION);
        weathers.add(suggestionsWeather);

        Weather.WeatherBean dailyWeather = (Weather.WeatherBean) weather.clone();
        dailyWeather.setItemType(NewsGlobal.TYPE_DAILYFORECAST);
        weathers.add(dailyWeather);


        //设置数据集合
        mWeatherAdapter.setNewData(weathers);
    }


    @Override
    protected void resetMemory() {
        mDisposable.clear();
    }
}
