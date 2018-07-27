package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ReturnGoodsBean;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.ClearEditText;
import com.yj.shopapp.view.YearPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class ReturnGoodsHistory extends BaseActivity {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.purchase_num)
    TextView purchaseNum;
    @BindView(R.id.purchase_money)
    TextView purchaseMoney;
    @BindView(R.id.returngoods_num)
    TextView returngoodsNum;
    @BindView(R.id.returngoods_money)
    TextView returngoodsMoney;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    private int CurrentPage = 1;
    private Returngoodsadpter returngoodsadpter;
    private String mKeyword = "";
    private String mMonth;
    private ReturnGoodsBean bean;
    private int mYear, Month, mDay;
    private int cYear, cMonth, cDay;
    private List<ReturnGoodsBean.ListBean> listBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_goods_history;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
        title.setText("退货商品列表");
        idRightBtu.setText("全部");
        if (isNetWork(mContext)) {
            requstReturnGoods();
        }
        if (loading != null) {
            loading.showContent();
        }
        returngoodsadpter = new Returngoodsadpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(returngoodsadpter);
        returngoodsadpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("itemid", bean.getList().get(position).getItemid());
                bundle.putString("shopname", bean.getList().get(position).getName());
                CommonUtils.goActivity(ReturnGoodsHistory.this, ReturnGoodsHistoryDatailsAcitivy.class, bundle);
            }
        });
        Refresh();
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        Month = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        cYear = dateAndTime.get(Calendar.YEAR);
        cMonth = dateAndTime.get(Calendar.MONTH);
        cDay = dateAndTime.get(Calendar.YEAR);
        timeTv.setText("全部");
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mKeyword = s.toString();
                listBeans.clear();
                CurrentPage = 1;
                requstReturnGoods();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
//        swipeRefreshLayout.setOnRefreshListener(v -> {
//            CurrentPage = 1;
//            requstReturnGoods();
//        });
        swipeRefreshLayout.setEnableRefresh(false);
        swipeRefreshLayout.setOnLoadMoreListener(v -> {
            CurrentPage++;
            requstReturnGoods();
        });
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void requstReturnGoods() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", mKeyword);
        params.put("p", CurrentPage + "");
        params.put("month", mMonth);
        HttpHelper.getInstance().post(mContext, Contants.PortU.RETURNHISTORY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(false);
                }
                CurrentPage--;

            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(true);
                    swipeRefreshLayout.setEnableLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (json.equals("[]")) {
                        if (loading != null && listBeans.size() == 0) {
                            loading.showEmpty();
                        }
                    } else {
                        bean = JSONObject.parseObject(json, ReturnGoodsBean.class);
                        listBeans.addAll(bean.getList());
                        returngoodsadpter.setList(listBeans);
                        setData();
                    }
                }
            }
        });
    }

    private void setData() {
        purchaseNum.setText(String.format("进货：%s件", bean.getInfo().getStock_num()));
        purchaseMoney.setText(String.format("金额：￥%s", bean.getInfo().getStock_money()));
        returngoodsNum.setText(String.format("退货：%s件", bean.getInfo().getNum()));
        returngoodsMoney.setText(String.format("金额：￥%s", bean.getInfo().getMoney()));
    }

    @OnClick({R.id.id_right_btu, R.id.timeSelectView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_right_btu:
                mKeyword = "";
                listBeans.clear();
                CurrentPage = 1;
                mMonth = "";
                timeTv.setText("全部");
                requstReturnGoods();
//                new MaterialDialog.Builder(mContext).title("请输入商品名").input("", "", false, new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//
//                    }
//                }).onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        mKeyword = dialog.getInputEditText().getText().toString();
//                        listBeans.clear();
//                        CurrentPage = 1;
//                        requstReturnGoods();
//                        dialog.dismiss();
//                    }
//                }).show();
                break;
            case R.id.timeSelectView:
                Context themed = new ContextThemeWrapper(mContext,
                        android.R.style.Theme_Holo_Light_Dialog);
                YearPickerDialog datePicker = new YearPickerDialog(themed, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mMonth = String.format("%d-%02d", year, month + 1);
                        timeTv.setText(String.format("%d-%02d", year, month + 1));
                        CurrentPage = 1;
                        listBeans.clear();
                        requstReturnGoods();
                    }
                }
                        , mYear, Month, mDay); //上下文，点击回调,Calendar年月日
                if (!datePicker.isHasNoDay()) {
                    datePicker.setHasNoDay(true);
                }
                datePicker.getDatePicker().init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cYear = year;
                        cMonth = monthOfYear;
                        cDay = dayOfMonth;
                    }
                });
                datePicker.getDatePicker().setMaxDate((new Date()).getTime());
                datePicker.show();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }
}
