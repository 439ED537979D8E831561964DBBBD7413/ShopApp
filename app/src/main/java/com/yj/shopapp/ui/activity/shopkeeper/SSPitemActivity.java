package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ubeen.Spitem;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.NewGoodRecyAdpter;
import com.yj.shopapp.ui.activity.adapter.SSPitemAdapter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.dialog.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by jm on 2016/6/21.
 */
public class SSPitemActivity extends BaseActivity implements SSPitemAdapter.OnViewClickListener, OnLoadMoreListener, OnRefreshListener {


    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    ImageView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.screenTv)
    RelativeLayout screenTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.view_transparent)
    View viewTransparent;
    @BindView(R.id.pullToRefresh)
    SmartRefreshLayout pullToRefresh;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.flipping)
    ImageView flipping;
    private int mCurrentPage = 1;
    private List<Spitem> spitemList = new ArrayList<>();
    private String cid = "0";
    private String price = "";
    private List<Industry> mdatas = new ArrayList<Industry>();
    private SSPitemAdapter oAdapter;
    private NewGoodRecyAdpter adpter;
    private View itemView;
    private String[] name = {"默认", "升序", "降序"};
    private PopupWindow pw;
    private ScreenLvAdpter screenLvAdpter, screenLvAdpter2;
    private RecyclerView recyclerView1, recyclerView2;
    private int currposition = 0;
    private int currposition2 = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_spitemgoods;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightTv.setVisibility(View.GONE);
        if (getIntent().hasExtra("industrylist")) {
            mdatas = getIntent().getParcelableArrayListExtra("industrylist");
        }
        contentTv.setText("促销特价");
        oAdapter = new SSPitemAdapter(mContext);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
            recyclerView.setAdapter(oAdapter);

        }
        oAdapter.setListener(this);
        Refresh();
        adpter = new NewGoodRecyAdpter(mContext, mdatas);
//        adpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                adpter.setTvColor(position);
//                mCurrentPage = 1;
//                cid = mdatas.get(position).getId();
//                spitemList.clear();
//                price = "";
//                pullToRefresh.setRefreshing(true);
//                refreshRequest();
//            }
//        });
        initpw();
        setTabLayout();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                currposition = position;
                spitemList.clear();
                oAdapter.setList(spitemList);
                if (pw != null) {
                    pw.dismiss();
                }
                if (position == 0) {
                    cid = "0";
                } else {
                    cid = mdatas.get(position - 1).getId();
                }
                mCurrentPage = 1;
                refreshRequest();
                currposition2 = 0;
                price = "";
                loading.showLoading();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (isNetWork(mContext)) {
            spitemList.clear();
            refreshRequest();
        }
    }

    private void Refresh() {
        pullToRefresh.setHeaderHeight(50);
        pullToRefresh.setFooterHeight(50);
        pullToRefresh.setOnRefreshListener(this);
        pullToRefresh.setOnLoadMoreListener(this);
        pullToRefresh.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        pullToRefresh.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private List<String> getlist() {
        List<String> list = new ArrayList<>();
        list.add("全部");
        for (Industry i : mdatas) {
            list.add(i.getName());
        }
        return list;
    }

    private void initpw() {
        screenLvAdpter = new ScreenLvAdpter(mContext);
        screenLvAdpter.setList(getlist());
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spitemList.clear();
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
                price = "";
                tabLayout.getTabAt(position).select();
                refreshRequest();
            }
        });
        screenLvAdpter2 = new ScreenLvAdpter(mContext);
        screenLvAdpter2.setList(Arrays.asList(name));
        screenLvAdpter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spitemList.clear();
                currposition2 = position;
                if (pw != null) {
                    pw.dismiss();
                }
                price = position + "";
                mCurrentPage = 1;
                refreshRequest();
            }
        });
        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view, null);
        ((TextView) itemView.findViewById(R.id.tagTv)).setText("价格");
        recyclerView1 = itemView.findViewById(R.id.recycler_view);
        recyclerView2 = itemView.findViewById(R.id.recycler_view_2);
        recyclerView1.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView2.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setAdapter(screenLvAdpter2);
    }

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        for (Industry i : mdatas) {
            tabLayout.addTab(tabLayout.newTab().setText(i.getName()));
        }
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        flipping.setRotation(180);
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
    }

    @OnClick(R.id.screenTv)
    public void onViewClicked() {
        showPW();
        viewTransparent.setVisibility(View.VISIBLE);
    }


    /***
     * 网络数据
     ***********************************************************/

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("price", price);
        params.put("cid", cid);
        //ShowLog.e("cid" + cid + "price" + price + "p" + mCurrentPage);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SPitem, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.finishLoadMore();
                    pullToRefresh.finishRefresh();
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (SSPitemActivity.this.isFinishing()) return;
                if (JsonHelper.isRequstOK(json, mContext)) {
                    spitemList.addAll(JSONArray.parseArray(json, Spitem.class));
                    oAdapter.setList(spitemList);
                    if (loading != null) {
                        loading.showContent();
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (spitemList.size() > 0) {
                        //showToastShort("没有更多的数据");
                        if (pullToRefresh != null) {
                            pullToRefresh.finishLoadMoreWithNoMoreData();
                        }
                        mCurrentPage--;
                    } else {
                        if (loading != null) {
                            loading.showEmpty();
                        }
                        oAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (pullToRefresh != null) {
                    pullToRefresh.finishLoadMore(false);
                }
                if (pullToRefresh != null) {
                    pullToRefresh.finishRefresh(false);
                }
                mCurrentPage--;
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.recy_item:
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", spitemList.get(position).getItemid());
                bundle.putString("unit", spitemList.get(position).getUnit());
                CommonUtils.goActivityForResult(mContext, SGoodsDetailActivity.class, bundle, 19, false);
                break;
            case R.id.addcartTv:
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
                    BugGoodsDialog.newInstance(spitemList.get(position)).show(getFragmentManager(), "123");
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (isNetWork(mContext)) {
            mCurrentPage++;
            refreshRequest();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isNetWork(mContext)) {
            spitemList.clear();
            mCurrentPage = 1;
            refreshRequest();
            pullToRefresh.setNoMoreData(false);
        }
    }

}
