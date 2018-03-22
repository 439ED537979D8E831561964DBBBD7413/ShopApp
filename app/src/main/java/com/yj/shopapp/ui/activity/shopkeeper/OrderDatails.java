package com.yj.shopapp.ui.activity.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.yj.shopapp.ui.activity.adapter.OrderListAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2017/12/25.
 *
 * @author LK
 */

public class OrderDatails extends BaseActivity {


    @BindView(R.id.Consignee)
    TextView Consignee;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.Rec1eiving_address)
    TextView Rec1eivingAddress;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.goodslist)
    ListView goodslist;
    @BindView(R.id.specialoffers)
    LinearLayout specialoffers;
    @BindView(R.id.paid_money)
    TextView paidMoney;
    @BindView(R.id.discount_money)
    TextView discountMoney;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.total_money)
    TextView totalMoney;
    @BindView(R.id.message_et)
    EditText messageEt;

    private String idstr = "";
    private String addressid = "";
    private OrderPreview preview;
    private OrderDatailsAdpter classadpter;
    private OrderListAdapter adapter1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderdetails;
    }

    @Override
    protected void initData() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (getIntent().hasExtra("idstr")) {
            idstr = getIntent().getStringExtra("idstr");
        }
        if (getIntent().hasExtra("addressid")) {
            addressid = getIntent().getStringExtra("addressid");
        }
        classadpter = new OrderDatailsAdpter(mContext);
        myRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        myRecyclerView.setAdapter(classadpter);
        adapter1 = new OrderListAdapter(mContext);
        goodslist.setAdapter(adapter1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            requestData();
        } else {
            showToastShort("无网络");
        }
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
                    if (Double.parseDouble(preview.getAllmoeny()) <= 500) {
                        showDialog();
                    }
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
        goodsNum.setText(String.format("共%s件商品", preview.getAllcount()));
        totalMoney.setText(Html.fromHtml("合计:￥" + "<font color=#FE4902>" + df.format(Double.parseDouble(preview.getAllmoeny()) - (double) preview.getCashback()) + "</font>"));

        paidMoney.setText(String.format("￥%s", preview.getAllmoeny()));
        if (preview.getCashback() != 0) {
            paidMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        discountMoney.setText(String.format("￥%d", preview.getCashback()));
        //paybleMoney.setText(String.format("￥%s", df.format(Double.parseDouble(preview.getAllmoeny()) - (double) preview.getCashback())));
        if (preview.getCouponlist().size() > 0) {
            adapter1.setList(preview.getCouponlist());
        } else {
            specialoffers.setVisibility(View.GONE);
        }
    }

    private void showDialog() {
        new MaterialDialog.Builder(mContext).title("温馨提示").content("【平台配送金额500元起】\n" +
                "您本次下单金额为" + preview.getAllmoeny() + "元【未达配送标准】").positiveText("确定提交").negativeText("继续下单").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                savedoorder();
            }
        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                CommonUtils.goActivity(mContext, SMainTabActivity.class, null, true);
            }
        }).show();
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
                    object.getString("oid");
                    //CommonUtils.setBroadCast(mContext, Contants.Bro.REFRESH_ORDERLIST);
                    EventBus.getDefault().post(new RefreshListCar());
                    showToastShort(Contants.NetStatus.NETSUCCESS);
                    finish();
                } else {
                    showToastShort(object.getString("message"));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    @OnClick({R.id.Continue_the_order, R.id.submit_order, R.id.exit_tv, R.id.Leaving_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Continue_the_order:
                CommonUtils.goActivity(mContext, SMainTabActivity.class, null, true);
                break;
            case R.id.submit_order:
                savedoorder();
                break;
            case R.id.exit_tv:
                finish();
                break;
            case R.id.Leaving_message:
                showSoftInputFromWindow(messageEt);
                break;
            default:
                break;
        }
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
