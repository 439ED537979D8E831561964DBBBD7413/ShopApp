package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class BindAccountActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.usernameTv)
    ClearEditText usernameTv;
    @BindView(R.id.accountType)
    TextView accountType;
    @BindView(R.id.account_tv)
    ClearEditText accountTv;
    private int type = 0;
    private String[] titles = {"绑定微信", "绑定支付宝"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_account;
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
        tips.setText(String.format("请仔细核对您的%s账号，以确保能准确成功的打款", type == 1 ? "微信" : "支付宝"));
        accountType.setText(type == 1 ? "微信" : "支付宝");
    }
    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
    }
    private void requestDeta() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("type", String.valueOf(type));
        params.put("name", usernameTv.getText().toString().trim());
        params.put("account_number", accountTv.getText().toString().trim());
        HttpHelper.getInstance().post(this, Contants.PortS.CHANNGE_INFO, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    showToastShort(object.getString("info"));
                    if (object.getInteger("status") == 1) {
                        finish();
                    }
                }
            }
        });
    }

    @OnClick(R.id.submit_tv)
    public void onViewClicked() {
        if (!accountTv.getText().toString().isEmpty()) {
            requestDeta();
        } else {
            showToastShort("请输入提现账号");
        }
    }
}
