package org.xdq.xdqnews.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.xdq.xdqnews.BuildConfig;
import org.xdq.xdqnews.R;
import org.xdq.xdqnews.base.BaseActivity;
import org.xdq.xdqnews.http.base.SimpleDisposableObserver;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by  xiangdingquan  on 2018/3/15.
 *
 *
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.imageSwitcher)
    ImageSwitcher mImageSwitcher;
    @BindView(R.id.tv_app_version)
    TextView mVersion;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private String[] imageUrls = {

            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521606479582&di=c30152e6e57569688a3585012b0287f5&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fb21c8701a18b87d6232299000d0828381f30fd48.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522201245&di=15d969c13a45ed8343c6d64c238fe8ec&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F32fa828ba61ea8d35e5c44059d0a304e251f58b0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521606568453&di=ba3fbdfc2cf74af6b2558d691d39f579&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Df6c0be665866d0166a14966bff42be72%2F0824ab18972bd407fae1ee5671899e510fb30972.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522201302&di=6ef46929552df0f7bb105db65eb2e3af&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F78310a55b319ebc462cd5e998826cffc1e17167c.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522201272&di=18dcad88852a9aa9011445c9fa9f0de4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F1%2F580ed3a031ea7.jpg"
    };


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuResId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
        setTitle("关于我们");
        mVersion.setText("V" + BuildConfig.VERSION_NAME);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(AboutActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_in));
        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_out));
    }


    private void loadImage() {
        Glide.with(this).load(imageUrls[new Random().nextInt(5)]).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mImageSwitcher.setImageDrawable(resource);
            }
        });
    }

    @Override
    protected void loadData() {
        mImageSwitcher.post(new Runnable() {
            @Override
            public void run() {
                loadImage();
            }
        });

        DisposableObserver<Long> observer = new SimpleDisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                loadImage();
            }
        };

        Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        mDisposable.add(observer);
    }

    @Override
    protected void resetMemory() {
        mDisposable.clear();
    }
}
