package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ClientOrderDetails;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClientOrderDetailsAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 *
 */
public class ClientOrderDetailsActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.order_id)
    TextView orderId;
    @BindView(R.id.orderdatails_rv)
    RecyclerView orderdatailsRv;
    @BindView(R.id.all_monry_tv)
    TextView allMonryTv;
    @BindView(R.id.profit_tv)
    TextView profitTv;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private String profitId;
    private ClientOrderDetails details;
    private ClientOrderDetailsAdpter detailsAdpter;
    private String shopName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_client_order_details;
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
        if (getIntent().hasExtra("id")) {
            profitId = getIntent().getStringExtra("id");
        }
        if (getIntent().hasExtra("name")) {
            shopName = getIntent().getStringExtra("name");
            title.setText(String.format("%s - 收益详情", shopName));
        }
        requestData();
        detailsAdpter = new ClientOrderDetailsAdpter(mContext);
        orderdatailsRv.setLayoutManager(new LinearLayoutManager(mContext));
        orderdatailsRv.setAdapter(detailsAdpter);
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", profitId);

        HttpHelper.getInstance().post(mContext, Contants.PortU.RECOMMEND_PROFITDETAILS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    details = JSONObject.parseObject(response, ClientOrderDetails.class);
                    if (details != null) {
                        setData();
                    }
                }
            }
        });
    }

    private void setData() {
        orderId.setText(String.format("订单号:%s", details.getMainorder()));
        profitTv.setText(String.format("收益￥%s", details.getProfit()));
        detailsAdpter.setList(details.getContents());
        Double money = 0.0;
        for (ClientOrderDetails.ContentsBean s : details.getContents()) {
            money += Double.parseDouble(s.getMoney());
        }
        allMonryTv.setText(String.format("总金额￥%s", money));
    }

}
