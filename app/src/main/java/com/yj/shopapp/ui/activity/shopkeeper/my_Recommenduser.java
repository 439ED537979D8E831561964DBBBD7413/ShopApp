package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Extend;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.RecommendAdpter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author LK
 */
public class my_Recommenduser extends BaseFragment implements RecommendAdpter.Callback {

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private List<Extend> list;
    private RecommendAdpter adpter;
    private List<Integer> showControl = new ArrayList<>();
    private int position;

    @Override
    public void init(Bundle savedInstanceState) {
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);
        emptyView.setText("暂无推荐\r\n\r\n推荐用户领百元红包，你还在等什么!");
        adpter = new RecommendAdpter(mActivity, this);
        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != swipeRefreshLayout) {
                swipeRefreshLayout.postDelayed(this::fresh, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;

                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fresh();
    }

    public void fresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshRequest();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my__recommenduser;
    }

    SwipeRefreshLayout.OnRefreshListener listener = this::refreshRequest;

    /**
     * 网络请求
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.EXTENDLIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    list = getlist(json);
                    for (int i = 0; i < list.size(); i++) {
                        showControl.add(1);
                    }
                    assert adpter != null;
                    adpter.setList(list);
                    adpter.setmshowControl(showControl);
                    listView.setAdapter(adpter);
                    listView.setEmptyView(emptyView);
                    listView.setSelection(position);
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });

    }

    private List<Extend> getlist(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Extend>>() {
        }.getType());
    }

    @Override
    public void click(View v) {
        switch (v.getId()) {
            case R.id.tv_3:
                position = listView.getFirstVisiblePosition();
                Bundle b = new Bundle();
                int index = (int) v.getTag();
                b.putString("uid", (list.get(index).getId()) + "");
                CommonUtils.goActivity(mActivity, RedPackActivity.class, b);
                break;
            case R.id.retv_4:
                int indexs = (int) v.getTag();
                if (1 == (list.get(indexs).getStatus())) {
                    //跳转到提现界面
                    position = listView.getFirstVisiblePosition();
                    ShowLog.e(position + "");
                    Bundle bundle = new Bundle();
                    bundle.putString("reward_type", "2");
                    bundle.putString("useruid", list.get((Integer) v.getTag()).getUid() + "");
                    bundle.putString("reward_id", list.get((Integer) v.getTag()).getRid() + "");
                    bundle.putString("reward", list.get((Integer) v.getTag()).getReward() + "");
                    CommonUtils.goActivity(mActivity, RedPackReFlect.class, bundle);
                }
                break;
            default:
                break;
        }
    }

}
