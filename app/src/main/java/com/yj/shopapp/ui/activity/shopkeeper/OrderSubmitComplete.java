package com.yj.shopapp.ui.activity.shopkeeper;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OutOfStork;
import com.yj.shopapp.ui.activity.adapter.OutOfStorkAdpter;
import com.yj.shopapp.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderSubmitComplete extends DialogFragment implements DialogInterface.OnKeyListener {

    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.content_tv)
    TextView contentTv;
    Unbinder unbinder;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.shopnum)
    TextView shopnum;
    @BindView(R.id.coupon_money_tv)
    TextView couponMoneyTv;
    @BindView(R.id.ordermoney)
    TextView ordermoney;
    private Context mActivity;
    private String json;
    private List<OutOfStork> outOfStorks = new ArrayList<>();
    private String oid;
    private OutOfStorkAdpter adpter;
    private String money;
    private String coupon_money;
    private String num;

    public static OrderSubmitComplete newInstance(String json) {

        Bundle args = new Bundle();
        args.putString("json", json);
        OrderSubmitComplete fragment = new OrderSubmitComplete();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mActivity = getActivity();
        json = getArguments().getString("json");
        JSONObject object = JSONObject.parseObject(json);
        oid = object.getString("oid");
        JSONArray array = object.getJSONArray("cancel");
        outOfStorks = array.toJavaList(OutOfStork.class);
        money = object.getString("money");
        coupon_money = object.getString("coupon_money");
        num = object.getString("num");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //StatusBarManager.getInstance().setStatusBar(getDialog().getWindow(), getResources().getColor(R.color.color_01ABFF));
        View rootView = inflater.inflate(R.layout.fragment_order_submit_complete, container);
        unbinder = ButterKnife.bind(this, rootView);
        this.getDialog().setOnKeyListener(this);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adpter = new OutOfStorkAdpter(mActivity, outOfStorks);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.setAdapter(adpter);
        contentTv.setText("缺货信息");
        shopnum.setText(String.format("数量：%s", num));
        couponMoneyTv.setText(String.format("优惠：%s", coupon_money));
        ordermoney.setText(Html.fromHtml("实付金额:" + "<font color=#e80505>" + "￥" + money + "</font>"));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window dialogWindow = getDialog().getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show(FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed())
                return;
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Go_carlist, R.id.go_orderdatails})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Go_carlist:
                getActivity().finish();
                break;
            case R.id.go_orderdatails:
                Bundle bundle = new Bundle();
                bundle.putString("oid", oid);
                CommonUtils.goActivity(mActivity, SOrderDatesActivity.class, bundle);
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        } else {
            return false;
        }
    }
}
