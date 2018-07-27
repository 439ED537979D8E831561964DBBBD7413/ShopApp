package com.yj.shopapp.ui.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Province;
import com.yj.shopapp.ubeen.RegisterClassifi;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.ui.activity.shopkeeper.AddressFragment;
import com.yj.shopapp.ui.activity.shopkeeper.SMainTabActivity;
import com.yj.shopapp.ui.activity.wholesale.WMainTabActivity;
import com.yj.shopapp.dialog.CenterDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Login;

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
 * Created by huanghao on 2016/11/22.
 */

public class RegisterActivity extends BaseActivity implements CenterDialog.OnCenterItemClickListener {

//    @BindView(R.id.province_sp)
//    Spinner provinceSp;

    public static boolean isshow = false;
    int role = 1;//角色，1 零售商 2  批发商
    String provinceId = "-1";
    String areaId = "-1";
    Province province;
    List<Province> provinceList = new ArrayList<>();


    List<String> provinceStrList = new ArrayList<>();
    ArrayAdapter<String> provinceAdapter;

    List<Province> areaList1 = new ArrayList<>();
    List<String> areaStrList1 = new ArrayList<>();
    ArrayAdapter<String> areaAdapter1;
    List<Province> areaList2 = new ArrayList<>();
    List<String> areaStrList2 = new ArrayList<>();
    List<RegisterClassifi> registerClassifiList = new ArrayList<>();
    String classfiId = "";
    ArrayAdapter<String> areaAdapter2;
    List<Province> areaList3 = new ArrayList<>();

    List<String> areaStrList3 = new ArrayList<>();
    ArrayAdapter<String> areaAdapter3;
    int requestCode = 999;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.password_txt)
    TextView passwordTxt;
    @BindView(R.id.password_edt)
    EditText passwordEdt;
    @BindView(R.id.phonenumbe_re)
    LinearLayout phonenumbeRe;
    @BindView(R.id.shopkeeper_rb)
    RadioButton shopkeeperRb;
    @BindView(R.id.wholesaler_rb)
    RadioButton wholesalerRb;
    @BindView(R.id.role_rg)
    RadioGroup roleRg;
    @BindView(R.id.classifitcation_tv)
    TextView classifitcationTv;
    @BindView(R.id.choose_classifi_btn)
    Button chooseClassifiBtn;
    @BindView(R.id.classifi_re)
    RelativeLayout classifiRe;
    @BindView(R.id.againpassword_txt)
    TextView againpasswordTxt;
    @BindView(R.id.againpassword_edt)
    EditText againpasswordEdt;
    @BindView(R.id.password_re)
    LinearLayout passwordRe;
    @BindView(R.id.site)
    TextView site;
    @BindView(R.id.area_btn)
    TextView areaBtn;
    @BindView(R.id.area_tv)
    TextView areaTv;
    @BindView(R.id.area_re)
    RelativeLayout areaRe;
    @BindView(R.id.edit_li)
    LinearLayout editLi;
    @BindView(R.id.register_txt)
    TextView registerTxt;
    @BindView(R.id.login_re)
    RelativeLayout loginRe;
    private Login uinfo;
    private String uid;
    private String token;
    private TextView tv;
    private EditText editText;
    //    @BindView(R.id.area_sp1)
