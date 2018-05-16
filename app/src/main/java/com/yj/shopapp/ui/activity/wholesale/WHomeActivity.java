package com.yj.shopapp.ui.activity.wholesale;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mining.app.zxing.MipcaActivityCapture;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.CenterDialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.ubeen.ReCode;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClassPagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.ui.activity.shopkeeper.FragmentSearchBoxSelect;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.NoticeDialog;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.wbeen.BannerInfo;
import com.yj.shopapp.wbeen.ClassList;
import com.yj.shopapp.wbeen.Lookitem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
public class WHomeActivity extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.banner_guide_content)
    BGABanner bannerGuideContent;
    @BindView(R.id.hometablayout)
    TabLayout hometablayout;
    @BindView(R.id.classViewPager)
    ViewPager classViewPager;

    private List<Notice> noticeLists = new ArrayList<>();
    private List<BannerInfo> advers = new ArrayList<>();
    //private List<Classise> classises = new ArrayList<>();
    private List<ClassList> classLists = new ArrayList<>();
    private List<String> imags = new ArrayList<>();
    private QBadgeView qBadgeView2;
    private ClassPagerAdpter pagerAdpter;
    private CenterDialog dialog;
    private boolean scanType = false;

    public static WHomeActivity newInstance() {
        Bundle args = new Bundle();
        WHomeActivity fragment = new WHomeActivity();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutID() {
        return R.layout.wtab_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        qBadgeView2 = new QBadgeView(mActivity);
        qBadgeView2.bindTarget(idRightBtu).setExactMode(true).setBadgeGravity(Gravity.END | Gravity.TOP);
        pagerAdpter = new ClassPagerAdpter(getChildFragmentManager());
        classViewPager.setAdapter(pagerAdpter);
        hometablayout.setupWithViewPager(classViewPager);

        bannerGuideContent.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) -> Glide.with(WHomeActivity.this)
                .load(model)
                .apply(new RequestOptions().centerCrop().dontAnimate())
                .into(itemView));
        if (NetUtils.isMobileConnected(mActivity)) {
            Refresh();
        }
        dialog = new CenterDialog(mActivity, R.layout.dialog_barcodeview, new int[]{R.id.dialog_close, R.id.dialog_cancel, R.id.dialog_sure}, 0.8);
        dialog.setOnCenterItemClickListener((Dialog, V) -> {
            switch (V.getId()) {
                case R.id.dialog_close:
                    dialog.dismiss();
                    break;
                case R.id.dialog_cancel:
                    scanType = true;
                    startScavenging();
                    dialog.dismiss();
                    break;
                case R.id.dialog_sure:
                    EditText editText = dialog.findViewById(R.id.ecit_phone);
                    if (editText.getText().toString().equals("")) {
                        Bundle b = new Bundle();
                        b.putString("itemnoid", "");
                        b.putInt("type", 0);
                        CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, b, false);
                    } else {
                        foundGoods("goodsIsExist", editText.getText().toString());
                    }
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        });
    }

    @OnClick({R.id.searchBtn, R.id.addGoods, R.id.lowBtn, R.id.search2Btn, R.id.salesPromotion_lin, R.id.search_rl, R.id.id_right_btu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchBtn:
                startScavenging();
                break;
            case R.id.addGoods:
                dialog.show();
                //addShop();
                break;
            case R.id.lowBtn:
                CommonUtils.goActivity(mActivity, WSalesActivity.class, null, false);
                break;
            case R.id.search2Btn:
                CommonUtils.goActivity(mActivity, WNewGoodAcitvity.class, null);
                break;
            case R.id.salesPromotion_lin:
                CommonUtils.goActivity(mActivity, WStopGoodsActivity.class, null, false);
                break;
            case R.id.search_rl:
                //商品搜索
                FragmentSearchBoxSelect.newInstance(0).show(mActivity.getFragmentManager(), "selectBox");
                break;
            case R.id.id_right_btu:
                //信息
                CommonUtils.goActivity(mActivity, WNewListActivity.class, null, false);
                break;
            default:
                break;
        }
    }

    private void Refresh() {
        //getHotbigtype();
        getAdvinfo();
        noticeSwitchList();
        getClassList();
    }

    private void startScavenging() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            toScan();
        }
    }

    public void noticeSwitchList() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.NOTICESWITCH_LIST, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
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
                        NoticeDialog.newInstance(noticeLists).show(mActivity.getFragmentManager(), "noticeDialog");
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
                    showToastShort(JsonHelper.errorMsg(response));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReCode msg) {
        if (msg.getStatus() == 1) {
            foundGoods("searchGoods", msg.getCode());
        } else {
            foundGoods("goodsIsExist", msg.getCode());
        }
    }
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
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    classLists = JSONArray.parseArray(response, ClassList.class);
                    pagerAdpter.setClassLists(classLists);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
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
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    public void foundGoods(final String Ation, final String itemnoid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemnoid", itemnoid);
        params.put("id", "");

        //显示ProgressDialog

        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);

        HttpHelper.getInstance().post(mActivity, Contants.PortA.LOOKITEM, params, new OkHttpResponseHandler<String>(mActivity) {

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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    if (Ation.equals("searchGoods")) {
                        JsonHelper<Lookitem> jsonHelper = new JsonHelper<Lookitem>(Lookitem.class);
                        Lookitem lookitem = jsonHelper.getData(json, null);

                        Bundle bundle = new Bundle();
                        bundle.putString("itemnoid", "");
                        bundle.putString("id", lookitem.getId());
                        CommonUtils.goActivity(mActivity, WGoodsDetailActivity.class, bundle, false);
                    }
                    if (Ation.equals("goodsIsExist")) {
                        Toast.makeText(getContext(), "商品已经存在了", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (Ation.equals("searchGoods")) {
                        showToastShort("系统查询不到该商品");
                        Bundle bundle = new Bundle();
                        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.W_type);
                        CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, 0, false);
                    } else if (Ation.equals("goodsIsExist")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("itemnoid", itemnoid == null ? "" : itemnoid);
                        bundle.putInt("type", 1);
                        CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, bundle, false);
                    }
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewsCount();
    }

    private void toScan() {
        Bundle bundle = new Bundle();
        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.W_type);
        if (scanType) {

        } else {
            bundle.putString("type", "home");
        }
        CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, 0, false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            toScan();
        } else {
            showToastShort("您未获取手机权限，请点击重试");
        }
    }

    @Override
    public void onRefresh() {
        Refresh();
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