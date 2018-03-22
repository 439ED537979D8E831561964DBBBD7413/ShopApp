package com.yj.shopapp.ui.activity.wholesale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.wbeen.Customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/11/26.
 */

public class WAddCashCouponActivity extends BaseActivity {
    final Calendar c = Calendar.getInstance();
    @BindView(R.id.starttime_btn)
    TextView starttimeTv;
    @BindView(R.id.endtime_btn)
    TextView endtimeTv;
    List<Customer> notes = new ArrayList<>();
    final static int AddCASHREPUESTCODE = 003;
    @BindView(R.id.shopername_tv)
    TextView shopernameTv;
    @BindView(R.id.classifitcationTv)
    TextView classifitcationTv;
    String customerId;
    String typeId;
    @BindView(R.id.money_btn)
    EditText moneyBtn;
    @BindView(R.id.condition_edt)
    EditText conditionEdt;
    @BindView(R.id.title)
    TextView title;
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_addcashcoupon;
    }

    @Override
    protected void initData() {
        title.setText("添加/修改现金券");
    }


    @OnClick(R.id.classifitcationTv)
    public void classificationOnclick() {
        Bundle bundle = new Bundle();
        bundle.putString("action", "choose");
        CommonUtils.goActivityForResult(mContext, WBigTypeActivity.class, bundle, AddCASHREPUESTCODE, false);
    }

    @OnClick(R.id.shopername_tv)
    public void shopNameOnclick() {
        CommonUtils.goActivityForResult(mContext, WCashCousponCustomerActivity.class, null, AddCASHREPUESTCODE, false);
    }

    @OnClick(R.id.starttime_btn)
    public void getStartTimeOnclick() {
        getDate(starttimeTv);
    }

    @OnClick(R.id.endtime_btn)
    public void getEndTimeOnclick() {
        getDate(endtimeTv);
    }

    @OnClick(R.id.save_tv)
    public void saveOnclick() {
        if (!checkData()) {
            return;
        }

        submit();
    }

    private void getDate(final TextView tv) {
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //  c.set(year, monthOfYear, dayOfMonth);
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddCASHREPUESTCODE) {
            shopernameTv.setText(data.getStringExtra("customerName"));
            customerId = data.getStringExtra("customerId");
        }
        if (resultCode == WBigTypeActivity.RESULTCODE) {
            typeId = data.getStringExtra("typeId");
            classifitcationTv.setText(data.getStringExtra("typename"));
        }
    }

    private void submit() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("useruid", customerId);
        params.put("bigtypeid", typeId);
        params.put("starttime", starttimeTv.getText().toString());
        params.put("endtime", endtimeTv.getText().toString());

        params.put("money", moneyBtn.getText().toString().trim().replace(" ", ""));
        params.put("available_money", conditionEdt.getText().toString().trim().replace(" ", ""));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Savecash, params, new OkHttpResponseHandler<String>(mContext) {
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
                    showToastShort("添加成功");
                    finish();
                }
            }
        });
    }

    private boolean checkData() {
        if (CommonUtils.isEmpty(typeId)) {
            showToastShort("商品类别不能为空！");
            return false;
        }
        if (CommonUtils.isEmpty(customerId)) {
            showToastShort("零售商不能为空！");
            return false;
        }
        if (CommonUtils.isEmpty(starttimeTv.getText().toString())) {
            showToastShort("开始时间不能为空！");
            return false;
        }
        if (CommonUtils.isEmpty(endtimeTv.getText().toString())) {
            showToastShort("结束时间不能为空！");
            return false;
        }
        if (CommonUtils.isEmpty(moneyBtn.getText().toString())) {
            showToastShort("金额不能为空！");
            return false;
        }
        if (CommonUtils.isEmpty(conditionEdt.getText().toString())) {
            showToastShort("满足条件不能为空！");
            return false;
        }
        return true;
    }
}

