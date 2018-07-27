package com.yj.shopapp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.WelcomeImags;
import com.yj.shopapp.ui.activity.shopkeeper.ClassifyListActivity;
import com.yj.shopapp.ui.activity.shopkeeper.CommodityDetails;
import com.yj.shopapp.ui.activity.shopkeeper.SGoodsDetailActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SMainTabActivity;
import com.yj.shopapp.ui.activity.wholesale.WMainTabActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.wbeen.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by LK on 2017/11/3.
 */

public class WelcomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.CountdownText)
    TextView CountdownText;
    @BindView(R.id.getintohome)
    TextView getintohome;
    @BindView(R.id.banner_guide_foreground)
    BGABanner bannerGuideForeground;
    @BindView(R.id.Contact_us)
    TextView ContactUs;
    @BindView(R.id.hide_Contact_us)
    LinearLayout hideContactUs;
    @BindView(R.id.hide_intohome)
    LinearLayout hideIntohome;

    private Context mContext;
    boolean isReqing;
    private String uType;
    private String uid;
    private String token;
    private List<WelcomeImags> welcomeImags = new ArrayList<>();
    private List<String> imags = new ArrayList<>();
    private static final int REQUEST_CODE = 1;
    private final int REQUEST_CODEC = 0x1001;
    private int currposition = 0;
    Unbinder unbinder;
    private String username;
    private String userpwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        unbinder = ButterKnife.bind(this);
        mContext = getApplicationContext();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        AppManager.getAppManager().addActivity(WelcomeActivity.this);
        String token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        username = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_NAME, "");
        userpwd = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_PWD, "");
        if (username.isEmpty() || userpwd.isEmpty() || token.isEmpty()) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (NetUtils.isNetworkConnected(mContext)) {
                PreferenceUtils.remove(mContext, Contants.Preference.TOKEN);
                login(username, userpwd);
            } else {
                Toast.makeText(mContext, "没有网络", Toast.LENGTH_SHORT).show();
                PreferenceUtils.remove(mContext, Contants.Preference.TOKEN);
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }
        initBanner();
        updateVersion();
    }

    private void initBanner() {
        bannerGuideForeground.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) -> Glide.with(mContext).load(model).into(itemView));
        bannerGuideForeground.setDelegate((banner, itemView, model, position) -> {
            WelcomeImags imags = welcomeImags.get(position);
            switch (imags.getType()) {
                case "1":
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("goodsId", imags.getItemid());
                    Intent intent1 = new Intent(WelcomeActivity.this, SGoodsDetailActivity.class);
                    intent1.putExtras(bundle1);
                    startActivity(intent1);

                    break;
                case "2":
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("Store_id", imags.getShop_id());
                    Intent intent2 = new Intent(WelcomeActivity.this, ClassifyListActivity.class);
                    intent2.putExtras(bundle2);
                    startActivity(intent2);

                    break;
                case "3":
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("shop_id", imags.getGoods_id());
                    Intent intent3 = new Intent(WelcomeActivity.this, CommodityDetails.class);
                    intent3.putExtras(bundle3);
                    startActivity(intent3);

                    break;
                case "4":
                    Bundle bundle = new Bundle();
                    bundle.putString("url", imags.getUrl());
                    bundle.putString("phone", welcomeImags.get(currposition).getTel());
                    Intent intent4 = new Intent(WelcomeActivity.this, MyWebView2.class);
                    intent4.putExtras(bundle);
                    startActivity(intent4);

                    break;
                default:
                    break;
            }
        });
        bannerGuideForeground.setOnPageChangeListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        goActivity();
    }

    private CountDownTimer timer = new CountDownTimer(4000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (CountdownText != null) {
                CountdownText.setText("跳过 " + (millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            goActivity();

        }
    };

    private void goActivity() {
        if ("1".equals(uType)) {
            Intent intent = new Intent(WelcomeActivity.this, SMainTabActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_home_in, R.anim.anim_home_out);
            finish();
        } else {
            Intent intent = new Intent(WelcomeActivity.this, WMainTabActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_home_in, R.anim.anim_home_out);
            finish();
        }
    }

    private void login(final String userName, final String userPwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("password", userPwd);
        params.put("version", CommonUtils.getVerCode(mContext) + "");
        params.put("app", "安卓");
        HttpHelper.getInstance().post(mContext, Contants.PortA.Login, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if (isReqing == true) {
                    return;
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                isReqing = true;
                PreferenceUtils.setPrefInt(mContext, Contants.Preference.ISLOGGIN, 1);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, getApplicationContext())) {
                    JsonHelper<Login> jsonHelper = new JsonHelper<Login>(Login.class);
                    final Login uinfo = jsonHelper.getData(json, null);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.UID, uinfo.getUid());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.AGENTUID, uinfo.getAgentuid());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.AREAID, uinfo.getAreaid());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.UTYPE, uinfo.getUtype());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.TOKEN, uinfo.getToken());
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.USER_NAME, userName);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.USER_PWD, userPwd);
                    PreferenceUtils.setPrefInt(mContext, "isVip", uinfo.getIs_vip());
                    PreferenceUtils.setPrefString(mContext, "CustomerService", uinfo.getCustomer_service_phone());
                    PreferenceUtils.setPrefString(mContext, "address", uinfo.getAddress());
                    PreferenceUtils.setPrefBoolean(mContext, "firstMain", true);
                    uType = uinfo.getUtype();
                    uid = uinfo.getUid();
                    token = uinfo.getToken();
                    getAndroidAdvmap(uid, token);
                    if (uType.equals("1")) {
                        getrewardArea(uid, token);
                    }
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

            }
        });
    }

    /**
     * 验证红包开放
     */
    private void getrewardArea(final String uid, final String token) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortU.REWARD_AREA, params, new OkHttpResponseHandler<String>(mContext) {
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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    int status = JSONObject.parseObject(json).getInteger("status");
                    PreferenceUtils.setPrefInt(mContext, "reward_area", status);
                    getService(uid, token);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

            }
        });
    }

    private void getService(String uid, String token) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.CHECK_OPEN, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (!json.isEmpty()) {
                    PreferenceUtils.setPrefString(mContext, "check_open", json);
                }
            }
        });
    }

    private void getAndroidAdvmap(String uid, String token) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
