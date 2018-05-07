package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LK on 2017/9/28.
 */

public class NotContentAdapter extends CommonBaseAdapter<Notice> {


    private int currentItem = -1;

    public NotContentAdapter(Context context) {
        super(context);
    }

    public void setDefSelect(int position) {
        this.currentItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.notcontent, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Notice info = list.get(i);
        holder.noticeTitle.setText(i + 1 + "." + info.getTitle());
        switch (info.getClassify()) {
            case "1":
                holder.noticeContent.setText("\u3000\u3000" + info.getContent());
                holder.noticeTime.setText(DateUtils.timed("" + info.getAddtime()));
                break;
            case "2":
                holder.webView.setVisibility(View.VISIBLE);
                holder.noticeContent.setVisibility(View.GONE);
                holder.noticeTime.setVisibility(View.GONE);
                holder.webView.loadUrl(info.getUrl());
                break;
            default:
                break;
        }
        holder.touchLl.setTag(i);
        if (currentItem == i) {
            holder.NotHidecontent.setVisibility(View.VISIBLE);
            holder.notImg.setImageResource(R.drawable.ic_button_arraw);
            holder.noticeTitle.setTextColor(Color.parseColor("#1ca5f5"));
        } else {
            holder.NotHidecontent.setVisibility(View.GONE);
            holder.notImg.setImageResource(R.drawable.ic_right_black);
            holder.noticeTitle.setTextColor(Color.parseColor("#000000"));
        }
        holder.touchLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                if (tag == currentItem) {
                    currentItem = -1;
                } else {
                    currentItem = tag;
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.notice_title)
        TextView noticeTitle;
        @BindView(R.id.notice_content)
        TextView noticeContent;
        @BindView(R.id.notice_time)
        TextView noticeTime;
        @BindView(R.id.Not_hidecontent)
        LinearLayout NotHidecontent;
        @BindView(R.id.not_img)
        ImageView notImg;
        @BindView(R.id.touch_ll)
        LinearLayout touchLl;
        @BindView(R.id.webView)
        WebView webView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
