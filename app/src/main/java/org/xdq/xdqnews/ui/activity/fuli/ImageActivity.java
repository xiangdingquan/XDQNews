package org.xdq.xdqnews.ui.activity.fuli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import org.xdq.xdqnews.R;
import org.xdq.xdqnews.adapter.ImageFragmentAdapter;
import org.xdq.xdqnews.base.BaseActivity;
import org.xdq.xdqnews.pojo.Image;
import org.xdq.xdqnews.ui.fragment.fuli.ImageFragment;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

/**
 * Created by  xiangdingquan  on 2018/3/20.
 */

public class ImageActivity extends BaseActivity {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String EXTRA_IMAGE_LIST = "image_list";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String TRANSIT_PIC = "picture";

    @BindView(R.id.picturePager)
    ViewPager mViewPager;

    private ImageFragmentAdapter mAdapter;
    private List<Image> mImages;
    private int mCurrentIndex;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image;
    }

    @Override
    protected int getMenuResId() {
        return R.menu.menu_image;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        showSystemUI();
        setDisplayHomeAsUpEnabled(true);
        setTitle("图片详情");
        mAdapter = new ImageFragmentAdapter(getSupportFragmentManager());
    }

    @Override
    protected void loadData() {
        mCurrentIndex = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        mImages = (List<Image>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
        if (mImages == null || mImages.isEmpty())
            return;
        for (Image image : mImages) {
            Fragment fragment = new ImageFragment();
            Bundle data = new Bundle();
            data.putString("url", image.getUrl());
            fragment.setArguments(data);
            mAdapter.addFragment(fragment);
        }
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());
    }


    @Override
    protected void resetMemory() {

    }

    public static Intent newIntent(Context context, List<Image> images, int index) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(ImageActivity.EXTRA_IMAGE_LIST, (Serializable) images);
        intent.putExtra(ImageActivity.EXTRA_IMAGE_INDEX, index);
        return intent;
    }
}
