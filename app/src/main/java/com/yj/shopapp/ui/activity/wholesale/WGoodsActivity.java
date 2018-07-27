package com.yj.shopapp.ui.activity.wholesale;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.WGoodsSearchDialogFragment;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.Goods;
import com.yj.shopapp.wbeen.Itemtype;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 * <p/>
 * 我的商品
 */
public class WGoodsActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.id_drawer_layout)
    LinearLayout idDrawerLayout;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;

    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    private List<Itemtype> itemtypeList = new ArrayList<>();
    private List<String> tArray = new ArrayList<>();
    private String username = "";
    private String type = "";
    private String categoryId;//商品分类ID
    private String keyWord;//首页关键词搜索
    private String bigtypeid = "";
    private int CurrentPosition = -1;
    private String titlename = "我的商品";
    private WGoodsAdapter oAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.wtab_goods;
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
        if (getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getStringExtra("categoryId");
        }
        if (getIntent().hasExtra("bigtypeid")) {
            bigtypeid = getIntent().getStringExtra("bigtypeid");
        }

        if (getIntent().hasExtra("keyword")) {
            keyWord = getIntent().getStringExtra("keyword");
        }
        if (getIntent().hasExtra("name")) {
            titlename = getIntent().getStringExtra("name");
        }
        title.setText(titlename);
        if (getIntent().hasExtra("bigtypeName")) {
            title.setText(getIntent().getStringExtra("bigtypeName"));
        }
        Refresh();
        oAdapter = new WGoodsAdapter(mContext);
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(oAdapter);
        }
        oAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("itemnoid", goodsList.get(position).getNumberid());
                bundle.putString("id", goodsList.get(position).getId());
                CommonUtils.goActivity(mContext, WGoodsDetailActivity.class, bundle, false);
                CurrentPosition = position;
            }
        });
        //loadItemtype(false);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Contants.Bro.CLEAR_ALL_FACTOR_GOODS_W);
        if (isNetWork(mContext)) {
            refreshRequest();
        }
        EventBus.getDefault().register(this);
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(v -> {
            mCurrentPage = 1;
            goodsList.clear();
            refreshRequest();
        });
        swipeRefreshLayout.setOnLoadMoreListener(v -> {
            mCurrentPage++;
            refreshRequest();
        });
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Goods Goods) {
        Goods mGoods = goodsList.get(CurrentPosition);
        //ShowLog.e(mGoods.toString());
        mGoods.setItemsum(Goods.getItemsum());
        mGoods.setName(Goods.getName());
        mGoods.setPrice(Goods.getPrice());
        mGoods.setSale_status(Goods.getSale_status());
        if (Goods.getImgurl() != null && !"".equals(Goods.getImgurl())) {
            mGoods.setImgurl(Goods.getImgurl());
        }
        oAdapter.notifyItemChanged(CurrentPosition);
    }

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("type", type == null ? "" : type);
        params.put("itemname", username);
        params.put("typeid", categoryId);//96
        params.put("industryid", bigtypeid);
        params.put("keyword", keyWord);
        ShowLog.e("uid"+uid+"|token"+token+"|p"+mCurrentPage+"|type"+type+"|itemname"+username+"|typeid"+categoryId+"|industryid"+bigtypeid+"|keyword"+keyWord);
        HttpHelper.getInstance().post(mContext, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                    swipeRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                    oAdapter.setList(goodsList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishLoadMore();
                    }
                } else {
                    showToastShort(Contants.NetStatus.NETLOADERROR);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                    swipeRefreshLayout.finishLoadMore();
                }
            }
        });

    }

