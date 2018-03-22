package com.yj.shopapp.ui.activity.wholesale;

import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CashierInputFilter;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.SPlist;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    EditText salePriceTv;
    String isGift = "0";
    long startTime = 0;
    long overTime = 0;
    String uid;
    String token;
    String itemid;
    SPlist sPlist;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.shopname)
    TextView shopname;
    String saveid;
    String type = "update";
    private String price;
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_salesdetail;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        title.setText("促销商品");
        shopname.setText(getIntent().getExtras().getString("goodsname"));
        InputFilter[] filters = new InputFilter[]{new CashierInputFilter()};
        salePriceTv.setFilters(filters);
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        itemid = getIntent().getExtras().getString("itemid");
        if (StringHelper.isEmpty(itemid)) {
            saveid = getIntent().getStringExtra("saveid");
            idRightBtu.setText("修改");
            sPlist = (SPlist) getIntent().getExtras().getSerializable("been");
            itemid = sPlist.getItemid();
            startimeTv.setText(DateUtils.getDateToString2(sPlist.getTime1() + "000"));
            overimeTv.setText(DateUtils.getDateToString2(sPlist.getTime2() + "000"));
            startTime = Long.valueOf(sPlist.getTime1() + "000");
            overTime = Long.valueOf(sPlist.getTime2() + "000");
            isGift = sPlist.getSales();
            salePriceTv.setText(sPlist.getDisstr());
            priceTv.setText(sPlist.getPrice());

        } else {
            idRightBtu.setText("添加");
        }
        if (type.equals("add")) {
            saveid = getIntent().getStringExtra("saveid");
            price = getIntent().getStringExtra("price");
            priceTv.setText(price);
        } else {
            saveid = sPlist.getUid();
        }
    }


    @OnClick(R.id.id_right_btu)
    public void save() {

        String salePrice = salePriceTv.getText().toString();
        if (!salePrice.isEmpty()) {
            saveSales("3", String.valueOf(startTime / 1000), String.valueOf(overTime / 1000),
                    salePrice, "");
        }

    }

    @OnClick(R.id.startimeTv)
    public void showstartTime() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        startTime = date.getTime();
                        startimeTv.setText(DateUtils.getDateToLong(date.getTime()));
                        System.out.println(date.getTime());
                    }
                })
                .setMinDate(new Date())
                .setIs24HourTime(true)
                .setInitialDate(new Date())
                .build()
                .show();
    }

    @OnClick(R.id.overimeTv)
    public void showoverTime() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        overTime = date.getTime();
                        overimeTv.setText(DateUtils.getDateToLong(date.getTime()));
                        System.out.println(date.getTime());
                    }
                })

                .setMinDate(startTime == 0 ? new Date() : new Date(startTime))
                .setIs24HourTime(true)
                .setInitialDate(new Date())
                .build()
                .show();


    }



    public void saveSales(String stype, String starttime, String stoptime
            , String disstr, String gift) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        if (!type.equals("add")) {
            params.put("saveid", sPlist.getId());
        } else {
            params.put("saveid", saveid);
        }


        params.put("stype", stype);
        params.put("itemid", itemid);
        params.put("starttime", starttime);
        params.put("stoptime", stoptime);
        params.put("disstr", disstr);
        if (isGift.equals("1")) {
            params.put("gift", gift);
        }
        //显示ProgressDialog

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
                System.out.println("response" + json);
                String stit = "";
                if (StringHelper.isEmpty(itemid)) {
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
