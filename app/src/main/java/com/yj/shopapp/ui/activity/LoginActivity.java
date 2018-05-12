package com.yj.shopapp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SMainTabActivity;
import com.yj.shopapp.ui.activity.wholesale.VerificationPhoneActivity;
import com.yj.shopapp.ui.activity.wholesale.WMainTabActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.SoftKeyInputHidWidget;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.ClearEditText;
import com.yj.shopapp.view.KeyboardLayout;
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
    @BindView(R.id.mainLl)
    KeyboardLayout mainLl;
    private KProgressHUD progressDialog;
    int index;
    boolean isReqing;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        AppManager.getAppManager().finishAllActivity();
        String username = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_NAME, "");
        String userpwd = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_PWD, "");
        userNameEt.setText(username);
        userPasswordEt.setText(userpwd);
        userPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //完成
                login();
                return true;
            }
        });
        mainLl.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                if (isActive) {
                    scrollToBottom();
                }
            }
        });
        setHideOrShowSoftInput(userNameEt);
        setHideOrShowSoftInput(userPasswordEt);
    }

    /**
     * 弹出软键盘时将SVContainer滑到底
     */
    private void scrollToBottom() {
        if (scroll != null) {
            scroll.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        scroll.smoothScrollTo(0, scroll.getBottom() + SoftKeyInputHidWidget.getStatusBarHeight(LoginActivity.this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 100);
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

        login(username, userpwd);
        progressDialog = growProgress(Contants.Progress.LOGIN_ING).show();
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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if (isReqing == true) {
                    return;
                }
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
                        CommonUtils.goActivity(mContext, WMainTabActivity.class, null, true);
                    }
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

    private void setHideOrShowSoftInput(View v) {
        if (!(v instanceof EditText)) {
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
//                    mHolder.ll_settings.setFocusable(true);
//                    mHolder.ll_settings.requestFocus();
                    return false;
                }
            });
        }
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                View innerView = ((ViewGroup) v).getChildAt(i);
                setHideOrShowSoftInput(innerView);
            }
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
