package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderChart;
import com.yj.shopapp.ubeen.PieData;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.PieChartAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.view.PieChart;
import com.yj.shopapp.view.YearPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class PieChartActivity extends BaseActivity {

    @BindView(R.id.screentext)
    TextView screentext;
    @BindView(R.id.timeScreen)
    TextView timeScreen;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.pic_chart)
    PieChart picChart;
    @BindView(R.id.screenData)
    TextView screenData;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private String time = "";
    private List<PieData> mdatas = new ArrayList<>();
    private OrderChart chart;
    private PieChartAdpter adpter;
    private List<OrderChart.ListBean> listBeans = new ArrayList<>();
    private int mYear, mMonth, mDay;
    private int cYear, cMonth, cDay;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pie_chart;
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
        rightTv.setText("全部");
        contentTv.setText("订单统计");
        adpter = new PieChartAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        myRecyclerView.setAdapter(adpter);
        if (isNetWork(mContext)) {
            onRequstData();
        }
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        mMonth = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        cYear = dateAndTime.get(Calendar.YEAR);
        cMonth = dateAndTime.get(Calendar.MONTH);
        cDay = dateAndTime.get(Calendar.YEAR);
        screentext.setText(String.format("%1$d-%2$02d", mYear, mMonth + 1));
    }

    private void initDatas() {
        mdatas.clear();
        for (int i = 0; i < chart.getList().size(); i++) {
            OrderChart.ListBean b = chart.getList().get(i);
            mdatas.add(new PieData(b.getClassX(), Float.parseFloat(b.getMoney())));
            float pressent = Float.parseFloat(b.getMoney()) / Float.parseFloat(chart.getAllmoney());
            b.setPercentage(String.format("%.2f%%", pressent * 100));
        }

        picChart.setData(mdatas);
    }

    private void onRequstData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("month", time);
        ShowLog.e(time);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ORDER_CHART, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (json.startsWith("{")) {
                    chart = JSONObject.parseObject(json, OrderChart.class);
                    screenData.setText(String.format("总金额：%1$s   共%3$s个订单   共%2$s件商品", chart.getAllmoney(), chart.getAllcount(), chart.getOrder_num()));
                    listBeans.addAll(chart.getList());
                    adpter.setList(listBeans);
                    initDatas();
                    if (loading != null) {
                        loading.showContent();
                    }
                } else {
                    screenData.setText("");
                    mdatas.clear();
                    picChart.setData(mdatas);
                    listBeans.clear();
                    adpter.notifyDataSetChanged();
                    if (loading != null) {
                        loading.showEmpty();
                    }
                }
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
            public void onBefore() {
                super.onBefore();
                listBeans.clear();
            }
        });
    }

    @OnClick({R.id.right_tv, R.id.timeScreen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                time = "";
                screentext.setText("全部");
                onRequstData();
                break;
            case R.id.timeScreen:
                Context themed = new ContextThemeWrapper(mContext,
                        android.R.style.Theme_Holo_Light_Dialog);
                YearPickerDialog datePicker = new YearPickerDialog(themed, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        time = String.format("%1$d-%2$02d", year, month + 1);
                        screentext.setText(String.format("%1$d-%2$02d", year, month + 1));
                        onRequstData();
                    }
                }
                        , mYear, mMonth, mDay); //上下文，点击回调,Calendar年月日
                if (!datePicker.isHasNoDay()) {
                    datePicker.setHasNoDay(true);
                }
                datePicker.getDatePicker().init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cYear = year;
                        cMonth = monthOfYear;
                        cDay = dayOfMonth;
                    }
                });
                datePicker.getDatePicker().setMaxDate((new Date()).getTime());
                datePicker.show();
                break;
        }
    }
}
