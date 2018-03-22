package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.yj.shopapp.ui.activity.LoginActivity;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BottomDialog;
import com.yj.shopapp.util.CenterDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.CustomPopDialog2;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;

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
public class SMyInfoActivity extends BaseActivity implements CenterDialog.OnCenterItemClickListener, BottomDialog.OnCenterItemClickListener {
    @BindView(R.id.title)
    TextView title;
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
    @BindView(R.id.app_updata)
    RelativeLayout appUpdata;
    @BindView(R.id.Cancellation)
    RelativeLayout Cancellation;
    @BindView(R.id.exit_esc)
    TextView exitEsc;
    @BindView(R.id.Recommender)
    RelativeLayout Recommender;
    @BindView(R.id.VersionHints)
    TextView VersionHints;
    private CenterDialog centerDialog;
    private BottomDialog bottomDialog;
    private TextView tv;
    private EditText editText;
    private static final int REQUEST_CODE = 1;
    private List<Address> notes = new ArrayList<Address>();
    private Bitmap bitmap;
    String cameraPath;
    String pw = "";

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("cameraPath", cameraPath);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_myinfo;
    }

    @Override
    protected void initData() {
        title.setText("个人中心");
        int index = PreferenceUtils.getPrefInt(mContext, Contants.Preference.CHECKNUM, 0);
        if (index == 1) {
            Recommender.setVisibility(View.VISIBLE);
        }
        String account = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_NAME, "");
        accountTv.setText(account);
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestAlertWindowPermission();
            }
        }
        centerDialog = new CenterDialog(mContext, R.layout.recommenderactivity, new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        centerDialog.setOnCenterItemClickListener(this);
        bottomDialog = new BottomDialog(mContext, R.layout.bottom_dialog, new int[]{R.id.save_photoalbum, R.id.dialog_cancel});
        bottomDialog.setOnCenterItemClickListener(this);
        //VersionUpdata.getInstance(false, mContext, getFragmentManager()).setCallback(this).updateVersion();
    }


    private void requestAlertWindowPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRequest();
    }

    @OnClick(R.id.Cancellation)
    public void CancellationOnclick() {
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.getMaterialDialog(mContext, "重要提示", "是否注销账号，一旦注销账号，该账号将不能再使用", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                DialogUtils inputDialogUtils = new DialogUtils();
                inputDialogUtils.getInputMaterialDialog(mContext, "请输入密码", "输入密码", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        pw = input.toString().trim().replace(" ", "");

                        String pasword = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_PWD, "");
                        if (pasword.equals(pw)) {
                            showToastShort("密码正确！");
                            Cancellation();
                        } else {
                            showToastShort("密码错误！");
                        }

                    }
                }, null, null);

                inputDialogUtils.show();
            }
        }, null);

        dialogUtils.show();


    }

    /**
     * 更新资料
     */
    @OnClick(R.id.updateInfo)
    public void clickupdateInfo() {
        CommonUtils.goActivity(mContext, SUserInfoActivity.class, null, false);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.updatePwd)
    public void clickupdatePwd() {
        CommonUtils.goActivity(mContext, SDoPasswdActivity.class, null, false);
    }


    @OnClick(R.id.news)
    public void clicknews() {
        CommonUtils.goActivity(mContext, SNewListActivity.class, null, false);
    }


    @OnClick(R.id.mSales)
    public void onClicksales() {
        Bundle bundle = new Bundle();
        bundle.putString("choosetype", "1");
        CommonUtils.goActivity(mContext, SChooseAgentActivity.class, bundle, false);
    }

    /**
     * 收藏
     */
    @OnClick(R.id.marks)
    public void onClickMarks() {
        CommonUtils.goActivity(mContext, SMarksActivity.class, null, false);
    }

    /**
     * 添加地址
     */
    @OnClick(R.id.address)
    public void onClickaddress() {
        if (notes.size() <= 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("been", new Address());
            CommonUtils.goActivityForResult(mContext, SAddressRefreshActivity.class, bundle, 0, false);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isEdit", true);
            CommonUtils.goActivity(mContext, SAddressActivity.class, bundle, false);
        }

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
    }


    private void saveImg(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "imgbitmap", "");
        ShowLog.e(path);
        //通知图库刷新
        showToastShort("保存成功");
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
    }

    /**********************
     * 操作
     *********************/
    private void Cancellation() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Delaccount, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("注销成功");
                    PreferenceUtils.remove(mContext, Contants.Preference.UID);
                    PreferenceUtils.remove(mContext, Contants.Preference.UTYPE);
                    PreferenceUtils.remove(mContext, Contants.Preference.TOKEN);
                    CommonUtils.goActivity(mContext, LoginActivity.class, null, true);
                }
            }
        });

    }

    private void refreshRequest() {

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
                notes.clear();
                System.out.println("m_tagjson" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Address> jsonHelper = new JsonHelper<Address>(Address.class);
                    notes.addAll(jsonHelper.getDatas(json));
                    //adapter.notifyDataSetChanged();
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                notes.clear();
            }
        });

    }


    @OnClick({R.id.app_updata, R.id.exit_esc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_updata:
                //VersionUpdata.getInstance(true, mContext, getFragmentManager()).updateVersion();
                break;
            case R.id.exit_esc:
                PreferenceUtils.remove(mContext, Contants.Preference.UID);
                PreferenceUtils.remove(mContext, Contants.Preference.UTYPE);
                PreferenceUtils.remove(mContext, Contants.Preference.TOKEN);
                PreferenceUtils.setPrefInt(mContext, Contants.Preference.ISLOGGIN, 0);
                CommonUtils.goActivity(this, LoginActivity.class, null, true);
                break;
            default:
                break;
        }
    }

    /**
     * 填写推荐人
     */
    @OnClick(R.id.Recommender)
    public void onViewClicked() {
        centerDialog.show();
        showbg();
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
        HttpHelper.getInstance().post(mContext, Contants.PortU.MYEXTEND, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    try {
                        JSONObject object = new JSONObject(json);
                        showToastShort(object.getString("info"));
                        ShowLog.e(object.getString("info"));
                        if (object.getString("status").equals("1")) {
                            centerDialog.dismiss();
                            hidebg();
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
                hidebg();
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
                saveImg(bitmap);
                break;
            case R.id.dialog_cancel:

                break;
            default:
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void LatestVersion() {
//        VersionHints.setText("已是最新版");
//        VersionHints.setClickable(false);
//    }
//
//    @Override
//    public void NotTheLatestEdition() {
//        VersionHints.setText("有新版本");
//    }
}
