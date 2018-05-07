package com.yj.shopapp.ui.activity.wholesale;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.ImgUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PhotoUtil;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Industrys;
import com.yj.shopapp.wbeen.UserInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/7.
 * 修改资料
 */
public class WUserInfoActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.info_shop_img)
    RelativeLayout infoShopImg;
    @BindView(R.id.shopnametv)
    TextView shopnametv;
    @BindView(R.id.shopname)
    RelativeLayout shopname;
    @BindView(R.id.industrystv)
    TextView industrystv;
    @BindView(R.id.industrys)
    RelativeLayout industrysRel;
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
//    @BindView(R.id.addresstv)
//    TextView addresstv;
//    @BindView(R.id.address)
//    RelativeLayout address;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.add_address_detail_edt)
    EditText add_address_detail_edt;


    String cameraPath;
    String uid;
    String token;

    UserInfo userInfo;
    List<Industrys> industrysList;
    @BindView(R.id.refresh_btn)
    ImageView refreshBtn;

    @OnClick(R.id.refresh_btn)
    public void OnclickRefresh(){
        BprogressDialog.show();
    }

    //显示ProgressDialog
    KProgressHUD BprogressDialog;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("cameraPath", cameraPath);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_userinfo;
    }

    @Override
    protected void initData() {
        title.setText("个人信息");
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }
        BprogressDialog = growProgress(Contants.Progress.BAIDU_ING);
        getIndustrys(false);
        report();
    }

    @OnClick(R.id.info_shop_img)
    public void onClickshopimg() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}
                    , 1);
        } else {
            showDialogPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            showDialogPhoto();
        }else {
            showToastShort("您未获取手机权限，请点击重试");
        }
    }
    @OnClick(R.id.industrys)
    public void clickindustrys() {

        if (industrysList.size() == 0) {
            getIndustrys(true);
            return;
        }
        String instr = industrystv.getText().toString();

        String[] selectItemArr = new String[industrysList.size()];

        int inNum = -1;
        int i = 0;
        for (Industrys ind : industrysList) {
            selectItemArr[i] = ind.getName();
            if (instr.equals(ind.getName())) {
                inNum = i;
            }
            i++;
        }
        showDialogList("请选择行业", selectItemArr, inNum, industrystv);
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

//    @OnClick(R.id.editaddres_txt)
//    public void clickaddress() {
//        showDialogToast("请输入地址", "请输入地址", addresstv);
//    }

    @OnClick(R.id.update)
    public void clickupdate() {
        if (!check()) {
            return;
        }
        save();
    }


    /************
     * 弹出框
     ********************/

    public void showDialogPhoto() {

        final String[] selectItemArr = new String[]{"拍照", "相册"};

//        new MaterialDialog.Builder(this)
//                .items(selectItemArr)
//                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
//                    @Override
//                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//
//                        if (which == 0) {//拍照
//
//                            File cameraFile = PhotoUtil.camera(WUserInfoActivity.this);
//                            if (cameraFile != null) {
//                                cameraPath = cameraFile.getAbsolutePath();
//                            }
//
//                        } else if (which == 1) {//本地相册
//                            PhotoUtil.OpenFinder(WUserInfoActivity.this);
//                        }
//                        return true; // allow selection
//                    }
//                })
//                .positiveText("确定")
//                .autoDismiss(true)
//                .show();

        new MaterialDialog.Builder(this)
                .title("选择图片")
                .negativeText("取消")
                .items(selectItemArr)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {//拍照
                            File cameraFile = PhotoUtil.camera(WUserInfoActivity.this);
                            if (cameraFile != null) {
                                cameraPath = cameraFile.getAbsolutePath();
                            }

                        } else if (which == 1) {//本地相册
                            PhotoUtil.OpenFinder(WUserInfoActivity.this);
                        }
                    }
                })
                .show();
    }


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

    public void showDialogList(final String title, final String[] selectItemArr, final int goodsNum, final TextView tv) {

        if (industrysList.size() == 0) {
            return;
        }

        new MaterialDialog.Builder(this)
                .title(title)
                .items(selectItemArr)
                .itemsCallbackSingleChoice(goodsNum, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tv.setText(text == null ? "" : text.toString());
                        for (Industrys ind : industrysList) {
                            if ((text == null ? "" : text.toString()).equals(ind.getName())) {
                                userInfo.setIndname(ind.getName());
                                userInfo.setIndustryid(ind.getId());
                            }
                        }

                        return true; // allow selection
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .autoDismiss(true)
                .show();
    }

    /******************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Contants.Photo.REQUEST_PHOTO_CODE
                && resultCode == Activity.RESULT_OK) {
            //调用系统相机成功
            submitShopImg();

        } else if (requestCode == Contants.Photo.REQUEST_FILE_CODE
                && resultCode == Activity.RESULT_OK) {
            //调用系统本地相册成功
            cameraPath = PhotoUtil.getPhotoPath(this, data);
            submitShopImg();
        }
    }

    /***********************
     * 网络数据操作
     *******************/

    public void report() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);
        progressDialog.show();

        HttpHelper.getInstance().post(mContext, Contants.PortA.GETUSERINFO, params, new OkHttpResponseHandler<String>(mContext) {

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

                    JsonHelper<UserInfo> jsonHelper = new JsonHelper<UserInfo>(UserInfo.class);
                    userInfo = jsonHelper.getData(json, null);
                    Uri imageUri = Uri.parse(userInfo.getShopimg());
                    simpleDraweeView.setImageURI(imageUri);
                    shopnametv.setText(userInfo.getShopname());
                    industrystv.setText(userInfo.getIndname());
                    linkmantv.setText(userInfo.getContacts());
                    userphonetv.setText(userInfo.getMobile());
                    homephonetv.setText(userInfo.getTel());
                    add_address_detail_edt.setText(userInfo.getAddress());
                    emailtv.setText(userInfo.getEmail());

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

    public void getIndustrys(final boolean isShow) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);
        HttpHelper.getInstance().post(mContext, Contants.PortA.GETINDUSTRYS, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
               // isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
               // isRequesting = true;
                if (isShow) {
                    progressDialog.show();
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {

                    JsonHelper<Industrys> jsonHelper = new JsonHelper<Industrys>(Industrys.class);
                    industrysList = jsonHelper.getDatas(json);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.INDUSTRYS, json);
                    if (isShow) {
                        clickindustrys();
                    }
                } else {
                    json = PreferenceUtils.getPrefString(mContext, Contants.Preference.INDUSTRYS, "");
                    if (JsonHelper.isRequstOK(json, mContext)) {
                        JsonHelper<Industrys> jsonHelper = new JsonHelper<Industrys>(Industrys.class);
                        industrysList = jsonHelper.getDatas(json);
                        if (isShow) {
                            clickindustrys();
                        }
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                String json = PreferenceUtils.getPrefString(mContext, Contants.Preference.INDUSTRYS, "");
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Industrys> jsonHelper = new JsonHelper<Industrys>(Industrys.class);
                    industrysList = jsonHelper.getDatas(json);
                    if (isShow) {
                        clickindustrys();
                    }
                }
            }
        });
    }


    public void submitShopImg() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("imgbase", ImgUtils.getCompressImage(cameraPath, 300, 300));

        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
        progressDialog.show();

        HttpHelper.getInstance().post(mContext, Contants.PortA.DOSHOPIMG, params, new OkHttpResponseHandler<String>(mContext) {

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
                System.out.println("手机相册保存路径" + cameraPath);
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("图片修改成功");
//                    Uri imageUri = Uri.parse("file:////" + cameraPath);
//                    simpleDraweeView.setImageURI(imageUri);
                    simpleDraweeView.setImageBitmap(ImgUtils.compressBitmap(cameraPath, 300, 300));

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

    public void save() {
        try {

            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", uid);
            params.put("token", token);
//            params.put("shopname", URLEncoder.encode(shopnametv.getText().toString(), "UTF-8"));
            params.put("shopname", shopnametv.getText().toString());
            params.put("industryid", userInfo.getIndustryid());
            params.put("contacts", linkmantv.getText().toString());
            params.put("mobile", userphonetv.getText().toString());
            params.put("tel", homephonetv.getText().toString());
            params.put("email", emailtv.getText().toString());
//            params.put("address", URLEncoder.encode(addresstv.getText().toString(), "UTF-8"));
            params.put("address", add_address_detail_edt.getText().toString());

            final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
            progressDialog.show();

            HttpHelper.getInstance().post(mContext, Contants.PortA.DOUSERINFO, params, new OkHttpResponseHandler<String>(mContext) {

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
                    System.out.println("response==");
                    System.out.println("response==" + json);
                    showToastLong(json);
                    System.out.println("response==什么问题");

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
        } catch (Exception e) {

        }
    }

    public boolean check() {

//        params.put("uid", uid);
//        params.put("token", token);
////            params.put("shopname", URLEncoder.encode(shopnametv.getText().toString(), "UTF-8"));
//        params.put("shopname", shopnametv.getText().toString());
//        params.put("industryid", userInfo.getIndustryid());
//        params.put("contacts", linkmantv.getText().toString());
//        params.put("mobile", userphonetv.getText().toString());
//        params.put("tel", homephonetv.getText().toString());
//        params.put("email", emailtv.getText().toString());
////            params.put("address", URLEncoder.encode(addresstv.getText().toString(), "UTF-8"));
//        params.put("address", addresstv.getText().toString()) ;

        if (StringHelper.isEmpty(shopnametv.getText().toString().trim())) {
            showToastShort("请填写商家名称");
            return false;
        }
        if (userInfo == null || StringHelper.isEmpty(userInfo.getIndustryid()) ||
                StringHelper.isEmpty(industrystv.getText().toString().trim())) {
            showToastShort("请选择行业类型");
            return false;
        }
//        if(StringHelper.isEmpty(linkmantv.getText().toString())){
//            showToastShort("请填写联系人");
//            return false;
//        }
        if (StringHelper.isEmpty(userphonetv.getText().toString())) {
            showToastShort("请填写手机号码");
            return false;
        }
//        if(StringHelper.isEmpty(homephonetv.getText().toString())){
//            showToastShort("请填写固定电话");
//            return false;
//        }
//        if(StringHelper.isEmpty(emailtv.getText().toString())){
//            showToastShort("请填写邮箱");
//            return false;
//        }
        if (StringHelper.isEmpty(add_address_detail_edt.getText().toString())) {
            showToastShort("请填商店地址");
            return false;
        }

        return true;
    }
}
