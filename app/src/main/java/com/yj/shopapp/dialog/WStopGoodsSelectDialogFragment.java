package com.yj.shopapp.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.adapter.WStopGoodsAdapter;
import com.yj.shopapp.ui.activity.wholesale.WGoodsDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.ClearEditText;
import com.yj.shopapp.wbeen.Classify;
import com.yj.shopapp.wbeen.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2018/6/10.
 *
 * @author LK
 */
public class WStopGoodsSelectDialogFragment extends BaseDialogFragment implements OnItemChildViewOnClickListener {
    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    @BindView(R.id.goods_recy)
    RecyclerView goodsRecy;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.translucent_mask)
    View translucentMask;


    private List<Classify> classLists = new ArrayList<>();
    private ScreenLvAdpter screenLvAdpter;
    private WStopGoodsAdapter stopGoodsAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private int mCurrentPage = 1;
    private String keyword = "";
    private String cid;
    private PopupWindow pw;
    private View itemView;

    public static WStopGoodsSelectDialogFragment newInstance(List<Classify> classLists) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("classify", (ArrayList<? extends Parcelable>) classLists);
        WStopGoodsSelectDialogFragment fragment = new WStopGoodsSelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.df_base_select_itemview;
    }

    @Override
    protected void initData() {
        valueEt.setHint("请输入商品名或条码");
        classLists = getArguments().getParcelableArrayList("classify");
        screenLvAdpter = new ScreenLvAdpter(mActivity, classLists);
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = classLists.get(position).getCid();
                screenLvAdpter.setDef(position);
            }
        });
        stopGoodsAdapter = new WStopGoodsAdapter(mActivity, this);
        goodsRecy.setLayoutManager(new LinearLayoutManager(mActivity));
        goodsRecy.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        goodsRecy.setAdapter(stopGoodsAdapter);
        Refresh();
        initpw();
        if (loading != null) {
            loading.showContent();
        }
        translucentMask.setVisibility(View.VISIBLE);
        new Handler().postDelayed(this::showPW, 200);
        valueEt.setOnClickListener(v -> {
            if (pw != null && pw.isShowing()) {

            } else {
                showPW();
                translucentMask.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initpw() {
        itemView = LayoutInflater.from(mActivity).inflate(R.layout.screening_view, null);
        RecyclerView recyclerView1 = itemView.findViewById(R.id.recycler_view);
        itemView.findViewById(R.id.recycler_view_2).setVisibility(View.GONE);
       itemView.findViewById(R.id.tagTv).setVisibility(View.GONE);
        recyclerView1.setLayoutManager(new GridLayoutManager(mActivity, 4));
        recyclerView1.setAdapter(screenLvAdpter);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        pw = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(false);
        pw.setTouchable(true);
        pw.showAsDropDown(titleView);
        pw.setOnDismissListener(() -> translucentMask.setVisibility(View.GONE));
    }

    private void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("cid", cid);
        params.put("keyword", keyword);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.STOPLIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.setEnableLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (!json.equals("[]")) {
                    goodsList.addAll(JSONArray.parseArray(json, Goods.class));
                    stopGoodsAdapter.setList(goodsList);
                } else {
                    if (goodsList.size() == 0) {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                        }
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    } else {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishLoadMore();
                        }
                    }
                    mCurrentPage--;
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
                mCurrentPage--;
            }
        });

    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            mCurrentPage++;
            refreshRequest();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void delClient(int pos) {
        //显示ProgressDialog
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", goodsList.get(pos).getId());

        HttpHelper.getInstance().post(mActivity, Contants.PortA.delstopitem, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.errorNo(json).equals("0")) {
                    refreshRequest();
                } else if (JsonHelper.getRequstOK(json) == 6) {

                } else {
                    Toast.makeText(mActivity, JsonHelper.errorMsg(json), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Toast.makeText(mActivity, Contants.NetStatus.NETDISABLEORNETWORKDISABLE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.finish_tv, R.id.select_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_tv:
                if (pw != null) {
                    pw.dismiss();
                }
                dismiss();
                break;
            case R.id.select_tv:
                keyword = valueEt.getText().toString();
                mCurrentPage = 1;
                goodsList.clear();
                if (pw != null) {
                    pw.dismiss();
                }
                hideImm(valueEt);
                refreshRequest();
                break;
        }
    }

    public void hideImm(EditText valuet) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(valuet.getWindowToken(), 0);
    }

    private void showDelectClint(final int position) {
        new MaterialDialog.Builder(mActivity)
                .content("是否将" + goodsList.get(position).getName() + "上架销售")
                .positiveText("是")
                .negativeText("否")
                .onPositive((materialDialog, dialogAction) -> delClient(position))
                .show();
    }

    @Override
    public void onChildViewClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.delect_tv:
                if (goodsList.get(position).getSale_status().equals("1")) {
                    showDelectClint(position);
                }
                break;
            case R.id.stopgoods_recycler_item:
                Bundle bundle = new Bundle();
                bundle.putString("itemnoid", goodsList.get(position).getNumberid());
                bundle.putString("id", goodsList.get(position).getId());
                CommonUtils.goActivity(mActivity, WGoodsDetailActivity.class, bundle);
                break;
        }
    }

}
