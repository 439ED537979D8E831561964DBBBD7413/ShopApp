package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.wbeen.AutoCoupon;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/12/14.
 */

public class WautoputCouponActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.classi_tv)
    TextView classiTv;
    @BindView(R.id.term_tv)
    TextView termTv;
    @BindView(R.id.money_edt)
    EditText moneyEdt;
    @BindView(R.id.condition_edt)
    EditText conditionEdt;
    int classiRequestCode = 001;
    String typeId;
    String term;
    String isopencash;
    @BindView(R.id.close_rb)
    RadioButton closeRb;
    @BindView(R.id.open_rb)
    RadioButton openRb;
    @BindView(R.id.role_rg)
    RadioGroup roleRg;
    String id;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.tishi)
    TextView tishi;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_autoputcpupon;
    }

    @Override
    protected void initData() {
        title.setText("自动发放详情");
        idRightBtu.setText("保存");
        roleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == closeRb.getId()) {
                    isopencash = "0";
                    lin.setVisibility(View.GONE);
                    tishi.setVisibility(View.VISIBLE);
                } else {
                    isopencash = "1";
                    lin.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
                }
            }
        });
        getDetailed();
    }


    private void getDetailed() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Cashautoinfo, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    Gson gson = new Gson();
                    AutoCoupon autoCoupon = gson.fromJson(json, AutoCoupon.class);
                    typeId = autoCoupon.getBigtypeid();
                    classiTv.setText(autoCoupon.getBigtypename());
                    termTv.setText(autoCoupon.getUse_days() + "天内");
                    conditionEdt.setText(autoCoupon.getAvailable_money());
                    isopencash = autoCoupon.getIsopencash();
                    moneyEdt.setText(autoCoupon.getMoney());
                    id = autoCoupon.getId();
                    if (isopencash == null || isopencash.equals("0")) {
                        roleRg.check(closeRb.getId());
                        lin.setVisibility(View.GONE);
                        tishi.setVisibility(View.VISIBLE);

                    } else if (isopencash.equals("1")) {
                        roleRg.check(openRb.getId());
                        lin.setVisibility(View.VISIBLE);
                        tishi.setVisibility(View.GONE);
                    }


                }
            }
        });
    }

    private void submi() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("isopencash", isopencash);
        params.put("bigtypeid", typeId);
        params.put("use_days", term);
        params.put("money", moneyEdt.getText().toString().trim().replace(" ", ""));
        params.put("available_money", conditionEdt.getText().toString().trim().replace(" ", ""));
        if (!CommonUtils.isEmpty(id)) {
            params.put("id", id);
        }

        HttpHelper.getInstance().post(mContext, Contants.PortA.Savecashauto, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("提交成功");


                }
            }
        });
    }





    @OnClick({R.id.classi_tv, R.id.term_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.classi_tv:
                Bundle bundle = new Bundle();
                bundle.putString("action", "choose");
                CommonUtils.goActivityForResult(mContext, WBigTypeActivity.class, bundle, classiRequestCode, false);
                break;
            case R.id.term_tv:
                showChooseify();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == classiRequestCode && resultCode == WBigTypeActivity.RESULTCODE) {
            typeId = data.getStringExtra("typeId");
            classiTv.setText(data.getStringExtra("typename"));


        }
    }

    public void showChooseify() {
        final String[] nameArray = {"15天内", "30天内", "45天内", "60天内"};
        final String[] array = {"15", "30", "45", "60"};
        int i = 0;


        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
        materialDialog.title("选择时限");
        materialDialog.items(nameArray);
        materialDialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                term = array[which];
                termTv.setText(nameArray[which]);


            }
        });


        materialDialog.positiveText(android.R.string.cancel);
        materialDialog.show();
    }

    private boolean checkData() {
        if (CommonUtils.isEmpty(typeId)) {
            showToastShort("类别不能为空");
            return false;
        }
        if (CommonUtils.isEmpty(termTv.getText().toString())) {
            showToastShort("请选择期限");
            return false;
        }
        if (CommonUtils.isEmpty(moneyEdt.getText().toString())) {
            showToastShort("金额不能为空");
            return false;
        }
        if (CommonUtils.isEmpty(conditionEdt.getText().toString())) {
            showToastShort("满足条件不能为空");
            return false;
        }
        return true;
    }

    @OnClick(R.id.id_right_btu)
    public void onClick() {
        if (checkData() == true) {
            submi();
        }

    }

}
