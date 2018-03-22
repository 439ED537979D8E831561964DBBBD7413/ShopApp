package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.adapter.SGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.AnimationUtil;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.InputFilterMinMax;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Itemtype;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SLowGoodsActivity extends BaseActivity implements GoodsRecyclerView {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.topsearchLy)
    LinearLayout topsearchLy;
    @BindView(R.id.id_drawer_layout)
    RelativeLayout idDrawerLayout;
    @BindView(R.id.first_low)
    TextView firstLow;
    @BindView(R.id.first_high)
    TextView firstHigh;
    @BindView(R.id.bgView)
    View bgView;


    private Context mContext = SLowGoodsActivity.this;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;


    private RecyclerViewHeaderFooterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新

    private int mCurrentPage = 0;

    private List<Goods> goodsList = new ArrayList<>();
    private List<Itemtype> itemtypeList = new ArrayList<>();
    List<String> tArray = new ArrayList<>();


    String uid;
    String token;
    String sort = "0";

    int minnum;
    int maxnum;
    int gsum;
    String text1;
    String text2;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_slow_goods;
    }

    @Override
    protected void initData() {
        title.setText("清仓特价");
        idRightBtu.setVisibility(View.VISIBLE);
        idRightBtu.setText("排序");

        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        SGoodsAdapter oAdapter = new SGoodsAdapter(mContext, goodsList, this, 1);

//        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置adapter
        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, oAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        refreshRequest();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
    }


    public void refreshRequest() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("sort", sort);
        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortU.lowpriceList, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                goodsList.clear();
                Log.e("m_tag", json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    mCurrentPage++;
                    goodsList.addAll(jsonHelper.getDatas(json));
                    if (goodsList.size() >= 10) {
                        adapter.addFooter(loadMoreView);
                    } else {
                        adapter.removeFooter(loadMoreView);
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    adapter.removeFooter(loadMoreView);
                } else {
                    showToastShort("暂无商品");
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                goodsList.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void loadMoreRequest() {
        if (isRequesting)
            return;
        if (goodsList.size() < 10) {
            return;
        }

        iLoadView.showLoadingView(loadMoreView);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("sort", sort);

        HttpHelper.getInstance().post(mContext, Contants.PortU.lowpriceList, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        mCurrentPage++;
                        goodsList.addAll(jsonHelper.getDatas(json));
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    iLoadView.showFinishView(loadMoreView);
                } else {
                    mCurrentPage--;
                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
                iLoadView.showErrorView(loadMoreView);
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
//        bundle.putString("itemnoid", goodsList.get(position).getNumberid());
        bundle.putString("goodsId", goodsList.get(position).getId());
        CommonUtils.goActivity(mContext, SGoodsDetailActivity.class, bundle, false);
    }

    @Override
    public void onLongItemClick(final int position) {
    }

    @Override
    public void CardClick(int postion) {
        //setDialogInput(postion);
        showAlertDialog(postion);
    }

    public class mLoadMoreClickListener implements LoadMoreClickListener {
        @Override
        public void clickLoadMoreData() {
        }
    }


    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {
        }

        @Override
        public void onScrollDown() {
        }

        @Override
        public void onBottom() {
            loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }




    private void showAlertDialog(final int postion) {
        text1 = "";
        text2 = "";
        final String goodsId = goodsList.get(postion).getId();
        requestMinandMaxNum(goodsId, postion);

    }


    /**
     * 请求最大和最小购买数量
     */
    private void requestMinandMaxNum(final String goodsId, final int postion) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", goodsId);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMS_LIMITS, params, new OkHttpResponseHandler<String>(mContext) {
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
                Log.e("lk_tag", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    minnum = Integer.parseInt(jsonObject.getString("minnum"));
                    maxnum = Integer.parseInt(jsonObject.getString("maxnum"));
                    gsum = Integer.parseInt(jsonObject.getString("itemsum"));
                    Log.e("m_tag", minnum + "+++++" + maxnum);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialog = inflater.inflate(R.layout.dialog_input_6, null);
                    final TextView textView = (TextView) dialog.findViewById(R.id.prompt_tv);
                    final EditText editText = (EditText) dialog.findViewById(R.id.inputEt);
                    if (gsum < minnum) {
                        editText.setFocusable(false);
                        text1 = "起购数量为" + minnum;
                        text2 = "库存为" + gsum;
                        minnum = gsum;
                        maxnum = gsum;
                    } else if (gsum < maxnum) {
                        if (minnum != 0) {
                            text1 = "起购数量为" + minnum;
                            text2 = "限购数量为" + gsum;
                        } else {
                            text1 = "限购数量为" + minnum;
                            text2 = "库存为" + gsum;
                        }
                        maxnum = gsum;
                    } else {
                        if (minnum != 0) {
                            text1 = "起购数量为" + minnum;
                        }
                        if (maxnum != 0) {
                            text2 = "限购数量为" + maxnum;
                        }
                    }
                    textView.setText(text1 + text2);
                    if (minnum != 0) {
                        editText.setText("" + minnum);
                    }else {
                        editText.setText("");
                    }
                    editText.setSelection(editText.getText().length());
                    editText.setFilters(new InputFilter[]{new InputFilterMinMax(minnum, maxnum)});
                    final AlertDialog dialog1 = new AlertDialog.Builder(mContext
                    ).setTitle("请填写购买数量！")
                            .setView(dialog)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", null).create();
                    dialog1.show();
                    dialog1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //确定按钮监听
                            String str = editText.getText().toString().trim();
                            if (StringHelper.isEmpty(str)) {
                                showToast("请填写购买数量！");
                            } else {
                                int num = Integer.parseInt(str);
                                if (num < minnum) {
                                    textView.setText("购买商品小于最小购买量");
                                    editText.setText(minnum + "");
                                    editText.setSelection(editText.getText().length());
                                } else {
                                    if(num>0){
                                        saveDolistcart(goodsId, str, goodsList.get(postion).getName());
                                        dialog1.dismiss();
                                    }else {
                                        textView.setText("请输入购买数量");
                                    }
                                }
                            }
                        }
                    });
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setTitle("请输入购买数量");
//                    builder.setView(dialog);
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //确定按钮监听
//                            string str = editText.getText().toString().trim();
//                            if (StringHelper.isEmpty(str)) {
//                                showToast("请填写购买数量！");
//                            } else {
//                                int num = Integer.parseInt(str);
//                                if (num < minnum) {
//                                    showToast("购买商品小于最小购买量");
//                                } else {
//                                    saveDolistcart(goodsId, str, goodsList.get(postion).getName());
//                                    dialog.dismiss();
//                                }
//                            }
//                        }
//                    });
//                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    private void showToast(String str) {
        Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void saveDolistcart(String itemid, String itemsum, final String name) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("itemsum", itemsum);

        final KProgressHUD kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);

        HttpHelper.getInstance().post(mContext, Contants.PortU.DOLISTCART, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                kProgressHUD.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                kProgressHUD.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                   // CommonUtils.setBroadCast(mContext, Contants.Bro.REFRESH_CARTLIST);
                    showToastShort("加入购物车成功");
                } else if (JsonHelper.getRequstOK(json) == 17) {

                    DialogUtils dialog = new DialogUtils();
                    dialog.getMaterialDialog(mContext, "提示", "当前商品库存不足，是否找同类别商品", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String finalName = "";
                            if (name.length() < 5) {
                                finalName = name.substring(0, 3).toString();
                            }
                            if (name.length() > 5) {
                                finalName = name.substring(0, 4).toString();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("keyword", finalName);
                            CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);
                        }
                    }, null);
                    dialog.show();
                    return;


                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }


    /************************/

    @OnClick(R.id.id_right_btu)
    public void openDrawer() {
        if (isRequesting)
            return;
        if (topsearchLy.getVisibility() == View.GONE) {
            bgView.setVisibility(View.VISIBLE);
            AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
                @Override
                public void onEnd() {
                    topsearchLy.setVisibility(View.VISIBLE);
                }
            }, topsearchLy, -CommonUtils.screenHeight(mContext) / 2, 0, 300);
        } else {
            bgView.setVisibility(View.GONE);
            AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
                @Override
                public void onEnd() {
                    topsearchLy.setVisibility(View.GONE);
                }
            }, topsearchLy, 0, -CommonUtils.screenHeight(mContext) / 2, 300);
        }
    }

    @OnClick(R.id.first_low)
    public void onFirstLowClicked() {
        //由低至高
        sort = "0";
        swipeRefreshLayout.setRefreshing(true);
        refreshRequest();
        bgView.setVisibility(View.GONE);
        AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
            @Override
            public void onEnd() {
                topsearchLy.setVisibility(View.GONE);
            }
        }, topsearchLy, 0, -CommonUtils.screenHeight(mContext) / 2, 300);
    }

    @OnClick(R.id.first_high)
    public void onFirstHighClicked() {
        //由高至低
        sort = "1";
        swipeRefreshLayout.setRefreshing(true);
        refreshRequest();
        bgView.setVisibility(View.GONE);
        AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
            @Override
            public void onEnd() {
                topsearchLy.setVisibility(View.GONE);
            }
        }, topsearchLy, 0, -CommonUtils.screenHeight(mContext) / 2, 300);
    }


    @Override
    public void onDestroy() {
//        mContext.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();

    }

    @Override
    public void showToastShort(String msg) {
        super.showToastShort(msg);
    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
}
