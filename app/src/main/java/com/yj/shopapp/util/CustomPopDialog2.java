package com.yj.shopapp.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.yj.shopapp.R;
import com.yj.shopapp.view.TagGroup;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class CustomPopDialog2 extends Dialog {

    public CustomPopDialog2(@NonNull Context context) {
        super(context);
    }

    public CustomPopDialog2(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;

        public interface onLongclick {
            void onLClick(View view);
        }

        private onLongclick longclick;

        public void setLongclick(onLongclick longclick) {
            this.longclick = longclick;
        }

        public Builder(Context context, Bitmap image) {
            this.context = context;
            this.image = image;
        }

        public CustomPopDialog2 create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomPopDialog2 dialog = new CustomPopDialog2(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_share_qrcode, null);
            dialog.addContentView(layout, new TagGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            ImageView img = (ImageView) layout.findViewById(R.id.img_qrcode);
            img.setImageBitmap(image);
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longclick.onLClick(v);
                    return true;
                }
            });
            return dialog;
        }
    }


}
