package org.xdq.xdqnews.ui.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.app.NewsApp;
import org.xdq.xdqnews.app.NewsGlobal;
import org.xdq.xdqnews.http.base.SimpleDisposableObserver;
import org.xdq.xdqnews.util.FileSizeUtil;
import org.xdq.xdqnews.util.FileUtil;
import org.xdq.xdqnews.util.SettingsUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  xiangdingquan  on 2018/3/15.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {
    private Preference cleanCache;
    private Preference theme;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        cleanCache = findPreference(NewsGlobal.CLEAR_CACHE);
        theme = findPreference(NewsGlobal.THEME);
        String[] colorNames = getActivity().getResources().getStringArray(R.array.color_name);
        int currentThemeIndex = SettingsUtil.getTheme();
        if (currentThemeIndex >= colorNames.length) {
            theme.setSummary("自定义主题颜色");
        } else {
            theme.setSummary(colorNames[currentThemeIndex]);
        }

        cleanCache.setOnPreferenceClickListener(this);
        theme.setOnPreferenceClickListener(this);
        String[] cachePaths = new String[]{FileUtil.getInternalCacheDir(NewsApp.getContext()), FileUtil.getExternalCacheDir(NewsApp.getContext())};
        DisposableObserver<String> observer = new SimpleDisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                cleanCache.setSummary(s);
            }
        };
        Observable.just(cachePaths)
                .map(new Function<String[], String>() {
                    @Override
                    public String apply(String[] strings) throws Exception {
                        return FileSizeUtil.getAutoFileOrFilesSize(strings);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mDisposable.add(observer);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == cleanCache) {
            DisposableObserver<Boolean> observer = new SimpleDisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    cleanCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(FileUtil.getInternalCacheDir(NewsApp.getContext()), FileUtil.getExternalCacheDir(NewsApp.getContext())));
                    Snackbar.make(getView(), "缓存已清除!", Snackbar.LENGTH_SHORT).show();
                }
            };
            Observable.just(FileUtil.delete(FileUtil.getInternalCacheDir(NewsApp.getContext())))
                    .map(new Function<Boolean, Boolean>() {
                        @Override
                        public Boolean apply(Boolean aBoolean) throws Exception {
                            return aBoolean && FileUtil.delete(FileUtil.getExternalCacheDir(NewsApp.getContext()));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            mDisposable.add(observer);
        } else if (preference == theme) {
            new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.theme)
                    .customColors(R.array.colors, null)
                    .doneButton(R.string.done)
                    .cancelButton(R.string.cancel)
                    .allowUserColorInput(false)
                    .allowUserColorInputAlpha(false)
                    .show((SettingActivity) getActivity());
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
