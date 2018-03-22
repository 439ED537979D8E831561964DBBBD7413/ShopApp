package com.yj.shopapp.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ShowHtml {

    private String html;
    private TextView showText;
    private Context mContext;

    public ShowHtml(Context context, String html, TextView showText) {
        this.html = html;
        this.showText = showText;
        this.mContext = context;
    }

    public void showText() {
        NetworkImageGetter imageGetter = new NetworkImageGetter();
        Spanned spanned = Html.fromHtml(html, imageGetter, null);
        // 图片文字居中显示
        showText.setGravity(Gravity.CENTER_HORIZONTAL);
        showText.setText(spanned);
    }

    public Spanned getSpanned() {

        return Html.fromHtml(html, null, null);
    }

    //html解析
    private final class NetworkImageGetter implements Html.ImageGetter {

        @Override
        public Drawable getDrawable(String source) {
            // TODO Auto-generated method stub

            final LevelListDrawable drawable = new LevelListDrawable();
//            Glide.with(mContext).load(source).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    if (resource != null) {
//                        BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
//                        drawable.addLevel(1, 1, bitmapDrawable);
//                        drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
//                        drawable.setLevel(1);
//                        showText.invalidate();
//                        showText.setText(showText.getText());
//                    }
//                }
//            });

            return drawable;

        }

    }

}
