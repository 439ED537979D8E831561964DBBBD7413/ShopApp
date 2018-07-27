package com.yj.shopapp.ui.activity.wholesale;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ErrorCorrectionAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.wbeen.WorderDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ErrorCorrectionActivity extends BaseActivity {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.errorCorrect_tv)
    TextView errorCorrectTv;
//    @BindView(R.id.scroll)
//    ScrollView scroll;
//    @BindView(R.id.mainLl)
//    KeyboardLayout mainLl;

    private String mItemid;
    private String mFields;
    private String mContent;
    private String[] names = {"供应商", "商品分类", "商品品牌", "单位", "商品名称", "商品条码", "商品编码", "商品规格", "商品位置"};
    private List<WorderDetails> worderDetails = new ArrayList<>();
    private ErrorCorrectionAdpter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_error_correction;
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
        if (getIntent().hasExtra("itemid")) {
            mItemid = getIntent().getStringExtra("itemid");
        }
        title.setText("纠错反馈");
        adpter = new ErrorCorrectionAdpter(mContext);
        setData();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (worderDetails.get(position).getStatus() == 1) {
                    worderDetails.get(position).setStatus(0);
                } else {
                    worderDetails.get(position).setStatus(1);
                }
                adpter.notifyDataSetChanged();
            }
        });
//        mainLl.setKeyboardListener((isActive, keyboardHeight) -> {
//            if (isActive) {
//                scrollToBottom();
//            }
//        });

    }

//    /**
//     * 弹出软键盘时将SVContainer滑到底
//     */
//    private void scrollToBottom() {
//        scroll.postDelayed(() -> scroll.smoothScrollTo(0, scroll.getBottom() + SoftKeyInputHidWidget.getStatusBarHeight(ErrorCorrectionActivity.this)), 100);
//    }

    private void setData() {
        for (int i = 0; i < names.length; i++) {
            worderDetails.add(new WorderDetails(names[i], 0));
        }
        adpter.setList(worderDetails);
    }

    private void submit() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", mItemid);
        params.put("fields", mFields);
        params.put("content", mContent);
        HttpHelper.getInstance().post(mContext, Contants.PortA.ERROR_RECOVERY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 1) {
                        showToastShort(object.getString("info"));
                        finish();
                    } else {
                        showToastShort(object.getString("info"));
                    }
                }
            }
        });
    }

    @OnClick({R.id.errorCorrect_RL, R.id.submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.errorCorrect_RL:

                new MaterialDialog.Builder(mContext).title("请输入纠错信息").input("", errorCorrectTv.getText().toString(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        errorCorrectTv.setText(dialog.getInputEditText().getText().toString());
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.submit_tv:
                setmFields();
                mContent = errorCorrectTv.getText().toString();
                submit();
                break;
        }
    }

    private void setmFields() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < worderDetails.size(); i++) {
            if (worderDetails.get(i).getStatus() == 1) {
                buffer.append(worderDetails.get(i).getName());
                buffer.append(",");
            }
        }
        buffer.deleteCharAt(buffer.length() - 1);
        mFields = buffer.toString();
    }

}
