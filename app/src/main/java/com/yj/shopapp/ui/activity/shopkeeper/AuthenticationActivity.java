package com.yj.shopapp.ui.activity.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phonenumber_txt)
    EditText phonenumberTxt;
    @BindView(R.id.vercode)
    ClearEditText vercode;
    @BindView(R.id.getVercode)
    TextView getVercode;
    private int type = 0;
    private String[] titles = {"添加微信", "添加支付宝"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }
        titleTv.setText(type != 0 ? titles[type - 1] : "");
        phonenumberTxt.setText(userPhone);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
    }

    public void getCode() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phonenumberTxt.getText().toString().trim().replace(" ", ""));
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
                timer.start();
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("验证码已发送");
                } else {
                    showToastShort("验证码60秒只能发送一次");
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });

    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @SuppressLint("DefaultLocale")
        @Override
        public void onTick(long millisUntilFinished) {
            if (getVercode != null) {
                getVercode.setClickable(false);
                getVercode.setTextColor(mContext.getResources().getColor(R.color.line3));
                getVercode.setText(String.format("%d秒后重新发送", millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            if (getVercode != null) {
                getVercode.setClickable(true);
                getVercode.setTextColor(mContext.getResources().getColor(R.color.colorc0E8EE7));
                getVercode.setText("获取验证码");
            }
        }
    };

    private void checkCode() {
        Map<String, String> params = new HashMap<>();
        params.put("code", vercode.getText().toString().trim().replace(" ", ""));
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
                ShowLog.e(json);
                JSONObject object = JSONObject.parseObject(json);
                if (object.getString("errcode").equals("0")) {
                    timer.cancel();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    Intent intent = new Intent(AuthenticationActivity.this, BindAccountActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    showToastShort("验证失败！请重新输入");
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });
    }

    @OnClick({R.id.getVercode, R.id.submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getVercode:
                if (!phonenumberTxt.getText().toString().isEmpty()) {
                    getCode();
                } else {
                    showToastShort("请输入手机号码");
                }
                break;
            case R.id.submit_tv:
                if (!vercode.getText().toString().isEmpty()) {
                    checkCode();
                } else {
                    showToastShort("请输入验证码");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
