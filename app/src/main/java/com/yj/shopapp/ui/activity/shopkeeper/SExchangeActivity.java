package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralInfo;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SExchangeActivity extends NewBaseFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.zhifubao_cb)
    CheckBox zhifubaoCb;
    @BindView(R.id.weixin_cb)
    CheckBox weixinCb;
    @BindView(R.id.number_et)
    ClearEditText numberEt;
    @BindView(R.id.activity_sexchange)
    NestedScrollView activitySexchange;
    @BindView(R.id.modify)
    TextView modify;
    private long integral;
    private long limt_integral;
    private String[] name = {"支付宝", "微信"};
    private IntegralInfo integralInfo;
    private boolean status = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sexchange;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        modify.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        //getIntegral();
        getEndIntegralChange();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


//    private void getIntegral() {
//        Map<String, String> params = new HashMap<>();
//        params.put("uid", uid);
//        params.put("token", token);
//        HttpHelper.getInstance().post(mActivity, Contants.PortU.UserIntegral, params, new OkHttpResponseHandler<String>(mActivity) {
//            @Override
//            public void onBefore() {
//                super.onBefore();
//            }
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//                ShowLog.e(json);
//                if (JsonHelper.isRequstOK(json, mActivity)) {
//                    JsonHelper<IntegralInfo> jsonHelper = new JsonHelper(IntegralInfo.class);
//                    integralInfo = jsonHelper.getData(json, null);
//                    integral = integralInfo.getIntegral();
//                    limt_integral = integralInfo.getMin_limit();
//                }
//            }
//        });
//    }

    private void getEndIntegralChange() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.ENDRALCHANGE, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if ("".equals(response)) {

                } else {
                    JSONObject object = JSONObject.parseObject(response);
                    modify.setVisibility(View.VISIBLE);
                    status = true;
                    numberEt.setText(object.getString("accountnumber"));
                    if ("1".equals(object.getString("changetype"))) {
                        weixinCb.setChecked(false);
                        zhifubaoCb.setChecked(true);
                        numberEt.setHint("请输入支付宝账号");
                    } else {
                        weixinCb.setChecked(true);
                        zhifubaoCb.setChecked(false);
                        numberEt.setHint("请输入微信账号");
                    }
                }
            }
        });
    }

    @OnClick({R.id.zhifubao_pay, R.id.weixin_pay, R.id.modify, R.id.submit, R.id.zhifubao_cb, R.id.weixin_cb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zhifubao_pay:
            case R.id.zhifubao_cb:
                if (status) {
                    weixinCb.setChecked(false);
                    zhifubaoCb.setChecked(true);
                    numberEt.setHint("请输入支付宝账号");
                }
                break;
            case R.id.weixin_pay:
            case R.id.weixin_cb:
                if (status) {
                    weixinCb.setChecked(true);
                    zhifubaoCb.setChecked(false);
                    numberEt.setHint("请输入微信账号");
                }
                break;
            case R.id.modify:
                status = false;
                break;
            case R.id.submit:

                break;
        }
    }

}
