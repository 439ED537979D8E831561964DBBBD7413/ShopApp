package com.yj.shopapp.ui.activity.ImgUtil;

import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class ViewHolder {
    private SparseArray<View> viewHolder;
    private View view;
    public CountDownTimer countDownTimer;

    public static ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    private ViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {

        return get(id);
    }

    public Button getButton(int id) {

        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public View getView(int id) {
        return get(id);
    }

    public CheckBox getCheckBox(int id) {
        return get(id);
    }

    public SimpleDraweeView getSimpleDraweeView(int id) {
        return get(id);
    }

    public CountDownTimer getCountDown() {
        return countDownTimer;
    }
}