//    public void loadMoreRequest() {
//        if (isRequesting)
//            return;
//        if (goodsList.size() < 10) {
//            return;
//        }
//        mCurrentPage++;
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("p", String.valueOf(mCurrentPage));
//        params.put("type", type == null ? "" : type);
//        params.put("itemname", username);
//        params.put("typeid", categoryId);//96
//        params.put("industryid", bigtypeid);
//        params.put("keyword", keyWord);
//        HttpHelper.getInstance().post(mContext, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                isRequesting = false;
//                if (swipeRefreshLayout != null) {
//                    swipeRefreshLayout.finishLoadMore();
//                }
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                isRequesting = true;
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//                ShowLog.e(json);
//                if (JsonHelper.isRequstOK(json, mContext)) {
//                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
//                    if (jsonHelper.getDatas(json).size() == 0) {
//
//                    } else {
//                        goodsList.addAll(jsonHelper.getDatas(json));
//                        oAdapter.setList(goodsList);
//                    }
//                } else if (JsonHelper.getRequstOK(json) == 6) {
//
//                } else {
//                    mCurrentPage--;
//                    showToastShort(Contants.NetStatus.NETLOADERROR);
//                }
//
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//                mCurrentPage--;
//
//            }
//        });
//    }
//    /**
//     * 加载商品分类
//     */
//    public void loadItemtype(final boolean isShow) {
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//        //显示ProgressDialog
//        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);
//
//        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMTYPE, params, new OkHttpResponseHandler<String>(mContext) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                isRequestingType = false;
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                isRequestingType = true;
//                if (isShow) {
//                    progressDialog.show();
//                }
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//
//                ShowLog.e(json);
//                if (JsonHelper.isRequstOK(json, mContext)) {
//                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
//                    itemtypeList = jsonHelper.getDatas(json);
//                    PreferenceUtils.setPrefString(mContext, Contants.Preference.ITEMTYPE, json);
//                    if (isShow) {
//                        mItemtype = null;
//                        showChooseify("选择商品类型", "0");
//                    }
//                } else {
//                    if (StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {
//
//                    } else {
//                        json = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
//                        JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
//                        itemtypeList = jsonHelper.getDatas(json);
//                        if (isShow) {
//                            mItemtype = null;
//                            showChooseify("选择商品类型", "0");
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                if (StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {
//
//                } else {
//                    String sjson = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
//                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
//                    itemtypeList = jsonHelper.getDatas(sjson);
//                    if (isShow) {
//                        mItemtype = null;
//                        showChooseify("选择商品类型", "0");
//                    }
//                }
//            }
//        });
//    }

//    /**************************
//     * 弹出框
//     *****************/
//
//    Itemtype mItemtype;
//
//    public void showChooseify(String title, String id) {
//        final List<Itemtype> list = new ArrayList<>();
//        Itemtype item = new Itemtype();
//        for (Itemtype itemtype : itemtypeList) {
//            if (itemtype.getPid().equals(id)) {
//                list.add(itemtype);
//            }
//            if (itemtype.getId().equals(id)) {
//                item = itemtype;
//            }
//        }
//        if (list.size() == 0) {
//            type = item.getId();
//            if (null != swipeRefreshLayout) {
//                swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        refreshRequest();
//                    }
//                }, 50);
//            }
//            return;
//        }
//        final String[] array;
//        if (mItemtype == null) {
//            array = new String[list.size()];
//            int i = 0;
//            for (Itemtype itemtype : list) {
//                array[i] = itemtype.getName();
//                i++;
//            }
//        } else {
//            array = new String[list.size() + 1];
//            int i = 1;
//            array[0] = mItemtype.getName();
//            for (Itemtype itemtype : list) {
//                array[i] = itemtype.getName();
//                i++;
//            }
//        }
//
//        new MaterialDialog.Builder(mContext)
//                .title(title)
//                .items(array)
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        if (mItemtype != null && mItemtype.getName().equals(array[which])) {
//                            type = mItemtype.getId();
//                            if (null != swipeRefreshLayout) {
//                                swipeRefreshLayout.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        refreshRequest();
//                                    }
//                                }, 50);
//                            }
//                        } else {
//                            if (mItemtype == null) {
//                                mItemtype = list.get(which);
//                                showChooseify(text == null ? "" : text.toString(), list.get(which).getId());
//                            } else {
//                                mItemtype = list.get(which - 1);
//                                showChooseify(text == null ? "" : text.toString(), list.get(which - 1).getId());
//                            }
//
//                        }
//                    }
//                })
//                .positiveText("重置")
//
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        type = "";
//                        if (null != swipeRefreshLayout) {
//                            swipeRefreshLayout.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    refreshRequest();
//                                }
//                            }, 50);
//                        }
//                    }
//                })
//                .show();
//    }

    @OnClick(R.id.id_right_btu)
    public void openDrawer() {
        WGoodsSearchDialogFragment.newInstance(bigtypeid).show(getFragmentManager(), "");
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
