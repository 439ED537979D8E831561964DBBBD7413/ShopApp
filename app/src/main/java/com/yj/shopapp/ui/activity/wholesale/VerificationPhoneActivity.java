package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.RegisterActivity;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by huanghao on 2016/11/24.
 */

public class VerificationPhoneActivity extends BaseActivity {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.phonenumber_txt)
    TextView phonenumberTxt;
    @BindView(R.id.phonenumber_edt)
    EditText phonenumberEdt;
    @BindView(R.id.code_edt)
    EditText codeEdt;
    @BindView(R.id.getcode_txt)
    TextView getcodeTxt;
    public static String REGISTER = "register";
    public static String FIND_PASSWORD = "findpassword";
    public static String ACTION_KEY = "action";
    int isSendmsg = 0;
    int num;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_verificationphone;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("num")) {
            num = getIntent().getIntExtra("num", 1);
        }
        if (num == 1) {
            title.setText("找回密码");
        } else {
            title.setText("注 册");
        }
    }


    public void getCode() {
        //jishi();
        timer.start();
        getcodeTxt.setBackground(getResources().getDrawable(
                R.drawable.huiseruanjiao));
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phonenumberEdt.getText().toString().trim().replace(" ", ""));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getmobilecode, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("验证码已发送");
                } else {
                    showToastShort("发送验证码失败！");
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });

    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            getcodeTxt.setClickable(false);
            getcodeTxt.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            getcodeTxt.setClickable(true);
            getcodeTxt.setBackground(getResources().getDrawable(
                    R.drawable.ic_redborder));
            getcodeTxt.setText("获取验证码");
        }
    };

    private void checkCode() {
        Map<String, String> params = new HashMap<>();
        params.put("code", codeEdt.getText().toString().trim().replace(" ", ""));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Checkmobilecode, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    timer.cancel();
                    if (getIntent().getStringExtra(ACTION_KEY).equals(REGISTER)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("phoneNumber", phonenumberEdt.getText().toString().trim().replace(" ", ""));
                        CommonUtils.goActivity(mContext, RegisterActivity.class, bundle, true);
                    } else if (getIntent().getStringExtra(ACTION_KEY).equals(FIND_PASSWORD)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("phoneNumber", phonenumberEdt.getText().toString().trim().replace(" ", ""));
                        CommonUtils.goActivity(mContext, FindPasswordActivity.class, bundle, true);
                    }

                } else {
                    showToastShort("验证失败！请重新输入");
                    isSendmsg = 0;
                }

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });
    }

    public void checknumberIsexit() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phonenumberEdt.getText().toString().trim().replace(" ", ""));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Checkusername, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {

                    getCode();
                } else {
                    showToastShort("电话号码已经存在！");
                }

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });
    }

    @OnClick({R.id.id_right_btu, R.id.getcode_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_right_btu:
                CommonUtils.goActivity(mContext, LoginActivity.class, null);
                break;

            case R.id.getcode_txt:
                isSendmsg = 0;
                if (CommonUtils.isEmpty(phonenumberEdt.getText().toString())) {
                    showToastShort("电话号码不能为空");
                    break;
                } else {
                    codeEdt.requestFocus();
                }
                if (getIntent().getStringExtra(ACTION_KEY).equals(REGISTER)) {
                    checknumberIsexit();
                } else {
                    getCode();
                }
                break;
            default:
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSendmsg = 1;
    }

    @OnClick(R.id.next)
    public void onClick() {

        if (CommonUtils.isEmpty(codeEdt.getText().toString())) {
            showToastShort("验证码不能为空");
        } else {
            isSendmsg = 1;
            checkCode();
        }
//        Bundle bundle = new Bundle();
//        bundle.putString("phoneNumber", phonenumberEdt.getText().toString().trim().replace(" ", ""));
//        CommonUtils.goActivity(mContext, RegisterActivity.class, bundle, true);

    }
}
