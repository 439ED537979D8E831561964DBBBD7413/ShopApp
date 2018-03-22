package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderChart;
import com.yj.shopapp.ubeen.PieData;
import com.yj.shopapp.ui.activity.Interface.OnDateListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.PieChartAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.Allpopupwindow;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.PieChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PieChartActivity extends BaseActivity implements OnDateListenter {

    @BindView(R.id.title)
    TextView title;
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
    private String startTime = "";
    private String endTime = "";
    private List<PieData> mdatas = new ArrayList<>();
    private OrderChart chart;
    private PieChartAdpter adpter;


    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            onRequstData();
        } else {
            showToastShort("网络未连接");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pie_chart;
    }

    @Override
    protected void initData() {
        title.setText("订单统计");
        adpter = new PieChartAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext));
        myRecyclerView.setAdapter(adpter);
    }

    private void initDatas() {
        mdatas.clear();
        for (int i = 0; i < chart.getList().size(); i++) {
            OrderChart.ListBean b = chart.getList().get(i);
            mdatas.add(new PieData(b.getClassX(), Float.parseFloat(b.getMoney())));
            float pressent = Float.parseFloat(b.getMoney()) / Float.parseFloat(chart.getAllmoney());
            b.setPercentage(pressent * 100 + "%");
        }
        picChart.setData(mdatas, PieChart.COUNT);
    }

    private void onRequstData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("start", startTime);
        params.put("end", endTime);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ORDER_CHART, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    chart = JSONObject.parseObject(json, OrderChart.class);
                    screentext.setText("今天");
                    screenData.setText(String.format("总金额：%1$s   共%3$s个订单共   %2$s件商品", chart.getAllmoney(), chart.getAllcount(), chart.getOrder_num()));
                    adpter.setList(chart.getList());
                    initDatas();
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    showToastShort("无数据");
                    screenData.setText("");
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
        });
    }


    @Override
    public void getDate(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        if (!"".equals(startTime) && !"".equals(endTime)) {
            screentext.setText(String.format("%1$s至%2$s", startTime, endTime));
        } else if (!"".equals(startTime) && "".equals(endTime)) {
            screentext.setText(String.format("%1$s至今", startTime));
        } else {
            screentext.setText("");
        }
        onResume();
    }

    @OnClick(R.id.timeScreen)
    public void onViewClicked() {
        new Allpopupwindow(mContext).setListenter(this).showAsDropDown(screentext);
    }

}
