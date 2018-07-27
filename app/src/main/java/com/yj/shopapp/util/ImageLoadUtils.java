package com.yj.shopapp.util;

/**
 * Created by LK on 2018/6/22.
 *
 * @author LK
 */
public class ImageLoadUtils {
    private static ImageLoadUtils instance;

    public static ImageLoadUtils getInstance() {
        if (instance == null) {
            instance = new ImageLoadUtils();
        }
        return instance;
    }
}
