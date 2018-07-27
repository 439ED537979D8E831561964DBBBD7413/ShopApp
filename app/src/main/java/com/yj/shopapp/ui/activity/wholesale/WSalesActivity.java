package com.yj.shopapp.ui.activity.wholesale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.dialog.WPromotionGoodsSelectDialogFragment;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.Classify;
import com.yj.shopapp.wbeen.MessgEvt;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 * 促销
 */
public class WSalesActivity extends BaseActivity {


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
    @BindView(R.id.flipping)
    ImageView flipping;
    @BindView(R.id.screenTv)
    RelativeLayout screenTv;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.view_transparent)
    View viewTransparent;
    private List<Classify> classLists = new ArrayList<>();

    private PopupWindow pw;
    private View itemView;
    private ScreenLvAdpter screenLvAdpter;
    private ScreenLvAdpter screenLvAdpter2;
    private String[] status = {"默认", "未开始", "促销中", "已过期"};
    private int sales_status;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_sales;
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

        title.setText("我的促销");
        if (getIntent().hasExtra("classlist")) {
            classLists.addAll(getIntent().getParcelableArrayListExtra("classlist"));
        }
        if (getIntent().hasExtra("sales_status")) {
            sales_status = getIntent().getIntExtra("sales_status", 0);
        }
        classLists.add(0, new Classify("0", "全部"));
        TabAdapter tabAdapter = new TabAdapter(mContext, getSupportFragmentManager());
        viewpager.setAdapter(tabAdapter);
        tabsTl.setupWithViewPager(viewpager);
        initpw();
    }

    private void initpw() {
        screenLvAdpter = new ScreenLvAdpter(mContext, Arrays.asList(status));
        screenLvAdpter2 = new ScreenLvAdpter(mContext, Arrays.asList(new String[]{"全部", "销售中", "停售中"}));
        screenLvAdpter.setOnItemClickListener((parent, view, position, id) -> {
            EventBus.getDefault().post(new MessgEvt(1, String.valueOf(position)));
            screenLvAdpter.setDef(position);
            if (pw != null) {
                pw.dismiss();
            }
        });
        screenLvAdpter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                screenLvAdpter2.setDef(position);
                EventBus.getDefault().post(new MessgEvt(2, String.valueOf(position)));
                if (pw != null) {
                    pw.dismiss();
                }
            }
        });
        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view, null);
        RecyclerView recyclerView1 = itemView.findViewById(R.id.recycler_view);
        RecyclerView recyclerView2 = itemView.findViewById(R.id.recycler_view_2);
        ((TextView) itemView.findViewById(R.id.classify_tv)).setText("促销状态");
        ((TextView) itemView.findViewById(R.id.tagTv)).setText("商品状态");
        recyclerView1.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView2.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setAdapter(screenLvAdpter2);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        pw = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.showAsDropDown(screenTv);
        pw.setOnDismissListener(() -> {
            viewTransparent.setVisibility(View.GONE);
            flipping.setRotation(0);
        });
        flipping.setRotation(180);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_view, menu);
//        //找到searchView
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setQueryHint("请输入商品名");
//        mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);
//
//        //设置输入框内容文字和提示文字的颜色
//        mSearchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
//        mSearchAutoComplete.setTextSize(15);
//        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                EventBus.getDefault().post(new MessgEvt(0, newText));
//                return true;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

    @OnClick({R.id.id_right_btu, R.id.screenTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_right_btu:
//                new MaterialDialog.Builder(mContext).title("请输入条码或商品名").input("", "", false, new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//
//                    }
//                }).onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        EventBus.getDefault().post(new MessgEvt(0, dialog.getInputEditText().getText().toString()));
//                        dialog.dismiss();
//                    }
//                }).show();
                WPromotionGoodsSelectDialogFragment.newInstance(classLists).show(getFragmentManager(), "promotionDialogFragment");
                break;
            case R.id.screenTv:
                showPW();
                viewTransparent.setVisibility(View.VISIBLE);
                break;
        }
    }


    public class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WSalesViewActivity.newInstance(classLists.get(position).getCid(), sales_status);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return classLists.get(position).getName();
        }

        @Override
        public int getCount() {
            return classLists.size();
        }
    }
}
