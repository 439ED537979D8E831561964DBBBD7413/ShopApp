package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralInfo;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.util.JsonHelper;
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
    //    @BindView(R.id.integral_et)
//    EditText integral_et;
//    @BindView(R.id.account_et)
//    EditText account_et;
//    @BindView(R.id.password_et)
//    EditText password_et;
//    @BindView(R.id.passwd_cb)
//    CheckBox password_cb;
//    @BindView(R.id.mention_way_tv)
//    TextView way;
    private String pwd;
    private String account;
    private String type;
    private long integral;
    private long limt_integral;
    private long integ;
    private String[] name = {"支付宝", "微信"};
    private IntegralInfo integralInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sexchange;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        //password_cb.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        //getLastRecord();
        getIntegral();
    }

//    private void showWayDialog() {
//        MaterialDialog dialog = new MaterialDialog.Builder(mActivity)
//                .title("请选择")
//                .items(name)
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
//                        way.setText(text);
//                        dialog.dismiss();
//                    }
//                })
//                .autoDismiss(false)
//                .build();
//        dialog.show();
//    }

//    @OnClick({R.id.mention_way_tv, R.id.all_integral, R.id.change_integral})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.mention_way_tv:
//                showWayDialog();
//                break;
//            case R.id.all_integral:
//                integral_et.setText(integral + "");
//                break;
//            case R.id.change_integral:
//                pwd = password_et.getText().toString();
//                account = account_et.getText().toString();
//                type = way.getText().toString();
//                if ("".equals(integral_et.getText().toString())) {
//                    integ = integral;
//                } else {
//                    integ = Integer.parseInt(integral_et.getText().toString());
//                }
//
//                if (pwd == null || account == null || type == null ||
//                        pwd.equals("") || account.equals("") || type.equals("")) {
//                    showToast("请填充完整的信息");
//                    return;
//                }
//
//                if (integ < limt_integral) {
//                    showToast("提示：积分兑换最小额度要大于" + limt_integral + "!");
//                    return;
//                }
//
//                if (integ > integral) {
//                    showToast("提示：您的积分不够您兑换的积分数！");
//                    return;
//                }
//                if (!pwd.equals(PreferenceUtils.getPrefString(mActivity, Contants.Preference.USER_PWD, ""))) {
//                    showToast("提示：密码填写错误");
//                    return;
//                }
//
//                getIntegralChangeApply();
//                break;
//        }
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            password_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//        } else {
//            password_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        }
    }


//    private void getIntegralChangeApply() {
//        Map<String, String> params = new HashMap<>();
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("password", password_et.getText().toString());
//        params.put("changetype", way.getText().toString());
//        params.put("accountnumber", account_et.getText().toString());
//        params.put("integral", integ + "");
//        HttpHelper.getInstance().post(mActivity, Contants.PortU.IntegralChangeApply, params, new OkHttpResponseHandler<String>(mActivity) {
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
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//                System.out.print(json);
//                if (JsonHelper.isRequstOK(json, mActivity)) {
//                    showToast("申请提现成功,请等待后台审核！");
//                } else {
//                    showToast("申请提现失败!");
//                }
//
//            }
//        });
//
//    }

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
                    integral = integralInfo.getIntegral();
                    limt_integral = integralInfo.getMin_limit();
                }
            }
        });
    }


    @OnClick({R.id.zhifubao_pay, R.id.weixin_pay, R.id.modify, R.id.submit,R.id.zhifubao_cb,R.id.weixin_cb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zhifubao_pay:
            case R.id.zhifubao_cb:
                weixinCb.setChecked(false);
                zhifubaoCb.setChecked(true);
                numberEt.setHint("请输入支付宝账号");
                break;
            case R.id.weixin_pay:
            case R.id.weixin_cb:
                weixinCb.setChecked(true);
                zhifubaoCb.setChecked(false);
                numberEt.setHint("请输入微信账号");
                break;
            case R.id.modify:

                break;
            case R.id.submit:

                break;
        }
    }

//    private void getLastRecord() {
//        Map<String, String> params = new HashMap<>();
//        params.put("uid", uid);
//        params.put("token", token);
//        HttpHelper.getInstance().post(mActivity, Contants.PortU.ENDINTEGRALCHANGE, params, new OkHttpResponseHandler<String>(mActivity) {
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//            }
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//                ShowLog.e(json);
//                if (JsonHelper.isRequstOK(json, mActivity)) {
//                    if (!"".equals(json)) {
//                        JSONObject object = JSONObject.parseObject(json);
//                        way.setText(name[Integer.parseInt(object.getString("changetype"))]);
//                        account_et.setText(object.getString("accountnumber"));
//                    }
//
//                }
//            }
//        });
//    }

}
