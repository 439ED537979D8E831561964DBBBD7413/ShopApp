package com.yj.shopapp.ui.activity.shopkeeper;

import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.MsgsDetail;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/4/28.
 */
public class SMyNewsDetailActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.sendUser_TX)
    TextView sendUserTX;
    @BindView(R.id.sendDate_TX)
    TextView sendDateTX;
    @BindView(R.id.contentTx)
    TextView contentTx;
    @BindView(R.id.msgTiele_tx)
    TextView msgTieleTx;

    String uid;
    String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newsdetail;
    }

    @Override
    protected void initData() {
        title.setText("消息详情");
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        Request();
    }


    public void Request() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", this.getIntent().getExtras().getString("id"));

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);

        HttpHelper.getInstance().post(mContext, Contants.PortU.Msgsp, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json,mContext)) {
                    JsonHelper<MsgsDetail> jsonHelper = new JsonHelper<MsgsDetail>(MsgsDetail.class);

                    MsgsDetail msgsDetail = jsonHelper.getData(json, null);

                    msgTieleTx.setText(msgsDetail.getMesstitle());
                    sendUserTX.setText(msgsDetail.getSenduser());
                    sendDateTX.setText(DateUtils.getDateToString2(msgsDetail.getAddtime()+"000"));
                    contentTx.setText(StringHelper.toBanJiao(msgsDetail.getMessstr()));
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


}
