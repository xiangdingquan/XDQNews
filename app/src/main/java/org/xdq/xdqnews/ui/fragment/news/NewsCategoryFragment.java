package org.xdq.xdqnews.ui.fragment.news;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xdq.xdqnews.R;
import org.xdq.xdqnews.adapter.NewsAdapter;
import org.xdq.xdqnews.base.BaseRefreshFragment;
import org.xdq.xdqnews.http.base.SimpleDisposableObserver;
import org.xdq.xdqnews.pojo.PaoWangItem;
import org.xdq.xdqnews.ui.activity.news.NewsDetailActivity;

import java.io.IOException;
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
 * Created by  xiangdingquan  on 2018/3/15.
 */

public class NewsCategoryFragment extends BaseRefreshFragment {

    @BindView(R.id.rv_news)
    RecyclerView mRecyclerView;

    private String mBaseUrl = "";

    private int mCurrentPage = 1;

    private NewsAdapter mNewsAdapter;

    private CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_categeory;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mBaseUrl = getArguments().getString("url");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mNewsAdapter = new NewsAdapter(getActivity(), null);
        mNewsAdapter.openLoadAnimation();
        mNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRefreshLayout.setEnabled(false);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromServer();
                        mRefreshLayout.setEnabled(true);
                        mNewsAdapter.loadMoreComplete();
                    }
                }, 1000);
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mNewsAdapter);
        //点击条目事件
        mNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PaoWangItem item = (PaoWangItem) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("url", item.getUrl());
                intent.putExtra("title", item.getName());
                startActivity(intent);
            }
        });
    }


    /**
     * 从服务器爬取数据
     * http://www.jcodecraeer.com/plus/list.php?tid=4&PageNo=1
     */
    private void getDataFromServer() {
        showRefreshing(true);
        mBaseUrl += "&PageNo=" + mCurrentPage;
        DisposableObserver<List<PaoWangItem>> observer = new SimpleDisposableObserver<List<PaoWangItem>>() {
            @Override
            public void onNext(List<PaoWangItem> paoWangItems) {
                mCurrentPage++;
                if (mNewsAdapter.getData().size() == 0) {
                    mNewsAdapter.setNewData(paoWangItems);
                } else {
                    mNewsAdapter.addData(mNewsAdapter.getData().size(), paoWangItems);
                }
                showRefreshing(false);
            }
        };
        Observable.just(mBaseUrl).map(new Function<String, List<PaoWangItem>>() {
            @Override
            public List<PaoWangItem> apply(String s) throws Exception {
                List<PaoWangItem> list = new ArrayList<>(15);
                try {
                    Document doc = Jsoup.connect(s).timeout(10000).get();
                    Elements elements = doc.select("div.container");
                    Element total = elements.get(elements.size() - 2);
                    Elements items = total.select("[class=archive-item clearfix]");
                    for (int i = 0; i < items.size(); i++) {
                        PaoWangItem item = new PaoWangItem();
                        item.setIcon("http://www.jcodecraeer.com" + items.get(i).select("img").first().attr("src"));
                        item.setName(items.get(i).select("a[href]").text());
                        item.setFrom("泡在网上的日子");
                        item.setUpdateTime(items.get(i).select("span").last().text());
                        item.setUrl("http://www.jcodecraeer.com" + items.get(i).select("a[href]").attr("href"));
                        list.add(item);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return list;
            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mDisposable.add(observer);
    }

    @Override
    protected void lazyFetchData() {
        mNewsAdapter.setNewData(null);
        mCurrentPage = 1;
        getDataFromServer();
    }

    @Override
    protected void resetMemory() {
        mDisposable.clear();
    }
}
