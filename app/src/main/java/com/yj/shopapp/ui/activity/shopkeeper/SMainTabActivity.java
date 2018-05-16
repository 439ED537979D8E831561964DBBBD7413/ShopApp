package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.HomeViewPager;
import com.yj.shopapp.ui.activity.base.BaseTabActivity;
import com.yj.shopapp.ui.activity.upversion.Download;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.MessageEvent;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.CustomViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 导航页面
 * 零售商
 */
public class SMainTabActivity extends BaseTabActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    public final static int CARLIST = 2135;
    @BindView(R.id.content)
    CustomViewPager content;
    @BindView(R.id.tabs_rg)
    RadioGroup tabsRg;
    private HomeViewPager viewPager;
    Unbinder unbinder;
    public String uid;
    public String token;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.sactivity_tab);
        unbinder = ButterKnife.bind(this);
        viewPager = new HomeViewPager(getSupportFragmentManager());
        content.setOffscreenPageLimit(4);
        content.setScanScroll(false);
        content.setOpenAnimation(false);
        content.setAdapter(viewPager);
        content.addOnPageChangeListener(this);
        tabsRg.setOnCheckedChangeListener(this);
        exitLoginActivity();
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        getSite();

    }

    private void exitLoginActivity() {
        if (AppManager.getAppManager().contains(LoginActivity.class)) {
            AppManager.getAppManager().finishActivity(LoginActivity.class);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        EventBus.getDefault().post(new MessageEvent(2, ""));
        switch (checkedId) {
            case R.id.tab_home:
                content.setCurrentItem(0);
                break;
            case R.id.tab_order:
                content.setCurrentItem(1);
                break;
            case R.id.tab_brand:
                content.setCurrentItem(2);
                break;
            case R.id.tab_client:
                content.setCurrentItem(3);
                EventBus.getDefault().post(new MessageEvent());
                break;
            case R.id.tab_mtinfo:
                content.setCurrentItem(4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取地址
     */
    private void getSite() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Uaddress, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onBefore() {
                super.onBefore();

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    Address object = JSONArray.parseArray(json, Address.class).get(0);
                    PreferenceUtils.setPrefString(mContext, "addressId", object.getId());
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    PreferenceUtils.remove(mContext, "addressId");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private boolean backPressedToExitOnce = false;

    public void exit() {
        if (backPressedToExitOnce) {
            AppManager.getAppManager().finishAllActivity();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(mContext, "再按一次「返回键」退出", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
        Fragment f = viewPager.getItem(0);
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabsRg.check(R.id.tab_home);
                break;
            case 1:
                tabsRg.check(R.id.tab_order);
                break;
            case 2:
                tabsRg.check(R.id.tab_brand);
                break;
            case 3:
                tabsRg.check(R.id.tab_client);
                break;
            case 4:
                tabsRg.check(R.id.tab_mtinfo);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        Download.onDestory();
        CommonUtils.fixInputMethodManagerLeak(SMainTabActivity.this);
        super.onDestroy();
        unbinder.unbind();
    }

}
