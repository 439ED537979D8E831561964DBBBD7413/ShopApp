package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.LimitedSale;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.BuGoodDetailsViewPager;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.view.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BuGoodDetails extends BaseActivity {

    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_viewpager)
    CustomViewPager myViewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private BuGoodDetailsViewPager buGoodDetailsViewPager;
    private boolean isok = false;
    private MaterialDialog dialog;
    private String ruleText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bu_good_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        contentTv.setText("限量抢购");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buGoodDetailsViewPager = new BuGoodDetailsViewPager(mContext, getSupportFragmentManager(), new String[]{"正在疯抢", "即将开抢", "我的抢购"});
        myViewpager.setAdapter(buGoodDetailsViewPager);
        myViewpager.setOpenAnimation(false);
        myViewpager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(myViewpager);
        rightTv.setText("规则");
        getRule();
        getGoodsNum();
    }

    public void setUpTabBadge(int position, int size) {

        TabLayout.Tab tab = tabLayout.getTabAt(position); // 更新Badge前,先remove原来的customView,否则Badge无法更新
        View customView = tab.getCustomView();
        if (customView != null) {
            ViewParent parent = customView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(customView);
            }
        }
        //更新CustomView
        tab.setCustomView(buGoodDetailsViewPager.getTabItemView(position, size));
        //需加上以下代码, 不然会出现更新Tab角标后, 选中的Tab字体颜色不是选中状态的颜色
        //Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition())).getCustomView()).setSelected(true);
        if (tab.isSelected()) {
            tab.select();
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_4c4c4c), 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LimitedSale sale) {
        if (sale.getId().equals("1")) {
            myViewpager.setCurrentItem(2);
        } else if (sale.getId().equals("2")) {
            getGoodsNum();
        }

    }

    private void getRule() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortS.MSG, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    ruleText = JSONObject.parseObject(response).getString("font");
                    if (!PreferenceUtils.getPrefBoolean(mContext, "ruleDialog", false)) {
                        dialog = new MaterialDialog.Builder(mContext).title("限量抢购须知").content(ruleText).positiveText("好的，我知道了")
                                .cancelable(false).show();
                        final TextView tv = dialog.getActionButton(DialogAction.POSITIVE);
                        tv.setOnClickListener(v -> {
                            if (isok) {
                                dialog.dismiss();
                            }
                        });
                        CountDownTimer countDownTimer = new CountDownTimer(15000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv.setText(String.format("%d秒后关闭", millisUntilFinished / 1000));

                            }

                            @Override
                            public void onFinish() {
                                tv.setText("好的，我知道了");
                                isok = true;
                            }
                        };
                        countDownTimer.start();
                        PreferenceUtils.setPrefBoolean(mContext, "ruleDialog", true);
                    }

                }

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });

    }

    private void getGoodsNum() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortS.SALES_NUM, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                JSONObject object = JSONObject.parseObject(response);
                if (!object.getString("start_num").equals("")) {
                    setUpTabBadge(0, Integer.parseInt(object.getString("start_num")));
                }
                if (!object.getString("nostart_num").equals("")) {
                    setUpTabBadge(1, Integer.parseInt(object.getString("nostart_num")));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.right_tv)
    public void onViewClicked() {
        new MaterialDialog.Builder(mContext).title("限量抢购须知").content(ruleText).positiveText("好的，我知道了")
                .cancelable(false).show();
    }
}
