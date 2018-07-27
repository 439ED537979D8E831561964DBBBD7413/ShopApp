package com.yj.shopapp.ui.activity.wholesale;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.dialog.WStopGoodsSelectDialogFragment;
import com.yj.shopapp.ui.activity.adapter.WStopGoodsPageAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.Classify;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/14.
 */
public class WStopGoodsActivity extends BaseActivity {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Classify> classLists = new ArrayList<>();
    private WStopGoodsPageAdpter pageAdpter;
    private int stop_status;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_wstopgoods;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleLayout)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {

        title.setText("暂停销售");
        if (getIntent().hasExtra("classlist")) {
            classLists.addAll(getIntent().getParcelableArrayListExtra("classlist"));
        }
        if (getIntent().hasExtra("stop_status")){
            stop_status=getIntent().getIntExtra("stop_status",0);
        }
        classLists.add(0, new Classify("0", "全部"));
        pageAdpter = new WStopGoodsPageAdpter(getSupportFragmentManager(), classLists,stop_status);
        viewpager.setAdapter(pageAdpter);
        tabsTl.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.id_right_btu)
    public void onViewClicked() {
        WStopGoodsSelectDialogFragment.newInstance(classLists).show(getFragmentManager(), "stopdialogfragment");
//        new MaterialDialog.Builder(mContext).title("请输入条码或商品名").input("", "", false, new MaterialDialog.InputCallback() {
//            @Override
//            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//
//            }
//        }).onPositive(new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                EventBus.getDefault().post(new MessgEvt(0, dialog.getInputEditText().getText().toString()));
//                dialog.dismiss();
//            }
//        }).show();
    }
}
