package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RedPackReFlect extends BaseActivity {


    String r_type, r_id, user_id;
    int type = -1;
    float reward;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.account_et)
    EditText accountEt;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.getcode_txt)
    TextView getcodeTxt;
    @BindView(R.id.switch_type)
    TextView switchType;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_red_pack_re_flect;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("reward_type")) {
            r_type = getIntent().getExtras().getString("reward_type");
        }
        if (getIntent().hasExtra("useruid")) {
            user_id = getIntent().getExtras().getString("useruid");
        }
        if (getIntent().hasExtra("reward_id")) {
            r_id = getIntent().getExtras().getString("reward_id");
        }
        if (getIntent().hasExtra("reward")) {
            reward = Float.parseFloat(getIntent().getExtras().getString("reward"));
        }
        if ("1".equals(r_type)) {
            title.setText("领取" + (int) reward + "超级红包");
        } else {
            title.setText("领取" + (int) reward + "元红包");
        }

    }



    @OnClick({R.id.rule_return, R.id.getcode_txt, R.id.submit, R.id.switch_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rule_return:
                finish();
                break;
            case R.id.getcode_txt:
                //获取验证码getcodeTxt
                getcodeTxt.setEnabled(false);
                timer.start();
                getCode();
                break;
            case R.id.submit:
                hintKbTwo();
                if (checkEmpty()) {
                    //提交
                    checkCode();
                }
                break;
            case R.id.switch_type:
                showWayDialog();
                break;
            default:
                break;
        }
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            getcodeTxt.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            getcodeTxt.setEnabled(true);
            getcodeTxt.setText("  获取验证码  ");
        }
    };

    /**
     * 网络请求
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("reward_type", r_type);
        params.put("useruid", user_id);
        params.put("type", type + "");
        params.put("accountnumber", accountEt.getText().toString().trim());
        params.put("reward_id", r_id);
        HttpHelper.getInstance().post(mContext, Contants.PortU.REWARDCHANGEAPPLY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (object.getInt("status") == 1) {
                            Toast.makeText(mContext, object.getString("info"), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            showToastLong(object.getString("info"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void showWayDialog() {
        String[] item = {"微信", "支付宝"};
        new MaterialDialog.Builder(this)
                .title("请选择")
                .items(item)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switchType.setText(text);
                        type = which + 1;
                        return false;
                    }
                })
                .show();


    }

    public void getCode() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", userPhone);
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("验证码已发送" + userPhone);
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

    private void checkCode() {
        Map<String, String> params = new HashMap<>();
        params.put("code", etCode.getText().toString().trim().replace(" ", ""));
        ShowLog.e(etCode.getText().toString().trim().replace(" ", ""));
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
                    refreshRequest();
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

    private Boolean checkEmpty() {
        if (type != -1) {
            if (!accountEt.getText().toString().trim().isEmpty() && !etCode.getText().toString().trim().isEmpty() && type != -1) {
                return true;

            } else {
                showToastShort("提现账号或验证码为填写");
                return false;
            }
        } else {
            showToastShort("领取方式未选择");
        }
        return false;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


}
