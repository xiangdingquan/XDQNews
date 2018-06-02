package org.xdq.xdqnews.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.pojo.Image;
import org.xdq.xdqnews.widget.RatioImageView;

import java.util.List;

/**
 * Created by  xiangdingquan  on 2018/3/19.
 */

public class ImageAdapter extends BaseQuickAdapter<Image, BaseViewHolder> {
    private Context mContext;
    private RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_glide_holder)
            .priority(Priority.HIGH);


    public ImageAdapter(Context context, @Nullable List<Image> data) {
        super(R.layout.item_image, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Image item) {
        RatioImageView imageView = helper.getView(R.id.iv_fuli);
        helper.addOnClickListener(R.id.iv_fuli);
        if (item.getHeight() != 0) {
            imageView.setOriginalSize(item.getWidth(), item.getHeight());
        } else {
            imageView.setOriginalSize(236, 354);
        }
        Glide.with(mContext).load(item.getUrl()).apply(options).into(imageView);
    }
}
