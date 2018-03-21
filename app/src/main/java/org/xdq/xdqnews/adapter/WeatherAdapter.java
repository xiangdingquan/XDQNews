package org.xdq.xdqnews.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.app.NewsGlobal;
import org.xdq.xdqnews.pojo.Weather;
import org.xdq.xdqnews.util.SizeUtils;
import org.xdq.xdqnews.util.TimeUtils;
import org.xdq.xdqnews.widght.WeatherChartView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by  xiangdingquan  on 2018/3/15.
 */

public class WeatherAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
    private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.getDefault());


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WeatherAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(NewsGlobal.TYPE_NOW, R.layout.item_weather_container);
        addItemType(NewsGlobal.TYPE_SUGGESTION, R.layout.item_suggestion_weather);
        addItemType(NewsGlobal.TYPE_DAILYFORECAST, R.layout.item_weather_container);
    }


    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case NewsGlobal.TYPE_NOW:
                Weather.WeatherBean now = (Weather.WeatherBean) item;
                LinearLayout nowContainer = helper.getView(R.id.contentLayout);
                nowContainer.removeAllViews();
                View view = View.inflate(mContext, R.layout.item_now_weather, null);
                TextView tvTime = view.findViewById(R.id.tv_now_time);
                TextView tvTemp = view.findViewById(R.id.tv_now_temp);
                TextView tvPop = view.findViewById(R.id.tv_now_pop);
                TextView tvWind = view.findViewById(R.id.tv_now_wind);
                tvTime.setText(TimeUtils.date2String(TimeUtils.string2Date(now.getLast_update(), sdf1), sdf2));
                tvTemp.setText(String.format("%s ℃", now.getNow().getTemperature()));
                tvPop.setText(now.getNow().getText());
                tvWind.setText(String.format("%s 风", now.getNow().getWind_direction()));
                nowContainer.addView(view);
                break;
            case NewsGlobal.TYPE_SUGGESTION:
                Weather.WeatherBean suggestion = (Weather.WeatherBean) item;
                Weather.WeatherBean.TodayBean.SuggestionBean suggestionBean = suggestion.getToday().getSuggestion();
                helper.setText(R.id.tv_suggestion_dress_info, suggestionBean.getDressing().getDetails());
                helper.setText(R.id.tv_suggestion_car_info, suggestionBean.getCar_washing().getDetails());
                helper.setText(R.id.tv_suggestion_out_info, suggestionBean.getSport().getDetails());
                helper.setText(R.id.tv_suggestion_travel_info, suggestionBean.getTravel().getDetails());
                helper.setText(R.id.tv_suggestion_flu_info, suggestionBean.getFlu().getDetails());
                helper.setText(R.id.tv_suggestion_uv_info, suggestionBean.getUv().getDetails());
                break;
            case NewsGlobal.TYPE_DAILYFORECAST:
                Weather.WeatherBean days = (Weather.WeatherBean) ((Weather.WeatherBean) item).clone();
                List<Weather.WeatherBean.FutureBean> future = days.getFuture();
                LinearLayout container = helper.getView(R.id.contentLayout);
                container.setPadding(0, SizeUtils.dp2px(mContext, 16), 0, SizeUtils.dp2px(mContext, 16));
                container.removeAllViews();
                container.addView(getChartView(future));
                break;
        }

    }

    private View getChartView(List<Weather.WeatherBean.FutureBean> future) {
        WeatherChartView chartView = new WeatherChartView(mContext);
        chartView.setWeather5(future);
        return chartView;
    }
}
