package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewOrderAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2018/3/28.
 *
 * @author LK
 */

public class FragmentSearchBoxSelect extends DialogFragment {
    @BindView(R.id.exit_tv)
    ImageView exitTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private Context mActivity;
    private int Type = 0;
    public String uid;
    public String token;
    private SNewOrderAdpter adapter;
    private List<NewOrder> orderList = new ArrayList<>();

    public static FragmentSearchBoxSelect newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        FragmentSearchBoxSelect fragment = new FragmentSearchBoxSelect();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type = getArguments().getInt("type");
        mActivity = getActivity();
        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mActivity, Contants.Preference.TOKEN, "");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        StatusBarManager.getInstance().setDialogWindowStyle(getDialog().getWindow(), getResources().getColor(R.color.colorf5f5f5));
        StatusBarManager.getInstance().setStatusBarTextColor(getDialog().getWindow(), true);
        View rootView = inflater.inflate(R.layout.activity_search, container);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading.showContent();
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        adapter = new SNewOrderAdpter(mActivity, orderList);
        myRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (orderList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("oid", orderList.get(position).getOid());
                    CommonUtils.goActivity(mActivity, SOrderDatesActivity.class, bundle);
                    dismiss();
                }
            }
        });
        if (Type != 0) {
            valueEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals("")) {

                    } else {
                        String input = valueEt.getText().toString();
                        refreshRequest(input);
                        loading.showLoading();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
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
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inManager = (InputMethodManager) valueEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    @OnClick({R.id.exit_tv, R.id.submitTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_tv:
                dismiss();
                break;
            case R.id.submitTv:
                if (Type == 0) {
                    String input = valueEt.getText().toString();
                    if (!input.equals("")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("name", "商品详情");
                        bundle.putString("keyword", input);
                        CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
                        dismiss();
                    }
                } else {
                    String input = valueEt.getText().toString();
                    if (!input.equals("")) {
                        hideImm();
                        refreshRequest(input);
                        loading.showLoading();
                    }
                }
                break;
        }
    }

    public void refreshRequest(String keyword) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", "1");
        params.put("keyword", keyword);
        params.put("ostatus", "0");
        HttpHelper.getInstance().post(mActivity, Contants.PortU.MYORDER, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    orderList.addAll(JSONArray.parseArray(json, NewOrder.class));
                    adapter.setList(orderList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    loading.showEmpty();
                }
                if (loading != null) {
                    loading.showContent();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                orderList.clear();
            }
        });
    }

    private void hideImm() {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(valueEt.getWindowToken(), 0);
    }
}
