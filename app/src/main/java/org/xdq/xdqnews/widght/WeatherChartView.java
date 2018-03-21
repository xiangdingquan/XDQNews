package org.xdq.xdqnews.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xdq.xdqnews.pojo.Weather;
import org.xdq.xdqnews.util.SizeUtils;
import org.xdq.xdqnews.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class WeatherChartView extends LinearLayout {

    private List<Weather.WeatherBean.FutureBean> dailyForecastList = new ArrayList<>();

    LayoutParams cellParams;
    LayoutParams rowParams;
    LayoutParams chartParams;

    public WeatherChartView(Context context) {
        this(context, null);
    }

    public WeatherChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        cellParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        chartParams = new LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(getContext(), 200));
    }

    private void letItGo() {
        removeAllViews();
        LinearLayout dateTitleView = new LinearLayout(getContext());
        dateTitleView.setLayoutParams(rowParams);
        dateTitleView.setOrientation(HORIZONTAL);
        dateTitleView.removeAllViews();

        LinearLayout weatherStrView = new LinearLayout(getContext());
        weatherStrView.setLayoutParams(rowParams);
        weatherStrView.setOrientation(HORIZONTAL);
        weatherStrView.removeAllViews();
        List<Integer> minTemp = new ArrayList<>();
        List<Integer> maxTemp = new ArrayList<>();
        for (int i = 0; i < dailyForecastList.size(); i++) {
            TextView tvDate = new TextView(getContext());
            tvDate.setGravity(Gravity.CENTER);
            tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            TextView tvWeather = new TextView((getContext()));
            tvWeather.setGravity(Gravity.CENTER);
            tvWeather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            if (i == 0) {
                tvDate.setText("今天");
            } else if (i == 1) {
                tvDate.setText("明天");
            } else {
                tvDate.setText(TimeUtils.getWeek(dailyForecastList.get(i).getDate(), TimeUtils.DATE_SDF));
            }
            tvWeather.setText(dailyForecastList.get(i).getText());
            minTemp.add(Integer.valueOf(dailyForecastList.get(i).getLow()));
            maxTemp.add(Integer.valueOf(dailyForecastList.get(i).getHigh()));
            weatherStrView.addView(tvWeather, cellParams);
            dateTitleView.addView(tvDate, cellParams);
        }
        addView(dateTitleView);
        addView(weatherStrView);
        ChartView chartView = new ChartView(getContext());
        chartView.setData(minTemp, maxTemp);
        chartView.setPadding(0, SizeUtils.dp2px(getContext(), 16), 0, SizeUtils.dp2px(getContext(), 16));
        addView(chartView, chartParams);
    }

    public void setWeather5(List<Weather.WeatherBean.FutureBean> list) {
        dailyForecastList.clear();
        dailyForecastList.addAll(list);
        letItGo();
    }
}