//        ShowLog.e("uid" + uid + "token" + token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ANDROID_ADVMAP, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (!json.startsWith("{\"errcode")) {
                    if (!"[]".equals(json) && json.length() != 0) {
                        welcomeImags = JSONArray.parseArray(json, WelcomeImags.class);
                        for (WelcomeImags w : welcomeImags) {
                            imags.add(w.getImgurl());
                        }
                        if (bannerGuideForeground != null) {
                            bannerGuideForeground.setData(imags, new ArrayList<String>());
                        }
                        if (imags.size() == 1) {
                            if (CountdownText != null) {
                                CountdownText.setVisibility(View.VISIBLE);
                            }
                            if (timer != null) {
                                timer.start();
                            }
                        }
                    } else {
                        goActivity();
                    }
                } else {
                    goActivity();
                }

            }


            @Override
            public void onAfter() {
                super.onAfter();
            }
        });
    }

    //检测版本
    public void updateVersion() {
        String version = String.valueOf(CommonUtils.getVerCode(mContext));
        final Map<String, String> params = new HashMap<>();
        params.put("version", version);
        params.put("type", "1");
        HttpHelper.getInstance().post(mContext, Contants.appd, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    PreferenceUtils.setPrefInt(mContext, "updateStatus", status);
                    if (status == 0) {
                        //删除文件
                        String path = PreferenceUtils.getPrefString(mContext, "apkPath", "");
                        if (!"".equals(path)) {
                            CommonUtils.deleteFile(path);
                        }
                    }
                }
            }
        });
    }


    @OnClick({R.id.CountdownText, R.id.getintohome, R.id.Contact_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.CountdownText:
                timer.cancel();
                goActivity();
                break;
            case R.id.getintohome:
                goActivity();
                break;
            case R.id.Contact_us:
                //拨打电话
                if (imags.size() == 0) return;
                if (!welcomeImags.get(currposition).getTel().equals("")) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //判断有没有拨打电话权限
                        if (PermissionChecker.checkSelfPermission(WelcomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            //请求拨打电话权限
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODEC);
                        } else {
                            callPhone();
                        }
                    } else {
                        callPhone();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODEC:
                if (PermissionChecker.checkSelfPermission(WelcomeActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + welcomeImags.get(currposition).getTel()));
            startActivity(intent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WelcomeActivity.this, Manifest.permission.CALL_PHONE)) {
                //已经禁止提示了("您已禁止该权限，需要重新开启");
                Toast.makeText(mContext, "您已禁止该权限，需要重新开启", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        AppManager.getAppManager().removeActivityFromStack(WelcomeActivity.this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currposition = position;
        if (welcomeImags.get(position).getTel().equals("")) {
            hideContactUs.setVisibility(View.GONE);
        } else {
            hideContactUs.setVisibility(View.VISIBLE);
        }
        if (position == imags.size() - 1) {
            hideIntohome.setVisibility(View.VISIBLE);
        } else {
            hideIntohome.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
