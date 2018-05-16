package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    Button exit;
    String cameraPath;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.new_goods_rl)
    RelativeLayout newGoodsRl;
    @BindView(R.id.account_tv)
    TextView accountTv;
    private BottomDialog bottomDialog;

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

    }

    @Override
    protected void initData() {
        title.setText("个人中心");
        String account = PreferenceUtils.getPrefString(mActivity, Contants.Preference.USER_NAME, "");
        accountTv.setText(account);
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }
        bottomDialog = new BottomDialog(mActivity, R.layout.bottom_dialog, new int[]{R.id.save_photoalbum, R.id.dialog_cancel});
        bottomDialog.setOnCenterItemClickListener(this);
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
        dialog2.setLongClick(new CustomPopDialog2.onLongClick() {
            @Override
            public void onLClick(View view) {
                bottomDialog.show();
            }
        });
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


}
