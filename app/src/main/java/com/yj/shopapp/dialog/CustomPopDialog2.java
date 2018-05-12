package com.yj.shopapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yj.shopapp.R;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class CustomPopDialog2 extends Dialog {
    ImageView imgQrcode;
    private onLongClick longClick;
    private Context context;

    public CustomPopDialog2(@NonNull Context context) {
        super(context, R.style.Dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_qrcode, null);
        setContentView(view);
        imgQrcode = view.findViewById(R.id.img_qrcode);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);// 此处可以设置dialog显示的位置为居中
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置dialog宽度为屏幕的4/5
        lp.height = display.getHeight();
        getWindow().setAttributes(lp);
        imgQrcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClick.onLClick(v);
                return true;
            }
        });
        imgQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public CustomPopDialog2 setLongClick(onLongClick longClick) {
        this.longClick = longClick;
        return this;
    }

    public interface onLongClick {
        void onLClick(View view);
    }
}
