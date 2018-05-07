package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ubeen.CartList;
import com.yj.shopapp.ubeen.Classify;
import com.yj.shopapp.ubeen.gMinMax;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.shopcartlistInterface;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.CarListViewPagerAdpter;
import com.yj.shopapp.ui.activity.adapter.SNewCarListAdapter;
import com.yj.shopapp.util.Center2Dialog;
import com.yj.shopapp.util.CenterDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.GoodsNumInputDialog;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.MessageEvent;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.CustomViewPager;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2017/12/21.
 *
 * @author LK
 */

public class SNewCartListActivity extends NewBaseFragment implements shopcartlistInterface.ModifyCountInterface, Center2Dialog.OnCenterItemClickListener {

    @BindView(R.id.total_num)
    TextView totalNum;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.myviewpager)
    CustomViewPager myviewpager;
    @BindView(R.id.my_checkbox)
    CheckBox myCheckbox;
    @BindView(R.id.buttonMenuLL)
    LinearLayout buttonMenuLL;
    @BindView(R.id.id_del_btu)
    ImageView idDelBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.my_RecyclerView)
    RecycleViewEmpty myRecyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;

    private CarListViewPagerAdpter pagerAdpter;
    private boolean ishaveanaddress;
    private List<Address> notes = new ArrayList<Address>();

    private boolean isSelect;
    private List<Classify> classname = new ArrayList<>();
    private int cid = 0;
    private int currposition = 0;
    private ViewHolder holder;
    //private Map<String, CartList> cartListMap = new HashMap<>();
    private SNewCarListAdapter adapter;
    private List<CartList> cartLists = new ArrayList<>();
    private int index = 0;
    private KProgressHUD kProgressHUD;
    private gMinMax gMinMaxes;
    private CartList mCartList;
    private double totalPrice = 0.00;
    private DecimalFormat df = new DecimalFormat("######0.00");
    private boolean isload = true;
    private CenterDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newcarlist;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        title.setText("购物车");
        kProgressHUD = growProgress("正在修改中");
        adapter = new SNewCarListAdapter(mActivity);
        adapter.setModifyCountInterface(this);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setAdapter(adapter);

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstItemPosition == -1) return;
                    if (tabLayout == null) return;
                    tabLayout.setFocusable(true);
                    tabLayout.setFocusableInTouchMode(true);
                    if (cartLists.size() > 0) {
                        if (tabLayout != null) {
                            tabLayout.getTabAt(cartLists.get(firstItemPosition).getTabposition()).select();
                        }
                    }
                }
            }
        });

