package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.MyWebView;
import com.yj.shopapp.ui.activity.PicasaActivity;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BottomDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.CustomPopDialog2;
import com.yj.shopapp.util.PreferenceUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 */
public class WMyInfoActivity extends BaseActivity implements BottomDialog.OnCenterItemClickListener {

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
    String uid;
    String token;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.new_goods_rl)
    RelativeLayout newGoodsRl;
    @BindView(R.id.account_tv)
    TextView accountTv;
    private BottomDialog bottomDialog;
    private Bitmap bitmap;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("cameraPath", cameraPath);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_myinfo;
    }

    @Override
    protected void initData() {
        title.setText("个人中心");
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        String account = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_NAME, "");
        accountTv.setText(account);
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }
        bottomDialog = new BottomDialog(mContext, R.layout.bottom_dialog, new int[]{R.id.save_photoalbum, R.id.dialog_cancel});
        bottomDialog.setOnCenterItemClickListener(this);
    }


    @OnClick(R.id.updateInfo)
    public void clickupdateInfo() {
        CommonUtils.goActivity(mContext, WUserInfoActivity.class, null, false);
    }

    @OnClick(R.id.new_goods_rl)
    public void clicknewgoodsrl() {
        Intent intent = new Intent(mContext, WNewGoodAcitvity.class);
        startActivity(intent);
    }

    @OnClick(R.id.updatePwd)
    public void clickupdatePwd() {
        CommonUtils.goActivity(mContext, WDoPasswdActivity.class, null, false);
    }


//    @OnClick(R.id.cashcouponmanager_rl)
//    public void cashcouponmanagerOnclick() {
//        CommonUtils.goActivity(mContext, WCashCouponActivity.class, null, false);
//    }

    @OnClick(R.id.news)
    public void clicknews() {
        CommonUtils.goActivity(mContext, WNewListActivity.class, null, false);
    }


    @OnClick(R.id.ImgManage)
    public void clickImgManage() {
        CommonUtils.goActivityForResult(mContext, PicasaActivity.class, null, 0, false);
    }

    @OnClick(R.id.mSales)
    public void onClicksales() {
        CommonUtils.goActivity(mContext, WSalesActivity.class, null, false);
    }

    //@OnClick(R.id.duetime_re)
//public void onclickduetime()
//{
//    CommonUtils.goActivity(mContext, WDuetimeActivity.class, null, false);
//}
    @OnClick(R.id.stop_goods_rl)
    public void onclickstopgoods() {
        CommonUtils.goActivity(mContext, WStopGoodsActivity.class, null, false);
    }

    @OnClick(R.id.exit)
    public void onClickExit() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("what", false);
        CommonUtils.goActivity(this, LoginActivity.class, bundle, false);
    }

    @OnClick(R.id.help)
    public void onClickHelp() {
        Bundle bundle = new Bundle();
        bundle.putString("wUrl", Contants.u + "index.php/Appi/help");
        CommonUtils.goActivity(this, MyWebView.class, bundle, false);
    }

    @OnClick(R.id.renew)
    public void onClickRenew() {
//        Bundle bundle = new Bundle();
//        bundle.putString("wUrl",Contants.u+"index.php/Appi/pay?u="+uid);
//        CommonUtils.goActivity(this, MyWebView.class,bundle,false);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(Contants.u + "index.php/Appi/pay?u=" + uid);
        intent.setData(content_url);
        startActivity(intent);

    }

    @OnClick(R.id.expand)
    public void onClickExpand() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.two_code);// 这里是获取图片Bitmap，也可以传入其他参数到Dialog中
        CustomPopDialog2.Builder dialogBuild = new CustomPopDialog2.Builder(mContext, bitmap);
        dialogBuild.setLongclick(new CustomPopDialog2.Builder.onLongclick() {
            @Override
            public void onLClick(View view) {
                bottomDialog.show();
            }
        });
        CustomPopDialog2 dialog = dialogBuild.create();
        dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
        dialog.show();
//        Bundle bundle = new Bundle();
//        bundle.putString("wUrl", Contants.u + "index.php/Appi/sales?u=" + uid);
//        CommonUtils.goActivity(this, MyWebView.class, bundle, false);
    }

    private void saveImg(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "imgbitmap", "");
        ShowLog.e(path);
        //通知图库刷新
        showToastShort("保存成功");
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
    }

    @OnClick(R.id.w_updata)
    public void onClick() {
        //VersionUpdata.getInstance(true, mContext, getFragmentManager()).updateVersion();
    }

    @Override
    public void OnCenterItemClick(BottomDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.save_photoalbum:
                saveImg(bitmap);
                break;
            case R.id.dialog_cancel:

                break;
            default:
                break;
        }
    }



}
