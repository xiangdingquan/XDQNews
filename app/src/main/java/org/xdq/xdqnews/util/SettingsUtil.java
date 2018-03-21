package org.xdq.xdqnews.util;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.app.NewsApp;
import org.xdq.xdqnews.app.NewsGlobal;

/**
 * Created by  xiangdingquan  on 2018/3/14.
 *
 *
 */

public class SettingsUtil {

    public static void setTtsVoiceType(String type) {
        SPUtil.put(NewsApp.getContext(), NewsGlobal.TTS_VOICE_TYPE, type);
    }

    public static String getTtsVoiceType() {
        return (String) SPUtil.get(NewsApp.getContext(), NewsGlobal.TTS_VOICE_TYPE, NewsApp.getContext().getResources().getStringArray(R.array.tts_voice_value)[0]);
    }

    public static void setWeatherShareType(String type) {
        SPUtil.put(NewsApp.getContext(), NewsGlobal.WEATHER_SHARE_TYPE, type);
    }

    public static String getWeatherShareType() {
        return (String) SPUtil.get(NewsApp.getContext(), NewsGlobal.WEATHER_SHARE_TYPE, NewsApp.getContext().getResources().getStringArray(R.array.share_type)[0]);
    }

    public static void setTheme(int themeIndex) {
        SPUtil.put(NewsApp.getContext(), NewsGlobal.THEME, themeIndex);
    }

    public static int getTheme() {
        return (int) SPUtil.get(NewsApp.getContext(), NewsGlobal.THEME, 4);
    }

    public static void setBusRefreshFreq(int freq) {
        SPUtil.put(NewsApp.getContext(), NewsGlobal.BUS_REFRESH_FREQ, freq);
    }

    public static int getBusRefreshFreq() {
        return (int) SPUtil.get(NewsApp.getContext(), NewsGlobal.BUS_REFRESH_FREQ, 10);
    }
}
