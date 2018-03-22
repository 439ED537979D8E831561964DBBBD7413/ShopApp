package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.baidu.BaiduTool;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/15.
 */
public class SAddressRefreshActivity extends BaseActivity implements BaiduTool.BaiduClient {

    public static final int REFRESHY_MSG = 2;

    Address mAddress;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.add_address_name_edt)
    EditText addAddressNameEdt;
    @BindView(R.id.add_address_shopname_edt)
    EditText addAddressShopnameEdt;
    @BindView(R.id.add_address_phone_edt)
    EditText addAddressPhoneEdt;
    @BindView(R.id.add_address_tel_edt)
    EditText addAddressTelEdt;
    @BindView(R.id.add_address_detail_edt)
    EditText addAddressDetailEdt;
    @BindView(R.id.manager_checkBox)
    CheckBox managerCheckBox;
    String latAndLot;

    String uid;
    String token;
    @BindView(R.id.refresh_btn)
    ImageView refreshBtn;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    private int back = 0;
    BaiduTool baiduTool;

    @OnClick(R.id.refresh_btn)
    public void OnclickRefresh() {
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            BprogressDialog.show();
            baiduTool.start();
        }
    }

    //显示ProgressDialog
    KProgressHUD BprogressDialog;

    private static final int BAIDU_READ_PHONE_STATE = 100;
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_address_refresh;
    }

    @Override
    protected void initData() {
        baiduTool = new BaiduTool(mContext, this);
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        BprogressDialog = growProgress(Contants.Progress.BAIDU_ING);
        mAddress = (Address) getIntent().getExtras().getSerializable("been");
        back = getIntent().getExtras().getInt("back");
        if (back != 0) {
            forewadImg.setVisibility(View.GONE);
        }
        if (StringHelper.isEmpty(mAddress.getId())) {
            title.setText("完善地址");
            //idRightBtu.setText("提交");
            addAddressNameEdt.setText("");
            addAddressShopnameEdt.setText("");
            addAddressPhoneEdt.setText("");
            addAddressTelEdt.setText("");
            addAddressDetailEdt.setText("");
            mAddress.setStatus("1");
        } else {
            title.setText("修改地址");
            //idRightBtu.setText("提交");

            addAddressNameEdt.setText(mAddress.getContacts());
            addAddressShopnameEdt.setText(mAddress.getShopname());
            addAddressPhoneEdt.setText(mAddress.getMobile());
            addAddressTelEdt.setText(mAddress.getTel());
            addAddressDetailEdt.setText(mAddress.getAddress());
        }

        if (mAddress.getStatus().equals("1")) {
            managerCheckBox.setChecked(true);
        } else {
            managerCheckBox.setChecked(false);
        }
    }


    public boolean check() {
        final String name = addAddressNameEdt.getText().toString().trim();
        final String shopname = addAddressShopnameEdt.getText().toString().trim();
        final String phone = addAddressPhoneEdt.getText().toString().trim();
        final String tel = addAddressTelEdt.getText().toString().trim();
        final String address = addAddressDetailEdt.getText().toString().trim();

        if (StringHelper.isEmpty(shopname)) {
            showToastShort("请填写店铺名称");
            return false;
        }
        if (StringHelper.isEmpty(phone)) {
            showToastShort("请填写手机号码");
            return false;
        }
        if (StringHelper.isEmpty(address)) {
            showToastShort("请填写详细地址");
            return false;
        }

        return true;
    }

    private boolean backPressedToExitOnce = false;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (back == 1) {
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
                return true;
            } else {
                return super.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void report(int status, String value) {
        if (status == 0) {
            addAddressDetailEdt.setText(value);
        } else {
            showToastShort("定位失败");
        }
        BprogressDialog.dismiss();
    }

    @Override
    public void getLocation(BDLocation location) {
        latAndLot = location.getLatitude() + "," + location.getLongitude();
    }



    @OnClick(R.id.submit)
    public void onClick() {
        if (!check()) {
            return;
        }

        final String name = addAddressNameEdt.getText().toString().trim();
        final String shopname = addAddressShopnameEdt.getText().toString().trim();
        final String phone = addAddressPhoneEdt.getText().toString().trim();
        final String tel = addAddressTelEdt.getText().toString().trim();
        final String address = addAddressDetailEdt.getText().toString().trim();


        if (StringHelper.isEmpty(name)) {
            showToastShort("请输入正确的用户名");
            return;
        }
        if (StringHelper.isEmpty(shopname)) {
            showToastShort("请输入正确的商店名");
            return;
        }
        if (StringHelper.isEmpty(phone)) {
            showToastShort("请输入正确的电话号码");
            return;
        }
//        if (StringHelper.isEmpty(tel)) {
//            showToastShort("请输入正确的固定号码");
//            return;
//        }
        if (StringHelper.isEmpty(address)) {
            showToastShort("请输入正确的地址");
            return;
        }

        if (!NetUtils.isNetworkAvailable(mContext)) {
            showToastShort(Contants.NetStatus.NETDISABLE);
            return;
        }

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("shopname", shopname);
        params.put("contacts", name);
        params.put("tel", tel);
        params.put("mobile", phone);
        params.put("address", address);
        params.put("status", managerCheckBox.isChecked() ? "1" : "0");
        params.put("id", StringHelper.isEmpty(mAddress.getId()) ? "" : mAddress.getId());
        params.put("location", StringHelper.isEmpty(latAndLot) ? "" : latAndLot);

        HttpHelper.getInstance().post(mContext, Contants.PortU.DoUaddress, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("添加成功");
                    CommonUtils.goResult(mContext, null, REFRESHY_MSG);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    /**
     * 定位权限
     */
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(SAddressRefreshActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        } else {
            baiduTool.start();
            BprogressDialog.show();
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    BprogressDialog.show();
                    baiduTool.start();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}
