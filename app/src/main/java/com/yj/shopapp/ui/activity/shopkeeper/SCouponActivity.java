package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.ScashCoupon;
import com.yj.shopapp.ui.activity.base.BaseTabActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by huanghao on 2016/12/3.
 */

public class SCouponActivity extends BaseTabActivity {


    @BindView(R.id.viewpager)
    ViewPager viewpager;
    FragmentManager fragmentManager;
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    Unbinder unbinder;
    CouponFragment availableFragment, notAvailableFragment;
    public String WId;
    public String uid;
    public String token;
    public String agentuid;
    public String idstr;
    ScashCoupon scashCoupon;

    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;

    //    @BindView(R.id.title)
//    TextView title;
    @OnClick(R.id.forewadImg)
    public void back() {
        this.finish();
    }

    @OnClick(R.id.id_right_btu)
    public void OkOnclick() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (ScashCoupon.CanuseBean cartList : scashCoupon.getCanuse()) {
            if (availableFragment.chooseArray.get(i) == 1) {
                stringBuffer.append(cartList.getId() + "|");
            }
            i++;
        }

        if (stringBuffer.toString().length() > 0) {
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);
            Bundle bundle = new Bundle();
            bundle.putString("CashCoupon", idstr);
            CommonUtils.goResult(mContext, bundle, 999);


        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sactivity_coupon);
        unbinder= ButterKnife.bind(this);
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        WId = PreferenceUtils.getPrefString(mContext, Contants.Preference.AGENTUID, "");
        agentuid = PreferenceUtils.getPrefString(mContext, Contants.Preference.AGENTUID, "");
        scashCoupon = (ScashCoupon) getIntent().getSerializableExtra("Coupon");
        title.setText("现金券列表");
        idRightBtu.setText("确定");

    }

    @Override
    protected void init(Bundle savedInstanceState) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                TabAdapter tabAdapter = new TabAdapter(mContext, SCouponActivity.this.getSupportFragmentManager());

                viewpager.setAdapter(tabAdapter);
                viewpager.setOffscreenPageLimit(2);
                viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {


                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tabsTl.setupWithViewPager(viewpager);

                tabsTl.setTabMode(TabLayout.MODE_FIXED);
            }
        }, 100);
    }

    public class TabAdapter extends FragmentPagerAdapter {

        public String[] pageName = Contants.Coupon;

        public TabAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                availableFragment = CouponFragment.newInstance(position);
                availableFragment.megsList = scashCoupon.getCanuse();
                return availableFragment;
            }
            if (position == 1) {
                notAvailableFragment = CouponFragment.newInstance(position);
                notAvailableFragment.megsList = scashCoupon.getUncanuse();
                return notAvailableFragment;
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageName[position];
        }

        @Override
        public int getCount() {
            return pageName.length;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
