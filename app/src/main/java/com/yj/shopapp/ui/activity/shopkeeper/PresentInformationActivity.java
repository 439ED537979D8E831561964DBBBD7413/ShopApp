package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.UserAccount;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.AccountManger;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PresentInformationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.zAdministration)
    TextView zAdministration;
    @BindView(R.id.addzhifubao)
    RelativeLayout addzhifubao;
    @BindView(R.id.zname)
    TextView zname;
    @BindView(R.id.zaccount)
    TextView zaccount;
    @BindView(R.id.zcheckbox)
    CheckBox zcheckbox;
    @BindView(R.id.wAdministration)
    TextView wAdministration;
    @BindView(R.id.addweixin)
    RelativeLayout addweixin;
    @BindView(R.id.wname)
    TextView wname;
    @BindView(R.id.waccount)
    TextView waccount;
    @BindView(R.id.wcheckbox)
    CheckBox wcheckbox;
    @BindView(R.id.weixinCard)
    RelativeLayout weixinCard;
    @BindView(R.id.zhifubaoCard)
    RelativeLayout zhifubaoCard;
    @BindView(R.id.bgView)
    View bgView;
    private UserAccount account;
    private KProgressHUD kProgressHUD;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_present_information;
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
        kProgressHUD = growProgress("修改成功");
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_4c4c4c), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            getChangeInfo();
        } else {
            showToastShort("网络异常");
        }
    }

    private void getChangeInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortS.MYCHANNGE_INFO, params, new OkHttpResponseHandler<String>(this) {
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
                    if (object.getInteger("status") == 1) {
                        account = object.toJavaObject(UserAccount.class);

                        addzhifubao.setVisibility("".equals(account.getData().getZfb()) ? View.VISIBLE : View.GONE);
                        addweixin.setVisibility("".equals(account.getData().getWx()) ? View.VISIBLE : View.GONE);

                        weixinCard.setVisibility("".equals(account.getData().getWx()) ? View.GONE : View.VISIBLE);
                        zhifubaoCard.setVisibility("".equals(account.getData().getZfb()) ? View.GONE : View.VISIBLE);

                        wcheckbox.setVisibility("".equals(account.getData().getWx()) ? View.GONE : View.VISIBLE);
                        zcheckbox.setVisibility("".equals(account.getData().getZfb()) ? View.GONE : View.VISIBLE);

                        wcheckbox.setChecked("1".equals(account.getData().getType()));
                        wcheckbox.setTextColor("1".equals(account.getData().getType()) ? mContext.getResources().getColor(R.color.colorc17A5F8) : mContext.getResources().getColor(R.color.color_999999));
                        zcheckbox.setChecked("2".equals(account.getData().getType()));
                        zcheckbox.setTextColor("2".equals(account.getData().getType()) ? mContext.getResources().getColor(R.color.colorc17A5F8) : mContext.getResources().getColor(R.color.color_999999));
                        wcheckbox.setText("1".equals(account.getData().getType()) ? "默认收款账户" : "设为默认");
                        zcheckbox.setText("2".equals(account.getData().getType()) ? "默认收款账户" : "设为默认");

                        wAdministration.setVisibility("".equals(account.getData().getWx()) ? View.GONE : View.VISIBLE);
                        zAdministration.setVisibility("".equals(account.getData().getZfb()) ? View.GONE : View.VISIBLE);

                        zaccount.setText(account.getData().getZfb());
                        waccount.setText(account.getData().getWx());
                        zname.setText(account.getData().getZfb_name());
                        wname.setText(account.getData().getWx_name());
                    }
                }
            }
        });
    }

    private void setDefAccount(int type, String name, String account) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("type", String.valueOf(type));
        params.put("name", name);
        params.put("account_number", account);
        HttpHelper.getInstance().post(this, Contants.PortS.CHANNGE_INFO, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if (kProgressHUD != null) {
                    kProgressHUD.show();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (kProgressHUD != null) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    showToastShort(object.getString("info"));
                    if (object.getInteger("status") == 1) {
                        getChangeInfo();
                    }
                }
            }
        });
    }

    @OnClick({R.id.zAdministration, R.id.addzhifubao, R.id.wAdministration, R.id.addweixin, R.id.zcheckbox, R.id.wcheckbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zAdministration:
                AccountManger.newInstance(2, CommonUtils.Take4bits(account.getData().getZfb())).show(getFragmentManager(), "zfb");
                break;
            case R.id.addzhifubao:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                CommonUtils.goActivity(mContext, AuthenticationActivity.class, bundle);
                break;
            case R.id.wAdministration:
                AccountManger.newInstance(1, CommonUtils.Take4bits(account.getData().getWx())).show(getFragmentManager(), "wx");
                break;
            case R.id.addweixin:
                Bundle wbundle = new Bundle();
                wbundle.putInt("type", 1);
                CommonUtils.goActivity(mContext, AuthenticationActivity.class, wbundle);
                break;
            case R.id.zcheckbox:
                if (!zcheckbox.isChecked()) {
                    zcheckbox.setChecked(true);
                    return;
                } else {
                    setDefAccount(2, account.getData().getZfb_name(), account.getData().getZfb());
                }
                break;
            case R.id.wcheckbox:
                if (!wcheckbox.isChecked()) {
                    wcheckbox.setChecked(true);
                    return;
                } else {
                    setDefAccount(1, account.getData().getWx_name(), account.getData().getWx());
                }
                break;
        }
    }
}
