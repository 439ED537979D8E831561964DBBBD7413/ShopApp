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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ubeen.Spitem;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.NewGoodRecyAdpter;
import com.yj.shopapp.ui.activity.adapter.SSPitemAdapter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/6/21.
 */
public class SSPitemActivity extends BaseActivity implements SSPitemAdapter.OnViewClickListener, SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.IndustryListRV)
    RecyclerView IndustryListRV;
    @BindView(R.id.screenTv)
    TextView screenTv;
    @BindView(R.id.view_transparent)
    View viewTransparent;

    private int mCurrentPage = 1;
    private List<Spitem> spitemList = new ArrayList<>();
    private String cid = "0";
    private String price = "";
    private List<Industry> mdatas = new ArrayList<Industry>();
    private SSPitemAdapter oAdapter;
    private NewGoodRecyAdpter adpter;
    private View FooterView;
    private View itemView;
    private ListView lv;
    private String[] name = {"默认排序", "价格升序", "价格降序"};
    private PopupWindow pw;
    private ScreenLvAdpter screenLvAdpter;
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_spitemgoods;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("industrylist")) {
            mdatas = getIntent().getParcelableArrayListExtra("industrylist");
        }
        title.setText("促销特价");
        oAdapter = new SSPitemAdapter(mContext);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext));
            recyclerView.setAdapter(oAdapter);
            recyclerView.addOnScrollListener(new OnViewScrollListenter() {
                @Override
                public void onBottom() {
                    mCurrentPage++;
                    refreshRequest();
                }
            });

        }
        oAdapter.setListener(this);
        pullToRefresh.setOnRefreshListener(this);

        adpter = new NewGoodRecyAdpter(mContext, mdatas);
        IndustryListRV.setLayoutManager(new GridLayoutManager(mContext, numberOfCalculatedColumns()));
        //IndustryListRV.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration4)));
        IndustryListRV.setAdapter(adpter);
        adpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adpter.setTvColor(position);
                mCurrentPage = 1;
                cid = mdatas.get(position).getId();
                spitemList.clear();
                price = "";
                pullToRefresh.setRefreshing(true);
                refreshRequest();
            }
        });
        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != pullToRefresh) {
                pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(true);
                        refreshRequest();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterView = LayoutInflater.from(mContext).inflate(R.layout.footerview, null);
        FooterView.setLayoutParams(lp);

        screenLvAdpter = new ScreenLvAdpter(mContext);
        screenLvAdpter.setList(Arrays.asList(name));
        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view, null);
        lv = (ListView) itemView.findViewById(R.id.screenLV);
        lv.setAdapter(screenLvAdpter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        spitemList.clear();
                        cid = "0";
                        price = "";
                        pullToRefresh.setRefreshing(true);
                        refreshRequest();
                        break;
                    case 1:
                        priceSort(1);
                        break;
                    case 2:
                        priceSort(2);
                        break;
                    default:
                        break;
                }
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
    protected void onResume() {
        super.onResume();
        if (spitemList != null) {
            oAdapter.setList(spitemList);
        } else {
            if (NetUtils.isNetworkConnected(mContext)) {
                if (null != pullToRefresh) {
                    pullToRefresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullToRefresh.setRefreshing(true);
                            refreshRequest();
                        }
                    }, 200);
                }
            } else {
                showToastShort("网络不给力");
            }
        }
    }


    @OnClick(R.id.screenTv)
    public void onViewClicked() {
        showPW();
        viewTransparent.setVisibility(View.VISIBLE);
    }

    /**
     * 价格排序
     *
     * @param RiseAndFall
     */
    private void priceSort(int RiseAndFall) {
        spitemList.clear();
        if (RiseAndFall == 1) {
            price = "1";
        } else {
            price = "2";
        }
        pullToRefresh.setRefreshing(true);
        refreshRequest();
    }

    @Override
    public void onRefresh() {
        spitemList.clear();
        mCurrentPage = 1;
        refreshRequest();
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
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Spitem> jsonHelper = new JsonHelper<Spitem>(Spitem.class);
                    spitemList.addAll(jsonHelper.getDatas(json));
                    oAdapter.setList(spitemList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (spitemList.size() > 0) {
                        //showToastShort("没有更多的数据");
                        if (spitemList.size() > 6) {
                            oAdapter.setmFooterView(FooterView);
                        } else {
                            oAdapter.removeFooterView();
                        }
                        mCurrentPage--;
                    } else {
                        showToastShort("无数据");
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
                    pullToRefresh.setRefreshing(false);
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
                BugGoodsDialog.newInstance(spitemList.get(position)).show(getFragmentManager(),"123");
                break;
            default:
                break;
        }
    }


}
