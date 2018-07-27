package com.yj.shopapp.ui.activity.wholesale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/11/11.
 */

public class WLikeCkeckActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //    @BindView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.value_Et)
    EditText value_Et;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新
    private int mCurrentPage = 0;
    private List<Goods> goodsList = new ArrayList<>();
    private WGoodsAdapter oAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_likecheck;
    }

    @Override
    protected void initData() {
        title.setText("条码模糊查询");
        oAdapter = new WGoodsAdapter(mContext);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(oAdapter);
        }
        Refresh();
        oAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("itemnoid", goodsList.get(position).getNumberid());
                bundle.putString("id", goodsList.get(position).getId());
                CommonUtils.goActivityForResult(mContext, WGoodsDetailActivity.class, bundle, 0, false);
            }
        });
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnRefreshListener(v -> refreshRequest());
        smartRefreshLayout.setOnLoadMoreListener(v -> loadMoreRequest());
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @OnClick(R.id.submitTv)
    public void sumitTvOnclick() {
        refreshRequest();
    }

    /***
     * 网络数据
     ***********************************************************/

    public void refreshRequest() {
        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", value_Et.getText().toString().trim().replace(" ", ""));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getitemsbynumber, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
                goodsList.clear();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                    oAdapter.setList(goodsList);
                    if (goodsList.size() == 0) {
                        showToastShort("没有任何记录！");
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    showToastShort("没有任何记录！");
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                goodsList.clear();
            }
        });

    }

    public void loadMoreRequest() {
        if (isRequesting)
            return;
        if (goodsList.size() < 10) {
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", value_Et.getText().toString().trim().replace(" ", ""));

        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
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

               // System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    if (jsonHelper.getDatas(json).size() == 0) {
                    } else {
                        goodsList.addAll(jsonHelper.getDatas(json));
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {

                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
            }
        });
    }

//    /**
//     * 保存购物车
//     */
//    public void saveDolistcart(String itemid, String itemsum) {
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("itemid", itemid);
//        params.put("itemsum", itemsum);
//
//        final KProgressHUD kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);
//
//        HttpHelper.getInstance().post(mContext, Contants.PortU.DOLISTCART, params, new OkHttpResponseHandler<String>(mContext) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                kProgressHUD.dismiss();
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                kProgressHUD.show();
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//
//                System.out.println("response" + json);
//
//                if (JsonHelper.isRequstOK(json, mContext)) {
//                    // CommonUtils.setBroadCast(mContext, Contants.Bro.REFRESH_CARTLIST);
//                    showToastShort("加入购物车成功");
//                } else {
//                    showToastShort(JsonHelper.errorMsg(json));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//            }
//        });
//    }

    /*********
     * 弹出框
     ***********/

    TextView unitTv;
    EditText inputEt;

    public void setDialogInput(final int pos) {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .customView(R.layout.dialog_add, true)
                .positiveText(R.string.right)
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String str = inputEt.getText().toString();
                        if (StringHelper.isEmpty(str)) {
                            showToastShort("请填写购买数量");
                        } else {
                            long num = Long.parseLong(str);
                            if (num > 0) {
                                //saveDolistcart(goodsList.get(pos).getId(), str);
                                dialog.dismiss();
                            } else {
                                showToastShort("购买数量必须大于0");
                            }
                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                        materialDialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .build();


        unitTv = (TextView) dialog.getCustomView().findViewById(R.id.unitTv);
        inputEt = (EditText) dialog.getCustomView().findViewById(R.id.inputEt);
        unitTv.setText(goodsList.get(pos).getId());
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            value_Et.setFocusable(true);
            value_Et.requestFocus();
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


//    @Override
//    public void CardClick(int postion) {
//        setDialogInput(postion);
//    }

//    @Override
//    public void onItemClick(int position) {
//
//    }

}
