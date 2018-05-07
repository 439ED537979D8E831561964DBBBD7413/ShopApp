package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2017/10/24.
 */

public class Recommend extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.id_drawer_layout)
    LinearLayout idDrawerLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    private myAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommended_awards;
    }

    @Override
    protected void initData() {
        title.setText("推荐奖励");
        idRightBtu.setText("超级红包");
        adapter = new myAdapter(getSupportFragmentManager());
        if (pager != null) {
            pager.setAdapter(adapter);
        }
        tabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(2);
    }


    @OnClick(R.id.id_right_btu)
    public void onViewClicked() {
        //跳转到领取界面
        refreshRequest();
    }

    /**
     * 网络请求
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.CHECK_REWARD, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                try {
                    JSONObject object = new JSONObject(json);
                    if (object.getInt("status") == 0) {
                        showToastLong(object.getString("info"));
                    } else {
                        //跳转到提现界面
                        Bundle bundle = new Bundle();
                        bundle.putString("reward_type", object.getString("reward_id"));
                        bundle.putString("reward", object.getString("reward"));
                        CommonUtils.goActivity(mContext, RedPackReFlect.class, bundle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public class myAdapter extends FragmentPagerAdapter {

        public myAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new activityRule();
                case 1:
                    return new my_Recommenduser();
                case 2:
                    return new myRedPack();
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabName)[position];
        }
    }
}
