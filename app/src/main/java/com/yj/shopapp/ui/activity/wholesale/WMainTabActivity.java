package com.yj.shopapp.ui.activity.wholesale;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.adapter.WhomePagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseTabActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 导航页面
 * 批发商
 */
public class WMainTabActivity extends BaseTabActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.whomeViewPager)
    CustomViewPager whomeViewPager;
    @BindView(R.id.tabs_rg)
    RadioGroup tabsRg;
    Unbinder unbinder;
    private int currentTab = 0;
    private WhomePagerAdpter pagerAdpter;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.wactivity_tab);
        unbinder = ButterKnife.bind(this);
        pagerAdpter = new WhomePagerAdpter(getSupportFragmentManager());
        whomeViewPager.setAdapter(pagerAdpter);
        whomeViewPager.setOpenAnimation(false);
        whomeViewPager.setScanScroll(false);
        whomeViewPager.setOffscreenPageLimit(2);
        whomeViewPager.addOnPageChangeListener(this);
        tabsRg.setOnCheckedChangeListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(String msg) {
        PreferenceUtils.remove(mContext, Contants.Preference.UID);
        PreferenceUtils.remove(mContext, Contants.Preference.UTYPE);
        PreferenceUtils.remove(mContext, Contants.Preference.TOKEN);
        CommonUtils.goActivity(mContext, LoginActivity.class, null, true);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
//
//        Fragment f = mFragments.get(currentTab);
//        /*然后在碎片中调用重写的onActivityResult方法*/
//        f.onActivityResult(requestCode, resultCode, data);
//    }

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
            new Handler().postDelayed(() -> backPressedToExitOnce = false, 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(mContext);
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
                tabsRg.check(R.id.tab_mtinfo);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_home:
                whomeViewPager.setCurrentItem(0);
                break;
            case R.id.tab_order:
                whomeViewPager.setCurrentItem(1);
                break;
            case R.id.tab_mtinfo:
                whomeViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }
}
