package com.yj.shopapp.ui.activity.shopkeeper;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.dialog.BottomDialog;
import com.yj.shopapp.dialog.CenterDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.dialog.CustomPopDialog2;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 *
 * @author LK
 */
public class SMyInfoActivity extends NewBaseFragment implements CenterDialog.OnCenterItemClickListener, BottomDialog.OnCenterItemClickListener {
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.account_tv)
    TextView accountTv;
    @BindView(R.id.updateInfo)
    RelativeLayout updateInfo;
    @BindView(R.id.updatePwd)
    RelativeLayout updatePwd;
    @BindView(R.id.address)
    RelativeLayout address;
    @BindView(R.id.news)
    RelativeLayout news;
    @BindView(R.id.marks)
    RelativeLayout marks;
    @BindView(R.id.mSales)
    RelativeLayout mSales;
    @BindView(R.id.expand)
    RelativeLayout expand;
    @BindView(R.id.exit_esc)
    TextView exitEsc;
    @BindView(R.id.Recommender)
    RelativeLayout Recommender;
    @BindView(R.id.bgView)
    RelativeLayout bgView;
    @BindView(R.id.Customer_service)
    TextView CustomerService;
    private CenterDialog centerDialog;
    private BottomDialog bottomDialog;
    private TextView tv;
    private EditText editText;
    private static final int REQUEST_CODE = 1;
    private List<Address> notes = new ArrayList<Address>();
    private String cameraPath;
    String pw = "";
    private final int REQUEST_CODEC = 0x1001;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cameraPath", cameraPath);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_myinfo;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(bgView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        int index = PreferenceUtils.getPrefInt(mActivity, Contants.Preference.CHECKNUM, 0);
        if (index == 1) {
            Recommender.setVisibility(View.VISIBLE);
        }
        String account = PreferenceUtils.getPrefString(mActivity, Contants.Preference.USER_NAME, "");
        accountTv.setText(account);
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestAlertWindowPermission();
            }
        }
        CustomerService.setText(PreferenceUtils.getPrefString(mActivity, "CustomerService", ""));

    }

    @Override
    protected void initData() {
        centerDialog = new CenterDialog(mActivity, R.layout.recommenderactivity, new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        centerDialog.setOnCenterItemClickListener(this);
        bottomDialog = new BottomDialog(mActivity, R.layout.bottom_dialog, new int[]{R.id.save_photoalbum, R.id.dialog_cancel});
        bottomDialog.setOnCenterItemClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mActivity)) {
            refreshRequest();
        } else {
            showToast("无网络");
        }
    }

    private void requestAlertWindowPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    /**
     * 更新资料
     */
    @OnClick(R.id.updateInfo)
    public void clickupdateInfo() {
        CommonUtils.goActivity(mActivity, SUserInfoActivity.class, null, false);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.updatePwd)
    public void clickupdatePwd() {
        CommonUtils.goActivity(mActivity, SDoPasswdActivity.class, null, false);
    }


    @OnClick(R.id.news)
    public void clicknews() {
        CommonUtils.goActivity(mActivity, SNewListActivity.class, null, false);
    }


    @OnClick(R.id.mSales)
    public void onClicksales() {
        Bundle bundle = new Bundle();
        bundle.putString("choosetype", "1");
        CommonUtils.goActivity(mActivity, SChooseAgentActivity.class, bundle, false);
    }

    /**
     * 收藏
     */
    @OnClick(R.id.marks)
    public void onClickMarks() {
        CommonUtils.goActivity(mActivity, SMarksActivity.class, null, false);
    }

    /**
     * 添加地址
     */
    @OnClick(R.id.address)
    public void onClickaddress() {
        if (notes.size() <= 0) {
            CommonUtils.goActivity(mActivity, SAddressRefreshActivity.class, null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isEdit", true);
            CommonUtils.goActivity(mActivity, SAddressActivity.class, bundle, false);
        }

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

    /**
     * 获取地址
     */
    private void refreshRequest() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Uaddress, params, new OkHttpResponseHandler<String>(mActivity) {

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
                notes.clear();
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<Address> jsonHelper = new JsonHelper<Address>(Address.class);
                    notes.addAll(jsonHelper.getDatas(json));
                    //adapter.notifyDataSetChanged();
                } else {
                    showToast(JsonHelper.errorMsg(json));
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                notes.clear();
            }
        });

    }


    @OnClick({R.id.exit_esc, R.id.call_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit_esc:
                PreferenceUtils.remove(mActivity, Contants.Preference.UID);
                PreferenceUtils.remove(mActivity, Contants.Preference.UTYPE);
                PreferenceUtils.remove(mActivity, Contants.Preference.TOKEN);
                PreferenceUtils.setPrefInt(mActivity, Contants.Preference.ISLOGGIN, 0);
                CommonUtils.goActivity(mActivity, LoginActivity.class, null, true);
                break;
            case R.id.call_phone:
                new MaterialDialog.Builder(mActivity).title("提示").positiveText("拨打").negativeText("取消")
                        .content("是否要拨打客服电话?")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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
                            }
                        }).show();
                break;

            default:
                break;
        }
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

    /**
     * 填写推荐人
     */
    @OnClick(R.id.Recommender)
    public void onViewClicked() {
        centerDialog.show();
        ((TextView) centerDialog.findViewById(R.id.dialog_cancel)).setText("我是自己注册的");
        centerDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        centerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        editText = (EditText) centerDialog.findViewById(R.id.ecit_phone);
        tv = (TextView) centerDialog.findViewById(R.id.prompt_tv);
    }


    /**
     * 绑定推荐人
     */
    public void myextend(String phone) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", this.uid);
        params.put("token", this.token);
        params.put("referee", phone);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.MYEXTEND, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    try {
                        JSONObject object = new JSONObject(json);
                        ShowLog.e(object.getString("info"));
                        if (object.getString("status").equals("1")) {
                            centerDialog.dismiss();
                            Recommender.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }
        });

    }

    @Override
    public void OnCenterItemClick(CenterDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                dialog.dismiss();
                break;
            case R.id.dialog_sure:
                if (isChinaPhoneLegal(editText.getText().toString())) {
                    myextend(editText.getText().toString().trim());
                } else {
                    tv.setText("请正确填写手机号码");
                }
                break;
            default:
                break;
        }
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
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
