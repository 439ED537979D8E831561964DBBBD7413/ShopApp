package com.yj.shopapp.util.load;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by LK on 2018/1/10.
 *
 * @author LK
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (path.toString().endsWith(".gif")) {
            Glide.with(context).load(path.toString()).apply(new RequestOptions()).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageView);
        } else {
            Glide.with(context).load(path.toString()).apply(new RequestOptions().centerCrop()).into(imageView);
        }

    }
}
