package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.mining.app.zxing.MipcaActivityCapture;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ubeen.IntegralInfo;
import com.yj.shopapp.ubeen.LookItem;
import com.yj.shopapp.ubeen.NotMfData;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.ubeen.ReCode;
import com.yj.shopapp.ubeen.Userinfo;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SRecyclerAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.GlideCircleTransform;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.NoticeDialog;
import com.yj.shopapp.util.NoticeMF;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.EasyBanner.GlideImageLoader;
import com.yj.shopapp.view.MyBanner;
import com.yj.shopapp.wbeen.BannerInfo;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;


/**
 * Created by jm on 2016/4/25.
 */
public class SHomeActivity extends NewBaseFragment implements NoticeDialog.OnCenterItemClickListener {


    @BindView(R.id.classi_gv)
    RecyclerView classiGv;
    @BindView(R.id.search2Btn)
    RelativeLayout search2Btn;
    @BindView(R.id.reward)
    LinearLayout reward;
    @BindView(R.id.reward_img)
    ImageView rewardImg;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.hot_mv)
    MarqueeView hot_mv;
    @BindView(R.id.bannerView)
    MyBanner bannerView;
    String agentuid;
    @BindView(R.id.service_imag)
    ImageView serviceImag;
    @BindView(R.id.newgoodsTv)
    TextView newgoodsTv;

    private List<BannerInfo> advers = new ArrayList<>();
    private ArrayList<Notice> notices = new ArrayList<Notice>();
    private List<NotMfData> hot_list_1;
    private List<Industry> mdatas = new ArrayList<Industry>();
    private List<Address> notes = new ArrayList<Address>();
    private Handler handler = new Handler();
    private float notY;
    private String notid;
    private List<Notice> noticeLists = new ArrayList<>();
    private NoticeDialog notDialog;
    private InputMethodManager imm;
    private MarqueeFactory mf;
    private int checksum;
    private SRecyclerAdapter adapter;
    private TextView dtitle, dtitle_tv, dtime_tv, dcontent_tv;
    private WebView dwebView;
    int notindex = 0;
    public final static int REQUESTCODE_SCAN_WHAT = 2;
    private String imagurl = "";
    private int CashingSwitch;
    String content;
    private JSONObject object = null;
    private static final int CAMERA_OK = 1;
    private QBadgeView qBadgeView1, qBadgeView2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    showDialogCheckGoods();
                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    showToast("请手动打开相机权限");
                }
                break;
        }
    }

    private void showDialogCheckGoods() {
        Bundle bundle = new Bundle();
        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
        CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, 001, false);
    }

    /**
     * 1:我的消息
     * 2:促销商品
     * 3:特价商品
     * 4:新品上市
     * 5:商品搜索
     * 6：查找商品
     * 7：积分管理
     * 8：推荐奖励
     * 9:增值服务
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.id_right_btu, R.id.salesPromotion_lin, R.id.lowBtn, R.id.search2Btn, R.id.searchBtn, R.id.integral_rl, R.id.search_rl, R.id.reward, R.id.service_imag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_right_btu:
                CommonUtils.goActivity(mActivity, SNewListActivity.class, null, false);
                break;
            case R.id.salesPromotion_lin:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("industrylist", (ArrayList<? extends Parcelable>) mdatas);
                CommonUtils.goActivity(mActivity, SSPitemActivity.class, bundle, false);
                break;
            case R.id.lowBtn:
                CommonUtils.goActivity(mActivity, SLowGoodsActivity.class, null);
                break;
            case R.id.search2Btn:
                Bundle bundles = new Bundle();
                bundles.putParcelableArrayList("industrylist", (ArrayList<? extends Parcelable>) mdatas);
                CommonUtils.goActivity(mActivity, SNewGoodsActivity.class, bundles, false);
                break;
            case R.id.search_rl:
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.getInputMaterialDialog(mActivity, "输入商品名称", "输入商品名称或条码", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle = new Bundle();
                                bundle.putString("name", "商品详情");
                                bundle.putString("keyword", input.toString());
                                CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
                            }
                        }, 200 * 1);
                    }
                }, null, null);
                dialogUtils.show();
                break;
            case R.id.searchBtn:
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA_OK);
                } else {
                    showDialogCheckGoods();
                }
                break;
            case R.id.integral_rl:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("status", CashingSwitch);
                CommonUtils.goActivity(mActivity, SIntegralActivity.class, bundle2);
                break;

            case R.id.reward:
                CommonUtils.goActivity(mActivity, Recommend.class, null, false);
                break;
            case R.id.service_imag:
                //ShowLog.e(object.getInteger("status") + "");
                getService();
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.stab_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        checksum = PreferenceUtils.getPrefInt(mActivity, "reward_area", -1);
        imagurl = PreferenceUtils.getPrefString(mActivity, "check_open", "");
        //ShowLog.e(imagurl);
        object = JSONObject.parseObject(imagurl);
        mf = new NoticeMF(mActivity);
        //0未开放，1开放
        if (checksum == 0) {
            reward.setEnabled(false);
            Glide.with(mActivity).load(R.drawable.reference).apply(new RequestOptions().transform(new GlideCircleTransform())).into(rewardImg);
        }
        mf.setOnItemClickListener(new MarqueeFactory.OnItemClickListener() {
            @Override
            public void onItemClickListener(MarqueeFactory.ViewHolder holder) {
                holder.mView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        notY = event.getY();
                        return false;
                    }
                });
                if (notY < 90) {
                    notid = hot_list_1.get(holder.position).getNum();
                } else {
                    notid = hot_list_1.get(holder.position).getNum_1();
                }
                Bundle bundle = new Bundle();
                bundle.putString("id", notid);
                bundle.putParcelableArrayList("notice", notices);
                CommonUtils.goActivity(mActivity, SMsgDetailActivity.class, bundle);
            }
        });
        notDialog = new NoticeDialog(mActivity, R.layout.dailog_hot, new int[]{R.id.dialog_next, R.id.dialog_sure, R.id.dialog_up});
        notDialog.setOnCenterItemClickListener(this);
        bannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (advers.get(position).getClassify() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goodsId", advers.get(position).getItemid());
                    CommonUtils.goActivity(mActivity, SGoodsDetailActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", advers.get(position).getUrl());
                    bundle.putString("title", advers.get(position).getTitle());
                    CommonUtils.goActivity(mActivity, SAdActivity.class, bundle);
                }
            }
        });
        if (bannerView != null) {
            bannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            bannerView.setDelayTime(3500);
            bannerView.setBannerAnimation(Transformer.Accordion);
            bannerView.setImageLoader(new GlideImageLoader());
            bannerView.setRefreshLayout(swipeRefreshLayout);
        }
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);
        adapter = new SRecyclerAdapter(mActivity, mdatas);
        if (classiGv != null) {
            classiGv.setLayoutManager(new GridLayoutManager(mActivity, 5));
            classiGv.setNestedScrollingEnabled(false);
            classiGv.setAdapter(adapter);
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (0 == mdatas.get(position).getResult()) {
                        showToast(mdatas.get(position).getName() + "暂未开放，敬请期待！");
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("CId", mdatas.get(position).getId());
                        bundle.putString("Name", mdatas.get(position).getName());
                        CommonUtils.goActivity(mActivity, SSecondActivity.class, bundle);
                    }
                }
            });
        }
        //getLogin_Number();
        qBadgeView1 = new QBadgeView(mActivity);
        qBadgeView1.bindTarget(newgoodsTv).setExactMode(true).setGravityOffset(10, 0, true).setBadgeNumber(10);
        qBadgeView2 = new QBadgeView(mActivity);
        qBadgeView2.bindTarget(idRightBtu).setExactMode(true).setBadgeGravity(Gravity.END | Gravity.TOP);
    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        Refresh();

                    }
                }, 200);
            }
        } else {
            showToast("网络不给力");
        }
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Refresh();

        }
    };


    public void Refresh() {
        getAdvinfo();
        //getrewardArea();
        refreshRequest();
        noticeSwitchList();
        check_extend();
        loadImag();
        getNotice();
        Change_Switch();
        getindustry();
        getSite();
        getNewsCount();
    }

    private void loadImag() {
        Glide.with(mActivity).load(object.getString("imgurl")).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)).into(serviceImag);
    }

    /**
     * 7、验证是否可以填写推荐人
     */
    private void check_extend() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CHECK_EXTEND, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JSONObject object = JSONObject.parseObject(json);
                    PreferenceUtils.setPrefInt(mActivity, Contants.Preference.CHECKNUM, object.getInteger("status"));

                }
            }
        });
    }

    private void Change_Switch() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CHANGE_SWITCH, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                JSONObject object = JSONObject.parseObject(json);
                CashingSwitch = object.getInteger("status");
                if (CashingSwitch == 1) {
                    getIntegral();
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
        HttpHelper.getInstance().post(getContext(), Contants.PortU.NOTICESWITCH_LIST, params, new OkHttpResponseHandler<String>(getContext()) {
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
                    String yestday = PreferenceUtils.getPrefString(mActivity, "s" + uid, "");
                    String today = DateUtils.getNowDate();
                    if (!yestday.equals(today)) {
                        notDialog.show();
                        if (!"1".equals(noticeLists.get(notindex).getClassify())) {
                            notDialog.hide();
                        }
                        dtitle = (TextView) notDialog.findViewById(R.id.notice_tiele);
                        dtitle_tv = (TextView) notDialog.findViewById(R.id.hot_title);
                        dtime_tv = (TextView) notDialog.findViewById(R.id.hot_time);
                        dcontent_tv = (TextView) notDialog.findViewById(R.id.hot_context);
                        dwebView = (WebView) notDialog.findViewById(R.id.webView);
                        dwebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                notDialog.show();

                            }

                        });
                        setDialogText();
                        PreferenceUtils.setPrefString(mActivity, "s" + uid, today);
                    }


                }
            }
        });
    }

    public void setDialogText() {
        //showbg();
        Notice notice = noticeLists.get(notindex);
        if ("1".equals(notice.getClassify())) {
            dtitle.setText(notice.getType());
            dtitle_tv.setText(notice.getTitle());
            dtime_tv.setText(DateUtils.getDateToLong(notice.getAddtime()));
            dcontent_tv.setText(notice.getContent());
            dtitle_tv.setVisibility(View.VISIBLE);
            dcontent_tv.setVisibility(View.VISIBLE);
            dtime_tv.setVisibility(View.VISIBLE);
            dwebView.setVisibility(View.GONE);
        } else {
            notDialog.hide();
            dtitle_tv.setVisibility(View.GONE);
            dcontent_tv.setVisibility(View.GONE);
            dtime_tv.setVisibility(View.GONE);
            dwebView.setVisibility(View.VISIBLE);
            dwebView.loadUrl(notice.getUrl());

        }
    }


    /**
     * 通知
     */
    private void getNotice() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("vercode", String.valueOf(CommonUtils.getVerCode(mActivity)));
        HttpHelper.getInstance().post(getContext(), Contants.PortU.Notice, params, new OkHttpResponseHandler<String>(getContext()) {
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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, getContext())) {
                    JsonHelper<Notice> jsonHelper = new JsonHelper<>(Notice.class);
                    notices = (ArrayList<Notice>) jsonHelper.getDatas(json);
                    int size = notices.size();
                    hot_list_1 = new ArrayList<NotMfData>();
                    for (int i = 0; i < notices.size(); i++) {
                        if (i % 2 == 0) {
                            content = notices.get(i).getTitle();
                        } else {
                            hot_list_1.add(new NotMfData(i + "", content, "" + (i + 1), notices.get(i).getTitle()));
                        }
                        if (size - 1 == i && size % 2 != 0) {
                            hot_list_1.add(new NotMfData((i + 1) + "", content, "-1", " "));
                        }
                    }
                    mf.resetData(hot_list_1);
                    hot_mv.setMarqueeFactory(mf);
                    hot_mv.startFlipping();
                    //getNoticeContent();
                }

            }
        });
    }

    /**
     * 获取登录数量
     */
    private void getLogin_Number() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Login_Number, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    PreferenceUtils.setPrefInt(mActivity, "logintnum", JSONObject.parseObject(json).getInteger("num"));
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });
    }

    /**
     * 获取地址
     */
    private void getSite() {

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
                ShowLog.e(json);
                if (json.startsWith("{errcode")) {
                    JSONObject object = JSONObject.parseObject(json);
                    String num = object.getString("errcode");
                    if (num.equals("06")) {
                        if (PreferenceUtils.getPrefInt(mActivity, Contants.Preference.ISLOGGIN, 0) == 1) {
                            getLogin_Number();
                            PreferenceUtils.setPrefInt(mActivity, Contants.Preference.ISLOGGIN, 0);
                        }
                    }
                }
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    notes = JSONArray.parseArray(json, Address.class);
                    PreferenceUtils.setPrefString(mActivity, "addressId", notes.get(0).getId());
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    public void getNewsCount() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getContext(), Contants.PortU.Noreadmsg, params, new OkHttpResponseHandler<String>(getContext()) {

            @Override
            public void onAfter() {
                super.onAfter();
                //progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
//                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
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

    private void getService() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CHECK_OPEN, params, new OkHttpResponseHandler<String>(mActivity) {
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
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JSONObject SObject = JSONObject.parseObject(json);
                    if (SObject.getInteger("status") == 1) {
                        CommonUtils.goActivity(mActivity, AdditionalservicesActivity.class, null);
                    } else {
                        showToast(object.getString("info"));
                    }
                }

            }
        });
    }

    private long curr_integral = 0;
    private int sum;//总积分
    private int number;//显示还需要多少积分
    private int money;//提示的金额
    private long limt_integral = 0;

    private void getIntegral() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getActivity(), Contants.PortU.UserIntegral, params, new OkHttpResponseHandler<String>(getActivity()) {
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
                System.out.print("   request   ===>>  " + json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<IntegralInfo> jsonHelper = new JsonHelper(IntegralInfo.class);
                    IntegralInfo integralInfo = jsonHelper.getData(json, null);
                    curr_integral = integralInfo.getIntegral();
                    limt_integral = integralInfo.getMin_limit();
                    sum = countintegral((int) curr_integral);
                    String str = integralInfo.getRatio();
                    if (str.matches("^[0-9]*(\\.?)[0-9]*")) {
                        float ratio = Float.parseFloat(integralInfo.getRatio());
                        number = (int) (sum - curr_integral);
                        money = (int) ((curr_integral / limt_integral) * limt_integral * ratio);
                    }
                    if (limt_integral <= curr_integral) {
                        showintegralDialog();
                    }
                }
            }
        });
    }

    private void showintegralDialog() {
        String yestday = PreferenceUtils.getPrefString(mActivity, uid, "");
        String today = DateUtils.getNowDate();
        if (!today.equals(yestday)) {
            AlertDialog.Builder integralDialog = new AlertDialog.Builder(mActivity);
            integralDialog.setIcon(R.drawable.integral);
            integralDialog.setTitle("积分兑换");
            ShowLog.e(money + "");
            integralDialog.setMessage("您现在有 " + curr_integral + "积分可以提现" + money + " 元现金，继续努力哦！");
            integralDialog.setPositiveButton("立即兑换", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("swtich", 1);
                    CommonUtils.goActivity(mActivity, SIntegralActivity.class, new Bundle());
                }
            });
            integralDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            integralDialog.show();
            //showPopupWindow(money);
            PreferenceUtils.setPrefString(mActivity, uid, today);
        }

    }

    private int countintegral(int num) {
        int index = 1;
        while (num > (index * limt_integral)) {
            index++;
        }
        return (int) (index * limt_integral);
    }

    private void getAdvinfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("agentuid", "");
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Advinfo, params, new OkHttpResponseHandler<String>(mActivity) {

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
                System.out.println("response" + response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<BannerInfo> adverJsonHelper = new JsonHelper<>(BannerInfo.class);
                    advers = adverJsonHelper.getDatas(response);
                    if (advers != null) {
                        List<String> imgs = new ArrayList<String>();
                        for (int i = 0; i < advers.size(); i++) {
                            imgs.add(advers.get(i).getImgurl());
                        }
                        if (bannerView != null) {
                            bannerView.setImages(imgs);
                            bannerView.start();
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

    @Override
    public void onStart() {
        super.onStart();
        bannerView.startAutoPlay();

    }

    @Override
    public void onStop() {
        super.onStop();
        bannerView.stopAutoPlay();
    }


    // 获取行业
    private void getindustry() {
        mdatas.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        //Log.e("m_tag", uid + token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Classifylist, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onResponse(Request request, String response) {
                System.out.println("response" + response);
                Log.e("m_tag", response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<Industry> jsonHelper = new JsonHelper<Industry>(Industry.class);
                    mdatas.addAll(jsonHelper.getDatas(response));
                    seveurl();
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(JsonHelper.errorMsg(response));
                }

            }

        });
    }

    /**
     * 保存图片
     */
    private void seveurl() {
        Map<String, String> urlmap = new HashMap<>();
        for (Industry i : mdatas) {
            urlmap.put(i.getId(), i.getUrl());
        }
        PreferenceUtils.Map2Json(mActivity, "imagurl", urlmap);
    }

    private void refreshRequest() {

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mActivity, Contants.PortU.GETUSERINFO, params, new OkHttpResponseHandler<String>(mActivity) {

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
                System.out.println("response" + response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<Userinfo> jsonHelper = new JsonHelper<Userinfo>(Userinfo.class);
                    final Userinfo userinfo = jsonHelper.getData(response, null);

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

    public void foundGoods(String itemnoid, final int type, final String checkgoods) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemnoid", itemnoid);
        params.put("id", "");
        params.put("agentuid", "");
        ShowLog.e(uid + "|" + token + "itemnoid" + itemnoid + "checkgoods" + checkgoods + "type");
        //显示ProgressDialog

        HttpHelper.getInstance().post(mActivity, Contants.PortU.LookItem, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                // progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                //progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<LookItem> jsonHelper = new JsonHelper<LookItem>(LookItem.class);
                    final LookItem lookItem = jsonHelper.getData(json, null);
                    if (checkgoods.equals("order")) {
                        if (lookItem.getStock() == "0") {
                            DialogUtils dialog = new DialogUtils();
                            dialog.getMaterialDialog(mActivity, "提示", "当前商品库存为0，是否找同类别商品", new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("bigtypeid", lookItem.getBigtypeid());
                                    CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
                                }
                            }, null);
                            dialog.show();
                        } else {
                            Bundle bundle = new Bundle();
                            if (checkgoods != null) {
                                bundle.putString("checkGoods", checkgoods);
                            }
                            bundle.putString("goodsId", lookItem.getId());
                            CommonUtils.goActivityForResult(mActivity, SGoodsDetailActivity.class, bundle, type, false);
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        if (checkgoods != null) {

                            bundle.putString("checkGoods", checkgoods);
                        }

                        bundle.putString("goodsId", lookItem.getId());
                        CommonUtils.goActivityForResult(mActivity, SGoodsDetailActivity.class, bundle, type, false);
                    }
                } else {
                    Toast.makeText(mActivity, "没有搜索到该商品", Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
                    CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, REQUESTCODE_SCAN_WHAT, false);
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShowLog.e("requestCode" + requestCode + "resultCode" + resultCode);
        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 1001) {
            if (data != null) {
                Bundle bundle = new Bundle();
                bundle.putString("agentuid", data.getStringExtra("agentuid"));
                bundle.putString("agentuName", data.getStringExtra("agentuName"));
                CommonUtils.goActivity(mActivity, SNewGoodsActivity.class, bundle, false);
            }

        }
        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 1002) {
            Bundle bundle = new Bundle();
            bundle.putString("agentuid", data.getStringExtra("agentuid"));
            bundle.putString("agentuName", data.getStringExtra("agentuName"));
            CommonUtils.goActivity(mActivity, SSPitemActivity.class, bundle, false);

        }
        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 0) {
            agentuid = data.getExtras().getString("agentuid");
            Bundle bundle = new Bundle();
            bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
            CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, REQUESTCODE_SCAN_WHAT, false);
        }
        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 1) {
            Bundle bundle = new Bundle();
            agentuid = data.getExtras().getString("agentuid");
            new MaterialDialog.Builder(mActivity)
                    .inputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                            InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .positiveText("确定")
                    .negativeText("取消")
                    .title("请先输入条码")
                    .input("请输入条码", "", false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            foundGoods(input == null ? "" : input.toString(), 1, null);
                        }
                    })
                    .show();
        } else if (resultCode == 19 && requestCode == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("checkGoods", "order");
            bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
            CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, REQUESTCODE_SCAN_WHAT, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReCode msg) {
        foundGoods(msg.getCode(), 0, "order");
    }

    @Override
    public void onDestroy() {
        if (notDialog != null) {
            notDialog.dismiss();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void OnCenterItemClick(NoticeDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_sure:
                //hidebg();
                dialog.dismiss();
                break;
            case R.id.dialog_next:
                if (notindex == noticeLists.size() - 1) {
                    showToast("没有更多内容了");
                } else if (notindex < noticeLists.size() - 1) {
                    notindex++;
                    setDialogText();
                }
                break;
            case R.id.dialog_up:
                if (notindex == 0) {
                    showToast("没有更多内容了");
                } else if (notindex >= 0) {
                    notindex--;
                    setDialogText();
                }
                break;

            default:
                break;
        }
    }

}