package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ubeen.Classify;
import com.yj.shopapp.ubeen.EventMassg;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.CarListViewPagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.MessageEvent;
import com.yj.shopapp.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2017/12/21.
 *
 * @author LK
 */

public class SNewCartListActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.total_num)
    TextView totalNum;
    @BindView(R.id.content_title)
    TextView contentTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    @BindView(R.id.my_checkbox)
    CheckBox myCheckbox;
    @BindView(R.id.buttonMenuLL)
    LinearLayout buttonMenuLL;
    @BindView(R.id.id_del_btu)
    TextView idDelBtu;
    private CarListViewPagerAdpter pagerAdpter;
    private boolean ishaveanaddress;
    private List<Address> notes = new ArrayList<Address>();
    private String siteid;
    private boolean isSelect;
    private List<Classify> classname = new ArrayList<>();
    private int cid = 0;
    private int currposition = 0;
    private ViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newcarlist;
    }

    @Override
    protected void initData() {
        contentTitle.setText("购物车");
        pagerAdpter = new CarListViewPagerAdpter(getSupportFragmentManager());
        myviewpager.setAdapter(pagerAdpter);
        myviewpager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(myviewpager);
        if (NetUtils.isNetworkConnected(mContext)) {
            getSite();
        } else {
            showToastShort("无网络");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            refreshRequest();
            EventBus.getDefault().post(new EventMassg(3, cid));
        } else {
            showToastShort("无网络");
        }
    }

    @OnClick({R.id.id_del_btu, R.id.submit_order, R.id.lift_tv_btn, R.id.my_checkbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_del_btu:
                EventBus.getDefault().post(new EventMassg(1, cid));
                break;
            case R.id.submit_order:
                if (ishaveanaddress) {
                    EventBus.getDefault().post(new EventMassg(0, siteid, cid));
                } else {
                    showAddressDialod();
                }
                break;
            case R.id.lift_tv_btn:
                finish();
                break;
            case R.id.my_checkbox:
                isSelect = !isSelect;
                if (isSelect) {
                    EventBus.getDefault().post(new EventMassg(2, true, cid));
                } else {
                    EventBus.getDefault().post(new EventMassg(2, false, cid));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置Tab的样式
     */
    private void setTabView() {
        if (tabLayout == null) return;
        holder = null;
        for (int i = 0; i < classname.size(); i++) {
            //依次获取标签
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //为每个标签设置布局
            tab.setCustomView(R.layout.tabview);
            holder = new ViewHolder(tab.getCustomView());
            //为标签填充数据
            Classify classify = classname.get(i);
            holder.tvTabName.setText(classify.getName());
            holder.tvTabNumber.setText("￥" + classify.getMoney());
        }

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

    /**
     * status 0 价格 1 是否全选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        switch (event.getStatus()) {
            case 0:
                totalNum.setText(Html.fromHtml("总计：" + "<font color=red >" + "￥" + event.getMessage() + "</font>"));
                break;
            case 1:
                myCheckbox.setChecked(event.isCheck());
                isSelect = event.isCheck();
                break;
            case 2:
                refreshRequest();
                break;
            default:
                break;
        }
    }

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.CARTCLASS, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (!"[]".equals(json)) {
                        classname = JSONArray.parseArray(json, Classify.class);
                        if (classname.size() > 1) {
                            classname.add(0, new Classify("0", "全部", "0.00", true));
                        } else {
                            classname.get(0).setSwitch(true);
                            cid = Integer.parseInt(classname.get(0).getId());
                        }
                        setTabLayoutName();
                    } else {
                        myviewpager.removeAllViews();
                        showToastShort("购物车是空的");
                        tabLayout.removeAllTabs();
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });
    }


    private void setTabLayoutName() {
        Double price = 0.00;
        for (Classify n : classname) {
            price += Double.parseDouble(n.getMoney());
        }
        if (classname.size() > 1) {
            DecimalFormat df = new DecimalFormat("#.00");
            classname.get(0).setMoney(df.format(price) + "");
        }
        pagerAdpter.setClassname(classname);
        setTabView();
    }

    private void showAddressDialod() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_text, null);
        new MaterialDialog.Builder(this)
                .title("提示")
                .customView(view, false)
                .negativeText("取消")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("been", new Address());
                        CommonUtils.goActivityForResult(mContext, SAddressRefreshActivity.class, bundle, 0, false);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 获取地址
     */
    private void getSite() {
        notes.clear();
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    ishaveanaddress = true;
                    notes.clear();
                    JsonHelper<Address> jsonHelper = new JsonHelper<Address>(Address.class);
                    notes.addAll(jsonHelper.getDatas(json));
                    siteid = notes.get(0).getId();
                } else {
                    showToastShort("没有收货地址");
                    ishaveanaddress = false;
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ShowLog.e(position + "");
        cid = Integer.parseInt(classname.get(position).getId());
        if (currposition != position) {
            EventBus.getDefault().post(new EventMassg(3, cid));
            currposition = position;
        }

        if (position == 0) {
            buttonMenuLL.setVisibility(View.VISIBLE);
            //myCheckbox.setClickable(true);
            idDelBtu.setEnabled(true);
        } else {
            idDelBtu.setEnabled(false);
            buttonMenuLL.setVisibility(View.GONE);
            //myCheckbox.setClickable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
