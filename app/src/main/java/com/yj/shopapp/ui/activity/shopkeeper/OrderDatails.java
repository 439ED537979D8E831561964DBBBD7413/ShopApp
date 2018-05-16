package com.yj.shopapp.ui.activity.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderPreview;
import com.yj.shopapp.ubeen.RefreshListCar;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.OrderDatailsAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.MessageEvent;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.SoftKeyInputHidWidget;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.view.KeyboardLayout;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2017/12/25.
 * 购物车提交详情界面
 *
 * @author LK
 */

public class OrderDatails extends BaseActivity {


    @BindView(R.id.hot_mv)
    TextView hotMv;
    @BindView(R.id.Consignee)
    TextView Consignee;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.Rec1eiving_address)
    TextView Rec1eivingAddress;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.discount_money)
    TextView discountMoney;
    @BindView(R.id.paid_money)
    TextView paidMoney;
    @BindView(R.id.message_et)
    EditText messageEt;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.titleview)
    RelativeLayout titleview;
    @BindView(R.id.Continue_the_order)
    TextView ContinueTheOrder;
    @BindView(R.id.mainLl)
    KeyboardLayout mainLl;
    @BindView(R.id.lift_bt)
    ImageView liftBt;
    @BindView(R.id.right_bt)
    ImageView rightBt;
    private String idstr = "";
    private String addressid = "";
    private OrderPreview preview;
    private OrderDatailsAdpter classadpter;
//    private boolean isShow = false;
//    private CenterDialog dialog;
//    private RecyclerView dialogreyc;
//    private OutOfStorkAdpter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderdetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

        if (getIntent().hasExtra("idstr")) {
            idstr = getIntent().getStringExtra("idstr");
        }
        if (getIntent().hasExtra("addressid")) {
            addressid = getIntent().getStringExtra("addressid");
        }
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        classadpter = new OrderDatailsAdpter(mContext);
        myRecyclerView.setLayoutManager(ms);
        myRecyclerView.setAdapter(classadpter);
        mainLl.setKeyboardListener((isActive, keyboardHeight) -> {
            if (isActive) {
                scrollToBottom();
            }
        });
//        dialog = new CenterDialog(mContext, R.layout.outofstorkdialog, new int[]{R.id.finish_tv, R.id.i_see}, 0.8);
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                return true;
//            }
//        });
//        adpter = new OutOfStorkAdpter(mContext);
//        dialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
//            @Override
//            public void OnCenterItemClick(CenterDialog dialog, View view) {
//                switch (view.getId()) {
//                    case R.id.finish_tv:
//                        dialog.dismiss();
//                        finish();
//                        break;
//                    case R.id.i_see:
//                        dialog.dismiss();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
        if (NetUtils.isNetworkConnected(mContext)) {
            requestData();
        } else {
            showToastShort("无网络");
        }
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 30);
        StatusBarUtil.setStatusBarTextColor(getWindow(), true);
    }

    /**
     * 弹出软键盘时将SVContainer滑到底
     */
    private void scrollToBottom() {
        scroll.postDelayed(() -> scroll.smoothScrollTo(0, scroll.getBottom() + SoftKeyInputHidWidget.getStatusBarHeight(OrderDatails.this)), 100);
    }

    private void requestData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", idstr);
        params.put("addressid", addressid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ORDER_PREVIEW, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    preview = JSONObject.parseObject(json, OrderPreview.class);
                    setdata();
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setdata() {
        DecimalFormat df = new DecimalFormat("#.00");
        classadpter.setList(preview.getClassX());
        Consignee.setText("收货人：" + preview.getAddress().getShopname());
        phone.setText(preview.getAddress().getMobile());
        Rec1eivingAddress.setText(preview.getAddress().getAddress());
        paidMoney.setText(String.format("￥%s", preview.getAllmoeny()));
        ContinueTheOrder.setText(Html.fromHtml("应付金额：" + "<font color=red>" + "￥" + preview.getAllmoeny() + "</fong>"));
        discountMoney.setText(String.format("￥%s", preview.getCashback()));
        if (getList2().equals("")) {
            hotMv.setVisibility(View.GONE);
        }
        hotMv.setText(getList2());
        hotMv.setSelected(true);
        liftBt.setVisibility(preview.getClassX().size() > 4 ? View.VISIBLE : View.GONE);
        rightBt.setVisibility(preview.getClassX().size() > 4 ? View.VISIBLE : View.GONE);
        if (preview.getCancel().size() > 0) {
            OutOfStockListDialog.newInstance(preview.getCancel()).show(getFragmentManager(), "outofstock");
        }
    }

    private String getList() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < preview.getCouponlist().size(); i++) {
            builder.append(preview.getCouponlist().get(i));
            if (i < preview.getCouponlist().size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String getList2() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < preview.getCouponlist().size(); i++) {
            builder.append(preview.getCouponlist().get(i));
            builder.append("\t\t");
        }
        return builder.toString();
    }

    public void savedoorder() {

        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", idstr);
        params.put("addressid", addressid);
        params.put("tradedate", DateUtils.getNowDate());
        params.put("remarks", messageEt.getText().toString());
        HttpHelper.getInstance().post(mContext, Contants.PortU.Doorder, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                JSONObject object = JSONObject.parseObject(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    EventBus.getDefault().post(new RefreshListCar());
                    String oid = object.getString("oid");
                    showToastShort(Contants.NetStatus.NETSUCCESS);
                    if (object.getJSONArray("cancel").size() == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("oid", oid);
                        CommonUtils.goActivity(OrderDatails.this, SOrderDatesActivity.class, bundle, true);
                    } else {
                        OrderSubmitComplete.newInstance(json).show(getFragmentManager(), "OrderSubmitComplete");
                        returnlistcart();
                    }
                } else {
                    showToastShort(object.getString("msg"));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    private void returnlistcart() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cancel", JSON.toJSON(preview.getCancel()).toString());
        HttpHelper.getInstance().post(mContext, Contants.PortU.RETURN_LISTCART, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
            }
        });

    }

    @OnClick({R.id.submit_order, R.id.exit_tv, R.id.Leaving_message, R.id.changesite, R.id.hot_mv, R.id.lift_bt, R.id.right_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_order:
                savedoorder();
                //OrderSubmitComplete.newInstance("").show(getFragmentManager(), "OrderSubmitComplete");
                break;
            case R.id.exit_tv:
                finish();
                break;
            case R.id.Leaving_message:
                showSoftInputFromWindow(messageEt);
                break;
            case R.id.changesite:
                CommonUtils.goActivity(mContext, SAddressActivity.class, null, false);
                break;
            case R.id.hot_mv:
                if (preview.getCouponlist() != null && preview.getCouponlist().size() > 0) {
                    new MaterialDialog.Builder(mContext).title("优惠活动").content(getList()).positiveText("我知道了").canceledOnTouchOutside(false).show();
                }
                break;
            case R.id.lift_bt:
                myRecyclerView.smoothScrollToPosition(0);
                break;
            case R.id.right_bt:
                myRecyclerView.smoothScrollToPosition(preview.getClassX().size() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        returnlistcart();
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

}
