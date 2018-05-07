package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.CartList;
import com.yj.shopapp.ubeen.EventMassg;
import com.yj.shopapp.ubeen.gMinMax;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.shopcartlistInterface;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewCarListAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author LK
 */
public class CarListPagerFragment extends NewBaseFragment implements shopcartlistInterface.ModifyCountInterface {

    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    private SNewCarListAdapter adapter;
    private List<CartList> cartLists = new ArrayList<>();
    private gMinMax gMinMaxes;
    private CartList mCartList;
    private KProgressHUD kProgressHUD;
    private String siteid;
    private int type;
    private Map<String, CartList> map;

    public static CarListPagerFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        CarListPagerFragment fragment = new CarListPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private int getTyep() {
        return getArguments().getInt("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_list_pager;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        type = getTyep();
        adapter = new SNewCarListAdapter(mActivity);
        adapter.setModifyCountInterface(this);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        kProgressHUD = growProgress("正在修改中");
        if (isNetWork(mActivity)) {
            refreshRequest();
        }

    }


    /**
     * status 0 提交 1 删除 2 全选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMassg event) {
        //ShowLog.e("eventbus" + event.getCid());
        switch (event.getStatus()) {
            case 0:
                if (event.getCid() == type) {
                    siteid = event.getSiteid();
                    //saveOrder();
                    checkOrderOpen();
                }
                break;
            case 2:
                if (event.getCid() == type) {
                    for (CartList i : cartLists) {
                        i.setChoosed(event.isIscheck());
                    }
                    adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new MessageEvent(3, type, cartLists));
                }
                break;
            case 3:
                if (event.getCid() == type) {
                    // pullToRefresh.setRefreshing(true);
                }
                break;
            case 4:
                map = event.getMap();
                setData();

                break;
            default:
                break;
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
            if (cartList.isChoosed()) {
                stringBuffer.append(cartList.getId() + "|");
            }
        }
        if (stringBuffer.toString().length() > 0) {
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);
            Bundle bundle = new Bundle();
            bundle.putString("idstr", idstr);
            bundle.putString("addressid", siteid);
            CommonUtils.goActivity(mActivity, OrderDatails.class, bundle);
        } else {
            showToast("请选择商品");
        }
    }

    /**********************
     * 网络请求
     ******************/

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", type + "");
        HttpHelper.getInstance().post(mActivity, Contants.PortU.ListCarr, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                cartLists.clear();
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    cartLists = JSONArray.parseArray(json, CartList.class);
                    setData();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });

    }

    private void setData() {
        if (map == null) {
            adapter.setList(cartLists);
            EventBus.getDefault().post(new MessageEvent(3, type, cartLists));
        } else {
            if (cartLists != null) {
                for (int i = 0; i < cartLists.size(); i++) {
                    CartList c = map.get(cartLists.get(i).getId());
                    if (cartLists.get(i).getId().equals(c.getId())) {
                        cartLists.set(i, c);
                    }
                }
                adapter.setList(cartLists);
                isAllCheck();
            } else {
                return;
            }
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
                    EventBus.getDefault().post(new MessageEvent(2, ""));
                }
            }
        });

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

    /**
     * 增加数量接口
     *
     * @param position 元素位置
     */
    @Override
    public void doIncrease(int position) {
        CartList cartList = cartLists.get(position);
        if (cartList.getSale_status().equals("1")) {
            requestMinandMaxNum(cartList.getItemid(), position, 0);
        }

    }

    /**
     * 减少数量接口
     *
     * @param position 元素位置
     */
    @Override
    public void doDecrease(int position) {
        CartList cartList = cartLists.get(position);
        if (cartList.getSale_status().equals("1")) {
            requestMinandMaxNum(cartList.getItemid(), position, 1);
        }

    }

    /**
     * 统计数据
     */
    @Override
    public void numClick(int position) {

//        double totalPrice = 0.00;
//        if (cartLists.size() > 0) {
//            for (CartList i : cartLists) {
//                if (i.isChoosed() && i.getSale_status().equals("1")) {
//                    totalPrice += Double.parseDouble(i.getMoneysum());
//                }
//            }
//            EventBus.getDefault().post(new MessageEvent(0, CommonUtils.decimal(totalPrice)));
//        } else {
//            EventBus.getDefault().post(new MessageEvent(0, CommonUtils.decimal(totalPrice)));
//        }
    }

    private void setTitlePrice() {
        Double price = 0.0;
        if (cartLists.size() != 0) {
            for (CartList c : cartLists) {
                price += Double.parseDouble(c.getMoneysum());
            }
            EventBus.getDefault().post(new MessageEvent(2, CommonUtils.decimal(price)));
        } else {
            EventBus.getDefault().post(new MessageEvent(2, ""));
        }

    }

    private void isAllCheck() {
        if (cartLists.size() != 0) {
            for (CartList i : cartLists) {
                if (!i.isChoosed() && i.getSale_status().equals("1")) {
                    EventBus.getDefault().post(new MessageEvent(1, type, false));
                    return;
                }
            }
            EventBus.getDefault().post(new MessageEvent(1, type, true));
        } else {
            EventBus.getDefault().post(new MessageEvent(1, type, false));
        }

    }

    /***
     * checkbox 接口
     * @param position  元素位置
     * @param isChecked 元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        cartLists.get(position).setChoosed(isChecked);
        isAllCheck();
//        statistics();
        EventBus.getDefault().post(new MessageEvent(3, type, cartLists));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
