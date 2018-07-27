package com.yj.shopapp.ui.activity.wholesale;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.GoodAddress;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.LiftAdpter;
import com.yj.shopapp.ui.activity.adapter.RightAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by LK on 2017/10/29.
 *
 * @author LK
 */

public class GoodSheives extends BaseActivity {
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.leftListView)
    ListView leftListView;
    @BindView(R.id.rightListView)
    ListView rightListView;
    List<GoodAddress> goodAddresses;
    List<GoodAddress.ChildrenBean> childrenBeans;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private LiftAdpter liftAdpter;
    private RightAdpter rightAdpter;
    private View tv2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goodsheives;
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
        title.setText("商品位置");
        liftAdpter = new LiftAdpter(mContext);
        rightAdpter = new RightAdpter(mContext);
        leftListView.setAdapter(liftAdpter);
        rightListView.setAdapter(rightAdpter);
        rightListView.setEmptyView(emptyView);
        emptyView.setText("暂无数据");
        getGoodSheives();
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                liftAdpter.setDefSelect(position);
                childrenBeans = goodAddresses.get(position).getChildren();
                rightAdpter.setList(childrenBeans);
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tv2 != null) {
                    tv2.setBackgroundColor(getResources().getColor(R.color.white));
                }
                view.setBackgroundColor(getResources().getColor(R.color.all_bg));
                tv2 = view;
                EventBus.getDefault().post(childrenBeans.get(position));
                finish();
            }
        });
    }


    /**
     * 获取货架
     */
    private void getGoodSheives() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortA.LOCALHOST_AREA, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    goodAddresses = JSONArray.parseArray(json, GoodAddress.class);
                    liftAdpter.setList(goodAddresses);
                    if (goodAddresses.size() > 0) {
                        liftAdpter.setDefSelect(0);
                        childrenBeans = goodAddresses.get(0).getChildren();
                        rightAdpter.setList(childrenBeans);
                    }
                }

            }
        });
    }

}
