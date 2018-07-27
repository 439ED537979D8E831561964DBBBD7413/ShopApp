package com.yj.shopapp.ui.activity.wholesale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BottomDialog;
import com.yj.shopapp.dialog.CustomPopDialog2;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.MyWebView;
import com.yj.shopapp.ui.activity.PicasaActivity;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 */
public class WMyInfoActivity extends NewBaseFragment implements BottomDialog.OnCenterItemClickListener {

    @BindView(R.id.updateInfo)
    RelativeLayout updateInfo;
    @BindView(R.id.updatePwd)
    RelativeLayout updatePwd;
    @BindView(R.id.news)
    RelativeLayout news;
    @BindView(R.id.ImgManage)
    RelativeLayout ImgManage;
    @BindView(R.id.mSales)
    RelativeLayout mSales;
    @BindView(R.id.renew)
    RelativeLayout renew;
    @BindView(R.id.help)
    RelativeLayout help;
    @BindView(R.id.exit)
    TextView exit;
    String cameraPath;

    @BindView(R.id.new_goods_rl)
    RelativeLayout newGoodsRl;
    @BindView(R.id.account_tv)
    TextView accountTv;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.Customer_service)
    TextView CustomerService;
    private final int REQUEST_CODEC = 0x1001;
    private BottomDialog bottomDialog;
    private static final int REQUEST_CODE = 1;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cameraPath", cameraPath);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_myinfo;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
        String account = PreferenceUtils.getPrefString(mActivity, Contants.Preference.USER_NAME, "");
        accountTv.setText(account);
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }
        bottomDialog = new BottomDialog(mActivity, R.layout.bottom_dialog, new int[]{R.id.save_photoalbum, R.id.dialog_cancel});
        bottomDialog.setOnCenterItemClickListener(this);
        CustomerService.setText(PreferenceUtils.getPrefString(mActivity, "CustomerService", ""));

    }


    @OnClick(R.id.updateInfo)
    public void clickupdateInfo() {
        CommonUtils.goActivity(mActivity, WUserInfoActivity.class, null, false);
    }

    @OnClick(R.id.new_goods_rl)
    public void clicknewgoodsrl() {
        Intent intent = new Intent(mActivity, WNewGoodAcitvity.class);
        startActivity(intent);
    }

    @OnClick(R.id.updatePwd)
    public void clickupdatePwd() {
        CommonUtils.goActivity(mActivity, WDoPasswdActivity.class, null, false);
    }

    @OnClick(R.id.news)
    public void clicknews() {
        CommonUtils.goActivity(mActivity, WNewListActivity.class, null, false);
    }


    @OnClick(R.id.ImgManage)
    public void clickImgManage() {
        CommonUtils.goActivityForResult(mActivity, PicasaActivity.class, null, 0, false);
    }

    @OnClick(R.id.mSales)
    public void onClicksales() {
        CommonUtils.goActivity(mActivity, WSalesActivity.class, null, false);
    }

    @OnClick(R.id.stop_goods_rl)
    public void onclickstopgoods() {
        CommonUtils.goActivity(mActivity, WStopGoodsActivity.class, null, false);
    }

    @OnClick(R.id.exit)
    public void onClickExit() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("what", false);
        CommonUtils.goActivity(mActivity, LoginActivity.class, bundle, false);
    }

    @OnClick(R.id.help)
    public void onClickHelp() {
        Bundle bundle = new Bundle();
        bundle.putString("wUrl", Contants.u + "index.php/Appi/help");
        CommonUtils.goActivity(mActivity, MyWebView.class, bundle, false);
    }

    @OnClick(R.id.renew)
    public void onClickRenew() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(Contants.u + "index.php/Appi/pay?u=" + uid);
        intent.setData(content_url);
        startActivity(intent);

    }

    @OnClick(R.id.expand)
    public void onClickExpand() {
        CustomPopDialog2 dialog2 = new CustomPopDialog2(mActivity);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setLongClick(view -> bottomDialog.show());
        dialog2.show();
    }

    private void saveImg(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, "imgbitmap", "");
        ShowLog.e(path);
        //通知图库刷新
        showToast("保存成功");
        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
    }

    @Override
    public void OnCenterItemClick(BottomDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.save_photoalbum:
                saveImg(BitmapFactory.decodeResource(getResources(), R.drawable.two_code_2));
                break;
            case R.id.dialog_cancel:

                break;
            default:
                break;
        }
    }

    @OnClick(R.id.call_phone)
    public void onViewClicked() {
        new MaterialDialog.Builder(mActivity).title("提示").positiveText("拨打").negativeText("取消")
                .content("是否要拨打客服电话?")
                .onPositive((dialog, which) -> {
                    //拨打电话
                    if (Build.VERSION.SDK_INT >= 23) {
                        //判断有没有拨打电话权限
                        if (PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            //请求拨打电话权限
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODEC);

                        } else {
                            callPhone();
                        }

                    } else {
                        callPhone();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODEC && PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            showToast("授权成功");
            callPhone();
        } else {
            showToast("授权失败");
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + CustomerService.getText().toString()));
            startActivity(intent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)) {
                //已经禁止提示了
                showToast("您已禁止该权限，需要重新开启");
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            }

        }

    }
}
