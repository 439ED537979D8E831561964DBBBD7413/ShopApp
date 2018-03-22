package com.yj.shopapp.ui.activity.wholesale;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mining.app.zxing.MipcaActivityCapture;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Classise;
import com.yj.shopapp.ubeen.NotMfData;
import com.yj.shopapp.ubeen.Notice;
import com.yj.shopapp.ubeen.NoticeHint;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SClassificationAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.ui.activity.shopkeeper.SMsgDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.NoticeDialog;
import com.yj.shopapp.util.NoticeMF;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.EasyBanner.GlideImageLoader;
import com.yj.shopapp.view.MyBanner;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.BannerInfo;
import com.yj.shopapp.wbeen.Index;
import com.yj.shopapp.wbeen.Lookitem;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by jm on 2016/4/25.
 */
public class WHomeActivity extends BaseFragment implements GoodsRecyclerView, ViewPager.OnPageChangeListener, NoticeDialog.OnCenterItemClickListener {

    @BindView(R.id.classi_gv)
    RecyclerView classiGv;
    @BindView(R.id.banner_tx)
    TextView bannerTx;
    @BindView(R.id.hot_img)
    ImageView hotImg;
    private NoticeMF mf;
    private InputMethodManager imm;

    public static WHomeActivity newInstance(WMainTabActivity wHomeActivity) {
        WHomeActivity newFragment = new WHomeActivity();
        if (wHomeActivity != null) {
            newFragment.setwMainTabActivity(wHomeActivity);
        }
        return newFragment;
    }

    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.wname_tx)
    TextView wnameTx;
    @BindView(R.id.finish_tv)
    TextView finishTv;
    @BindView(R.id.message_tx)
    TextView messageTx;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.myinfoLy)
    LinearLayout myinfoLy;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.newcount_tv)
    TextView newcount_tv;
    @BindView(R.id.bannerView)
    MyBanner bannerView;
    @BindView(R.id.hot_mv)
    MarqueeView hot_mv;
    int notindex;
    private float notY;
    private String notid;
    SClassificationAdapter classiAdapter;
    private RecyclerViewHeaderFooterAdapter adapter;
    NoticeDialog notDialog;
    List<Notice> noticeLists = new ArrayList<>();
    Handler handler = new Handler();
    private List<BannerInfo> advers = new ArrayList<>();
    List<Classise> classises = new ArrayList<>();
    List<NotMfData> hot_list_1;
    List<BannerInfo> bannerList = new ArrayList<>();
    private ArrayList<Notice> notices = new ArrayList<>();
    List<String> hot_list;
    List<String> paths = new ArrayList<>();
    WMainTabActivity wMainTabActivity;
    private TextView dtitle, dtitle_tv, dtime_tv, dcontent_tv;
    private WebView dwebView;
    String content;

    public void setwMainTabActivity(WMainTabActivity wMainTabActivity) {
        this.wMainTabActivity = wMainTabActivity;
    }

    @OnClick(R.id.myinfoLy)
    public void myInfo() {
        CommonUtils.goActivity(mActivity, WMyInfoActivity.class, null, false);
    }

    @OnClick(R.id.search_rl)
    public void onViewClicked() {
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.getInputMaterialDialog(mActivity, "输入商品名称", "输入商品名称或条码", new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString("keyword", input.toString());
                        CommonUtils.goActivity(mActivity, WGoodsActivity.class, bundle);
                    }
                }, 200 * 1);
            }
        }, null, null);
        dialogUtils.show();
    }

    public void addShop() {
        new MaterialDialog.Builder(mActivity)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("确定")
                .title("请先输入条码")
                .input("如果不填写请按确定", "", true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        if (input == null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("itemnoid", input == null ? "" : input.toString());
                            bundle.putInt("type", 0);
                            CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, bundle, false);
                        } else {
                            foundGoods("goodsIsExist", input.toString());
                        }
                    }
                })
                .negativeText("扫描")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.original_type);
                        CommonUtils.goActivityForResult(mActivity, MipcaActivityCapture.class, bundle, 6, false);
                    }
                })
                .show();
    }

    @OnClick(R.id.id_right_btu)
    public void openMsgs() {
        CommonUtils.goActivity(mActivity, WNewListActivity.class, null, false);
    }

    @OnClick(R.id.more_txt)
    public void moreOnclick() {
        wMainTabActivity.setOnCheckChange(2);
    }


    @Override
    public int getLayoutID() {
        return R.layout.wtab_home;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mf = new NoticeMF(mActivity);
        mf.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<RelativeLayout, NotMfData>() {
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

        bannerView.setOnPageChangeListener(this);
        bannerView.setRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

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
            showToastShort("网络不给力");
        }
        classiAdapter = new SClassificationAdapter(mActivity, classises, this);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 4);
        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, classiAdapter);
        if (classiGv != null) {
            classiGv.setLayoutManager(layoutManager);
            classiGv.setNestedScrollingEnabled(false);
            classiGv.setAdapter(adapter);
        }
        getNotice();
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Refresh();
        }
    };


    private void Refresh() {
        refreshRequest();
        getHotbigtype();
        getAdvinfo();
        noticeSwitchList();
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
                                showbg();
                            }
                        });
                        setDialogText();
                        PreferenceUtils.setPrefString(mActivity, "w" + uid, today);
                    }

                }
            }
        });
    }

    public void setDialogText() {
        showbg();
        Notice notice = noticeLists.get(notindex);
        if ("1".equals(notice.getClassify())) {
            // setManager(0);
            dtitle.setText(notice.getType());
            dtitle_tv.setText(notice.getTitle());
            dtime_tv.setText(DateUtils.getDateToLong(notice.getAddtime()));
            dcontent_tv.setText("\u3000\u3000" + notice.getContent());
            dtitle_tv.setVisibility(View.VISIBLE);
            dcontent_tv.setVisibility(View.VISIBLE);
            dtime_tv.setVisibility(View.VISIBLE);
            dwebView.setVisibility(View.GONE);
        } else {
            //setManager(1);
            dtitle_tv.setVisibility(View.GONE);
            dcontent_tv.setVisibility(View.GONE);
            dtime_tv.setVisibility(View.GONE);
            dwebView.setVisibility(View.VISIBLE);
            dwebView.loadUrl(notice.getUrl());

        }
    }

    /**
     * 获取通知
     */
    private void getNotice() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getContext(), Contants.PortA.Notice, params, new OkHttpResponseHandler<String>(getContext()) {
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
                    hot_list_1 = new ArrayList<>();
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
                    if (mf != null) {
                        mf.resetData(hot_list_1);
                        hot_mv.setMarqueeFactory(mf);
                        hot_mv.startFlipping();
                        //getNoticeContentw();
                    }
                }

            }
        });
    }

    private void getNoticeContentw() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.NoticeSwitch, params, new OkHttpResponseHandler<String>(mActivity) {
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
                System.out.print("request" + json);
                Log.d("m_tag", json);
                if (json != null && !json.startsWith("errcode")) {
                    try {
                        JSONObject object = new JSONObject(json);
                        String id = object.optString("id");
                        String title = object.optString("title");
                        String addtime = object.optString("addtime");
                        String type = object.optString("type");
                        String content = object.optString("content");
                        NoticeHint hint = new NoticeHint(id, title, addtime, type, content);
                        //showHotDialog(hint);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
            }

            @Override
            public void onResponse(Request request, String response) {
                System.out.println("response" + response);
                //{"title":"bsnsns","imgurl":"http:\/\/u.19diandian.com\/Public\/uploads\/20160922\/bb709e943990c779e76a2d08af6f4d20.jpg","tag":"bsbsb"}
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<BannerInfo> adverJsonHelper = new JsonHelper<>(BannerInfo.class);
                    advers = adverJsonHelper.getDatas(response);
                    if (advers != null) {
                        List<String> imgs = new ArrayList<String>();
//                        List<string> title = new ArrayList<string>();
                        for (int i = 0; i < advers.size(); i++) {
                            imgs.add(advers.get(i).getImgurl());
                        }
                        bannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                        bannerView.setBannerAnimation(Transformer.Accordion);
                        bannerView.setImageLoader(new GlideImageLoader());
                        bannerView.setImages(imgs);
                        bannerView.start();
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

    private void stringToPaths(String pathString) {
        String[] pathsArray = pathString.split(",");
        for (String s : pathsArray) {
            paths.add(s);
        }

    }

    private void getHotbigtype() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mActivity, Contants.PortA.industryslist, params, new OkHttpResponseHandler<String>(mActivity) {

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
                //[{"id":"12","name":"\u7eb8\u5dfe\u7c7b","imgurl":"http:\/\/u.19diandian.com\/Public\/uploads\/20161016\/5803998159031.jpg"},{"id":"13","name":"\u526f\u98df\u7c7b","imgurl":"http:\/\/u.19diandian.com\/Public\/uploads\/20161019\/5806efa476d9e.jpg"}]
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<Classise> jsonHelper = new JsonHelper<Classise>(Classise.class);
                    classises.clear();
                    classises.addAll(jsonHelper.getDatas(response));
                    adapter.notifyDataSetChanged();
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

    public void getNewsCount() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(getContext(), Contants.PortA.Noreadmsg, params, new OkHttpResponseHandler<String>(getContext()) {

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
                super.onResponse(request, json);
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("count")) {

                            if (Integer.parseInt(jsonObject.getString("count")) > 99) {
                                newcount_tv.setVisibility(View.VISIBLE);
                                newcount_tv.setText(jsonObject.getString("count") + "+");
                            }
                            if (Integer.parseInt(jsonObject.getString("count")) < 99 && (Integer.parseInt(jsonObject.getString("count")) != 0)) {
                                if (Integer.parseInt(jsonObject.getString("count")) < 10) {
                                    newcount_tv.setText("\u0020" + jsonObject.getString("count") + "\u0020");
                                } else {
                                    newcount_tv.setText(jsonObject.getString("count"));
                                }
                                newcount_tv.setVisibility(View.VISIBLE);
                            }
                            if ((Integer.parseInt(jsonObject.getString("count")) == 0)) {
                                if (null != newcount_tv) {
                                    newcount_tv.setVisibility(View.GONE);
                                }
                            }
                        }


                    } catch (JSONException e) {
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


    public void getGoodsInfo(final String Itemnoid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemnoid", Itemnoid);

        HttpHelper.getInstance().post(getContext(), Contants.PortA.GetItemStr, params, new OkHttpResponseHandler<String>(getContext()) {

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
                super.onResponse(request, json);
                System.out.println("response" + json);
//{"errcode":"06"}
                int statusCode = 0;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("errcode")) {
                        statusCode = Integer.parseInt(jsonObject.getString("errcode"));
                        if (statusCode == 06) {
                            Bundle bundle = new Bundle();
                            bundle.putString("itemnoid", Itemnoid == null ? "" : Itemnoid);
                            bundle.putInt("type", 0);
                            CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, bundle, false);
                        }

                    } else {
                        Toast.makeText(getContext(), "商品已经存在", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }


            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    private void refreshRequest() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mActivity, Contants.PortA.Index, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onBefore() {
                super.onBefore();

            }

            @Override
            public void onResponse(Request request, String response) {
                System.out.println("response" + response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JsonHelper<Index> jsonHelper = new JsonHelper<Index>(Index.class);
                    final Index indexInfo = jsonHelper.getData(response, null);
                    messageTx.setText("软件到期时间:" + DateUtils.getDateToString4(indexInfo.getUtime() + "000") + ",请及时续费");
                    messageTx.setVisibility(View.GONE);
                    PreferenceUtils.setPrefString(mActivity, Contants.Preference.DUETIME, indexInfo.getUtime());
                    Uri imageUri = Uri.parse(indexInfo.getShopimg());
                    //开始下载
                    simpleDraweeView.setImageURI(imageUri);
                } else {
                    showToastShort(Contants.NetStatus.NETERROR);
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
                System.out.println("response----homefoundGoods" + json);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Contants.Photo.REQUEST_SCAN_CODE == resultCode && requestCode == 0) {
            foundGoods("searchGoods", data.getExtras().getString("result"));
        } else if (Contants.Photo.REQUEST_SCAN_CODE == resultCode && requestCode == 6) {
            foundGoods("goodsIsExist", data.getExtras().getString("result"));


//            Bundle bundle = new Bundle();
//            bundle.putString("itemnoid", data.getExtras().getString("result")==null?"":data.getExtras().getString("result"));
//            bundle.putInt("type",1);
//            CommonUtils.goActivity(mActivity, WGoodsAddActivity.class, bundle, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewsCount();
    }

    @Override
    public void onDestroy() {
        if (notDialog != null) {
            notDialog.dismiss();
        }
        super.onDestroy();

    }

    @Override
    public void CardClick(int postion) {

    }


    private void toScan() {
        Bundle bundle = new Bundle();
        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.W_type);
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
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("bigtypeid", classises.get(position).getId());
        bundle.putString("bigtypeName", classises.get(position).getName());
        CommonUtils.goActivity(mActivity, WGoodsActivity.class, bundle);
        // }
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @OnClick({R.id.searchBtn, R.id.lowBtn, R.id.search2Btn, R.id.salesPromotion_lin, R.id.integral_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.integral_rl:
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                } else {
                    toScan();
                }
                break;
            case R.id.searchBtn:
                addShop();
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
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            position = advers.size();
        }
        int i = (position - 1) % advers.size();

        try {
            bannerTx.setText(advers.get(i).getTitle().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void OnCenterItemClick(NoticeDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_sure:
                dialog.dismiss();
                hidebg();
                break;
            case R.id.dialog_next:
                if (notindex == noticeLists.size() - 1) {
                    showToastShort("没有更多内容了");
                } else if (notindex < noticeLists.size() - 1) {
                    notindex++;
                    setDialogText();
                }
                break;
            case R.id.dialog_up:
                if (notindex == 0) {
                    showToastShort("没有更多内容了");
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