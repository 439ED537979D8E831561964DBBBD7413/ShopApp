package com.yj.shopapp.ui.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.CartList;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.RVHolder;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.shopcartlistInterface;
import com.yj.shopapp.util.CommonUtils;

/**
 * Created by LK on 2017/12/27.
 *
 * @author LK
 */

public class SNewCarListAdapter extends Common2Adapter<CartList> {
    private final static int contentview = 0;
    private final static int foortView = 1;
    private View foootView;
    private int isFootview = 0;
    private shopcartlistInterface.ModifyCountInterface modifyCountInterface;

    public SNewCarListAdapter(Context context) {
        super(context);
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case contentview:
                view = LayoutInflater.from(context).inflate(R.layout.goodlistitem, parent, false);
                break;
            case foortView:
                view = foootView;
                break;
        }
        return new RVHolder(view);
    }

    private boolean isFooterViewPos(int position) {
        return position == getItemCount() - isFootview;
    }

    public void setFoootView(View foootView) {
        this.foootView = foootView;
        isFootview = 1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterViewPos(position)) {
            return foortView;
        } else {
            return contentview;
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size() + isFootview;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.goodlistitem;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isFooterViewPos(position)) {
            return;
        }
        final CartList cartList = list.get(position);
        holder.getTextView(R.id.shopspec).setText(String.format("规格：%1$s%2$s", cartList.getSpecs(), cartList.getUnit()));
        if (cartList.getSale_status().equals("0")) {
            holder.getView(R.id.checkBoxlayout).setVisibility(View.GONE);
            holder.getCheckBox(R.id.checkBox).setButtonDrawable(R.drawable.ic_cross_grey);
            holder.getCheckBox(R.id.checkBox).setChecked(false);
            holder.getTextView(R.id.tv_tips).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.checkBoxlayout).setVisibility(View.VISIBLE);
            holder.getCheckBox(R.id.checkBox).setButtonDrawable(R.drawable.checkbox_style);
            holder.getCheckBox(R.id.checkBox).setChecked(cartList.isChoosed());
            holder.getTextView(R.id.tv_tips).setVisibility(View.GONE);
        }
        holder.getTextView(R.id.shapname).setText(cartList.getName());
        holder.getTextView(R.id.Unit_Price).setText("￥" + CommonUtils.decimal(Double.parseDouble(cartList.getUnitprice())));
        holder.getTextView(R.id.barcode).setText(Html.fromHtml("条码：" + "<font color=#0578eb>" + cartList.getItemnumber() + "</font>"));
        holder.getTextView(R.id.allprice).setText(String.format("小计￥%s", CommonUtils.decimal(Double.parseDouble(cartList.getMoneysum()))));
        holder.getTextView(R.id.num).setText(cartList.getItemcount());
        if (cartList.getSale_status().equals("1")) {
            Glide.with(context).load(cartList.getImgurl()).apply(new RequestOptions().centerCrop())
                    .into(holder.getSimpleDraweeView(R.id.itemimag));
        } else {
            setImg(holder.getSimpleDraweeView(R.id.itemimag), cartList.getImgurl());
        }
        holder.getTextView(R.id.add).setOnClickListener(v -> modifyCountInterface.doIncrease(position));
        holder.getTextView(R.id.cutdown).setOnClickListener(v -> modifyCountInterface.doDecrease(position));
        holder.getCheckBox(R.id.checkBox).setOnClickListener(v -> modifyCountInterface.checkGroup(position, !cartList.isChoosed()));
        holder.getTextView(R.id.num).setOnClickListener(v -> modifyCountInterface.numClick(position));

    }

    public void setItemData(int position, CartList newcartList) {
        list.set(position, newcartList);
        notifyDataSetChanged();
    }

    public void setModifyCountInterface(shopcartlistInterface.ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    private void setImg(SimpleDraweeView mImg, String url) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(CommonUtils.dip2px(context, 90), CommonUtils.dip2px(context, 90)))
                .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setOverlay(context.getResources().getDrawable(R.drawable.ic_nogoods))
                .build();
        mImg.setHierarchy(hierarchy);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mImg.getController())
                .setControllerListener(new BaseControllerListener<>())
                .setImageRequest(request).build();
        mImg.setController(controller);
    }
}
