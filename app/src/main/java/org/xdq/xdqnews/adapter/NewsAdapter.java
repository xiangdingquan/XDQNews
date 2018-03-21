package org.xdq.xdqnews.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.pojo.PaoWangItem;

import java.util.List;

/**
 * Created by  xiangdingquan  on 2018/3/15.
 */

public class NewsAdapter extends BaseQuickAdapter<PaoWangItem, BaseViewHolder> {

    private Context mContext;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);

    public NewsAdapter(Context context, @Nullable List<PaoWangItem> data) {
        super(R.layout.item_paowang, data);
        mContext = context;

    }

    @Override
    protected void convert(final BaseViewHolder helper, PaoWangItem item) {
        helper.setText(R.id.tv_paowang_name, item.getName());
        helper.setText(R.id.tv_paowang_info, item.getUpdateTime() + " • " + item.getFrom());
        Glide.with(mContext).load(item.getIcon()).apply(options).into((ImageView) helper.getView(R.id.iv_paowang_icon));
    }
}
