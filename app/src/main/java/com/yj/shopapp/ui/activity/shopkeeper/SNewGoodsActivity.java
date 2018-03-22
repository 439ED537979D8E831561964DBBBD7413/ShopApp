package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.listeners.GroupListener;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.NewGoodRecyAdpter;
import com.yj.shopapp.ui.activity.adapter.SNewGoodsAdpter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DisplayUtil;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StickyDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/30.
 */
public class SNewGoodsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, GoodsItemListenter {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.first_low)
    TextView firstLow;
    @BindView(R.id.first_high)
    TextView firstHigh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.sort_price)
    TextView sortPrice;
    @BindView(R.id.IndustryList)
    RecyclerView IndustryList;
    @BindView(R.id.hide_view)
    LinearLayout hideView;
    @BindView(R.id.screenTv)
    TextView screenTv;
    @BindView(R.id.view_transparent)
    View viewTransparent;

    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();

    private String sort = "";
    private String price = "";
    private String cid = "0";
    private List<Industry> mdatas;
    private NewGoodRecyAdpter insdustryAdpter;
    private View FooterView;
    private SNewGoodsAdpter goodsAdpter;
    private PopupWindow pw;
    private ScreenLvAdpter screenLvAdpter;
    private ListView lv;

    private View itemView;
    private List<String> times = new ArrayList<>();
    private String daye="";
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_wnewgood;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("agentuid")) {
            agentuid = getIntent().getStringExtra("agentuid");
        }
        if (getIntent().hasExtra("industrylist")) {
            mdatas = getIntent().getParcelableArrayListExtra("industrylist");
        }
        //RequestDeta();
        title.setText("新品上市");
        //底部view
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterView = LayoutInflater.from(mContext).inflate(R.layout.footerview, null);
        FooterView.setLayoutParams(lp);

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(this);
        StickyDecoration decoration = StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //组名回调
                        if (goodsList.size() > position) {
                            //获取组名，用于判断是否是同一组
                            return goodsList.get(position).getDate();
                        }
                        return null;
                    }
                })
                .setGroupBackground(Color.parseColor("#f1f1f1"))
                .setGroupHeight(CommonUtils.dip2px(this, 36))
                .setDivideColor(Color.parseColor("#c2ccd0"))
                .setGroupTextColor(Color.parseColor("#000000"))
                .setDivideHeight(CommonUtils.dip2px(this, 1))
                .setGroupTextSize(DisplayUtil.sp2px(this, 13))
                .setTextSideMargin(CommonUtils.dip2px(this, 10))
                .build();
        goodsAdpter = new SNewGoodsAdpter(mContext, goodsList, true);
        goodsAdpter.setListenter(this);
        insdustryAdpter = new NewGoodRecyAdpter(mContext, mdatas);
        IndustryList.setLayoutManager(new GridLayoutManager(mContext, numberOfCalculatedColumns()));
        //IndustryList.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration4)));
        IndustryList.setAdapter(insdustryAdpter);
        insdustryAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodsList.clear();
                mCurrentPage = 1;
                insdustryAdpter.setTvColor(position);
                cid = mdatas.get(position).getId();
                sort = "";
                price = "";
                swipeRefreshLayout.setRefreshing(true);
                refreshRequest();
            }
        });
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(decoration);
            recyclerView.setAdapter(goodsAdpter);
            recyclerView.addOnScrollListener(new OnViewScrollListenter() {
                @Override
                public void onBottom() {
                    mCurrentPage++;
                    refreshRequest();
                }
            });
        }
        times = DateUtils.test(10);
        //daye = times.get(0);

        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        refreshRequest();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }

        screenLvAdpter = new ScreenLvAdpter(mContext);
        screenLvAdpter.setList(times);

        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view, null);
        lv = (ListView) itemView.findViewById(R.id.screenLV);
        lv.setAdapter(screenLvAdpter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                daye = times.get(position);
                goodsList.clear();
                refreshRequest();
                screenLvAdpter.setDefSelect(position);
                pw.dismiss();
            }
        });

    }


    private int numberOfCalculatedColumns() {
        int size = mdatas.size();
        if (size < 5) {
            return size;
        } else {
            if (size % 2 == 0) {
                return size / 2;
            } else {
                return 5;
            }
        }
    }

    /**
     * 数据加载
     */
    public void refreshRequest() {
        if (isRequesting) return;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        params.put("p", String.valueOf(mCurrentPage));
        //params.put("sort", sort);
        params.put("cid", cid);
        params.put("price", price);
        params.put("date", daye);
        //ShowLog.e(String.format("daye:%1$s cid:%2$s price:%3$s", daye, cid, price));
        //ShowLog.e("uid"+uid+"token"+token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.TODAYNEWITEMS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    //adapter.removeFooter(loadMoreView);
                    if (goodsList.size() > 6) {
                        goodsAdpter.setmFooterView(FooterView);
                    } else {
                        goodsAdpter.removeFooterView();
                    }
                    mCurrentPage--;
                } else {
                    mCurrentPage--;
                    showToastShort(Contants.NetStatus.NETLOADERROR);
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
        pw = new PopupWindow(itemView, 500, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.showAsDropDown(screenTv);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewTransparent.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefresh() {
        goodsList.clear();
        mCurrentPage = 1;
        refreshRequest();
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
                BugGoodsDialog.newInstance(goodsList.get(position)).show(getFragmentManager(),"123");
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {
        goodsList.get(position).setSelected(isChecked);
    }


}

