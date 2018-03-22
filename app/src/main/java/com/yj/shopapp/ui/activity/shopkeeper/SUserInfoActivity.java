package com.yj.shopapp.ui.activity.shopkeeper;

import android.text.InputType;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Userinfo;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/7.
 * 修改资料
 */
public class SUserInfoActivity extends BaseActivity {

    String uid;
    String token;


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.shopnametv)
    TextView shopnametv;
    @BindView(R.id.shopname)
    RelativeLayout shopname;
    @BindView(R.id.linkmantv)
    TextView linkmantv;
    @BindView(R.id.linkman)
    RelativeLayout linkman;
    @BindView(R.id.userphonetv)
    TextView userphonetv;
    @BindView(R.id.userphone)
    RelativeLayout userphone;
    @BindView(R.id.homephonetv)
    TextView homephonetv;
    @BindView(R.id.homephone)
    RelativeLayout homephone;
    @BindView(R.id.emailtv)
    TextView emailtv;
    @BindView(R.id.email)
    RelativeLayout email;
    @BindView(R.id.update)
    Button update;

    Userinfo userinfo;
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_userinfo;
    }

    @Override
    protected void initData() {
        title.setText("个人信息");
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        report();
    }



    @OnClick(R.id.shopname)
    public void clickshopname() {
        showDialogToast("请输入商家名称", "请输入商家名称", shopnametv);
    }

    @OnClick(R.id.linkman)
    public void clicklinkman() {
        showDialogToast("请输入联系人姓名", "请输入联系人姓名", linkmantv);
    }

    @OnClick(R.id.userphone)
    public void clickuserphone() {
        showDialogToast("请输入手机号码", "请输入手机号码", userphonetv);
    }

    @OnClick(R.id.homephone)
    public void clickhomephone() {
        showDialogToast("请输入座机号码", "请输入座机号码", homephonetv);
    }

    @OnClick(R.id.email)
    public void clickemail() {
        showDialogToast("请输入邮箱", "请输入邮箱", emailtv);
    }


    @OnClick(R.id.update)
    public void clickupdate() {
        save();
    }


    /************
     * 弹出框
     ********************/

    public void showDialogToast(String title, String input, final TextView tv) {

        new MaterialDialog.Builder(this)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("确定")
                .negativeText("取消")
//                .title(title)
                .input(input, tv.getText().toString(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        tv.setText(input.toString());
                    }
                }).show();
    }
    /******************************************/

    /***********************
     * 网络数据操作
     *******************/

    public void report() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);
        progressDialog.show();

        HttpHelper.getInstance().post(mContext, Contants.PortU.GETUSERINFO, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {

                    JsonHelper<Userinfo> jsonHelper = new JsonHelper<Userinfo>(Userinfo.class);
                    userinfo  = jsonHelper.getData(json,null);
                    shopnametv.setText(userinfo.getShopname());
                    linkmantv.setText(userinfo.getContacts());
                    userphonetv.setText(userinfo.getMobile());
                    homephonetv.setText(userinfo.getTel());
                    emailtv.setText(userinfo.getEmail());
                } else {
                    showToastShort(Contants.NetStatus.NETLOADERROR);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }


    public void save() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("shopname", shopnametv.getText().toString());
        params.put("contacts", linkmantv.getText().toString());
        params.put("mobile", userphonetv.getText().toString());
        params.put("tel", homephonetv.getText().toString());
        params.put("email", emailtv.getText().toString());


        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
        progressDialog.show();

        HttpHelper.getInstance().post(mContext, Contants.PortU.DoUserinfo, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort(Contants.NetStatus.NETSUCCESS);
                } else {
                    showToastShort(Contants.NetStatus.NETERROR);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }

    public boolean check(){
//        params.put("shopname", shopnametv.getText().toString());
//        params.put("contacts", linkmantv.getText().toString());
//        params.put("mobile", userphonetv.getText().toString());
//        params.put("tel", homephonetv.getText().toString());
//        params.put("email", emailtv.getText().toString());
        if(StringHelper.isEmpty(shopnametv.getText().toString())){
            showToastShort("请填写商家名称");
            return false;
        }
//        if(StringHelper.isEmpty(linkmantv.getText().toString())){
//            showToastShort("请填写联系人");
//            return false;
//        }
        if(StringHelper.isEmpty(userphonetv.getText().toString())){
            showToastShort("请填写手机号码");
            return false;
        }
//        if(StringHelper.isEmpty(homephonetv.getText().toString())){
//            showToastShort("请填写座机号码");
//            return false;
//        }
//        if(StringHelper.isEmpty(emailtv.getText().toString())){
//            showToastShort("请填写邮箱");
//            return false;
//        }

        return true;
    }



}
