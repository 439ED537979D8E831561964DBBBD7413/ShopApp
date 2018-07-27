package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.mining.app.zxing.MipcaActivityCapture;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.AllScanCodeDialogFragment;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ubeen.HotIndex;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ubeen.LimitedSale;
import com.yj.shopapp.ubeen.NotMfData;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.ubeen.Userinfo;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SRecyclerAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.GlideCircleTransform;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NoticeDialog;
import com.yj.shopapp.util.NoticeMF;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.BannerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import q.rorbin.badgeview.QBadgeView;


/**
 * Created by jm on 2016/4/25.
 */
public class SHomeActivity extends NewBaseFragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {


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
    String agentuid;
    @BindView(R.id.service_imag)
    ImageView serviceImag;
    @BindView(R.id.newgoodsTv)
    TextView newgoodsTv;
    @BindView(R.id.sales_time)
    TextView salesTime;
    @BindView(R.id.good_img)
    ImageView goodImg;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.shopprice2)
    TextView shopprice2;
    @BindView(R.id.price_tv2)
    TextView priceTv2;
    @BindView(R.id.hotshopimag)
    ImageView hotshopimag;
    @BindView(R.id.hotshopprice)
    TextView hotshopprice;
    @BindView(R.id.hotshopimag2)
    ImageView hotshopimag2;
    @BindView(R.id.hotshopprice2)
    TextView hotshopprice2;
    @BindView(R.id.banner_guide_content)
    BGABanner bannerGuideContent;
    @BindView(R.id.bugood_bg)
    ImageView bugoodBg;
    @BindView(R.id.hotGoods_bg)
    ImageView hotGoodsBg;
    @BindView(R.id.right_item)
    RelativeLayout rightItem;
    Unbinder unbinder;

    private List<BannerInfo> advers = new ArrayList<>();
    private ArrayList<Notice> notices = new ArrayList<Notice>();
    private List<NotMfData> hot_list_1;
    private List<Industry> mdatas = new ArrayList<Industry>();
    private List<Address> notes = new ArrayList<Address>();
    private float notY;
    private String notid;
    private List<Notice> noticeLists = new ArrayList<>();
    private MarqueeFactory mf;
    private int checksum;
    private SRecyclerAdapter adapter;
    public final static int REQUESTCODE_SCAN_WHAT = 2;
    private String imagurl = "";
    String content;
    private JSONObject object = null;
    private static final int CAMERA_OK = 1;
    private QBadgeView qBadgeView1, qBadgeView2;
    private List<LimitedSale> limitedSaleList = new ArrayList<>();
    private List<HotIndex> hotIndexList = new ArrayList<>();
    private List<String> imags = new ArrayList<>();

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }

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
        bundle.putString("type", "home");
        CommonUtils.goActivity(mActivity, MipcaActivityCapture.class, bundle);
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
    @OnClick({R.id.id_right_btu, R.id.salesPromotion_lin, R.id.lowBtn, R.id.search2Btn, R.id.searchBtn, R.id.integral_rl, R.id.search_rl, R.id.reward, R.id.service_imag, R.id.limitBuGood, R.id.goto_sales, R.id.goto_hot})
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
                AllScanCodeDialogFragment.newInstance("").show(getFragmentManager(), "allscancode");
                //FragmentSearchBoxSelect.newInstance(0).show(getFragmentManager(), "selectBox");
                break;
            case R.id.searchBtn:
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA_OK);
                } else {
                    showDialogCheckGoods();
                }
                break;
            case R.id.integral_rl:
                CommonUtils.goActivity(mActivity, SIntegralActivity.class, null);
                break;

            case R.id.reward:
                CommonUtils.goActivity(mActivity, Recommend.class, null, false);
                break;
            case R.id.service_imag:
                //ShowLog.e(object.getInteger("status") + "");
                // getService();
                showToast("您所处区域暂未开通该服务!");

                break;
            case R.id.goto_sales:
                if (limitedSaleList.size() > 0) {
                    CommonUtils.goActivity(mActivity, BuGoodDetails.class, null);
                } else {
                    showToast("暂无活动，敬请期待");
                }
                break;
            case R.id.goto_hot:
                //跳转至热门商品
                if (hotIndexList.size() > 0) {
                    CommonUtils.goActivity(mActivity, HotGoodActivity.class, null);
                } else {
                    showToast("暂无商品，敬请期待");
                }
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
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        checksum = PreferenceUtils.getPrefInt(mActivity, "reward_area", -1);
        imagurl = PreferenceUtils.getPrefString(mActivity, "check_open", "");
        object = JSONObject.parseObject(imagurl);
        mf = new NoticeMF(mActivity);
        hot_mv.setMarqueeFactory(mf);
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(this);
        initAdpter();
        initBanner();
        initRecy();
        initBadgerView();
    }

    /**
     * 初始化小红点
     */
    private void initBadgerView() {
        qBadgeView1 = new QBadgeView(mActivity);
        qBadgeView1.bindTarget(newgoodsTv).setExactMode(true).setGravityOffset(10, 0, true);
        qBadgeView2 = new QBadgeView(mActivity);
        qBadgeView2.bindTarget(idRightBtu).setExactMode(true).setBadgeGravity(Gravity.END | Gravity.TOP);
    }

    /**
     * 初始化Adpter
     */
    private void initAdpter() {
        adapter = new SRecyclerAdapter(mActivity, mdatas);
    }

    /**
     * 初始化Recy
     */
    private void initRecy() {
        if (classiGv != null) {
            classiGv.setLayoutManager(new GridLayoutManager(mActivity, 5));
            classiGv.setNestedScrollingEnabled(false);
            classiGv.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
        }
    }

    /**
     * 初始化Banner
     */
    private void initBanner() {
        bannerGuideContent.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) -> Glide.with(SHomeActivity.this)
                .load(model)
                .apply(new RequestOptions().centerCrop().dontAnimate())
                .into(itemView));
        bannerGuideContent.setDelegate((BGABanner.Delegate<ImageView, String>) (banner, itemView, model, position) -> {
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
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            onRefresh();
        }
        if (checksum == 0) {
            reward.setEnabled(false);
            Glide.with(mActivity).load(R.drawable.reference).apply(new RequestOptions().transform(new GlideCircleTransform())).into(rewardImg);
        }
        hot_mv.setOnItemClickListener((mView, mData, mPosition) -> {
            mView.setOnTouchListener((v, event) -> {
                notY = event.getY();
                return false;
            });
            if (notY < 90) {
                notid = hot_list_1.get(mPosition).getNum();
            } else {
                notid = hot_list_1.get(mPosition).getNum_1();
            }
            Bundle bundle = new Bundle();
            bundle.putString("id", notid);
            bundle.putParcelableArrayList("notice", notices);
            CommonUtils.goActivity(mActivity, SMsgDetailActivity.class, bundle);
        });

    }

    @Override
    public void onRefresh() {
        getTodayItemsNum();
        getAdvinfo();
        //getrewardArea();
        refreshRequest();
        check_extend();
        loadImag();
        // Change_Switch();
        getNotice();
        getindustry();
        getSite();
        getNewsCount();
        getlimitedSaleList();
        getHotIndex();
        openDialogNum();
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewsCount();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mdatas.size() == 0) return;
        if (0 == mdatas.get(position).getResult()) {
            showToast(mdatas.get(position).getName() + "暂未开放，敬请期待！");
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("CId", mdatas.get(position).getId());
            bundle.putString("Name", mdatas.get(position).getName());
            CommonUtils.goActivity(mActivity, SSecondActivity.class, bundle);
        }
    }

    private void loadImag() {
        if (null != object) {
            Glide.with(mActivity).load(object.getString("imgurl")).into(serviceImag);
        }
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

    private void openDialogNum() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.OPENNUM, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                JSONObject object = JSONObject.parseObject(response);
                String yestday = PreferenceUtils.getPrefString(mActivity, "open" + uid, "");
                String today = DateUtils.getNowDate();
                if (!yestday.equals(today)) {
                    PreferenceUtils.setPrefInt(mActivity, "openNum", object.getInteger("num"));
                    PreferenceUtils.setPrefString(mActivity, "open" + uid, today);
                }
                int openNum = PreferenceUtils.getPrefInt(mActivity, "openNum", 1);
                if (openNum > 0 && PreferenceUtils.getPrefBoolean(mActivity, "firstMain", false)) {
                    --openNum;
                    noticeSwitchList();
                    PreferenceUtils.setPrefInt(mActivity, "openNum", openNum);
                    PreferenceUtils.setPrefBoolean(mActivity, "firstMain", false);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void getTodayItemsNum() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.GETTODAYITEMSNUM, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
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
                    JSONObject object = JSONObject.parseObject(response);
                    int num = object.getInteger("num");
                    if (num == 0) {
                        qBadgeView1.hide(true);
                    } else {
                        qBadgeView1.setBadgeNumber(num);
                    }
                }
            }
        });
    }

    public void getHotIndex() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getContext(), Contants.PortS.HOT_INDEX, params, new OkHttpResponseHandler<String>(getContext()) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (response.equals("")) return;
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    hotIndexList = JSONArray.parseArray(response, HotIndex.class);
                    setHotListData(hotIndexList);
                }
            }
        });
    }

    private void setHotListData(List<HotIndex> listData) {
        int size = listData.size();
        if (size == 0) {
            hotGoodsBg.setVisibility(View.VISIBLE);
        } else if (size > 1) {
            Glide.with(mActivity).load(listData.get(0).getImgurl()).into(hotshopimag);
            Glide.with(mActivity).load(listData.get(1).getImgurl()).into(hotshopimag2);
            hotshopprice.setText(String.format("￥%s", listData.get(0).getPrice()));
            hotshopprice.setBackgroundColor(getResources().getColor(R.color.color_f44421));
            hotshopprice2.setText(String.format("￥%s", listData.get(1).getPrice()));
            hotshopprice2.setBackgroundColor(getResources().getColor(R.color.color_f44421));
        } else {
            Glide.with(mActivity).load(listData.get(0).getImgurl()).into(hotshopimag);
            hotshopprice.setText(String.format("￥%s", listData.get(0).getPrice()));
            hotshopprice.setBackgroundColor(getResources().getColor(R.color.color_f44421));
        }
    }

    /**
     * 弹出框
     */
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
//                    String yestday = PreferenceUtils.getPrefString(mActivity, "s" + uid, "");
//                    String today = DateUtils.getNowDate();
                    if (PreferenceUtils.getPrefInt(mActivity, "appVersion", 0) == 0) {
                        //if (!yestday.equals(today)) {
                        NoticeDialog.newInstance(noticeLists).show(getFragmentManager(), "noticeDialog");
                        // PreferenceUtils.setPrefString(mActivity, "s" + uid, today);
                        // }
                    }
                }
            }
        });
    }

    /**
     * 获取限购商品列表
     */
    private void getlimitedSaleList() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getContext(), Contants.PortS.AINDEX, params, new OkHttpResponseHandler<String>(getContext()) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    limitedSaleList = JSONArray.parseArray(json, LimitedSale.class);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                //flashSaleAdpter.setList(limitedSaleList);
                setLimitedsaleData(limitedSaleList);
                if (limitedSaleList.size() > 0) {
                    if (bugoodBg != null) {
                        bugoodBg.setVisibility(View.GONE);
                    }
                    long time = DateUtils.ContrastTime(Long.parseLong(limitedSaleList.get(0).getStart()));
                    //ShowLog.e(time + "");
                    //ShowLog.e(DateUtils.getnowEndTime(1) + "");
                    if (time != -1) {

                        if (time > 60 * 60 * 24 * 1000 * 2) {
                            //时间大于两天
                            salesTime.setText(String.format("%1$s日  %2$s:00", DateUtils.getDay(Long.valueOf(limitedSaleList.get(0).getStart()) * 1000)
                                    , DateUtils.getHour(Long.parseLong(limitedSaleList.get(0).getStart()) * 1000)));
                        } else {
                            //两天内
                            if (time > 60 * 60 * 24 * 1000) {
                                if (time > DateUtils.getnowEndTime(1) - System.currentTimeMillis()) {
                                    salesTime.setText(String.format("%1$s日  %2$s:00", DateUtils.getDay(Long.valueOf(limitedSaleList.get(0).getStart()) * 1000), DateUtils.getHour(Long.parseLong(limitedSaleList.get(0).getStart()) * 1000)));
                                } else {
                                    //大于一天
                                    salesTime.setText(String.format("明天  %s:00", DateUtils.getHour(Long.parseLong(limitedSaleList.get(0).getStart()) * 1000)));
                                }
                            } else {
                                //小于一天
                                if (time > (DateUtils.getnowEndTime(0) - System.currentTimeMillis())) {
                                    salesTime.setText(String.format("明天  %s:00", DateUtils.getHour(Long.parseLong(limitedSaleList.get(0).getStart()) * 1000)));
                                } else {
                                    salesTime.setText(String.format("今天  %s:00", DateUtils.getHour(Long.parseLong(limitedSaleList.get(0).getStart()) * 1000)));
                                }
                            }
                        }

                    } else {
                        salesTime.setText("正在疯抢");
                    }
                } else {
                    if (salesTime != null) {
                        salesTime.setText("");
                    }
                    if (bugoodBg != null) {
                        bugoodBg.setVisibility(View.VISIBLE);
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


    private void setLimitedsaleData(List<LimitedSale> limitedsaleData) {
        int size = limitedsaleData.size();
        if (size == 0) {
            return;
        } else if (size > 1) {

            rightItem.setVisibility(View.VISIBLE);
            Glide.with(mActivity).load(limitedsaleData.get(0).getImgurl()).into(goodImg);
            Glide.with(mActivity).load(limitedsaleData.get(1).getImgurl()).into(image2);
            shopprice.setText(String.format("￥%s", limitedsaleData.get(0).getUnitprice()));
            shopprice2.setText(String.format("￥%s", limitedsaleData.get(1).getUnitprice()));
            priceTv.setText(String.format("%s", limitedsaleData.get(0).getPrice()));
            priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priceTv2.setText(String.format("%s", limitedsaleData.get(1).getPrice()));
            priceTv2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            Glide.with(mActivity).load(limitedsaleData.get(0).getImgurl()).into(goodImg);
            shopprice.setText(String.format("￥%s", limitedsaleData.get(0).getUnitprice()));
            priceTv.setText(String.format("%s", limitedsaleData.get(0).getPrice()));
            priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            rightItem.setVisibility(View.INVISIBLE);
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
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Notice, params, new OkHttpResponseHandler<String>(mActivity) {
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
                if (JsonHelper.isRequstOK(json, mActivity)) {
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
                    mf.setData(hot_list_1);
                    if (hot_mv != null) {
                        hot_mv.startFlipping();
                    }
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
                        PreferenceUtils.remove(mActivity, "addressId");
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

    /**
     * 获取新消息
     */
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

    /**
     * 增值服务
     */
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

    // 获取行业
    private void getindustry() {
        // mdatas.clear();
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
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    mdatas = JSONArray.parseArray(response, Industry.class);
//                    JsonHelper<Industry> jsonHelper = new JsonHelper<Industry>(Industry.class);
//                    mdatas.addAll(jsonHelper.getDatas(response));
                    seveurl();
                    adapter.setList(mdatas);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    public void foundGoods(String itemnoid, final int type, final String checkgoods) {
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("uid", uid);
//        params.put("token", token);
//        //params.put("itemnoid", itemnoid);
////        params.put("id", "");
////        params.put("agentuid", "");
//        params.put("keyword", itemnoid);
//        //ShowLog.e(uid + "|" + token + "itemnoid" + itemnoid + "checkgoods" + checkgoods + "type");
//        //显示ProgressDialog
//
//        HttpHelper.getInstance().post(mActivity, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mActivity) {
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//                ShowLog.e(json);
//                if (JsonHelper.isRequstOK(json, mActivity)) {
////                    JsonHelper<LookItem> jsonHelper = new JsonHelper<LookItem>(LookItem.class);
////                    final LookItem lookItem = jsonHelper.getData(json, null);
//                    if (checkgoods.equals("order")) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                AllScanCodeDialogFragment.newInstance(itemnoid).show(getFragmentManager(), "allscancode");
//                            }
//                        }, 300);
//
////                        if (goodsList.size() > 1) {
////                            //跳转到列表
////                            Bundle bundle = new Bundle();
////                            bundle.putString("keyword", itemnoid);
////                            CommonUtils.goActivityForResult(mActivity, SGoodsActivity.class, bundle, type, false);
////                        } else {
////                            //跳转到详情
////                            Goods g = goodsList.get(0);
////                            Bundle bundle = new Bundle();
////                            bundle.putString("checkGoods", checkgoods);
////                            bundle.putString("goodsId", g.getId());
////                            CommonUtils.goActivityForResult(mActivity, SGoodsDetailActivity.class, bundle, type, false);
////                        }
////                        if (lookItem.getStock().equals("0")) {
////                            DialogUtils dialog = new DialogUtils();
////                            dialog.getMaterialDialog(mActivity, "提示", "当前商品库存为0，是否找同类别商品", new MaterialDialog.SingleButtonCallback() {
////                                @Override
////                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
////                                    Bundle bundle = new Bundle();
////                                    bundle.putString("bigtypeid", lookItem.getBigtypeid());
////                                    CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
////                                }
////                            }, null);
////                            dialog.show();
////                        } else {
////                            Bundle bundle = new Bundle();
////                            bundle.putString("checkGoods", checkgoods);
////                            bundle.putString("goodsId", lookItem.getId());
////                            CommonUtils.goActivityForResult(mActivity, SGoodsDetailActivity.class, bundle, type, false);
////                        }
//                    } else {
////                        Bundle bundle = new Bundle();
////                        bundle.putString("checkGoods", checkgoods);
////                        bundle.putString("goodsId", lookItem.getId());
////                        CommonUtils.goActivityForResult(mActivity, SGoodsDetailActivity.class, bundle, type, false);
//                    }
//                } else {
//                    Toast.makeText(mActivity, "没有搜索到该商品", Toast.LENGTH_LONG).show();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
//                    bundle.putString("type", "home");
//                    CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, REQUESTCODE_SCAN_WHAT, false);
//                }
//
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//            }
//        });
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //ShowLog.e("requestCode" + requestCode + "resultCode" + resultCode);
////        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 1001) {
////            if (data != null) {
////                Bundle bundle = new Bundle();
////                bundle.putString("agentuid", data.getStringExtra("agentuid"));
////                bundle.putString("agentuName", data.getStringExtra("agentuName"));
////                CommonUtils.goActivity(mActivity, SNewGoodsActivity.class, bundle, false);
////            }
////
////        }
////        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 1002) {
////            Bundle bundle = new Bundle();
////            bundle.putString("agentuid", data.getStringExtra("agentuid"));
////            bundle.putString("agentuName", data.getStringExtra("agentuName"));
////            CommonUtils.goActivity(mActivity, SSPitemActivity.class, bundle, false);
////
////        }
//        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 0) {
//            agentuid = data.getExtras().getString("agentuid");
//            Bundle bundle = new Bundle();
//            bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
//            CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, REQUESTCODE_SCAN_WHAT, false);
//        }
//        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT && requestCode == 1) {
//            Bundle bundle = new Bundle();
//            agentuid = data.getExtras().getString("agentuid");
//            new MaterialDialog.Builder(mActivity)
//                    .inputType(InputType.TYPE_CLASS_TEXT |
//                            InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
//                            InputType.TYPE_TEXT_FLAG_CAP_WORDS)
//                    .positiveText("确定")
//                    .negativeText("取消")
//                    .title("请先输入条码")
//                    .input("请输入条码", "", false, new MaterialDialog.InputCallback() {
//                        @Override
//                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//                            //foundGoods(input == null ? "" : input.toString(), 1, null);
//                        }
//                    })
//                    .show();
//        } else if (resultCode == 19 && requestCode == 0) {
//            Bundle bundle = new Bundle();
//            bundle.putString("checkGoods", "order");
//            bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
//            CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, REQUESTCODE_SCAN_WHAT, false);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(ReCode msg) {
//        if (msg.getStatus() == 1) {
//            foundGoods(msg.getCode(), 0, "order");
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

}