package com.yj.shopapp.ui.activity.wholesale;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mining.app.zxing.MipcaActivityCapture;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.CenterDialog;
import com.yj.shopapp.dialog.WGoodsSearchV4DialogFragment;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClassPagerAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.KeybordS;
import com.yj.shopapp.util.NoticeDialog;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.BannerInfo;
import com.yj.shopapp.wbeen.ClassList;
import com.yj.shopapp.wbeen.Classify;
import com.yj.shopapp.wbeen.Power;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import q.rorbin.badgeview.QBadgeView;


/**
 * Created by jm on 2016/4/25.
 */
public class WHomeActivity extends NewBaseFragment {

    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.banner_guide_content)
    BGABanner bannerGuideContent;
    @BindView(R.id.hometablayout)
    TabLayout hometablayout;
    @BindView(R.id.classViewPager)
    ViewPager classViewPager;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.mAppBar)
    AppBarLayout mAppBar;


    private List<Notice> noticeLists = new ArrayList<>();
    private List<BannerInfo> advers = new ArrayList<>();
    //private List<Classise> classises = new ArrayList<>();
    private List<ClassList> classLists = new ArrayList<>();
    private List<String> imags = new ArrayList<>();
    private QBadgeView qBadgeView2;
    private ClassPagerAdpter pagerAdpter;
    private CenterDialog dialog;
    private boolean scanType = false;
    private List<Classify> classifies = new ArrayList<>();
    private Power power;
    private String preCode;

    public static WHomeActivity newInstance() {
        Bundle args = new Bundle();
        WHomeActivity fragment = new WHomeActivity();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wtab_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        qBadgeView2 = new QBadgeView(mActivity);
        qBadgeView2.bindTarget(idRightBtu).setExactMode(true).setBadgeGravity(Gravity.END | Gravity.TOP);
        pagerAdpter = new ClassPagerAdpter(getChildFragmentManager());
        classViewPager.setAdapter(pagerAdpter);
        hometablayout.setupWithViewPager(classViewPager);


        swipeRefreshLayout.setOnRefreshListener(this::Refresh);
        bannerGuideContent.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) -> Glide.with(WHomeActivity.this)
                .load(model)
                .apply(new RequestOptions().centerCrop().dontAnimate())
                .into(itemView));
        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });
        //EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            Refresh();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick({R.id.searchBtn, R.id.addGoods, R.id.lowBtn, R.id.search2Btn, R.id.salesPromotion_lin, R.id.search_rl, R.id.id_right_btu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchBtn:
                scanType = false;
                startScavenging();
                break;
            case R.id.addGoods:
                showDialog();
                break;
            case R.id.lowBtn:
                Bundle salesBundle = new Bundle();
                salesBundle.putParcelableArrayList("classlist", (ArrayList<? extends Parcelable>) classifies);
                salesBundle.putInt("sales_status", power.getIs_sales());
                CommonUtils.goActivity(mActivity, WSalesActivity.class, salesBundle);
                break;
            case R.id.search2Btn:
                Bundle newsBundle = new Bundle();
                newsBundle.putParcelableArrayList("classlist", (ArrayList<? extends Parcelable>) classifies);
                CommonUtils.goActivity(mActivity, WNewGoodAcitvity.class, newsBundle);
                break;
            case R.id.salesPromotion_lin:
                Bundle stopBundle = new Bundle();
                stopBundle.putParcelableArrayList("classlist", (ArrayList<? extends Parcelable>) classifies);
                stopBundle.putInt("stop_status", power.getSupplierid());
                CommonUtils.goActivity(mActivity, WStopGoodsActivity.class, stopBundle);
                break;
            case R.id.search_rl:
                //商品搜索
                WGoodsSearchV4DialogFragment.newInstance("0").show(getFragmentManager(), "goodssearch");
                //FragmentSearchBoxSelect.newInstance(3).show(getFragmentManager(), "selectBox");
                break;
            case R.id.id_right_btu:
                //信息
                CommonUtils.goActivity(mActivity, WNewListActivity.class, null);
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        dialog = new CenterDialog(mActivity, R.layout.dialog_barcodeview, new int[]{R.id.dialog_close, R.id.dialog_cancel, R.id.dialog_sure}, 0.8);
        dialog.setOnCenterItemClickListener((Dialog, V) -> {
            switch (V.getId()) {
                case R.id.dialog_close:
                    KeybordS.closeKeybord(dialog.findViewById(R.id.ecit_phone), mActivity);
                    dialog.dismiss();
                    break;
                case R.id.dialog_cancel:
                    scanType = true;
                    KeybordS.closeKeybord(dialog.findViewById(R.id.ecit_phone), mActivity);
                    new Handler().postDelayed(this::startScavenging, 100);
                    dialog.dismiss();
                    break;
                case R.id.dialog_sure:
                    EditText editText = dialog.findViewById(R.id.ecit_phone);

                    if (editText.getText().toString().equals("")) {
                        new Handler().postDelayed(() -> {
                            Bundle b = new Bundle();
                            b.putString("itemnoid", "");
                            b.putInt("type", 0);
                            b.putBoolean("isshow", true);
                            CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, b, false);
                        }, 200);
                    } else {
                        //new Handler().postDelayed(() -> foundGoods("goodsIsExist", editText.getText().toString()), 200);
                        foundGoods("goodsIsExist", editText.getText().toString());
                    }
                    KeybordS.closeKeybord(dialog.findViewById(R.id.ecit_phone), mActivity);
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        });
        dialog.show();
        ((EditText) dialog.findViewById(R.id.ecit_phone)).setInputType(InputType.TYPE_CLASS_NUMBER);
        KeybordS.openKeybord(dialog.findViewById(R.id.ecit_phone), mActivity);
    }

    private void Refresh() {
        //getHotbigtype();
        getAdvinfo();
        noticeSwitchList();
        getClassList();
        getNewsCount();
        operableField();
    }

    private void startScavenging() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            toScan();
        }
    }

    /**
     * 全局权限
     */
    private void operableField() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        //显示ProgressDialog
        HttpHelper.getInstance().post(mActivity, Contants.PortA.OPERABLEFIELD, params, new OkHttpResponseHandler<String>(mActivity) {

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
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    power = JSONObject.parseObject(json, Power.class);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    public void noticeSwitchList() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.NOTICESWITCH_LIST, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<Notice> jsonHelper = new JsonHelper<>(Notice.class);
                    noticeLists = jsonHelper.getDatas(json);

                    String yestday = PreferenceUtils.getPrefString(mActivity, "w" + uid, "");
                    String today = DateUtils.getNowDate();
                    if (!yestday.equals(today)) {
                        NoticeDialog.newInstance(noticeLists).show(getFragmentManager(), "noticeDialog");
                        PreferenceUtils.setPrefString(mActivity, "w" + uid, today);
                    }

                }
            }
        });
    }

    private void getAdvinfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mActivity, Contants.PortA.Advinfo, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                imags.clear();
            }

            @Override
            public void onResponse(Request request, String response) {
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<BannerInfo> adverJsonHelper = new JsonHelper<>(BannerInfo.class);
                    advers = adverJsonHelper.getDatas(response);
                    if (advers != null) {
                        for (int i = 0; i < advers.size(); i++) {
                            imags.add(advers.get(i).getImgurl());
                        }
                        if (bannerGuideContent != null) {
                            bannerGuideContent.setData(imags, new ArrayList<String>());
                        }
                    }

                } else {
                    showToast(JsonHelper.errorMsg(response));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

        });

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ReCode msg) {
//        ShowLog.e(msg.getCode());
//        if (msg.getStatus() == 1) {
//            foundGoods("searchGoods", msg.getCode());
//        } else if (msg.getStatus() == 2) {
//            foundGoods("goodsIsExist", msg.getCode());
//        }
    //  }
//    private void getHotbigtype() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//
//        HttpHelper.getInstance().post(mActivity, Contants.PortA.industryslist, params, new OkHttpResponseHandler<String>(mActivity) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                if (swipeRefreshLayout != null) {
//                    swipeRefreshLayout.setRefreshing(false);
//                }
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                classises.clear();
//            }
//
//            @Override
//            public void onResponse(Request request, String response) {
//                ShowLog.e(response);
//                if (JsonHelper.isRequstOK(response, mActivity)) {
//                    JsonHelper<Classise> jsonHelper = new JsonHelper<Classise>(Classise.class);
//                    classises.addAll(jsonHelper.getDatas(response));
//                } else {
//                    showToastShort(JsonHelper.errorMsg(response));
//                }
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//            }
//        });
//    }

    private void getClassList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mActivity, Contants.PortA.CLASSLIST, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                classifies.clear();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    classLists = JSONArray.parseArray(response, ClassList.class);
                    pagerAdpter.setClassLists(classLists);
                    for (ClassList c : classLists) {
                        classifies.add(new Classify(c.getCid(), c.getName()));
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void getNewsCount() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getContext(), Contants.PortA.Noreadmsg, params, new OkHttpResponseHandler<String>(getContext()) {

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
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    int number = Integer.parseInt(jsonObject.getString("count"));
                    if (number == 0) {
                        qBadgeView2.hide(true);
                    } else {
                        qBadgeView2.setBadgeNumber(number);
                    }
                } else {
                    Toast.makeText(mActivity, Contants.NetStatus.NETLOADERROR, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    public void foundGoods(final String Ation, final String itemnoid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
//        params.put("itemnoid", itemnoid);
        //params.put("id", "");
        params.put("keyword", itemnoid);
        //显示ProgressDialog
        HttpHelper.getInstance().post(mActivity, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    if (Ation.equals("goodsIsExist")) {
                        Toast.makeText(getContext(), "商品已经存在了", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Ation.equals("goodsIsExist")) {
                        new Handler().postDelayed(() -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("itemnoid", itemnoid == null ? "" : itemnoid);
                            bundle.putInt("type", 1);
                            bundle.putBoolean("isshow", true);
                            CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, bundle, false);
                        }, 200);
                    }
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

    private void toScan() {
        Bundle bundle = new Bundle();
        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.W_type);
        ShowLog.e("scanType" + scanType);
        if (scanType) {
            bundle.putString("type", "whomedialog");
        } else {
            bundle.putString("type", "whome");
        }
        CommonUtils.goActivity(mActivity, MipcaActivityCapture.class, bundle);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            toScan();
        } else {
            showToast("您未获取手机权限，请点击重试");
        }
    }

    public void addShop() {
        new MaterialDialog.Builder(mActivity)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("确定")
                .title("请先输入条码")
                .input("如果不填写请按确定", "", true, (dialog, input) -> {
                    if (input == null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("itemnoid", "");
                        bundle.putInt("type", 0);
                        CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, bundle, false);
                    } else {
                        foundGoods("goodsIsExist", input.toString());
                    }
                })
                .negativeText("扫描")
                .onNegative((dialog, which) -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.original_type);
                    CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, 6, false);
                })
                .show();
    }

}