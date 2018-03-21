package org.xdq.xdqnews.ui.fragment.fuli;

import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.app.GlideApp;
import org.xdq.xdqnews.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by  xiangdingquan  on 2018/3/20.
 */

public class ImageFragment extends BaseFragment {
    private String mUrl;

    @BindView(R.id.picture)
    ImageView mImageView;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void initViews() {
        mUrl = getArguments().getString("url");
    }

    @Override
    protected void lazyFetchData() {
        GlideApp.with(this).load(mUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.IMMEDIATE)
                .centerCrop()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(mImageView);
    }

    @Override
    protected void resetMemory() {

    }
}
