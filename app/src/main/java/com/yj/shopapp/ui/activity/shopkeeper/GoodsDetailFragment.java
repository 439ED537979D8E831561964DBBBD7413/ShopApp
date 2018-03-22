package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.GoodData;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsDetailFragment extends NewBaseFragment {

    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.shopbrand)
    TextView shopbrand;
    @BindView(R.id.barcode)
    TextView barcode;
    @BindView(R.id.shopspec)
    TextView shopspec;
    private GoodData.DataBean bean;
    private String Specs = "";

    public static GoodsDetailFragment newInstance(GoodData.DataBean bean) {

        Bundle args = new Bundle();
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    private GoodData.DataBean getBean() {
        return getArguments().getParcelable("bean");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        bean = getBean();

    }
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        shopname.setText(String.format("【商品名称】%s", bean.getName()));
        barcode.setText(String.format("【商品条码】%s", bean.getItemsum()));
        Specs = "";
        for (String s : bean.getSpecs()) {
            Specs += s + "\r";
        }
        shopspec.setText(String.format("【商品规格】%s", Specs));
        shopbrand.setText(String.format("【商品品牌】%s", bean.getBrand()));
    }

}
