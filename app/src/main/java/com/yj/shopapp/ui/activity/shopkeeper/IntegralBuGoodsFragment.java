package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ubeen.ExcGoods;
import com.yj.shopapp.ubeen.IntegralInfo;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegraAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.NoticeMF2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class IntegralBuGoodsFragment extends NewBaseFragment implements IntegraAdapter.OnViewClickListener {

    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.hot_mv)
    MarqueeView hotMv;
    private IntegraAdapter adapter;
    private ExcGoods mData;
    private String goodnumber = "";
    private String site;
    private List<String> mdeta = new ArrayList<>();
    private IntegralInfo integralInfo;
    private long Integral;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integral_bu_goods;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new IntegraAdapter(mActivity, this);
        if (myRecyclerView != null) {
            myRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
            myRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void initData() {

        if (NetUtils.isNetworkConnected(mActivity)) {
            getgoodlist();
            getEveryBody();
            getIntegral();
            getSite();
        } else {
            showToast("无网络");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mData != null) {
//            adapter.setList(mData.getData());
//        } else {
//            initData();
//        }
//    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.Redeem_now:
                if (Long.parseLong(mData.getData().get(position).getIntegral()) < Integral) {
                    showInputDialog(position);
                } else {
                    showToast("积分不够");
                }
                break;
            case R.id.onItemclick:
                Bundle bundle = new Bundle();
                bundle.putString("site", site);
                bundle.putString("gid", mData.getData().get(position).getId());
                bundle.putString("url", mData.getData().get(position).getDetails());
                CommonUtils.goActivity(mActivity, ExchangeOfGoodsDetails.class, bundle);
                break;
        }

    }

    private void getIntegral() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.UserIntegral, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<IntegralInfo> jsonHelper = new JsonHelper(IntegralInfo.class);
                    integralInfo = jsonHelper.getData(json, null);
                    Integral = integralInfo.getIntegral();
                }
            }
        });
    }

    private void showInputDialog(final int position) {
        goodnumber = "";
        new MaterialDialog.Builder(mActivity)
                .title("请输入换购数量")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("请输入数量", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        goodnumber = dialog.getInputEditText().getText().toString();
                        if (!"".equals(goodnumber)) {
                            changeGoods(mData.getData().get(position).getId());
                        }
                    }
                })
                .canceledOnTouchOutside(false)
                .show();
    }

    private void changeGoods(String gid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("goods_id", gid);
        params.put("num", goodnumber);
        params.put("addressid", site);
        //ShowLog.e(String.format("%1$s|%2$s|%3$s|%4$s|%5$s",uid,token,gid,goodnumber,site));
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CHANGE_GOODS, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                showToast(JSONObject.parseObject(json).getString("info"));
            }

            @Override
            public void onAfter() {
                super.onAfter();
                getgoodlist();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void getEveryBody() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.EVERYBODY_CHANGING, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    if (!"[]".equals(json)) {
                        mdeta = JSONArray.parseArray(json, String.class);
                        //文字向左翻转
                        final MarqueeFactory<TextView, String> marqueeFactory2 = new NoticeMF2(mActivity);
                        marqueeFactory2.resetData(mdeta);
                        //MarqueeView设置Factory
                        hotMv.setMarqueeFactory(marqueeFactory2);
                        hotMv.startFlipping();

                    }
                }

            }
        });
    }

    private void getgoodlist() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.GOODS, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    mData = JSONObject.parseObject(json, ExcGoods.class);
                    adapter.setList(mData.getData());
                }
            }
        });
    }

    /**
     * 获取地址
     */
    private void getSite() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Uaddress, params, new OkHttpResponseHandler<String>(mActivity) {

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
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    site = JSONArray.parseArray(json, Address.class).get(0).getId();
                } else {
                    showToast("没有收货地址");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        hotMv.stopFlipping();
    }

}
