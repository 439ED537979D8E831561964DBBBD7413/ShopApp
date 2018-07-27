package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gavin.com.library.StickyDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BugGoodsDialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.NewGoodRecyAdpter;
import com.yj.shopapp.ui.activity.adapter.SNewGoodsAdpter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DisplayUtil;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by Administrator on 2016/8/30.
 */
public class SNewGoodsActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, GoodsItemListenter {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.screenTv)
    RelativeLayout screenTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.view_transparent)
    View viewTransparent;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.flipping)
    ImageView flipping;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;

    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();

    private String sort = "";
    private String price = "";
    private String cid = "0";
    private List<Industry> mdatas;
    private NewGoodRecyAdpter insdustryAdpter;
    private SNewGoodsAdpter goodsAdpter;
    private PopupWindow pw;
    private ScreenLvAdpter screenLvAdpter, screenLvAdpter2;

    private View itemView;
    private List<String> times = new ArrayList<>();
    private List<String> times2 = new ArrayList<>();
    private String daye = "";
    private RecyclerView recyclerView1, recyclerView2;
    private int currposition = 0;
    private int currposition2 = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_snewgood;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {

        title.setText("每日新品");
        if (getIntent().hasExtra("agentuid")) {
            agentuid = getIntent().getStringExtra("agentuid");
        }
        if (getIntent().hasExtra("industrylist")) {
            mdatas = getIntent().getParcelableArrayListExtra("industrylist");
        }
        Refresh();
        StickyDecoration decoration = StickyDecoration.Builder
                .init(position -> {
                    //组名回调
                    //获取组名，用于判断是否是同一组
                    if (goodsList.size() > 0) {
                        return goodsList.get(position).getDate();
                    }
                    return "";
                })
                .setGroupBackground(Color.parseColor("#f4f5f9"))
                .setGroupHeight(CommonUtils.dip2px(this, 34))
                .setDivideColor(Color.parseColor("#E3E3E3"))
                .setGroupTextColor(Color.parseColor("#000000"))
                .setDivideHeight(1)
                .setGroupTextSize(DisplayUtil.sp2px(this, 14))
                .setTextSideMargin(CommonUtils.dip2px(this, 14))
                .build();
        goodsAdpter = new SNewGoodsAdpter(mContext, goodsList, true);
        goodsAdpter.setListenter(this);
        insdustryAdpter = new NewGoodRecyAdpter(mContext, mdatas);
        insdustryAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodsList.clear();
                mCurrentPage = 1;
                insdustryAdpter.setTvColor(position);
                cid = mdatas.get(position).getId();
                sort = "";
                price = "";
                swipeRefreshLayout.autoRefresh();
            }
        });
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(decoration);
            recyclerView.setAdapter(goodsAdpter);
        }
        times = DateUtils.test(10, "MM-dd");
        times2 = DateUtils.test(10, "yyyy-MM-dd");
        times.add(0, "全部");
        initpw();
        setTabLayout();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                currposition = position;
                goodsList.clear();
                daye = "";
                if (pw != null) {
                    pw.dismiss();
                }
                if (position == 0) {
                    cid = "0";
                } else {
                    cid = mdatas.get(position - 1).getId();
                }
                currposition2 = 0;
                mCurrentPage = 1;
                loading.showLoading();
                refreshRequest();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (isNetWork(mContext)) {
            goodsList.clear();
            mCurrentPage = 1;
            refreshRequest();
        }
    }


    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void initpw() {
        screenLvAdpter = new ScreenLvAdpter(mContext);
        screenLvAdpter.setList(getlist());
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodsList.clear();
                currposition = position;
                if (pw != null) {
                    pw.dismiss();
                }
                if (position == 0) {
                    cid = "0";
                } else {
                    cid = mdatas.get(position - 1).getId();
                }
                mCurrentPage = 1;
                tabLayout.getTabAt(position).select();
                refreshRequest();
            }
        });
        screenLvAdpter2 = new ScreenLvAdpter(mContext);
        screenLvAdpter2.setList(times);
        screenLvAdpter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodsList.clear();
                if (pw != null) {
                    pw.dismiss();
                }
                currposition2 = position;
                if (position == 0) {
                    daye = "";
                } else {
                    daye = times2.get(position - 1);
                }
                mCurrentPage = 1;
                refreshRequest();
            }
        });
        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view, null);
        recyclerView1 = itemView.findViewById(R.id.recycler_view);
        recyclerView2 = itemView.findViewById(R.id.recycler_view_2);
        recyclerView1.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView2.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setAdapter(screenLvAdpter2);
    }

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        for (Industry i : mdatas) {
            tabLayout.addTab(tabLayout.newTab().setText(i.getName()));
        }
    }

    private List<String> getlist() {
        List<String> list = new ArrayList<>();
        list.add("全部");
        for (Industry i : mdatas) {
            list.add(i.getName());
        }
        return list;
    }

    /**
     * 数据加载
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("cid", cid);
        params.put("price", price);
        params.put("date", daye);
        //ShowLog.e("cid" + cid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.TODAYNEWITEMS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (SNewGoodsActivity.this.isFinishing()) return;
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (loading != null) {
                        loading.showContent();
                    }
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    if (goodsList.size() == 0) {
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    }
                    mCurrentPage--;
                } else {

                    mCurrentPage--;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishRefresh(false);
                        swipeRefreshLayout.finishLoadMore(false);
                    }
                }
                goodsAdpter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                goodsList.clear();
                mCurrentPage--;
                goodsAdpter.notifyDataSetChanged();
            }
        });

    }

    @OnClick(R.id.screenTv)
    public void onViewClicked() {
        showPW();
        viewTransparent.setVisibility(View.VISIBLE);
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
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewTransparent.setVisibility(View.GONE);
                flipping.setRotation(0);
            }
        });
        screenLvAdpter.setDef(currposition);
        screenLvAdpter2.setDef(currposition2);
        flipping.setRotation(180);
    }

    @Override
    public void onClick(View V, int position) {
        switch (V.getId()) {
            case R.id.itemview:
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", goodsList.get(position).getId());
                CommonUtils.goActivity(mContext, SGoodsDetailActivity.class, bundle, false);
                break;
            case R.id.addcartTv:
                //showAlertDialog(position);
                if (getAddressId().equals("")) {
                    new MaterialDialog.Builder(mContext).title("温馨提示!")
                            .content("您暂未添加收货地址!")
                            .positiveText("去完善信息")
                            .negativeText("下次吧")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    CommonUtils.goActivity(mContext, SAddressRefreshActivity.class, null);
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    BugGoodsDialog.newInstance(goodsList.get(position)).show(getFragmentManager(), "123");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {
        goodsList.get(position).setSelected(isChecked);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        goodsList.clear();
        refreshRequest();
        swipeRefreshLayout.setNoMoreData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        refreshRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pw = null;
        itemView = null;
    }

}

