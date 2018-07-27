package com.yj.shopapp.ui.activity.wholesale;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.dialog.WNewGoodsSelectDialogFragment;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.adapter.WNewGoodsPageAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;
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
 * Created by Administrator on 2016/8/30.
 */
public class WNewGoodAcitvity extends BaseActivity {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.newgoods_tablayout)
    TabLayout newgoodsTablayout;
    @BindView(R.id.flipping)
    ImageView flipping;
    @BindView(R.id.screenTv)
    RelativeLayout screenTv;
    @BindView(R.id.newgoods_vp)
    ViewPager newgoodsVp;
    @BindView(R.id.view_transparent)
    View viewTransparent;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private SearchView searchView;
    private WNewGoodsPageAdpter pageAdpter;
    private List<Classify> classLists = new ArrayList<>();
    private List<String> times = new ArrayList<>();
    private List<String> times2 = new ArrayList<>();
    private String[] name = {"默认", "升序", "降序"};

    private PopupWindow pw;
    private View itemView;
    private ScreenLvAdpter screenLvAdpter;
    private ScreenLvAdpter screenLvAdpter2;
    private ScreenLvAdpter screenLvAdpter3;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_wnewgood;
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

        title.setText("每日新品");
        if (getIntent().hasExtra("classlist")) {
            classLists.addAll(getIntent().getParcelableArrayListExtra("classlist"));
        }
        classLists.add(0, new Classify("0", "全部"));
        pageAdpter = new WNewGoodsPageAdpter(getSupportFragmentManager(), classLists);
        newgoodsVp.setAdapter(pageAdpter);
        newgoodsTablayout.setupWithViewPager(newgoodsVp);
        times = DateUtils.test(10, "MM-dd");
        times2 = DateUtils.test(10, "yyyy-MM-dd");
        times.add(0, "全部");
        initpw();
    }

    private void initpw() {
        screenLvAdpter = new ScreenLvAdpter(mContext, Arrays.asList(name));
        screenLvAdpter.setOnItemClickListener((parent, view, position, id) -> {
            EventBus.getDefault().post(new MessgEvt(1, String.valueOf(position)));
            screenLvAdpter.setDef(position);
            if (pw != null) {
                pw.dismiss();
            }
        });
        screenLvAdpter2 = new ScreenLvAdpter(mContext, times);
        screenLvAdpter2.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                EventBus.getDefault().post(new MessgEvt(2, ""));
            } else {
                EventBus.getDefault().post(new MessgEvt(2, times2.get(position - 1)));
            }
            screenLvAdpter2.setDef(position);
            if (pw != null) {
                pw.dismiss();
            }
        });
        screenLvAdpter3 = new ScreenLvAdpter(mContext, Arrays.asList(new String[]{"全部", "销售中", "停售中"}));
        screenLvAdpter3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                screenLvAdpter3.setDef(position);
                EventBus.getDefault().post(new MessgEvt(3, String.valueOf(position)));
                if (pw != null) {
                    pw.dismiss();
                }
            }
        });
        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view2, null);
        ((TextView) itemView.findViewById(R.id.classify_tv2)).setText("价格");
        RecyclerView recyclerView1 = itemView.findViewById(R.id.classify_2_rv);
        RecyclerView recyclerView2 = itemView.findViewById(R.id.status_2_rv);
        RecyclerView recyclerView3 = itemView.findViewById(R.id.status_3_rv);
        recyclerView1.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView2.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView3.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setAdapter(screenLvAdpter2);
        recyclerView3.setAdapter(screenLvAdpter3);
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
                WNewGoodsSelectDialogFragment.newInstance(classLists).show(getFragmentManager(), "newgoodsdialogfragment");
                break;
            case R.id.screenTv:
                showPW();
                viewTransparent.setVisibility(View.VISIBLE);
                break;
        }
    }
}
