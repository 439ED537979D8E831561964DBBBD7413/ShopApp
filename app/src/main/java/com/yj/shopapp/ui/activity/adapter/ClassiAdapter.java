package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Classise;

import java.util.List;

/**
 * Created by huanghao on 2016/10/31.
 */

public class ClassiAdapter extends BaseAdapter {
    List<Classise>classises;
    Context context;
    String ws;
    public ClassiAdapter(List<Classise>classises, Context context,String ws) {
        super();
        this.classises=classises;
        this.context=context;
        this.ws=ws;

    }

    @Override
    public int getCount() {
        return classises.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Classise item=classises.get(position);
        Hodle hodle;
        if (convertView==null)
        {
            hodle=new Hodle();
            convertView= LayoutInflater.from(context).inflate(R.layout.category_item,null);
            hodle.img=(SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView);
            hodle.nameTxt=(TextView)convertView.findViewById(R.id.name_tv);
            convertView.setTag(hodle);
        }
        else {
            hodle=(Hodle) convertView.getTag();
        }
        hodle.nameTxt.setText(item.getName());

//        if (position==classises.size()-1) {
//            if (ws.equals("w")) {
//                hodle.img.setBackgroundResource(R.drawable.icon_add);
//
//            }
//        }
//        else {
            Uri uri= Uri.parse(item.getImgurl());
            hodle.img.setImageURI(uri);
//        }

        return convertView;
    }
    public class Hodle
    {
        TextView nameTxt;
        SimpleDraweeView img;
    }
}
