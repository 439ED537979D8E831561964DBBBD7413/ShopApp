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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.ImgUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Goods;
import com.yj.shopapp.wbeen.Itemtype;
import com.yj.shopapp.wbeen.Itemunit;
import com.yj.shopapp.wbeen.Lookitem;

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

import static com.yj.shopapp.R.id.goodAddress_Tx;

/**
 * Created by jm on 2016/4/26.
 */
public class WGoodsDetailActivity extends BaseActivity {
    @BindView(R.id.goodExplain_Tx)
    TextView goodExplain_Tx;
    @BindView(R.id.goodsode_Tx)
    TextView goodsode_Tx;
    @BindView(R.id.title)
    TextView title;
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
    @BindView(R.id.goodsdetaail_Tx)
    EditText goodsdetaailTx;
    @BindView(R.id.goodsdetailRL)
    RelativeLayout goodsdetailRL;
    @BindView(R.id.submit)
    CardView submit;
    Lookitem lookitem;
    @BindView(R.id.goodspriceTx)
    TextView goodspriceTx;
    @BindView(R.id.goodinventoryTx)
    TextView goodinventoryTx;
    @BindView(R.id.changeLy)
    LinearLayout changeLy;
    @BindView(R.id.chengbenpriceTx)
    TextView chengbenpriceTx;
    @BindView(R.id.min_goodinventoryTx)
    TextView min_goodinventoryTx;
    @BindView(R.id.max_goodinventoryTx)
    TextView max_goodinventoryTx;
    @BindView(R.id.goodsupplier_Tx)
    TextView goodsupplier_Tx;
    @BindView(R.id.goodsupplierRL)
    RelativeLayout goodsupplierRL;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.goodscodeRL)
    RelativeLayout goodscodeRL;
    @BindView(goodAddress_Tx)
    TextView goodAddressTx;
    @BindView(R.id.goodscbrand_tv)
    TextView goodscbrandTv;
    @BindView(R.id.item_min_num)
    TextView itemMinNum;
    @BindView(R.id.item_max_num)
    TextView itemMaxNum;
    @BindView(R.id.stopitemsum)
    TextView stopitemsum;
    private MaterialEditText elementScale;
    private MaterialEditText maxelementScale;
    private MaterialEditText minelementScale;
    @BindView(R.id.choose)
    ImageView choose;
    @BindView(R.id.choose1)
    ImageView choose1;
    private String agencyname;
    private String agencyId;
    public static final int TYPE = 10010;

    String itemnoid;
    String id;
    String uid;
    String token;

    String chooseimgid;
    String chooseUrl;
    String stock;
    String addressID;
    Uri imageUri;
    int ischooseNewGood = 0;//如果选中==1不选中==0
    int isstopsels = 0;//如果选中=1不选中=0
    List<Itemunit> itemunitList = new ArrayList<>();
    List<Itemtype> itemtypeList = new ArrayList<>(); //商品分类

    List<UserGroup> userGroupList = new ArrayList<>();
    List<String> priceArray = new ArrayList<>();

    KProgressHUD progressDialog = null;
    String saveid;
    List<GoodAddress> goodAddresses;
    String minnum = "";
    String maxnum = "";
    GoodAddress.ChildrenBean bean;
    private Goods mGoods = new Goods();

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_goodsdetail;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(mContext);
        title.setText("商品详情");
        idRightBtu.setText("加入促销");
        setResult(0);
        goodsdetailRL.setFocusable(true);
        itemnoid = getIntent().getExtras().getString("itemnoid");
        id = getIntent().getExtras().getString("id");
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        progressDialog = growProgress(Contants.Progress.LOAD_ING);
        reportUnits();
    }

    public void refreshRequest() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemnoid", itemnoid);
        params.put("id", id);

        HttpHelper.getInstance().post(mContext, Contants.PortA.LOOKITEM, params, new OkHttpResponseHandler<String>(mContext) {

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
                Log.e("m_tag", json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Lookitem> jsonHelper = new JsonHelper<Lookitem>(Lookitem.class);
                    lookitem = jsonHelper.getData(json, null);
                    chengbenpriceTx.setText(lookitem.getCostprice());
                    goodspriceTx.setText(lookitem.getSprice());
                    stock = lookitem.getStock();
                    goodinventoryTx.setText(stock);
                    goodscbrandTv.setText(lookitem.getBrandname());
                    goodsnaneTx.setText(lookitem.getName());
                    goodsclassifyTx.setText(lookitem.getTypename());
                    goodsbarTx.setText(lookitem.getItemnoid());
                    goodsunitTx.setText(lookitem.getUnit());
                    goodsnormsTx.setText(lookitem.getSpecs());
                    goodsupplier_Tx.setText(lookitem.getSuppilername());
                    goodsode_Tx.setText(lookitem.getCustomnumber());
                    goodExplain_Tx.setText(lookitem.getSpecialnote());
                    agencyId = lookitem.getSupplierid();
                    min_goodinventoryTx.setText(lookitem.getMinitemsum());
                    max_goodinventoryTx.setText(lookitem.getMaxitemsum());
                    itemMinNum.setText(lookitem.getMinnum());
                    itemMaxNum.setText(lookitem.getMaxnum());
                    stopitemsum.setText(lookitem.getStopitemsum());
                    goodAddressTx.setText(lookitem.getLocalhostname());
                    if (lookitem.getSale_status().equals("0")) {
                        choose1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
                        isstopsels = 0;
                    } else {
                        choose1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
                        isstopsels = 1;
                    }

                    if (Integer.parseInt(lookitem.getIs_new()) == 1) {
                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
                        ischooseNewGood = 1;
                    } else {
                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
                        ischooseNewGood = 0;
                    }
                    goodsdetaailTx.setText(lookitem.getBrochure().replace("<br/>", ""));

                    imageUri = Uri.parse(lookitem.getImgurl());
                    //开始下载
                    //获取网络的图片
                    //simpleDraweeView.setImageURI(imageUri);
                    Glide.with(mContext).load(imageUri).into(simpleDraweeView);

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

    public void reportUnits() {

        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);


        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMUNIT, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                reportType();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);

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

    public void reportType() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);


        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMTYPE, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                loadIClient();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);

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

    /**
     * yonghuzu
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
                refreshRequest();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<UserGroup> jsonHelper = new JsonHelper<UserGroup>(UserGroup.class);
                    userGroupList = jsonHelper.getDatas(json);
                    priceArray.clear();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }


    public void submitGoodsInfo() {

        String pricestr = "";
//        for(priceArray)
        for (int i = 0; i < userGroupList.size(); i++) {
            if (priceArray.size() != userGroupList.size() + 1) {
                pricestr = pricestr + "0|";
            } else {
                if (StringHelper.isEmpty(priceArray.get(i + 1).trim())) {
                    pricestr = pricestr + "0|";
                } else {
                    pricestr = pricestr + priceArray.get(i + 1).trim() + "|";
                }
            }
        }

        if (!pricestr.equals("")) {
            pricestr = pricestr.substring(0, pricestr.length() - 1);
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", lookitem.getId());
        params.put("pricestr", pricestr);
        params.put("imgid", StringHelper.isEmpty(chooseimgid) ? (StringHelper.isEmpty(lookitem.getImgid()) ? "" : lookitem.getImgid()) : chooseimgid);
        params.put("sprice", goodspriceTx.getText().toString().trim());
        params.put("stock", stock); //原库存
        params.put("astock", goodinventoryTx.getText().toString().trim());//修改库存
        params.put("maxitemsum", max_goodinventoryTx.getText().toString().trim().replace(" ", ""));
        params.put("minitemsum", min_goodinventoryTx.getText().toString().trim().replace(" ", ""));
        // params.put("typeid", StringHelper.isEmpty(lookitem.getTypeid()) ? "" : lookitem.getTypeid());
        params.put("costprice", chengbenpriceTx.getText().toString().trim().replace(" ", ""));
        params.put("typename", goodsclassifyTx.getText().toString().trim());
        params.put("name", goodsnaneTx.getText().toString().trim());
        params.put("itemnoid", goodsbarTx.getText().toString().trim());
        params.put("localhost", bean != null ? bean.getId() : "");//商品地址id
//        params.put("unitid", StringHelper.isEmpty(lookitem.getUnitid()) ? "" : lookitem.getUnitid());
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
        params.put("minnum", itemMinNum.getText().toString().trim());
        params.put("maxnum", itemMaxNum.getText().toString().trim());
        params.put("stopitemsum", stopitemsum.getText().toString());
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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("提交成功");
                    mGoods.setPrice(goodspriceTx.getText().toString().trim());
                    mGoods.setItemsum(goodinventoryTx.getText().toString().trim());
                    mGoods.setName(goodsnaneTx.getText().toString().trim());
                    EventBus.getDefault().post(mGoods);
                    finish();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getChildrenBean(GoodAddress.ChildrenBean bean) {
        this.bean = bean;
        goodAddressTx.setText(bean.getName());
    }

    public void submitGoodsImg() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("imgbase", ImgUtils.getCompressImage(chooseUrl, 300, 300));
        params.put("numberid", lookitem.getItemnoid());

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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        mGoods.setImgurl(jsonObject.getString("imgurl"));
                        lookitem.setImgid(jsonObject.getString("imgid"));
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

//    @OnClick(R.id.changeLy)
//    public void AlterCardInput(){
//        setDialogInput(goodspriceTx,goodinventoryTx);
//    }

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

    @OnClick(R.id.goodspriceTx)
    public void onClickSpriceLay() { //修改批发价
//        setDialogInput(goodspriceTx, goodinventoryTx);
        setDialogInputSprice(goodspriceTx);
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
        Log.d("m_tag", minnum + maxnum);
        CommonUtils.goActivityForResult(mContext, WPriceEditActivity.class, bundle, WPriceEditActivity.EDIT_CODE, false);
    }

    @OnClick(R.id.goodinventoryTx)
    public void onClickInventoryLay() { //修改库存
//        setDialogInput(goodspriceTx, goodinventoryTx);
        setDialogInput(goodinventoryTx);
    }

    @OnClick(R.id.goodsAddressRL)
    public void onViewClicked() {
        CommonUtils.goActivity(mContext, GoodSheives.class, null, false);

    }

    @OnClick(R.id.simpleDraweeView)
    public void choose() {
        if (lookitem == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("GoodsNumber", lookitem.getItemnoid());
        CommonUtils.goActivityForResult(mContext, ChooseActivity.class, bundle, 0, false);
    }


    @OnClick(R.id.goodsunitRL)
    public void showUnit() {
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

    @OnClick(R.id.goodsupplierRL)
    public void getAgency() {
        Bundle bundle = new Bundle();
        bundle.putString("choosetype", "0");
        CommonUtils.goActivityForResult(mContext, WAgencyActivity.class, bundle, 10012, false);
    }

    //跳到商品品牌
    @OnClick(R.id.goodsbrand)
    public void onClick() {
        CommonUtils.goActivityForResult(mContext, WBrandActivity.class, new Bundle(), 10013, false);
    }

    @OnClick(R.id.goodsExplainRL)
    public void goodsExplainRL() {
        showDialogToastM("商品特别说明", "输入说明", goodExplain_Tx);
    }


    @OnClick(R.id.submit)
    public void submit() {

        if (!check()) {
            return;
        }
        if (NetUtils.isNetworkConnected(mContext)) {
            progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
            progressDialog.show();

            if (StringHelper.isEmpty(chooseUrl)) { //没有改动图片
                submitGoodsInfo();
            } else {
                if (StringHelper.isEmpty(chooseimgid)) {
                    submitGoodsImg();
                } else {
                    submitGoodsInfo();
                }
            }
            submit.setClickable(false);
        } else {
            showToastShort("网络不给力");
        }
    }

    @OnClick(R.id.goodscodeRL)
    public void goodscodeRL() {
        showDialogToastM("请输入货号编码", "输入货号编码", goodsode_Tx);
    }

    public void showDialogToastM(String title, String input, final TextView tv) {

        new MaterialDialog.Builder(this)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("确定")
                .negativeText("取消")
//                .title(title)
                .input(input, tv.getText().toString(), true, new MaterialDialog.InputCallback() {
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
                        tv.setText(input.toString());
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
                lookitem.setUnitid(itemunitList.get(which).getId());
                lookitem.setUnit(text == null ? "" : text.toString());

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


        builder.negativeText("取消");
        builder.positiveText("确定");

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
        int a = 1;
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
                        String maxelementScaleValue = maxelementScale.getText().toString();
                        String minelementScaleValue = minelementScale.getText().toString();
                        if (StringHelper.isEmpty(elementScaleValue)) {
                            showToastShort("库存不能为空");
                            return;
                        }
                        if (StringHelper.isEmpty(maxelementScaleValue)) {
                            showToastShort("最大库存不能为空");
                            return;
                        }
                        if (StringHelper.isEmpty(minelementScaleValue)) {
                            showToastShort("最小库存不能为空");
                            return;
                        }
                        if (Integer.parseInt(maxelementScaleValue) <= Integer.parseInt(minelementScaleValue)) {
                            showToastShort("最大库存必须大于最大小库存");
                            return;
                        }
                        max_goodinventoryTx.setText(maxelementScale.getText().toString().trim());
                        min_goodinventoryTx.setText(minelementScale.getText().toString().trim());
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
        maxelementScale = (MaterialEditText) dialog.getCustomView().findViewById(R.id.max_dialog_element_et);
        minelementScale = (MaterialEditText) dialog.getCustomView().findViewById(R.id.min_dialog_element_et);
        maxelementScale.setText(max_goodinventoryTx.getText().toString().trim());
        minelementScale.setText(min_goodinventoryTx.getText().toString().trim());
        elementScale.setText(t1.getText().toString());
        System.out.println("==========" + t1.getText().toString());
        dialog.show();
    }

    Itemtype mItemtype;
    String inpustr = "";

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
            lookitem.setTypeid(item.getId());
            lookitem.setTypename(item.getName());
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
                            lookitem.setTypeid(mItemtype.getId());
                            lookitem.setTypename(mItemtype.getName());
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
                .negativeText("取消")
                .show();
    }

    String brangName;
    String brandId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ChooseActivity.CHOOSE_IMAGE_WHAT:
                chooseimgid = data.getExtras().getString("imgid");
                chooseUrl = data.getExtras().getString("chooseUrl");
                if (StringHelper.isEmpty(chooseimgid)) {
//                    Uri imageUri = Uri.parse("file://" + chooseUrl);
//                    //开始下载
//                    simpleDraweeView.setImageURI(imageUri);
                    //simpleDraweeView.setImageBitmap(ImgUtils.compressBitmap(chooseUrl, 300, 300));
                    Glide.with(mContext).load(chooseUrl).apply(new RequestOptions().centerCrop()).into(simpleDraweeView);
                } else {
                    Uri imageUri = Uri.parse(chooseUrl);
                    //开始下载
                    //simpleDraweeView.setImageURI(imageUri);
                    Glide.with(mContext).load(imageUri).into(simpleDraweeView);
                }
                break;
            case Contants.Photo.REQUEST_SCAN_CODE:
                System.out.println("============" + data.getExtras().getString("result"));
                goodsbarTx.setText(data.getExtras().getString("result"));
                break;
            case WAgencyActivity.CHOOSEAGENT_TYPE_WHAT:
                agencyname = data.getExtras().getString("agentuName");
                agencyId = data.getExtras().getString("agentuid");
                goodsupplier_Tx.setText(agencyname);
                break;
            case 33:
                if (data.getStringExtra("costPrice") == null) {
                    chengbenpriceTx.setText("");
                } else {
                    chengbenpriceTx.setText(data.getStringExtra("costPrice"));
                }
                goodspriceTx.setText(data.getStringExtra("wholesalePrice"));
                goodinventoryTx.setText(data.getStringExtra("inventory"));
                max_goodinventoryTx.setText(data.getStringExtra("max"));
                min_goodinventoryTx.setText(data.getStringExtra("min"));
                break;
            case WBrandActivity.goback:
                brangName = data.getExtras().getString("name");
                brandId = data.getExtras().getString("id");
                goodscbrandTv.setText(brangName);
                break;
            case WPriceEditActivity.BACK_TO:
                if (data.getStringExtra("costPrice") == null) {
                    chengbenpriceTx.setText("");
                } else {
                    chengbenpriceTx.setText(data.getStringExtra("costPrice"));
                }
                goodspriceTx.setText(data.getStringExtra("wholesalePrice"));
                goodinventoryTx.setText(data.getStringExtra("inventory"));
                max_goodinventoryTx.setText(data.getStringExtra("max"));
                min_goodinventoryTx.setText(data.getStringExtra("min"));
                itemMinNum.setText(data.getStringExtra("minnum"));
                itemMaxNum.setText(maxnum = data.getStringExtra("maxnum"));
                stopitemsum.setText(data.getStringExtra("stopnum"));
                break;
            default:
                break;

        }
    }


    @OnClick(R.id.id_right_btu)
    public void salesSubmit() {

        Bundle bundle = new Bundle();
        bundle.putString("itemid", id);
        bundle.putString("saveid", lookitem.getSaveid());
        bundle.putString("price", lookitem.getSprice());
        bundle.putString("type", "add");
        bundle.putString("goodsname", goodsnaneTx.getText().toString());
        CommonUtils.goActivity(mContext, WSalesDetailActivity.class, bundle, false);
    }

    public boolean check() {

        if (StringHelper.isEmpty(goodspriceTx.getText().toString().trim())) {
            showToastShort("请填写批发价");
            return false;
        }
        if (StringHelper.isEmpty(goodsclassifyTx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("请选择商品类型");
            return false;
        }
        if (StringHelper.isEmpty(goodsnaneTx.getText().toString().trim())) {
            showToastShort("请填写商品名");
            return false;
        }
        if (StringHelper.isEmpty(goodsunitTx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("请选择商品单位");
            return false;
        }
        if (StringHelper.isEmpty(goodsbarTx.getText().toString().trim().replace(" ", "")) && StringHelper.isEmpty(goodsode_Tx.getText().toString().trim().replace(" ", ""))) {
            showToastShort("商品条码和货品编码必须填一个！");
            return false;
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imageUri == null) {
            simpleDraweeView.setImageURI("");

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }
}
