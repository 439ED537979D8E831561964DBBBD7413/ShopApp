package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ubeen.OrderPreview;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/5/9.
 *
 * @author LK
 */
public class OutofstockListAdpter extends Common2Adapter {
    public OutofstockListAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.outofstocklistview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position) instanceof OrderPreview.CancelBean) {
            OrderPreview.CancelBean bean = (OrderPreview.CancelBean) list.get(position);
            holder.getTextView(R.id.shopname).setText(bean.getName());
            holder.getTextView(R.id.shopnum).setText(String.format("条码：%s", bean.getItemid()));
            holder.getTextView(R.id.shopCount).setText(String.format("提交数量：%1$s%2$s", bean.getOrdernum(), bean.getUnit()));
            holder.getTextView(R.id.outofstock).setText(Html.fromHtml("缺货：" + "<font color=red>" + bean.getNum() + bean.getUnit() + "</font>"));
        } else {
            OrderDatesBean.OosdataBean oosdataBean = (OrderDatesBean.OosdataBean) list.get(position);
            holder.getTextView(R.id.shopname).setText(oosdataBean.getName());
            holder.getTextView(R.id.shopnum).setText(String.format("条码：%s", oosdataBean.getItemnumber()));
            //holder.getTextView(R.id.shopCount).setText(String.format("提交数量：%1$s%2$s", oosdataBean.getOrdernum(), oosdataBean.getUnit()));
            holder.getTextView(R.id.outofstock).setText(Html.fromHtml("缺货：" + "<font color=red>" + oosdataBean.getNum() + oosdataBean.getUnit() + "</font>"));
        }


    }
}
