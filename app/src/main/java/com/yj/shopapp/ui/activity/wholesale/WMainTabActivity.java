package com.yj.shopapp.ui.activity.wholesale;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.base.BaseTabActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.PreferenceUtils;

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
public class WMainTabActivity extends BaseTabActivity implements View.OnClickListener {


    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.tab_home)
    RadioButton tabHome;
    @BindView(R.id.tab_order)
    RadioButton tabOrder;
    @BindView(R.id.tab_mtinfo)
    RadioButton tabMtinfo;


    private int currentTab = 0;
    Unbinder unbinder;

//    private void checkAndUpdate(Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            VersionUpdata.getInstance(false, mContext,getFragmentManager()).updateVersion();
//
//        } else {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                VersionUpdata.getInstance(false, mContext,getFragmentManager()).updateVersion();
//            } else {//申请权限
//                ActivityCompat.requestPermissions((Activity) context,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    VersionUpdata.getInstance(false, mContext,getFragmentManager()).updateVersion();
//                } else {
//                    new ConfirmDialog(this, new Callback() {
//                        @Override
//                        public void callback(int position) {
//                            if (position == 1) {
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
//                                startActivity(intent);
//                            }
//                        }
//                    }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.wactivity_tab);
        unbinder = ButterKnife.bind(this);
        mFragments.add(WHomeActivity.newInstance());
        mFragments.add(new WOrderActivity());
        mFragments.add(new WTestActivity());


        // 默认显示第一页
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, mFragments.get(currentTab));
        ft.commitAllowingStateLoss();

        tabHome.setOnClickListener(this);
        tabOrder.setOnClickListener(this);
        tabMtinfo.setOnClickListener(this);
        EventBus.getDefault().register(mContext);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(String msg) {
        PreferenceUtils.remove(mContext, Contants.Preference.UID);
        PreferenceUtils.remove(mContext, Contants.Preference.UTYPE);
        PreferenceUtils.remove(mContext, Contants.Preference.TOKEN);
        CommonUtils.goActivity(mContext, LoginActivity.class, null, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                if (currentTab == 0) {
                    break;
                }
                tabHome.setChecked(true);
                tabOrder.setChecked(false);
                tabOrder.setTextColor(getResources().getColor(R.color.text_tab));
                tabHome.setTextColor(getResources().getColor(R.color.blue));
                setOnCheckChange(0);
                break;
            case R.id.tab_order:
                if (currentTab == 1) {
                    break;
                }
                tabOrder.setChecked(true);
                tabHome.setChecked(false);
                tabHome.setTextColor(getResources().getColor(R.color.text_tab));
                tabOrder.setTextColor(getResources().getColor(R.color.blue));
                setOnCheckChange(1);
                break;
            case R.id.tab_mtinfo:
                CommonUtils.goActivity(mContext, WMyInfoActivity.class, null, false);
                break;
            default:
                break;
        }
    }

    public void setOnCheckChange(int i) {
        Fragment fragment = mFragments.get(i);
        FragmentTransaction ft = obtainFragmentTransaction(i);

        mFragments.get(currentTab).onPause(); // 暂停当前tab

        if (fragment.isAdded()) {
            fragment.onResume(); // 启动目标tab的onResume()
        } else {
            ft.add(R.id.content, fragment);
        }
        showTab(i);
        ft.commitAllowingStateLoss();
    }


    /**
     * 切换tab
     */
    private void showTab(int idx) {
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/

        Fragment f = mFragments.get(currentTab);
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);


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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(mContext);
    }

}
