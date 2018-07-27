package com.yj.shopapp.ui.activity.shopkeeper;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/18.
 */
public class SDoPasswdActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.passwd_Et)
    EditText passwdEt;
    @BindView(R.id.surepasswd_Et)
    EditText surepasswdEt;
    @BindView(R.id.title_view)
    RelativeLayout titleView;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_dopasswd;
    }

    @Override
    protected void initData() {
        title.setText("修改密码");
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    private boolean checkTv() {
        if (StringHelper.isEmpty(passwdEt.getText().toString())) {
            showToastShort("新密码不能为空");
            return false;
        }
        if (StringHelper.isEmpty(surepasswdEt.getText().toString())) {
            showToastShort("确认密码不能为空");
            return false;
        }
        if (passwdEt.getText().toString().length()<6) {

            showToastShort("新密码长度小于6位");
            return false;
        }
        if (surepasswdEt.getText().toString().length()<6) {
            showToastShort("新密码不能为空");
            return false;
        }
        if (CommonUtils.isContainChinese(surepasswdEt.getText().toString())){
            showToastShort("确认密码包含中文");
            return false;
        }
        if (surepasswdEt.getText().toString().equals(passwdEt.getText().toString())) {
            return true;
        } else {
            showToastShort("新密码和确认密码不一致");
        }
        return false;
    }


    @OnClick(R.id.Definitemodification)
    public void onViewClicked() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            return;
        }
        if (!checkTv()) {
            return;
        }

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("passwd", passwdEt.getText().toString());

        HttpHelper.getInstance().post(mContext, Contants.PortU.Dopasswd, params, new OkHttpResponseHandler<String>(mContext) {

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

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort(Contants.NetStatus.NETSUCCESS);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.USER_PWD, passwdEt.getText().toString());
                    finish();
                } else {
                    showToastShort(Contants.NetStatus.NETERROR);
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
