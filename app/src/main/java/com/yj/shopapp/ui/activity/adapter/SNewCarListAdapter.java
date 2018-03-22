package com.yj.shopapp.ui.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.CartList;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.shopcartlistInterface;
import com.yj.shopapp.util.CommonUtils;

/**
 * Created by LK on 2017/12/27.
 *
 * @author LK
 */

public class SNewCarListAdapter extends CommonAdapter<CartList> {

    private shopcartlistInterface.ModifyCountInterface modifyCountInterface;
    private boolean isSwitch;

    public SNewCarListAdapter(Context context, boolean isSwitch) {
        super(context);
        this.isSwitch = isSwitch;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.goodlistitem;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartList cartList = list.get(position);
        holder.getCheckBox(R.id.checkBox).setVisibility(isSwitch ? View.VISIBLE : View.GONE);
        if (cartList.getSale_status().equals("0")) {
            holder.getCheckBox(R.id.checkBox).setClickable(false);
        } else {
            holder.getCheckBox(R.id.checkBox).setChecked(cartList.isChoosed());
        }
        holder.getTextView(R.id.shapname).setText(cartList.getName());
        holder.getTextView(R.id.Unit_Price).setText("单价￥" + CommonUtils.decimal(Double.parseDouble(cartList.getPrice())));
        holder.getTextView(R.id.barcode).setText(Html.fromHtml("条码：" + "<font color=#0578eb>" + cartList.getItemnumber() + "</font>"));
        holder.getTextView(R.id.allprice).setText(String.format("总价￥%s", CommonUtils.decimal(Double.parseDouble(cartList.getMoneysum()))));
        holder.getTextView(R.id.num).setText(cartList.getItemcount());
        if (cartList.getSale_status().equals("1")) {
            Glide.with(context).load(cartList.getImgurl()).apply(new RequestOptions().centerCrop().override(180,180))
                    .into(holder.getSimpleDraweeView(R.id.itemimag));
        } else {
            setOverlays(holder.getSimpleDraweeView(R.id.itemimag), cartList.getImgurl());
        }
        holder.getSimpleDraweeView(R.id.itemimag);
        holder.getTextView(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position);
            }
        });
        holder.getTextView(R.id.cutdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position);
            }
        });
        holder.getCheckBox(R.id.checkBox).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartList.setChoosed(isChecked);
                modifyCountInterface.checkGroup(position, isChecked);
            }
        });

    }

    private void setOverlays(SimpleDraweeView view, String url) {
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setOverlay(context.getResources().getDrawable(R.drawable.nogoods))
                .build();
        view.setHierarchy(hierarchy);
        view.setImageURI(url);

    }

    public void setItemData(int position, CartList newcartList) {
        list.set(position, newcartList);
        modifyCountInterface.statistics();
        notifyDataSetChanged();
    }

    public void setModifyCountInterface(shopcartlistInterface.ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }


}
