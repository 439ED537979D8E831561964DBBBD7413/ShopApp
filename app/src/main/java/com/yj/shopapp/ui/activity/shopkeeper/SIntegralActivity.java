package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralInfo;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegralViewPagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分管理界面
 */
public class SIntegralActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.filletTextView4)
    RadioButton filletTextView4;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;
    @BindView(R.id.m_LinearLayout)
    RadioGroup mLinearLayout;
    @BindView(R.id.ranking)
    TextView ranking;
    private Activity mContext = this;
    private KProgressHUD kProgressHUD;
    private long curr_integral = 0;
    private long limt_integral = 0;
    private IntegralInfo integralInfo;
    private int status;
    private String site;
    private IntegralViewPagerAdpter pagerAdpter;
    private int CashingSwitch;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            getIntegral();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sintegral;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("status")) {
            status = getIntent().getIntExtra("status", -1);
        }
        if (getIntent().hasExtra("swtich")) {
            CashingSwitch = getIntent().getIntExtra("swtich", -1);
        }
        //filletTextView4.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
        kProgressHUD = growProgress(Contants.Progress.LOAD_ING);
        mLinearLayout.setOnCheckedChangeListener(this);
        pagerAdpter = new IntegralViewPagerAdpter(getSupportFragmentManager(),3);
        myViewpager.addOnPageChangeListener(this);
        myViewpager.setAdapter(pagerAdpter);
        mLinearLayout.check(CashingSwitch == 1 ? R.id.filletTextView4 : R.id.filletTextView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            getIntegral();
        } else {
            showToastShort("无网络");
        }
    }

    private void getIntegral() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortU.UserIntegral, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                kProgressHUD.dismiss();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<IntegralInfo> jsonHelper = new JsonHelper(IntegralInfo.class);
                    integralInfo = jsonHelper.getData(json, null);
                    curr_integral = integralInfo.getIntegral();
                    limt_integral = integralInfo.getMin_limit();
                    //ShowLog.e("integral" + curr_integral + "limt_integral" + limt_integral);
                    textView4.setText(Html.fromHtml("积分总额" + "<br/>" + "<font color=#ed961b size=18>" + integralInfo.getIntegral() + "</font>"));
                    ranking.setText(integralInfo.getRanking());
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.filletTextView:
                myViewpager.setCurrentItem(0);
                break;
            case R.id.filletTextView2:
                myViewpager.setCurrentItem(1);
                break;
            case R.id.filletTextView3:
                myViewpager.setCurrentItem(2);
                break;
//            case R.id.filletTextView4:
//                myViewpager.setCurrentItem(3);
//                break;
        }
    }

    @OnClick({R.id.tv_lift, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_lift:
                finish();
                break;
            case R.id.tv_right:
                showRuleDialog();
                break;
            default:
                break;
        }
    }

    private void showRuleDialog() {
        String remake = integralInfo.getRemark().trim();
        remake = remake.replaceAll(" ", "");
        remake = remake.replaceAll("；", ";\r\n");
        new MaterialDialog.Builder(mContext)
                .title("积分规则")
                .content(remake)
                .positiveText("我知道了")
                .show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mLinearLayout.check(R.id.filletTextView);
                break;
            case 1:
                mLinearLayout.check(R.id.filletTextView2);
                break;
            case 2:
                mLinearLayout.check(R.id.filletTextView3);
                break;
//            case 3:
//                mLinearLayout.check(R.id.filletTextView4);
//                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
