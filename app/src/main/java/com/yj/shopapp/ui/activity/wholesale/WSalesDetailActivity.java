package com.yj.shopapp.ui.activity.wholesale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CashierInputFilter;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.SPlist;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/7.
 */
public class WSalesDetailActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.startimeTv)
    TextView startimeTv;
    @BindView(R.id.overimeTv)
    TextView overimeTv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.sale_price_tv)
    TextView salePriceTv;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.goods_tips)
    TextView goodsTips;
    @BindView(R.id.goods_tipsLL)
    LinearLayout goodsTipsLL;
    private String price;
    private int year, month, day, hour, minute;
    private long startTime = 0;
    private long overTime = 0;
    private String isGift = "0";
    private String type = "update";
    private String itemid;
    private SPlist sPlist;
    private String shijiancuo = "";
    private String saveid;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_salesdetail;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        title.setText("促销商品");
        if (getIntent().hasExtra("goodsname")) {
            shopname.setText(getIntent().getStringExtra("goodsname"));
        }
        InputFilter[] filters = new InputFilter[]{new CashierInputFilter()};
        salePriceTv.setFilters(filters);
        if (getIntent().hasExtra("itemid")) {
            itemid = getIntent().getStringExtra("itemid");
        }
        if (getIntent().hasExtra("isModify")) {
            idRightBtu.setVisibility(getIntent().getStringExtra("isModify").equals("1") ? View.VISIBLE : View.GONE);
        }
        if (StringHelper.isEmpty(itemid)) {
            idRightBtu.setText("修改");
            sPlist = Objects.requireNonNull(getIntent().getExtras()).getParcelable("been");
            saveid = Objects.requireNonNull(sPlist).getSaveid();
            itemid = Objects.requireNonNull(sPlist).getId();
//            startimeTv.setText(DateUtils.getDateToString2(sPlist.getTime1() + "000"));
//            overimeTv.setText(DateUtils.getDateToString2(sPlist.getTime2() + "000"));

            isGift = sPlist.getSale_status();
            salePriceTv.setText(sPlist.getUnitprice());
            priceTv.setText(sPlist.getPrice());

        } else {
            idRightBtu.setText("保存");
        }
        if (type.equals("add")) {
            saveid = getIntent().getStringExtra("saveid");
            price = getIntent().getStringExtra("price");
            priceTv.setText(price);
        }
        getSaleDetails();
    }

    @OnClick({R.id.startimeTv, R.id.overimeTv, R.id.goods_tips, R.id.sale_price_tv, R.id.id_right_btu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.startimeTv:
                initDateTime(0);
                break;
            case R.id.overimeTv:
                initDateTime(1);
                break;
            case R.id.goods_tips:
                showTipsDialog();
                break;
            case R.id.sale_price_tv:
                showModifySalePriceDialog();
                break;
            case R.id.id_right_btu:
                String salePrice = salePriceTv.getText().toString();
                if (!salePrice.isEmpty()) {
                    saveSales("3", salePrice, "");
                } else {
                    showToastShort("请输入促销价格");
                }
                break;
        }
    }

    private void getSaleDetails() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", saveid);
        HttpHelper.getInstance().post(mContext, Contants.PortA.SALEDETAILS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1 && object.get("data") != null) {
                        JSONObject data = object.getJSONObject("data");
                        startimeTv.setText(DateUtils.getDateToString2(data.getString("time1") + "000"));
                        overimeTv.setText(DateUtils.getDateToString2(data.getString("time2") + "000"));
                        startTime = Long.valueOf(data.getString("time1"));
                        overTime = Long.valueOf(data.getString("time2"));
                        goodsTips.setText(data.getString("remark"));
                    } else {
                        //showToastShort(object.getString("info"));
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    /**
     * 获取当前的日期和时间
     */
    @SuppressLint("DefaultLocale")
    private void initDateTime(int type) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, (view, year, month, dayOfMonth) -> {
            shijiancuo = String.format("%d年%02d月%d日", year, month + 1, dayOfMonth);
            showTimePickerDialog(type);
        }, year, month - 1, day).show();
    }

    @SuppressLint("DefaultLocale")
    private void showTimePickerDialog(int type) {
        new TimePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, (view, hourOfDay, minute) -> {
            shijiancuo += " " + String.format("%02d时%02d分", hourOfDay, minute);
            if (type == 0) {
                startTime = DateUtils.transForMilliSecondByTim(shijiancuo, "yyyy年MM月dd日 HH时mm分");
                startimeTv.setText(shijiancuo);
            } else {
                overTime = DateUtils.transForMilliSecondByTim(shijiancuo, "yyyy年MM月dd日 HH时mm分");
                overimeTv.setText(shijiancuo);
            }

        }, hour, minute, true).show();
    }

    private void showTipsDialog() {
        new MaterialDialog.Builder(mContext).title("请输入促销提示信息").input("", goodsTips.getText().toString(), new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

            }
        }).canceledOnTouchOutside(false).positiveText("确定").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                goodsTips.setText(dialog.getInputEditText().getText().toString());
                dialog.dismiss();
            }
        }).show();
    }

    private void showModifySalePriceDialog() {
        new MaterialDialog.Builder(mContext).title("请输入促销价格").inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER).input("", salePriceTv.getText().toString(), new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

            }
        }).canceledOnTouchOutside(false).positiveText("确定").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (!dialog.getInputEditText().getText().toString().equals("")) {
                    salePriceTv.setText(dialog.getInputEditText().getText().toString());
                    dialog.dismiss();
                } else {
                    showToastShort("请输入促销价格");
                }
            }
        }).show();
    }

    public void saveSales(String stype, String disstr, String gift) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("saveid", saveid);
        params.put("stype", stype);
        params.put("itemid", itemid);
        params.put("starttime", String.valueOf(startTime));
        params.put("stoptime", String.valueOf(overTime));
        params.put("disstr", disstr);
        params.put("remark", "");
        params.put("specialnote", goodsTips.getText().toString());
        if (isGift.equals("1")) {
            params.put("gift", gift);
        }
        //显示ProgressDialog
        ShowLog.e("saveid" + saveid + "itemid" + itemid);
        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
        progressDialog.show();

        HttpHelper.getInstance().post(mContext, Contants.PortA.SAVESP, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                String stit = "";
                if (type.equals("update")) {
                    stit = "修改促销成功，在“我的促销”中查看。";
                } else {
                    stit = "加入促销成功，在“我的促销”中查看。";
                }

                if (JsonHelper.isRequstOK(json, mContext)) {
                    new MaterialDialog.Builder(mContext)
                            .content(stit)
                            .positiveText("确定")
//                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                    finish();
                                }
                            })
                            .show();

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
