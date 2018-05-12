package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SBrand2Adapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/12 0012.
 */

public class Seek_brand extends BaseActivity {
    public static final int GOBACKONE = 3;
    public static final int GOBACKTWO = 4;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.brand_recy)
    RecyclerView brandRecy;
    private List<TagGroup> groups = new ArrayList<TagGroup>();
    private String typeid = "";
    private String cid = "";
    private SBrand2Adapter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seek_b;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("typeid")) {
            typeid = getIntent().getStringExtra("typeid");
        }
        if (getIntent().hasExtra("cid")) {
            cid = getIntent().getStringExtra("cid");
        }
        adpter = new SBrand2Adapter(mContext);
        if (brandRecy != null) {
            brandRecy.setLayoutManager(new GridLayoutManager(mContext, 4));
            brandRecy.setAdapter(adpter);
        }
        adpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bId = groups.get(position).getId();
                Bundle b = new Bundle();
                b.putString("bid", bId);
                b.putString("gname", groups.get(position).getName());
                CommonUtils.goResult(mContext, b, GOBACKONE);
            }
        });
        if (NetUtils.isNetworkConnected(mContext)) {
            getScreen();
        } else {
            showToastShort("无网络");
        }
    }


    /**
     * 获取group 名字
     */
    private void getScreen() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("industryid", typeid);
        params.put("cid", cid);
        ShowLog.e("Cid" + cid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.BrandList, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
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
                    JsonHelper<TagGroup> jsonHelper = new JsonHelper<TagGroup>(TagGroup.class);
                    groups.addAll(jsonHelper.getDatas(json));
                    adpter.setList(groups);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    @OnClick(R.id.submitTv)
    public void onClick() {
        String content = valueEt.getText().toString().trim();
        Bundle b = new Bundle();
        b.putString("content", content);
        CommonUtils.goResult(mContext, b, GOBACKTWO);
    }

    @OnClick(R.id.exit_tv)
    public void onViewClicked() {
        finish();
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
