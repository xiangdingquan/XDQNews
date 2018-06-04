package org.xdq.xdqnews.ui.activity.news;

import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xdq.xdqnews.R;
import org.xdq.xdqnews.base.BaseActivity;
import org.xdq.xdqnews.http.base.SimpleDisposableObserver;
import org.xdq.xdqnews.widght.CustomWebView;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  xiangdingquan  on 2018/3/16.
 */

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.wv_news)
    CustomWebView mWebView;


    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected int getMenuResId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void loadData() {
        setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
        DisposableObserver<String> observer = new SimpleDisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                mWebView.loadHtmlDetail(s);
            }
        };
        Observable.just(url).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                Document doc = Jsoup.connect(s).timeout(10000).get();
                Element element= doc.select("div.articlecon.clearfix").first();
                return element.html();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        mDisposable.add(observer);
    }

    @Override
    protected void resetMemory() {
        mDisposable.clear();
    }
}
