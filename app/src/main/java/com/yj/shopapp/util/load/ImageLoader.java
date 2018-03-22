package com.yj.shopapp.util.load;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by LK on 2018/1/10.
 *
 * @author LK
 */

public abstract class ImageLoader implements ImageLoaderInterface<ImageView> {
    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }
}
