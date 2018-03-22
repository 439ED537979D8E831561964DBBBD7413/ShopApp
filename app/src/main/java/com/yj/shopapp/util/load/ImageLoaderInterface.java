package com.yj.shopapp.util.load;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Created by LK on 2018/1/10.
 *
 * @author LK
 */

public interface ImageLoaderInterface<T extends View> extends Serializable {
    void displayImage(Context context, Object path, T imageView);

    T createImageView(Context context);
}