//        pagerAdpter = new CarListViewPagerAdpter(getChildFragmentManager());
//        myviewpager.setAdapter(pagerAdpter);
//        myviewpager.setOpenAnimation(false);
//        myviewpager.addOnPageChangeListener(this);
//        tabLayout.setupWithViewPager(myviewpager);
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            isload = true;
            refreshRequest();
        } else {
            showToast("无网络");
        }
    }

    @OnClick({R.id.id_del_btu, R.id.submit_order, R.id.my_checkbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_del_btu:
                delCart();
                break;
            case R.id.submit_order:
                if (PreferenceUtils.getPrefString(mActivity, "addressId", "").equals("")) {
                    showAddressDialod();
                } else {
                    checkOrderOpen();
                }
                break;
            case R.id.my_checkbox:
                isSelect = !isSelect;
                for (CartList c : cartLists) {
                    if (c.getSale_status().equals("0")) {
                        c.setChoosed(false);
                    } else {
                        c.setChoosed(isSelect);
                    }
                }
                statisticalData();
                isAllCheck();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void showDialog(Double moeny, final String idstr) {
        dialog = new CenterDialog(mActivity, R.layout.carlistsubmitinfo, new int[]{R.id.exit_tv, R.id.finish_tv}, 0.8);
        dialog.show();
        dialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.exit_tv:
                        Bundle bundle = new Bundle();
                        bundle.putString("idstr", idstr);
                        bundle.putString("addressid", PreferenceUtils.getPrefString(mActivity, "addressId", ""));
                        CommonUtils.goActivity(mActivity, OrderDatails.class, bundle);
                        dialog.dismiss();
                        break;
                    case R.id.finish_tv:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        ((TextView) dialog.findViewById(R.id.onetext)).setText(Html.fromHtml("平台配送金额" + "<font color=#e72c21>" + "500" + "</font>" + "元起送"));
        ((TextView) dialog.findViewById(R.id.Single_amount)).setText(Html.fromHtml("您本次下单金额为" + "<font color=#e72c21>" + df.format(moeny) + "</font>" + "元"));
    }

    /**
     * 设置Tab的样式
     */
    private void setTabView() {
        if (tabLayout == null) return;
        tabLayout.removeAllTabs();
        holder = null;
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_fc2b32));
        for (int i = 0; i < classname.size(); i++) {
            //依次获取标签
            TabLayout.Tab tab = tabLayout.newTab();
            //为每个标签设置布局
            tab.setCustomView(R.layout.tabview);
            holder = new ViewHolder(tab.getCustomView());
            //为标签填充数据
            Classify classify = classname.get(i);
            holder.tvTabName.setText(classify.getName());
            holder.tvTabNumber.setText("￥" + classify.getMoney());
            tabLayout.addTab(tab);
        }
        setTablayoutclick();
    }

    @Override
    public void doIncrease(int position) {
        if (cartLists.get(position).getSale_status().equals("1")) {
            requestMinandMaxNum(cartLists.get(position).getItemid(), position, 0);
        }
    }

    @Override
    public void doDecrease(int position) {
        if (cartLists.get(position).getSale_status().equals("1")) {
            requestMinandMaxNum(cartLists.get(position).getItemid(), position, 1);
        }
    }

    /**
     * 数量接口
     *
     * @param position
     */
    @Override
    public void numClick(final int position) {
        final CartList c = cartLists.get(position);
        if (c.getSale_status().equals("1")) {
            GoodsNumInputDialog.newInstance(c.getItemid(), c.getUnit(), Integer.parseInt(c.getItemcount())).setListener(new GoodsNumInputDialog.OnShopNumListener() {
                @Override
                public void goodsNum(String number) {
                    changeNumber(c.getId(), number, position);
                }
            }).show(mActivity.getFragmentManager(), "goodsdilog");
        }
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        if (cartLists.get(position).getSale_status().equals("0")) {
            cartLists.get(position).setChoosed(true);
            delCart();
        } else {
            cartLists.get(position).setChoosed(isChecked);
            isAllCheck();
            statisticalData();
        }
    }

    private void isAllCheck() {
        isSelect = false;
        for (CartList i : cartLists) {
            if (!i.getSale_status().equals("0")) {
                if (i.isChoosed()) {
                    isSelect = true;
                } else {
                    isSelect = false;
                    break;
                }
            }
        }
        if (cartLists.size() == 0) {
            isSelect = false;
        }
        myCheckbox.setChecked(isSelect);
        if (myCheckbox.isChecked()) {
            alltabbg();
        } else {
            setTabView();
        }
    }

    private void alltabbg() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (classname.get(i).isSelect()) {
                TabLayout.Tab t = tabLayout.getTabAt(i);
                ((TextView) t.getCustomView().findViewById(R.id.tab_name)).setTextColor(getResources().getColor(R.color.color_fc2b32));
                ((TextView) t.getCustomView().findViewById(R.id.tab_money)).setTextColor(getResources().getColor(R.color.color_fc2b32));

            } else {
                TabLayout.Tab t = tabLayout.getTabAt(i);
                ((TextView) t.getCustomView().findViewById(R.id.tab_name)).setTextColor(getResources().getColor(R.color.black));
                ((TextView) t.getCustomView().findViewById(R.id.tab_money)).setTextColor(getResources().getColor(R.color.black));
                if (tabLayout.getSelectedTabPosition() == i) {
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.black));
                }
            }

        }
    }

    @Override
    public void OnCenterItemClick(Center2Dialog dialog, View view) {
        dialog.dismiss();

    }

    class ViewHolder {
        TextView tvTabName;
        TextView tvTabNumber;

        public ViewHolder(View tabView) {
            if (tabView != null) {
                tvTabName = (TextView) tabView.findViewById(R.id.tab_name);
                tvTabNumber = (TextView) tabView.findViewById(R.id.tab_money);
            }
        }
    }

    private void checkOrderOpen() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CHECK_ORDER_OPEN, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                JSONObject object = JSONObject.parseObject(json);
                if (object.getInteger("status") == 1) {
                    saveOrder();
                } else {
                    showToast(object.getString("info"));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    public void saveOrder() {
        String idstr = "";
        StringBuffer stringBuffer = new StringBuffer();
        for (CartList cartList : cartLists) {
            if (cartList.isChoosed() && cartList.getSale_status().equals("1")) {
                stringBuffer.append(cartList.getId() + "|");
            }
        }
        if (stringBuffer.toString().length() > 0) {
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);
            if (totalPrice < 500) {
                showDialog(totalPrice, idstr);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("idstr", idstr);
                bundle.putString("addressid", PreferenceUtils.getPrefString(mActivity, "addressId", ""));
                CommonUtils.goActivity(mActivity, OrderDatails.class, bundle);
            }
        } else {
            showToast("请选择商品");
        }
    }

    public void refreshRequest2() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", "");
        HttpHelper.getInstance().post(mActivity, Contants.PortU.ListCarr, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    cartLists = JSONArray.parseArray(json, CartList.class);
                    String psi = "";
                    if (classname.size() > 0) {
                        for (int i = 0; i < cartLists.size(); i++) {
                            if (i == 0) {
                                index = 0;
                                psi = cartLists.get(i).getCid();
                                classname.get(index).setPage(i);
                                cartLists.get(i).setTabposition(index);
                            } else {
                                if (!cartLists.get(i).getCid().equals(psi)) {
                                    index++;
                                    cartLists.get(i).setTabposition(index);
                                    classname.get(index).setPage(i);
                                    psi = cartLists.get(i).getCid();
                                } else {
                                    cartLists.get(i).setTabposition(index);
                                }
                            }
                        }
                    }
                    loading.showContent();
                } else {
                    loading.showEmpty();
                }
                adapter.setList(cartLists);
                statisticalData();
                isAllCheck();
                addEmptyView();
                setTablayoutSelect();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                cartLists.clear();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

        });

    }

    private void addEmptyView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cartLists.size() > 0) {
                    int height = myRecyclerView.computeVerticalScrollExtent();
                    int itemheight = myRecyclerView.getLayoutManager().getChildAt(0).getHeight();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , height - itemheight);
                    View view = new View(mActivity);
                    view.setLayoutParams(layoutParams);
                    adapter.setFoootView(view);
                }
            }
        }, 300);
    }

    private List<CartList> c3 = new ArrayList<>();

    private void setTablayoutSelect() {
        for (Classify c : classname) {
            for (CartList c2 : cartLists) {
                if (c.getId().equals(c2.getCid())) {
                    c3.add(c2);
                }
            }
            if (c3.size() > 1) {
                for (CartList c4 : c3) {
                    if (c4.getSale_status().equals("1")) {
                        c.setSelect(true);
                    }
                }
            } else {
                if (c3.size() > 0) {
                    if (c3.get(0).getSale_status().equals("1")) {
                        c.setSelect(true);
                    }
                }
            }
            c3.clear();
        }
    }

    /**
     * status 0 价格 1 是否全选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event.getStatus() == 2) {
            if (myCheckbox.isChecked()) {
                myCheckbox.setChecked(false);
                for (CartList c : cartLists) {
                    c.setChoosed(false);
                }
                statisticalData();
                adapter.notifyDataSetChanged();
            }
        } else {
            isload = true;
            if (loading != null) {
                loading.showLoading();
            }
            refreshRequest();
        }
    }

    private void statisticalData() {
        totalPrice = 0.00;
        for (CartList c : cartLists) {
            if (c.isChoosed() && c.getSale_status().equals("1")) {
                totalPrice += Double.parseDouble(c.getMoneysum());
            }
        }
        totalNum.setText(Html.fromHtml("合计：" + "<font color=red >" + "￥" + df.format(totalPrice) + "</font>"));
    }

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CARTCLASS, params, new OkHttpResponseHandler<String>(mActivity) {
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
                    if (!"[]".equals(json)) {
                        classname = JSONArray.parseArray(json, Classify.class);
                        setTabView();
                    } else {
                        setTabView();
                    }
                }
                if (isload) {
                    refreshRequest2();
                }

            }

            @Override
            public void onBefore() {
                super.onBefore();
                classname.clear();
            }
        });
    }

    public void delCart() {
        String idstr;
        StringBuffer stringBuffer = new StringBuffer();
        for (CartList cartList : cartLists) {
            if (cartList.isChoosed()) {
                stringBuffer.append(cartList.getId() + "|");
            }
        }
        if (stringBuffer.toString().length() > 0) {
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);
            final String finalIdstr = idstr;
            new MaterialDialog.Builder(mActivity)
                    .content("是否删除选中的商品?")
                    .positiveText("删除")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            delCartReport(finalIdstr);
                            materialDialog.dismiss();
                        }
                    })
                    .show();

        } else {
            showToast("请选择商品");
        }


    }

    public void delCartReport(String idstr) {
        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", idstr);

        HttpHelper.getInstance().post(mActivity, Contants.PortU.DelListCart, params, new OkHttpResponseHandler<String>(mActivity) {

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
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    showToast("删除成功");
                    if (NetUtils.isNetworkConnected(mActivity)) {
                        isload = true;
                        refreshRequest();
                        refreshRequest2();
                    } else {
                        showToast("无网络");
                    }
                } else {
                    showToast(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    private void setTablayoutclick() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) return;
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) return;
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        //这里就可以根据业务需求处理点击事件了。
                        ((LinearLayoutManager) myRecyclerView.getLayoutManager()).scrollToPositionWithOffset(classname.get(position).getPage(), 0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showAddressDialod() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_text, null);
        new MaterialDialog.Builder(mActivity)
                .title("提示")
                .customView(view, false)
                .negativeText("取消")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("been", new Address());
                        CommonUtils.goActivityForResult(mActivity, SAddressRefreshActivity.class, bundle, 0, false);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 请求最大和最小购买数量
     */
    private void requestMinandMaxNum(final String goodsId, final int position, final int type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", goodsId);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.ITEMS_LIMITS, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                kProgressHUD.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    gMinMaxes = JSONObject.parseObject(json, gMinMax.class);
                    if (gMinMaxes != null) {
                        range(gMinMaxes, position, type);
                    }

                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                kProgressHUD.dismiss();
            }
        });
    }

    private void range(gMinMax gMinMaxes, int position, int type) {
        CartList cartList = cartLists.get(position);
        int count = Integer.parseInt(cartList.getItemcount());
        switch (type) {
            case 0:
                //增加
                count++;
                if (!"0".equals(gMinMaxes.getMaxnum())) {
                    if (count <= Integer.parseInt(gMinMaxes.getMaxnum())) {
                        changeNumber(cartList.getId(), count + "", position);
                    } else {
                        showToast("最大购买数量为" + gMinMaxes.getMaxnum());
                        kProgressHUD.dismiss();
                    }
                } else {
                    changeNumber(cartList.getId(), count + "", position);
                }
                break;
            case 1:
                //减少
                if (count > 1) {
                    count--;
                    if (!"0".equals(gMinMaxes.getMinnum())) {
                        if (count >= Integer.parseInt(gMinMaxes.getMinnum())) {
                            changeNumber(cartList.getId(), count + "", position);
                        } else {
                            showToast("最小购买数量为" + gMinMaxes.getMaxnum());
                            kProgressHUD.dismiss();
                        }
                    } else {
                        changeNumber(cartList.getId(), count + "", position);
                    }
                } else {
                    showToast("最少购买一件");
                    kProgressHUD.dismiss();
                }
                break;
            default:
                break;
        }
    }

    public void changeNumber(String itemid, final String itemsum, final int position) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", itemid);
        params.put("itemcount", itemsum);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.SaveListCart, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                kProgressHUD.dismiss();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                kProgressHUD.dismiss();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    mCartList = JSONObject.parseObject(json, CartList.class);
                    CartList mcartlist = cartLists.get(position);
                    mcartlist.setItemcount(itemsum);
                    mcartlist.setMoneysum(mCartList.getMoneysum());
                    adapter.setItemData(position, mcartlist);
                    isload = false;
                    refreshRequest();
                    statisticalData();
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
