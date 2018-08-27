package com.yj.shopapp.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SMainTabActivity;
import com.yj.shopapp.ui.activity.wholesale.VerificationPhoneActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.SoftKeyBoardListener;
import com.yj.shopapp.util.SoftKeyInputHidWidget;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.ClearEditText;
import com.yj.shopapp.wbeen.Login;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by jm on 2016/4/26.
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.user_name_et)
    ClearEditText userNameEt;
    @BindView(R.id.user_password_et)
    ClearEditText userPasswordEt;
    @BindView(R.id.login_btn)
    CardView loginBtn;
    @BindView(R.id.reg_tv)
    TextView regTv;
    @BindView(R.id.forget_password_tv)
    TextView forgetPasswordTv;
    @BindView(R.id.bgView)
    LinearLayout bgView;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.fillView)
    View fillView;
    @BindView(R.id.login_img_bg)
    RelativeLayout loginImgBg;
    int index;
    boolean isReqing;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {
        AppManager.getAppManager().finishAllActivity();
        String username = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_NAME, "");
        String userpwd = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_PWD, "");
        userNameEt.setText(username);
        userPasswordEt.setText(userpwd);
        userPasswordEt.setOnEditorActionListener((v, actionId, event) -> {
            //完成
            login();
            return true;
        });
        SoftKeyBoardListener.setListener(LoginActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                scrollToBottom();
            }

            @Override
            public void keyBoardHide(int height) {
            }
        });
    }

    /**
     * 弹出软键盘时将SVContainer滑到底
     */
    private void scrollToBottom() {
        try {
            scroll.postDelayed(() -> scroll.smoothScrollTo(0, scroll.getBottom() + SoftKeyInputHidWidget.getStatusBarHeight(LoginActivity.this)), 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setStatusBar() {

    }

    //注册
    @OnClick(R.id.reg_tv)
    public void onClcikReg() {

        Bundle bundle = new Bundle();
        bundle.putString(VerificationPhoneActivity.ACTION_KEY, VerificationPhoneActivity.REGISTER);
        CommonUtils.goActivity(mContext, VerificationPhoneActivity.class, bundle);
    }

    @OnClick(R.id.forget_password_tv)
    public void onClcikForget() {

        Bundle bundle = new Bundle();
        bundle.putInt("num", 1);
        bundle.putString(VerificationPhoneActivity.ACTION_KEY, VerificationPhoneActivity.FIND_PASSWORD);

        CommonUtils.goActivity(mContext, VerificationPhoneActivity.class, bundle);


    }

    /**
     * 验证红包开放
     */
    private void getrewardArea(final String uid, final String token) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortU.REWARD_AREA, params, new OkHttpResponseHandler<String>(mContext) {
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
                getService(uid, token);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    index = JSONObject.parseObject(json).getInteger("status");
                    PreferenceUtils.setPrefInt(mContext, "reward_area", index);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort("网络异常或服务器异常");
            }
        });
    }

    private void getService(String uid, String token) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.CHECK_OPEN, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (!json.isEmpty()) {
                    PreferenceUtils.setPrefString(mContext, "check_open", json);
                }
                CommonUtils.goActivity(mContext, SMainTabActivity.class, null, true);
            }
        });
    }

    @OnClick(R.id.login_btn)
    public void login() {

        final String username = userNameEt.getText().toString().trim();
        final String userpwd = userPasswordEt.getText().toString().trim();
        if (StringHelper.isEmpty(username) || StringHelper.isEmpty(userpwd)) {
            showToastShort("请输入正确的用户名和密码！");
            return;
        }
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            return;
        }
        //显示ProgressDialog
        if (isNetWork(mContext)) {
            login(username, userpwd);
        }
    }

    private void login(final String userName, final String userPwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("password", userPwd);
        params.put("version", verCode + "");
        params.put("app", "安卓");
        HttpHelper.getInstance().post(mContext, Contants.PortA.Login, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                isReqing = true;
                PreferenceUtils.setPrefInt(mContext, Contants.Preference.ISLOGGIN, 1);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Login> jsonHelper = new JsonHelper<Login>(Login.class);
                    final Login uinfo = jsonHelper.getData(json, null);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.UID, uinfo.getUid());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.AGENTUID, uinfo.getAgentuid());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.AREAID, uinfo.getAreaid());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.UTYPE, uinfo.getUtype());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.TOKEN, uinfo.getToken());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.USER_NAME, userName);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.USER_PWD, userPwd);
                    PreferenceUtils.setPrefInt(mContext, "isVip", uinfo.getIs_vip());
                    PreferenceUtils.setPrefString(mContext, "CustomerService", uinfo.getCustomer_service_phone());
                    PreferenceUtils.setPrefString(mContext, "address", uinfo.getAddress());
                    if ("1".equals(uinfo.getUtype())) {
                        getrewardArea(uinfo.getUid(), uinfo.getToken());
                    } else {
                        //CommonUtils.goActivity(mContext, WMainTabActivity.class, null, true);
                    }
                    PreferenceUtils.setPrefBoolean(mContext, "firstMain", true);
                } else {
                    showToastShort(JSONObject.parseObject(json).getString("info"));
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

}
