package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Reward;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CenterDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2017/10/16.
 */

public class RedPackActivity extends BaseActivity implements CenterDialog.OnCenterItemClickListener {
    @BindView(R.id.rule_return)
    ImageView ruleReturn;
    @BindView(R.id.my_rule)
    TextView myRule;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    private List<Reward> list = new ArrayList<>();
    String userId;
    int index;
    private CenterDialog centerDialog;
    TextView title_tv;
    @Override
    protected int getLayoutId() {
        return R.layout.redpackactivity;
    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mContext)) {
            refreshRequest();
        }
        if (getIntent().hasExtra("uid")) {
            userId = getIntent().getExtras().getString("uid");
        }
        centerDialog = new CenterDialog(mContext, R.layout.redp_dialog, new int[]{R.id.dialog_cancel, R.id.dialog_sure, R.id.tv_next});
        centerDialog.setOnCenterItemClickListener(this);
    }

    /**
     * 网络请求
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.REWARDLIST, params, new OkHttpResponseHandler<String>(mContext) {

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
                ShowLog.e(json);
                super.onResponse(request, json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Reward> jsonHelper = new JsonHelper<Reward>(Reward.class);
                    list = jsonHelper.getDatas(json);
                    tv1.setText(((int) list.get(0).getReward()) + "元");
                    tv2.setText(((int) list.get(1).getReward()) + "元");
                    tv3.setText(((int) list.get(2).getReward()) + "元");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });

    }

    @OnClick({R.id.one_redpack, R.id.two_redpack, R.id.three_redpack, R.id.rule_return, R.id.my_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rule_return:
                finish();
                break;
            case R.id.my_rule:
                refreshRequests();
                break;
            case R.id.one_redpack:
                index = 0;
                showdialog();
                break;
            case R.id.two_redpack:
                index = 1;
                showdialog();
                break;
            case R.id.three_redpack:
                index = 2;
                showdialog();
                break;
                default:
                    break;
        }
    }

    /**
     * 网络请求
     */
    public void refreshRequests() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.CHECK_REWARD, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                try {
                    JSONObject object = new JSONObject(json);
                    if (object.getInt("status") == 0) {
                        showToastLong(object.getString("info"));
                    } else {
                        //跳转到提现界面
                        Bundle bundle = new Bundle();
                        bundle.putString("reward_type", object.getString("reward_id"));
                        bundle.putString("reward", object.getString("reward"));
                        CommonUtils.goActivity(mContext, RedPackReFlect.class, bundle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showdialog() {
        showbg();
        centerDialog.show();
        ((TextView) centerDialog.findViewById(R.id.pack_content)).setText(list.get(index).getRemark());
        title_tv = (TextView) centerDialog.findViewById(R.id.notice_tiele);
        title_tv.setText(((int) list.get(index).getReward()) + "元红包");

    }


    private void submit(String rwid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", userId);
        params.put("reward_id", rwid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.REWARD_BINDING, params, new OkHttpResponseHandler<String>(mContext) {
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
                try {
                    JSONObject object = new JSONObject(json);
                    showToastShort(object.getString("info"));
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


    @Override
    public void OnCenterItemClick(CenterDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                hidebg();
                dialog.dismiss();
                break;
            case R.id.dialog_sure:
                submit(list.get(index).getId());
                hidebg();
                dialog.dismiss();
                this.finish();
                break;
            case R.id.tv_next:
                if (index < list.size() - 1) {
                    index++;
                    ((TextView) dialog.findViewById(R.id.pack_content)).setText(list.get(index).getRemark());
                    title_tv.setText(((int) list.get(index).getReward()) + "元红包");
                }
                break;
                default:
                    break;
        }
    }
}
