package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by LK on 2017/10/15.
 */

public class activityRule extends BaseFragment {


    @BindView(R.id.rule_details)
    TextView ruleDetails;
    int start;

    @Override
    public void init(Bundle savedInstanceState) {
        if (NetUtils.isNetworkConnected(mActivity)) {
            refreshRequest();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activiti_rule;
    }

    /**
     * 网络请求
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.REWARD_RULE, params, new OkHttpResponseHandler<String>(mActivity) {

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
                try {
                    JSONObject object = new JSONObject(json);
                    int status = object.getInt("status");
                    String rule = object.getString("rule");
                    String[] newStr = rule.split(";");
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    if (isAdded()) {
                        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.red));
                        for (int i = 0; i < newStr.length; i++) {
                            if (i == newStr.length - 1) {
                                start = builder.length();
                                builder.append(newStr[i]);
                            } else {
                                builder.append(newStr[i]);
                            }
                            builder.append("\r\n\r\n");

                        }
                        int end = builder.length();
                        builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ruleDetails.setText(builder);
                    }
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
}
