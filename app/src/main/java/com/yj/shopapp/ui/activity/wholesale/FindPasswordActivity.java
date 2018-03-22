package com.yj.shopapp.ui.activity.wholesale;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/11/25.
 */

public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.password_edt)
    EditText passwordEdt;
    @BindView(R.id.againpassword_edt)
    EditText againpasswordEdt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_findpassword;
    }

    @Override
    protected void initData() {
        title.setText("找回密码");
        idRightBtu.setVisibility(View.GONE);
    }


    private void UpdatePassword() {
        Map<String, String> params = new HashMap<>();
        params.put("newpwd", passwordEdt.getText().toString().trim().replace(" ", ""));
        params.put("confirmpwd", againpasswordEdt.getText().toString().trim().replace(" ", ""));
        params.put("mobile", getIntent().getStringExtra("phoneNumber"));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Setpasswd, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("密码修改成功");
                    //FindPasswordActivity.this.finish();
                    CommonUtils.goActivity(mContext, LoginActivity.class,null,true);
                }
                else {
                    showToastShort("密码修改失败");
                }
            }
        });
    }

    @OnClick(R.id.register_txt)
    public void onClick() {
        if (checkDataIsTrue()) {
            UpdatePassword();
        }
    }
    private boolean checkDataIsTrue()
    {
        if (StringHelper.isEmpty(passwordEdt.getText().toString()))
        {
            showToastShort("密码不能为空");
            return false;
        }
        if (!StringHelper.isequal(passwordEdt.getText().toString(),againpasswordEdt.getText().toString()))
        {
            showToastShort("两次输入的密码必须一致");
            return false;
        }

        return true;
    }
}
