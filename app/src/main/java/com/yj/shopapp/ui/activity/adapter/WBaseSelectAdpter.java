package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.wbeen.Itemtype;
import com.yj.shopapp.wbeen.Itemunit;

/**
 * Created by LK on 2018/5/18.
 *
 * @author LK
 */
public class WBaseSelectAdpter extends Common2Adapter {


    public WBaseSelectAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.wbaseselectrecyitemview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.size() == 0) return;
        if (list.get(0) instanceof TagGroup) {
            TagGroup tagGroup = (TagGroup) list.get(position);
            holder.getTextView(R.id.itemname).setText(tagGroup.getName());
            holder.getCheckBox(R.id.itemchexkbox).setChecked(false);
        } else if (list.get(0) instanceof Itemtype) {
            Itemtype itemtype = (Itemtype) list.get(position);
            holder.getTextView(R.id.itemname).setText(itemtype.getName());
            holder.getCheckBox(R.id.itemchexkbox).setChecked(false);
        } else if (list.get(0) instanceof Itemunit) {
            Itemunit itemunit = (Itemunit) list.get(position);
            holder.getTextView(R.id.itemname).setText(itemunit.getName());
            holder.getCheckBox(R.id.itemchexkbox).setChecked(false);
        }

    }
}