//    Spinner areaSp1;
//    @BindView(R.id.area_sp2)
//    Spinner areaSp2;
//    @BindView(R.id.area_sp3)
//    Spinner areaSp3;
    private CenterDialog centerDialog;
    String imagurl = "";

    @OnClick(R.id.choose_classifi_btn)
    public void chooseClassifiOnclick() {
        getRegisterClassfi();
    }

    @OnClick(R.id.area_btn)
    public void areaOnclick() {
//        Bundle bundle = new Bundle();
//        bundle.putString("role", role + "");
//        CommonUtils.goActivityForResult(mContext, AreaActivity.class, bundle, requestCode, false);
        AddressFragment.newInstance(1).setListenter(new AddressFragment.onCitySelectListenter() {
            @Override
            public void value(String Cider, String address) {
                provinceId = Cider;
                String name = address;
                areaTv.setText(name);
            }
        }).show(getFragmentManager(), "add");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registered;
    }

    @Override
    protected void initData() {
        title.setText("注册");
        areaAdapter1 = new ArrayAdapter<String>(mContext, R.layout.my_simple_spinner_item, areaStrList1);
        areaAdapter2 = new ArrayAdapter<String>(mContext, R.layout.my_simple_spinner_item, areaStrList2);
        areaAdapter3 = new ArrayAdapter<String>(mContext, R.layout.my_simple_spinner_item, areaStrList3);
        provinceAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_spinner_item, provinceStrList);

        idRightBtu.setVisibility(View.GONE);
        centerDialog = new CenterDialog(mContext, R.layout.recommenderactivity, new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        centerDialog.setOnCenterItemClickListener(this);
        getProvince();
    }

    /**
     * 验证红包开放
     */
    private void getrewardArea(final String uid, final String token) {
        Map<String, String> params = new HashMap<>();
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
                    JSONObject object = JSONObject.parseObject(json);
                    PreferenceUtils.setPrefInt(mContext, "reward_area", object.getInteger("status"));
                    getService(uid, token, object.getInteger("status"));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void getService(String uid, String token, final int index) {
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
                JSONObject object = null;

                int status = -1;
                if (json != null && !json.startsWith("{errcode")) {
                    object = JSONObject.parseObject(json);
                    status = object.getInteger("status");
                }
                if (status == 1) {
                    imagurl = object.getString("imgurl");
                    PreferenceUtils.setPrefString(mContext, "check_open", imagurl);
                }
                if (index == 1) {
                    showDialog();
                } else {
                    registertohome();
                }
//                Bundle bundle = new Bundle();
//                bundle.putInt("index", index);
//                bundle.putString("imagurl", imagurl);
//                CommonUtils.goActivity(mContext, SMainTabActivity.class, bundle, true);
            }
        });
    }

    private void getProvince() {
        provinceList.clear();
        provinceStrList.clear();
        if (role == 0) {
            //showToastShort("请选择角色");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("putype", role + "");
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getprovince, params, new OkHttpResponseHandler<String>(mContext) {
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
                    JsonHelper<Province> jsonHelper = new JsonHelper<Province>(Province.class);
                    provinceList.addAll(jsonHelper.getDatas(json));
                    for (int i = 0; i < provinceList.size(); i++) {
                        provinceStrList.add(provinceList.get(i).getArea_name());
                    }
                    provinceAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void showChooseify() {

        String[] array = new String[registerClassifiList.size()];
        int i = 0;
        for (RegisterClassifi registerClassifi : registerClassifiList) {
            array[i] = registerClassifi.getName();
            i++;
        }

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
        materialDialog.title("选择行业类别");
        materialDialog.items(array);
        materialDialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                classfiId = registerClassifiList.get(which).getId();
                classifitcationTv.setText(registerClassifiList.get(which).getName());


            }
        });


        materialDialog.positiveText(android.R.string.cancel);
        materialDialog.show();
    }

    private void clearAllData() {
        provinceList.clear();
        provinceStrList.clear();
        provinceAdapter.notifyDataSetChanged();
        areaList1.clear();
        areaStrList1.clear();
        areaAdapter1.notifyDataSetChanged();

        areaList2.clear();
        areaStrList2.clear();
        areaAdapter2.notifyDataSetChanged();

        areaList3.clear();
        areaStrList3.clear();
        areaAdapter3.notifyDataSetChanged();

    }

    public void getArea(final List<Province> areaList, final List<String> areaStrList, final ArrayAdapter arrayAdapter, final Spinner areaSp) {
        areaList.clear();
        areaStrList.clear();
        if (role == 0) {
            //showToastShort("请选择角色");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("putype", role + "");
        params.put("parent_id", areaId);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getchildarea, params, new OkHttpResponseHandler<String>(mContext) {
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
                    JsonHelper<Province> jsonHelper = new JsonHelper<Province>(Province.class);
                    areaList.addAll(jsonHelper.getDatas(json));
                    for (int i = 0; i < areaList.size(); i++) {
                        areaStrList.add(areaList.get(i).getArea_name());
                    }
                    arrayAdapter.notifyDataSetChanged();
                    areaSp.setSelection(0, false);
                    // areaSp1.showContextMenu();
                }
            }
        });
    }

    private void getRegisterClassfi() {

        registerClassifiList.clear();
        HttpHelper.getInstance().get("http://u.19diandian.com/index.php/Appi/getindustrylist", new OkHttpResponseHandler<String>(mContext) {
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
                    JsonHelper<RegisterClassifi> jsonHelper = new JsonHelper<RegisterClassifi>(RegisterClassifi.class);
                    registerClassifiList.addAll(jsonHelper.getDatas(json));
                    showChooseify();
                } else {
                    showToastShort("获取分类失败！");
                }
            }
        });
    }

    private void confrimRegister() {

        Map<String, String> params = new HashMap<>();
        params.put("putype", role + "");
        params.put("mobile", getIntent().getStringExtra("phoneNumber"));
        params.put("areaid", provinceId);
        params.put("password", passwordEdt.getText().toString().trim().replace(" ", ""));
        HttpHelper.getInstance().post(mContext, Contants.PortA.Doreg, params, new OkHttpResponseHandler<String>(mContext) {
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
                    showToastShort("注册成功");
                    //调登陆界面
                    String username = getIntent().getStringExtra("phoneNumber");
                    String userpwd = passwordEdt.getText().toString().trim();
                    ShowLog.e(username + userpwd);
                    login(username, userpwd);
                } else {
                    showToastShort("注册失败！，请检查后重试");

                }
            }
        });
    }

    /**
     * 绑定推荐人
     */
    public void myextend(final String phone) {

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
                    JSONObject object = JSONObject.parseObject(json);
                    showToastShort(object.getString("info"));
                    if (object.getString("status").equals("1")) {
                        hidebg();
                        centerDialog.dismiss();
                        registertohome();
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

    public void showDialog() {
        centerDialog.show();
        showbg();
        ((TextView) centerDialog.findViewById(R.id.dialog_cancel)).setText("跳过");
        centerDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        centerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        editText = (EditText) centerDialog.findViewById(R.id.ecit_phone);
        tv = (TextView) centerDialog.findViewById(R.id.prompt_tv);
    }


    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @OnClick(R.id.login_re)
    public void onClick() {
        if (checkDataIsTrue()) {
            confrimRegister();
        }
    }

    private void login(final String userName, final String userPwd) {
        ShowLog.e("RegisterActivity");
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("password", userPwd);
        params.put("version", verCode + "");
        params.put("app", "安卓");
        HttpHelper.getInstance().post(mContext, Contants.PortA.Login, params, new OkHttpResponseHandler<String>(mContext) {

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
                System.out.println("response" + json);
                PreferenceUtils.setPrefInt(mContext, Contants.Preference.ISLOGGIN, 1);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Login> jsonHelper = new JsonHelper<Login>(Login.class);
                    uinfo = jsonHelper.getData(json, null);
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
                    uid = uinfo.getUid();
                    token = uinfo.getToken();
                    getrewardArea(uinfo.getUid(), uinfo.getToken());

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

    private void registertohome() {
        if (uinfo.getUtype().equals("1")) {
            CommonUtils.goActivity(mContext, SMainTabActivity.class, null, true);
        } else {
            CommonUtils.goActivity(mContext, WMainTabActivity.class, null, true);
        }
    }

    /**
     * 提交数据前的检测
     */
    private boolean checkDataIsTrue() {
        if (StringHelper.isEmpty(passwordEdt.getText().toString())) {
            showToastShort("密码不能为空");
            return false;
        }
        if (!StringHelper.isequal(passwordEdt.getText().toString(), againpasswordEdt.getText().toString())) {
            showToastShort("两次输入的密码必须一致");
            return false;
        }
//        if (role == 0) {
//            showToastShort("请先选择角色和区域");
//            return false;
//        }
//        if (role == 2 && CommonUtils.isEmpty(classfiId)) {
//            showToastShort("请选择行业类别！");
//            return false;
//
//        }
        if (provinceId.equals("-1")) {
            showToastShort("请选择区域");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

        }
    }

    @Override
    public void OnCenterItemClick(CenterDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                hidebg();
                dialog.dismiss();
                registertohome();
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
}
