package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.adapter.HomeViewPager;
import com.yj.shopapp.ui.activity.base.BaseTabActivity;
import com.yj.shopapp.ui.activity.upversion.Callback;
import com.yj.shopapp.ui.activity.upversion.ConfirmDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.VersionUpdata;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 导航页面
 * 零售商
 */
public class SMainTabActivity extends BaseTabActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    public final static int CARLIST = 2135;
    @BindView(R.id.content)
    ViewPager content;
    @BindView(R.id.tab_home)
    RadioButton tabHome;
    @BindView(R.id.tab_order)
    RadioButton tabOrder;
    @BindView(R.id.tab_client)
    TextView tabClient;
    @BindView(R.id.tab_mtinfo)
    TextView tabMtinfo;
    @BindView(R.id.tabs_rg)
    RadioGroup tabsRg;
    private HomeViewPager viewPager;
    Unbinder unbinder;
    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.sactivity_tab);
        unbinder= ButterKnife.bind(this);
        //checkAndUpdate(mContext);
        viewPager = new HomeViewPager(getSupportFragmentManager());
        content.setAdapter(viewPager);
        content.addOnPageChangeListener(this);
        tabsRg.setOnCheckedChangeListener(this);
        exitLoginActivity();

    }

    private void exitLoginActivity() {
        if (AppManager.getAppManager().contains(LoginActivity.class)) {
            AppManager.getAppManager().finishActivity(LoginActivity.class);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            default:
                break;
        }
    }

    @OnClick({R.id.tab_client, R.id.tab_mtinfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_client:
                CommonUtils.goActivity(mContext, SNewCartListActivity.class, null, false);
                break;
            case R.id.tab_mtinfo:
                CommonUtils.goActivity(mContext, SMyInfoActivity.class, null, false);
                break;
            default:
                break;
        }
    }

    private void checkAndUpdate(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            VersionUpdata.getInstance(false, mContext, getFragmentManager()).updateVersion();
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                VersionUpdata.getInstance(false, mContext, getFragmentManager()).updateVersion();
            } else {//申请权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    VersionUpdata.getInstance(false, mContext, getFragmentManager()).updateVersion();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            if (position == 1) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                startActivity(intent);
                            }
                        }
                    }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                }
                break;
            default:
                break;
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
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
