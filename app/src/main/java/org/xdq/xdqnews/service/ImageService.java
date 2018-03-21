package org.xdq.xdqnews.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;
import org.xdq.xdqnews.event.ImageComingEvent;
import org.xdq.xdqnews.pojo.Image;

import java.io.Serializable;
import java.util.List;

/**
 * Created by  xiangdingquan  on 2018/3/19.
 */

public class ImageService extends IntentService {
    private static final String KEY_EXTRA_IMAGE_FROM = "from";
    private static final String KEY_EXTRA_IMAGE_LIST = "data";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ImageService() {
        super("image");
    }

    public static void start(Context context, String from, List<Image> list) {
        Intent intent = new Intent(context, ImageService.class);
        intent.putExtra(KEY_EXTRA_IMAGE_FROM, from);
        intent.putExtra(KEY_EXTRA_IMAGE_LIST, (Serializable) list);
        context.startService(intent);
    }


    public static void stop(Context context) {
        context.stopService(new Intent(context, ImageService.class));
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String from = intent.getStringExtra(KEY_EXTRA_IMAGE_FROM);
        List<Image> images = (List<Image>) intent.getSerializableExtra(KEY_EXTRA_IMAGE_LIST);

        for (Image image : images) {
            try {
                Bitmap bitmap = Glide.with(this)
                        .asBitmap()
                        .load(image.getUrl())
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                image.setWidth(bitmap.getWidth());
                image.setHeight(bitmap.getHeight());
                EventBus.getDefault().post(new ImageComingEvent(from, image));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
