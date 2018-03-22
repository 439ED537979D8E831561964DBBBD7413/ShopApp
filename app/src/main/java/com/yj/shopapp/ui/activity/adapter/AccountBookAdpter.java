package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.AccountBook;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/3/10.
 *
 * @author LK
 */

public class AccountBookAdpter extends CommonAdapter<AccountBook> {
    public AccountBookAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.accountbookitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountBook book = list.get(position);
        holder.getTextView(R.id.order_time).setText(DateUtils.timet(book.getTime()));
        holder.getTextView(R.id.itemname).setText(book.getRemark());
        holder.getTextView(R.id.orderStatus).setText(Contants.OrderState[Integer.parseInt(book.getType())]);
        holder.getTextView(R.id.orderPrice).setText("下单金额：" + book.getMoney());
        holder.getTextView(R.id.orderIntegral).setText("积分：" + book.getIntegral());
    }
}
