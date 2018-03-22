package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mining.app.zxing.MipcaActivityCapture;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.GoodAddress;
import com.yj.shopapp.ubeen.UserGroup;
import com.yj.shopapp.ui.activity.ChooseActivity;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.ImgUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Itemtype;
import com.yj.shopapp.wbeen.Itemunit;
import com.yj.shopapp.wbeen.WGoodsAdd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/26.
 */
public class WGoodsAddActivity extends BaseActivity {
    @BindView(R.id.goodExplain_Tx)
    TextView goodExplain_Tx;
    @BindView(R.id.goodsode_Tx)
    TextView goodsode_Tx;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.min_goodinventoryTx)
    TextView min_goodinventoryTx;
    @BindView(R.id.max_goodinventoryTx)
    TextView max_goodinventoryTx;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.goodsclassify_Tx)
    TextView goodsclassifyTx;
    @BindView(R.id.goodsclassifyRL)
    RelativeLayout goodsclassifyRL;
    @BindView(R.id.goodsnane_Tx)
    TextView goodsnaneTx;
    @BindView(R.id.goodsnameRL)
    RelativeLayout goodsnameRL;
    @BindView(R.id.goodsbar_Tx)
    TextView goodsbarTx;
    @BindView(R.id.goodsbarRL)
    RelativeLayout goodsbarRL;
    @BindView(R.id.goodsunit_Tx)
    TextView goodsunitTx;
    @BindView(R.id.goodsunitRL)
    RelativeLayout goodsunitRL;
    @BindView(R.id.goodsnorms_Tx)
    TextView goodsnormsTx;
    @BindView(R.id.goodsnormsRL)
    RelativeLayout goodsnormsRL;

    @BindView(R.id.goodsdetailRL)
    RelativeLayout goodsdetailRL;
    @BindView(R.id.submit)
    CardView submit;
    @BindView(R.id.goodspriceTx)
    TextView goodspriceTx;
    @BindView(R.id.goodinventoryTx)
    TextView goodinventoryTx;
    @BindView(R.id.changeLy)
    LinearLayout changeLy;
    @BindView(R.id.goodsdetaail_Tx)
    EditText goodsdetaailTx;
    @BindView(R.id.chengbenpriceTx)
    TextView chengbenpriceTx;
    @BindView(R.id.goodsupplierRL)
    RelativeLayout goodsupplierRL;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.goodscodeRL)
    RelativeLayout goodscodeRL;
    @BindView(R.id.goodAddress_Tx)
    TextView goodAddressTx;
    @BindView(R.id.stopsnameTv)
    TextView stopsnameTv;
    @BindView(R.id.choose1_re)
    RelativeLayout choose1Re;
    @BindView(R.id.goodscbrand_tv)
    TextView goodscbrandTv;
    @BindView(R.id.item_min_num)
    TextView itemMinNum;
    @BindView(R.id.item_max_num)
    TextView itemMaxNum;
    @BindView(R.id.stopitemsum)
    TextView stopitemsum;
    private MaterialEditText elementScale;
    @BindView(R.id.choose)
    ImageView choose;
    @BindView(R.id.choose1)
    ImageView choose1;
    private String agencyname;
    private String agencyId;
    @BindView(R.id.goodsupplier_Tx)
    TextView goodsupplier_Tx;
    int isstopsels = 1;//如果选中=1不选中=0
    boolean isshow = false;

    String uid;
    String token;
    GoodAddress.ChildrenBean bean;
    String chooseimgid;
    String chooseUrl;

    String Typeid;
    String Unitid;
    String Imgid;
    String Itemnoid;
    int ischooseNewGood = 0;//如果选中==1不选中==0

    List<Itemunit> itemunitList = new ArrayList<>();
    List<Itemtype> itemtypeList = new ArrayList<>(); //商品分类
    List<UserGroup> userGroupList = new ArrayList<>();
    List<String> priceArray = new ArrayList<>();

    KProgressHUD progressDialog = null;
    private String brandId;
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_goodsdetail;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(mContext);
        title.setText("添加商品");
        goodsdetailRL.setFocusable(true);
        goodsdetaailTx.setHint("请输入商品介绍");
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        isshow = getIntent().getExtras().getBoolean("isshow");
        Itemnoid = getIntent().getExtras().getString("itemnoid");
        goodsbarTx.setText(Itemnoid);
        progressDialog = growProgress(Contants.Progress.LOAD_ING);

        reportUnits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getChildrenBean(GoodAddress.ChildrenBean bean) {
        this.bean = bean;
        goodAddressTx.setText(bean.getName());
    }

    /**
     * 加载单位
     */
    public void reportUnits() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMUNIT, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                reportType();
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

                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Itemunit> jsonHelper = new JsonHelper<Itemunit>(Itemunit.class);
                    itemunitList = jsonHelper.getDatas(json);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    public void getGoodsInfo(final String Ation, final String noid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemnoid", noid);

        HttpHelper.getInstance().post(mContext, Contants.PortA.GetItemStr, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
//                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (Ation.equals("getGoodsInfo")) {
                        JsonHelper<WGoodsAdd> jsonHelper = new JsonHelper<WGoodsAdd>(WGoodsAdd.class);
                        WGoodsAdd wGoodsAdd = jsonHelper.getData(json, null);
                        goodsdetaailTx.setText(wGoodsAdd.getBrochure().replace("<br/>", ""));
                        goodsnaneTx.setText(wGoodsAdd.getName());
                        goodsbarTx.setText(wGoodsAdd.getItemnoid());
                        goodsnormsTx.setText(wGoodsAdd.getSpecs());
                        goodsdetaailTx.setText(wGoodsAdd.getBrochure());

                        Uri imageUri = Uri.parse(wGoodsAdd.getImgurl());
                        //开始下载
                        simpleDraweeView.setImageURI(imageUri);

                        Imgid = wGoodsAdd.getImgid();
                    } else if (Ation.equals("goodsIsExist")) {
                        Toast.makeText(WGoodsAddActivity.this, "商品已经存在了！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Ation.equals("goodsIsExist")) {
                        goodsbarTx.setText(noid);
                    }

                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    /**
     * 分类
     */
    public void reportType() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);


        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMTYPE, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
                //loadIClient();
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

                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
                    itemtypeList = jsonHelper.getDatas(json);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.ITEMTYPE, json);

                } else {
                    if (!StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {
                        json = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
                        JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
                        itemtypeList = jsonHelper.getDatas(json);
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (!StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {
                    String sjson = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
                    itemtypeList = jsonHelper.getDatas(sjson);
                }
            }
        });
    }

    public void submitGoodsInfo() {

        String pricestr = "";
        for (int i = 0; i < userGroupList.size(); i++) {
            if (priceArray.size() != userGroupList.size() + 2) {
                pricestr = pricestr + "0|";
            } else {
                if (StringHelper.isEmpty(priceArray.get(i + 2).trim())) {
                    pricestr = pricestr + "0|";
                } else {
                    pricestr = pricestr + priceArray.get(i + 2).trim() + "|";
                }
            }
        }
        if (!pricestr.equals("")) {
            pricestr = pricestr.substring(0, pricestr.length() - 1);
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", "");
        params.put("imgid", StringHelper.isEmpty(Imgid) ? "" : Imgid);
        params.put("sprice", goodspriceTx.getText().toString().trim());
        params.put("costprice", chengbenpriceTx.getText().toString().trim().replace(" ", ""));
        params.put("pricestr", pricestr);
        params.put("astock", goodinventoryTx.getText().toString().trim());
        params.put("maxitemsum", max_goodinventoryTx.getText().toString()); //最大库存
        params.put("minitemsum", min_goodinventoryTx.getText().toString()); //最小库存
        // params.put("typeid", Typeid);
        params.put("typename", goodsclassifyTx.getText().toString().trim());
        params.put("name", goodsnaneTx.getText().toString().trim());
        params.put("itemnoid", goodsbarTx.getText().toString().trim());
        // params.put("unitid", Unitid);
        params.put("unitname", goodsunitTx.getText().toString().trim());
        params.put("specs", goodsnormsTx.getText().toString().trim());
        params.put("brochure", goodsdetaailTx.getText().toString().trim());
        params.put("is_new", ischooseNewGood + "");
        params.put("customnumber", goodsode_Tx.getText().toString().trim());
        params.put("brand", brandId);
        params.put("sale_status", isstopsels + "");
        params.put("brochure", goodsdetaailTx.getText().toString().trim());
        params.put("specialnote", goodExplain_Tx.getText().toString().trim());
        params.put("supplierid", agencyId);
        params.put("localhost", bean != null ? bean.getId() : "");//商品地址id

        HttpHelper.getInstance().post(mContext, Contants.PortA.SAVEITEM, params, new OkHttpResponseHandler<String>(mContext) {

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
                    if (!isshow) {
                        showToastShort("提交成功");
                        Bundle bundle = new Bundle();
                        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.W_type);
                        CommonUtils.goActivityForResult(mContext, MipcaActivityCapture.class, bundle, 0, false);
                    } else {
                        showToastShort("提交成功");
                        finish();
                    }
//                    new MaterialDialog.Builder(mContext)
//                            .content("添加商品成功")
//                            .positiveText("确定")
////                            .negativeText("取消")
//                            .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                                    finish();
//                                }
//                            })
//                            .show();
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


    public void submitGoodsImg() {
        if (!StringHelper.isEmpty(Imgid) || StringHelper.isEmpty(chooseUrl)) {
            submitGoodsInfo();
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("imgbase", ImgUtils.getCompressImage(chooseUrl, 300, 300));
        params.put("numberid", goodsbarTx.getText().toString().trim());

        HttpHelper.getInstance().post(mContext, Contants.PortA.UPIMG, params, new OkHttpResponseHandler<String>(mContext) {

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
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        Imgid = jsonObject.getString("imgid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    submitGoodsInfo();
                } else {
                    showToastShort(Contants.NetStatus.NETERROR);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                progressDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.choose_re)
    public void chooseNewGood() {
        if (ischooseNewGood == 0) {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
            ischooseNewGood = 1;

        } else {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
            ischooseNewGood = 0;

        }
    }

    @OnClick(R.id.choose1_re)
    public void stopsals() {
        if (isstopsels == 1) {
            choose1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
            isstopsels = 0;

        } else {
            choose1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
            isstopsels = 1;

        }
    }

    @OnClick(R.id.goodsupplierRL)
    public void getAgency() {
        Bundle bundle = new Bundle();
        bundle.putString("choosetype", "0");
        CommonUtils.goActivityForResult(mContext, WAgencyActivity.class, bundle, 10012, false);
    }

    @OnClick(R.id.goodsExplainRL)
    public void goodsExplainRL() {
        showDialogToastM("商品特别说明", "输入说明", goodExplain_Tx);
    }

    @OnClick(R.id.changeLy)
    public void onclichengbenpriceLay() {//修改成本价
//        showDialogToastM("成本价", "输入成本价", chengbenpriceTx);
        Bundle bundle = new Bundle();
        bundle.putString("costPrice", chengbenpriceTx.getText().toString()); //成本价
        bundle.putString("wholesalePrice", goodspriceTx.getText().toString());//批发价
        bundle.putString("inventory", goodinventoryTx.getText().toString()); //库存
        bundle.putString("max", max_goodinventoryTx.getText().toString()); //最大库存
        bundle.putString("min", min_goodinventoryTx.getText().toString()); //最小库存
        bundle.putString("minnum", itemMinNum.getText().toString());
        bundle.putString("maxnum", itemMaxNum.getText().toString());
        bundle.putString("stopnum", stopitemsum.getText().toString());
        CommonUtils.goActivityForResult(mContext, WPriceEditActivity.class, bundle, WPriceEditActivity.EDIT_CODE, false);
    }

    @OnClick(R.id.goodspriceTx)
    public void onClickSpriceLay() { //修改批发价
        setDialogInputSprice(goodspriceTx);
    }

    @OnClick(R.id.goodinventoryTx)
    public void onClickInventoryLay() { //修改库存
        setDialogInput(goodinventoryTx);
    }

    @OnClick(R.id.simpleDraweeView)
    public void onClickChoose() {

        Bundle bundle = new Bundle();
        bundle.putString("GoodsNumber", goodsbarTx.getText().toString());
        CommonUtils.goActivityForResult(mContext, ChooseActivity.class, bundle, 0, false);
    }

    @OnClick(R.id.goodscodeRL)
    public void goodscodeRL() {
        showDialogToastM("请输入货号编码", "输入货号编码", goodsode_Tx);
    }

    @OnClick(R.id.goodsunitRL)
    public void onClockshowUnit() {
        String goodstr = goodsunitTx.getText().toString();

        String[] selectItemArr = new String[itemunitList.size()];

        int goodsNum = -1;
        int i = 0;
        for (Itemunit itemunit : itemunitList) {
            selectItemArr[i] = itemunit.getName();
            if (goodstr.equals(itemunit.getName())) {
                goodsNum = i;
            }
            i++;
        }
        showDialogList("请选择商品单位", selectItemArr, goodsNum, goodsunitTx);
    }

    @OnClick(R.id.goodsclassifyRL)
    public void showGoodsClassIfy() {
        mItemtype = null;
        showChooseify("请选择分类", "0");
    }

    @OnClick(R.id.goodsnameRL)
    public void showName() {
        showDialogToast("请输入商品名称", "请输入商品名称", goodsnaneTx);
    }

    @OnClick(R.id.goodsbarRL)
    public void showBar() {
        showDialogToast2("请输入商品条码", "请输入商品条码", goodsbarTx);
    }

    @OnClick(R.id.goodsnormsRL)
    public void showNorm() {
        showDialogToast("请输入商品规格", "请输入商品规格", goodsnormsTx);
    }

    @OnClick(R.id.submit)
    public void submit() {

        if (!check()) {
            return;
        }
        if (NetUtils.isNetworkConnected(mContext)) {
            progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
            progressDialog.show();
            submitGoodsImg();
        } else {
            showToastShort("网络不给力");
        }
    }

    public void showDialogToastM(String title, String input, final TextView tv) {

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

    public void showDialogToast2(String title, String input, final TextView tv) {

        new MaterialDialog.Builder(this)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("确定")
                .negativeText("扫描")
//                .title(title)
                .input(input, tv.getText().toString(), true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input == null) {
                            Toast.makeText(WGoodsAddActivity.this, "条码不能为空！", Toast.LENGTH_SHORT).show();
                        } else {
                            getGoodsInfo("goodsIsExist", input.toString());
                            Log.e("m_tag", "2");
                        }
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Bundle bundle = new Bundle();
                bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.original_type);
                CommonUtils.goActivityForResult(mContext, MipcaActivityCapture.class, bundle, 0, false);
            }
        })
                .show();
    }

    public void showDialogList(final String title, final String[] selectItemArr, final int goodsNum, final TextView tv) {

        if (itemunitList.size() == 0) {
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(title);
        builder.items(selectItemArr);
        builder.itemsCallbackSingleChoice(goodsNum, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                tv.setText(text == null ? "" : text.toString());
                Unitid = itemunitList.get(which).getId();
                return true; // allow selection
            }
        });
        if (title.equals("请选择商品单位")) {

            builder.neutralText("手动输入");
            builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    new MaterialDialog.Builder(mContext)


                            .input("输入单位", null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                                    if (input != null) {
                                        tv.setText(input.toString());
                                    }
                                }
                            })
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // goodsclassifyTx.setText(inpustr);
                                }
                            })
                            .show();

                }
            });
        }
        builder.positiveText("确定");
        builder.negativeText("取消");
        builder.autoDismiss(true);
        builder.show();
    }

    MaterialEditText[] materialEditTextsArray;

    public void setDialogInputSprice(final TextView t1) {
        LinearLayout linearLayout;

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_input3, true)
                .positiveText(R.string.right)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        t1.setText(materialEditTextsArray[0].getText().toString().trim());
                        if (materialEditTextsArray != null) {
                            priceArray.clear();
                            for (int i = 0; i < userGroupList.size() + 1; i++) {
                                priceArray.add(materialEditTextsArray[i].getText().toString().trim());
                                dialog.cancel();
                            }
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                        materialDialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .build();


        linearLayout = (LinearLayout) dialog.getCustomView().findViewById(R.id.linearLayout);

        materialEditTextsArray = new MaterialEditText[userGroupList.size() + 1];
        for (int i = 0; i < userGroupList.size() + 1; i++) {

            View view = getLayoutInflater().inflate(R.layout.dialog_input0, null);
            materialEditTextsArray[i] = (MaterialEditText) view.findViewById(R.id.dialog_element_et);
            if (i == 0) {
                materialEditTextsArray[i].setFloatingLabelText("批发价");
                materialEditTextsArray[i].setHint("请输入批发价");
            } else {
                materialEditTextsArray[i].setFloatingLabelText(userGroupList.get(i - 1).getName());
                materialEditTextsArray[i].setHint("请输入" + userGroupList.get(i - 1).getName() + "组批发价");
            }
            materialEditTextsArray[i].setText(priceArray.size() <= i ? "" : priceArray.get(i).toString());
            linearLayout.addView(view);
        }

        if (materialEditTextsArray != null && materialEditTextsArray.length > 0) {
            materialEditTextsArray[0].setText(t1.getText().toString().trim());
        }

        materialEditTextsArray[0].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    String str = materialEditTextsArray[0].getText().toString();
                    if (StringHelper.isEmpty(str)) {
                        for (MaterialEditText materialEditText : materialEditTextsArray) {
                            materialEditText.setText("0");
                        }
                    } else {
                        try {
                            float price = Float.valueOf(str);
                            int i = 0;
                            for (MaterialEditText materialEditText : materialEditTextsArray) {
                                if (i != 0) {
                                    materialEditText.setText(String.valueOf(price * Float.valueOf(userGroupList.get(i - 1).getDis()) / 100));
                                }
                                i++;
                            }
                        } catch (Exception e) {
                            int i = 0;
                            for (MaterialEditText materialEditText : materialEditTextsArray) {
                                if (i != 0) {
                                    materialEditText.setText("0");
                                }
                                i++;
                            }
                        }
                    }
                }
            }
        });


        dialog.show();
    }

    public void setDialogInput(final TextView t1) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_input2, true)
                .positiveText(R.string.right)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String elementScaleValue = elementScale.getText().toString();

                        if (StringHelper.isEmpty(elementScaleValue)) {
                            showToastShort("库存不能为空");
                            return;
                        }

                        t1.setText(elementScaleValue);
                        dialog.dismiss();

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                        materialDialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .build();


        elementScale = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_element_et);
        elementScale.setText(t1.getText().toString());
        System.out.println("==========" + t1.getText().toString());
        dialog.show();
    }

    /**************************
     * 弹出框
     *****************/

    Itemtype mItemtype;

    public void showChooseify(String title, String id) {
        final List<Itemtype> list = new ArrayList<>();
        Itemtype item = new Itemtype();
        for (Itemtype itemtype : itemtypeList) {
            if (itemtype.getPid().equals(id)) {
                list.add(itemtype);
            }
            if (itemtype.getId().equals(id)) {
                item = itemtype;
            }
        }
        if (list.size() == 0) {
            Typeid = item.getId();
            goodsclassifyTx.setText(item.getName());
            return;
        }
        final String[] array;
        if (mItemtype == null) {
            array = new String[list.size()];
            int i = 0;
            for (Itemtype itemtype : list) {
                array[i] = itemtype.getName();
                i++;
            }
        } else {
            array = new String[list.size() + 1];
            int i = 1;
            array[0] = mItemtype.getName();
            for (Itemtype itemtype : list) {
                array[i] = itemtype.getName();
                i++;
            }
        }

        new MaterialDialog.Builder(mContext)
                .title(title)
                .items(array)
                .negativeText("取消")
                .neutralText("手动输入")

                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        new MaterialDialog.Builder(mContext)


                                .input("输入类别", null, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                                        if (input != null) {
                                            goodsclassifyTx.setText(input.toString());
                                        }
                                    }
                                })
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        // goodsclassifyTx.setText(inpustr);
                                    }
                                })
                                .show();
                    }
                })
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (mItemtype != null && mItemtype.getName().equals(array[which])) {
                            Typeid = mItemtype.getId();
                            goodsclassifyTx.setText(mItemtype.getName());
                        } else {
                            if (mItemtype == null) {
                                mItemtype = list.get(which);
                                showChooseify(text == null ? "" : text.toString(), list.get(which).getId());
                            } else {
                                mItemtype = list.get(which - 1);
                                showChooseify(text == null ? "" : text.toString(), list.get(which - 1).getId());
                            }

                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ChooseActivity.CHOOSE_IMAGE_WHAT:
                Imgid = null;
                chooseimgid = data.getExtras().getString("imgid");
                chooseUrl = data.getExtras().getString("chooseUrl");
                if (StringHelper.isEmpty(chooseimgid)) {
//                    Uri imageUri = Uri.parse("file://" + chooseUrl);
//                    //开始下载
//                    simpleDraweeView.setImageURI(imageUri);

                    simpleDraweeView.setImageBitmap(ImgUtils.compressBitmap(chooseUrl, 300, 300));

                } else {
                    Imgid = chooseimgid;
                    Uri imageUri = Uri.parse(chooseUrl);
                    //开始下载
                    simpleDraweeView.setImageURI(imageUri);
                }
                break;
            case Contants.Photo.REQUEST_SCAN_CODE:
                System.out.println("============" + data.getExtras().getString("result"));

                getGoodsInfo("goodsIsExist", data.getExtras().getString("result"));
                Log.e("m_tag", "3");
                break;
            case WAgencyActivity.CHOOSEAGENT_TYPE_WHAT:
                agencyname = data.getExtras().getString("agentuName");
                agencyId = data.getExtras().getString("agentuid");
                goodsupplier_Tx.setText(agencyname);
                break;
            case WBrandActivity.goback:
                brangName = data.getExtras().getString("name");
                brandId = data.getExtras().getString("id");
                goodscbrandTv.setText(brangName);
                break;
            case WPriceEditActivity.BACK_TO:
                chengbenpriceTx.setText(data.getExtras().getString("costPrice"));
                goodspriceTx.setText(data.getExtras().getString("wholesalePrice"));
                goodinventoryTx.setText(data.getExtras().getString("inventory"));
                max_goodinventoryTx.setText(data.getExtras().getString("max"));
                min_goodinventoryTx.setText(data.getExtras().getString("min"));
                itemMinNum.setText(data.getStringExtra("minnum"));
                itemMaxNum.setText(data.getStringExtra("maxnum"));
                stopitemsum.setText(data.getStringExtra("stopnum"));
                break;
            default:
                break;
        }
    }


    public void saveSa1les(String saveid, String stype, String itemid, String starttime, String stoptime
            , String disstr, String gift) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("saveid", saveid);
        params.put("stype", stype);
        params.put("itemid", itemid);
        params.put("starttime", starttime);
        params.put("stoptime", stoptime);
        params.put("disstr", disstr);
        params.put("gift", gift);

        //显示ProgressDialog

        progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
        progressDialog.show();

        HttpHelper.getInstance().post(mContext, Contants.PortA.SAVESP, params, new OkHttpResponseHandler<String>(mContext) {

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

    /**
     * 用户组
     */
    public void loadIClient() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        //显示ProgressDialog

        HttpHelper.getInstance().post(mContext, Contants.PortA.UserGroup, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();

                if (getIntent().getExtras().getInt("type") == 1) {
                    getGoodsInfo("getGoodsInfo", Itemnoid);
                } else {
                    progressDialog.dismiss();
                }
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
                    JsonHelper<UserGroup> jsonHelper = new JsonHelper<UserGroup>(UserGroup.class);
                    userGroupList = jsonHelper.getDatas(json);
                    priceArray.clear();
                } else if (JsonHelper.getRequstOK(json) == 6) {

                } else {

                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    public boolean check() {
        if (StringHelper.isEmpty(chengbenpriceTx.getText().toString().trim())) {
            showToastShort("请填写成本价");
            return false;
        }
        if (StringHelper.isEmpty(goodspriceTx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("请填写批发价");
            return false;
        }
        if (StringHelper.isEmpty(Typeid) && StringHelper.isEmpty(goodsclassifyTx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("请选择商品类型");
            return false;
        }
        if (StringHelper.isEmpty(goodsnaneTx.getText().toString().trim())) {
            showToastShort("请填写商品名");
            return false;
        }
        if (StringHelper.isEmpty(Unitid) && StringHelper.isEmpty(goodsunitTx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("请选择商品单位");
            return false;
        }
        if (StringHelper.isEmpty(Unitid) && StringHelper.isEmpty(goodsbarTx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("请商品条码");
            return false;
        }
        if (StringHelper.isEmpty(goodsbarTx.getText().toString().trim().replace(" ", "")) && StringHelper.isEmpty(goodsode_Tx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("商品条码和货品编码必须填一个！");
            return false;
        }
        return true;

    }





    @OnClick(R.id.goodsAddressRL)
    public void onViewClicked() {
        CommonUtils.goActivity(mContext, GoodSheives.class, null, false);
    }

    private void showAddress(List<GoodAddress> goodAddresses) {
        final List<GoodAddress> address = goodAddresses;
        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
        materialDialog.title("选择商品地址");
        for (GoodAddress array : address) {
            materialDialog.items(array.getName());
        }
        materialDialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                //addressID = address.get(which).getId();
                goodAddressTx.setText(text);
                dialog.dismiss();
            }
        });
        materialDialog.show();
    }

    @OnClick(R.id.goodsbrand)
    public void onClick() {
        CommonUtils.goActivityForResult(mContext, WBrandActivity.class, new Bundle(), 10014, false);
    }

    private String brangName;

}
