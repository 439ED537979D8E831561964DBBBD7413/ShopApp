package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mining.app.zxing.MipcaActivityCapture;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.CenterDialog;
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
import com.yj.shopapp.util.KeybordS;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Goods;
import com.yj.shopapp.wbeen.Itemtype;
import com.yj.shopapp.wbeen.Itemunit;
import com.yj.shopapp.wbeen.Lookitem;
import com.yj.shopapp.wbeen.Power;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/26.
 */
public class WGoodsDetailActivity extends BaseActivity {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.chengbenpriceTx)
    TextView chengbenpriceTx;
    @BindView(R.id.goodspriceTx)
    TextView goodspriceTx;
    @BindView(R.id.item_min_num)
    TextView itemMinNum;
    @BindView(R.id.item_max_num)
    TextView itemMaxNum;
    @BindView(R.id.goodinventoryTx)
    TextView goodinventoryTx;
    @BindView(R.id.stopitemsum)
    TextView stopitemsum;
    @BindView(R.id.min_goodinventoryTx)
    TextView minGoodinventoryTx;
    @BindView(R.id.max_goodinventoryTx)
    TextView maxGoodinventoryTx;
    @BindView(R.id.changeLy)
    LinearLayout changeLy;
    @BindView(R.id.add_hotindex)
    CheckBox addHotindex;
    @BindView(R.id.add_stopsale)
    CheckBox addStopsale;
    @BindView(R.id.goodsupplier_Tx)
    TextView goodsupplierTx;
    @BindView(R.id.goodsupplierRL)
    RelativeLayout goodsupplierRL;
    @BindView(R.id.goodsclassify_Tx)
    TextView goodsclassifyTx;
    @BindView(R.id.goodsclassifyRL)
    RelativeLayout goodsclassifyRL;
    @BindView(R.id.goodscbrand_tv)
    TextView goodscbrandTv;
    @BindView(R.id.goodsbrand)
    RelativeLayout goodsbrand;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.goodsnane_Tx)
    TextView goodsnaneTx;
    @BindView(R.id.goodsnameRL)
    RelativeLayout goodsnameRL;
    @BindView(R.id.goodsbar_Tx)
    TextView goodsbarTx;
    @BindView(R.id.goodsbarRL)
    RelativeLayout goodsbarRL;
    @BindView(R.id.goodsode_Tx)
    TextView goodsodeTx;
    @BindView(R.id.goodscodeRL)
    RelativeLayout goodscodeRL;
    @BindView(R.id.goodsunit_Tx)
    TextView goodsunitTx;
    @BindView(R.id.goodsunitRL)
    RelativeLayout goodsunitRL;
    @BindView(R.id.goodsnorms_Tx)
    TextView goodsnormsTx;
    @BindView(R.id.goodsnormsRL)
    RelativeLayout goodsnormsRL;
    @BindView(R.id.goodExplain_Tx)
    TextView goodExplainTx;
    @BindView(R.id.goodsExplainRL)
    RelativeLayout goodsExplainRL;
    @BindView(R.id.goodAddress_Tx)
    TextView goodAddressTx;
    @BindView(R.id.goodsAddressRL)
    RelativeLayout goodsAddressRL;
    @BindView(R.id.goodsdetaail_Tx)
    TextView goodsdetaailTx;
    @BindView(R.id.goodsdetailRL)
    RelativeLayout goodsdetailRL;
    @BindView(R.id.submit)
    CardView submit;
    @BindView(R.id.goto_salesTV)
    TextView gotoSalesTV;
    @BindView(R.id.goto_salesRL)
    RelativeLayout gotoSalesRL;
    @BindView(R.id.retail_price)
    TextView retailPrice;
    private MaterialEditText elementScale;
    private MaterialEditText maxelementScale;
    private MaterialEditText minelementScale;
    private String agencyname;
    private String agencyId;
    public static final int TYPE = 10010;
    private String itemnoid;
    private String id;
    private String chooseimgid;
    private String chooseUrl;
    private String stock;
    private String addressID;
    private Uri imageUri;
    private int ischooseNewGood = 0;//如果选中==1不选中==0
    private int isstopsels;//如果选中=1不选中=0


    private List<UserGroup> userGroupList = new ArrayList<>();
    private List<String> priceArray = new ArrayList<>();
    private KProgressHUD progressDialog = null;
    private String saveid;
    private List<GoodAddress> goodAddresses;
    private String minnum = "";
    private String maxnum = "";
    private GoodAddress.ChildrenBean bean;
    private Goods mGoods = new Goods();
    private Lookitem lookitem;
    private String brangName;
    private String brandId;
    private CenterDialog dialog;
    private EditText dialog_edit;
    private Power power;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_goodsdetail;
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
        EventBus.getDefault().register(mContext);
        title.setText("商品详情");
        idRightBtu.setText("纠错");
        //setResult(0);
        if (getIntent().hasExtra("itemnoid")) {
            itemnoid = getIntent().getStringExtra("itemnoid");
        }
        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
        gotoSalesRL.setVisibility(View.VISIBLE);
        progressDialog = growProgress(Contants.Progress.LOAD_ING);
        if (isNetWork(mContext)) {
            requsetdata();
        }
//        addHotindex.setOnCheckedChangeListener(this);
//        addStopsale.setOnCheckedChangeListener(this);
        dialog = new CenterDialog(mContext, R.layout.dialog_barcodeview, new int[]{R.id.dialog_close, R.id.dialog_cancel, R.id.dialog_sure}, 0.8);
        addHotindex.setOnClickListener(v -> {
            if (power.getIs_hot() == 0) {
                addHotindex.setChecked(!lookitem.getIs_hot().equals("0"));
                showToastShort("您暂未有权限修改");
            } else {
                ischooseNewGood = addHotindex.isChecked() ? 1 : 0;
            }
        });
        addStopsale.setOnClickListener(v -> {
            if (power.getSupplierid() == 0) {
                addStopsale.setChecked(!lookitem.getSale_status().equals("0"));
                showToastShort("您暂未有权限修改");
            } else {
                isstopsels = addStopsale.isChecked() ? 1 : 0;
            }
        });
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()) {
//            case R.id.add_hotindex:
//                if (isChecked) {
//                    ischooseNewGood = 0;
//                } else {
//                    ischooseNewGood = 1;
//                }
//                break;
//            case R.id.add_stopsale:
//                if (isChecked) {
//                    isstopsels = 0;
//                } else {
//                    isstopsels = 1;
//                }
//                break;
//        }
//    }

    //所有请求
    private void requsetdata() {
        //reportUnits();
        refreshRequest();
        //loadIClient();
        //reportType();
        operableField();
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
                ShowLog.e(json);
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
                    goodsbarTx.setHint("");
                    goodsbarTx.setText(lookitem.getItemnoid());
                    goodsunitTx.setText(lookitem.getUnit());
                    goodsnormsTx.setText(lookitem.getSpecs());
                    goodsupplierTx.setText(lookitem.getSuppilername());
                    goodsodeTx.setText(lookitem.getCustomnumber());
                    goodExplainTx.setText(lookitem.getSpecialnote());
                    agencyId = lookitem.getSupplierid();
                    minGoodinventoryTx.setText(lookitem.getMinitemsum());
                    maxGoodinventoryTx.setText(lookitem.getMaxitemsum());
                    itemMinNum.setText(lookitem.getMinnum());
                    itemMaxNum.setText(lookitem.getMaxnum());
                    stopitemsum.setText(lookitem.getStopitemsum());
                    goodAddressTx.setText(lookitem.getLocalhostname());
                    goodsdetaailTx.setText(lookitem.getBrochure().replace("<br/>", ""));
                    imageUri = Uri.parse(lookitem.getImgurl());
                    retailPrice.setText(lookitem.getVipprice());
                    if (lookitem.getIs_hot().equals("1")) {
                        addHotindex.setChecked(true);
                    }
                    if (lookitem.getSale_status().equals("1")) {
                        addStopsale.setChecked(true);
                    }
                    isstopsels = lookitem.getSale_status().equals("0") ? 0 : 1;
                    ischooseNewGood = lookitem.getIs_hot().equals("0") ? 0 : 1;
                    if (lookitem.getIs_sales().equals("1")) {
                        gotoSalesTV.setText("已加入");
                    }
                    //开始下载
                    //获取网络的图片
                    simpleDraweeView.setImageURI(imageUri);
                    //Glide.with(mContext).load(imageUri).into(simpleDraweeView);

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

    private void operableField() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        //显示ProgressDialog
        HttpHelper.getInstance().post(mContext, Contants.PortA.OPERABLEFIELD, params, new OkHttpResponseHandler<String>(mContext) {

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
                    power = JSONObject.parseObject(json, Power.class);
//                    if (power.getIs_hot() == 0) {
//                        addHotindex.setChecked(false);
//                    }
//                    if (power.getSupplierid() == 0) {
//                        addStopsale.setChecked(false);
//                    }
                    setbgcolor();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }


    /***
     * 提交商品信息
     */
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
        params.put("maxitemsum", maxGoodinventoryTx.getText().toString().trim().replace(" ", ""));
        params.put("minitemsum", minGoodinventoryTx.getText().toString().trim().replace(" ", ""));
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
        //params.put("is_hot", ischooseNewGood + "");
        params.put("customnumber", goodsodeTx.getText().toString().trim());
        params.put("brand", brandId);
        // params.put("sale_status", isstopsels + "");

        params.put("specialnote", goodExplainTx.getText().toString().trim());
        params.put("supplierid", agencyId);
        params.put("minnum", itemMinNum.getText().toString().trim());
        params.put("maxnum", itemMaxNum.getText().toString().trim());
        params.put("stopitemsum", stopitemsum.getText().toString());
        params.put("vipprice", retailPrice.getText().toString());
        HttpHelper.getInstance().post(mContext, Contants.PortA.SAVEITEM, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                //progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                JSONObject object = JSONObject.parseObject(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    //showToastShort("提交成功");
                    showToastShort(object.getString("info"));

                    try {
                        mGoods.setPrice(goodspriceTx.getText().toString().trim());
                        mGoods.setItemsum(goodinventoryTx.getText().toString().trim());
                        mGoods.setName(goodsnaneTx.getText().toString().trim());
                        mGoods.setSale_status(String.valueOf(isstopsels));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(mGoods);
                    finish();
                } else {
                    showToastShort(object.getString("info"));
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

    /**
     * 提交商品图片
     */
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
                        JSONObject object = JSONObject.parseObject(json);
                        mGoods.setImgurl(object.getString("imgurl"));
                        lookitem.setImgid(object.getString("imgid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    submitGoodsInfo();
                } else {
                    //showToastShort(Contants.NetStatus.NETERROR);
                    //progressDialog.dismiss();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                //progressDialog.dismiss();
            }
        });
    }

//    @OnClick(R.id.changeLy)
//    public void AlterCardInput(){
//        setDialogInput(goodspriceTx,goodinventoryTx);
//    }

    //    @OnClick(R.id.choose_re)
//    public void chooseNewGood() {
//        if (ischooseNewGood == 0) {
//            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
//            ischooseNewGood = 1;
//        } else {
//            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
//            ischooseNewGood = 0;
//        }
//    }
//
//    @OnClick(R.id.choose1_re)
//    public void stopsals() {
//        if (isstopsels == 1) {
//            choose1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
//            isstopsels = 0;
//        } else {
//            choose1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
//            isstopsels = 1;
//
//        }
//    }
    @OnClick({R.id.goodsupplierRL, R.id.goodsExplainRL, R.id.changeLy, R.id.simpleDraweeView, R.id.goodscodeRL, R.id.goodsunitRL, R.id.goodsclassifyRL, R.id.goodsnameRL
            , R.id.goodsbarRL, R.id.goodsnormsRL, R.id.submit, R.id.goodsAddressRL, R.id.goodsbrand, R.id.goodsdetailRL, R.id.goto_salesRL, R.id.id_right_btu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //供应商
            case R.id.goodsupplierRL:
                if (power.getSupplierid() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("choosetype", "0");
                    CommonUtils.goActivityForResult(mContext, WAgencyActivity.class, bundle, 10012, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //商品提示
            case R.id.goodsExplainRL:
                if (power.getSpecialnote() == 1) {
                    showDialogs(4, "商品提示", "请输入商品提示", "取消", goodExplainTx);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                //showDialogToastM("商品特别说明", "输入说明", goodExplain_Tx);
                break;
            //修改价格库存
            case R.id.changeLy:
                Bundle bundle1 = new Bundle();
                bundle1.putString("costPrice", chengbenpriceTx.getText().toString()); //成本价
                bundle1.putString("wholesalePrice", goodspriceTx.getText().toString());//批发价
                bundle1.putString("inventory", goodinventoryTx.getText().toString()); //库存
                bundle1.putString("max", maxGoodinventoryTx.getText().toString()); //最大库存
                bundle1.putString("min", minGoodinventoryTx.getText().toString()); //最小库存
                bundle1.putString("minnum", itemMinNum.getText().toString());//起购
                bundle1.putString("maxnum", itemMaxNum.getText().toString());//限购
                bundle1.putString("stopnum", stopitemsum.getText().toString());//停售库存
                bundle1.putParcelable("power", power);
                bundle1.putString("vipprice", retailPrice.getText().toString());
                CommonUtils.goActivityForResult(mContext, WPriceEditActivity.class, bundle1, WPriceEditActivity.EDIT_CODE, false);
                break;
            //修改批发价
//            case R.id.goodspriceTx:
//                //setDialogInputSprice(goodspriceTx);
//                break;
            //修改库存
//            case R.id.goodinventoryTx:
//                //setDialogInput(goodinventoryTx);
//                break;
            //修改商品图片
            case R.id.simpleDraweeView:
                if (power != null && power.getImgid() == 1) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("GoodsNumber", goodsbarTx.getText().toString());
                    CommonUtils.goActivityForResult(mContext, ChooseActivity.class, bundle2, 0, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //修改货号编码
            case R.id.goodscodeRL:
                if (power.getCustomnumber() == 1) {
                    //showDialogToastM("请输入货号编码", "输入货号编码", goodsode_Tx);
                    showDialogs(2, "货号编码", "请输入货号编码", "取消", goodsodeTx);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //修改单位
            case R.id.goodsunitRL:
                if (power.getUnitid() == 1) {
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("title_name", "选择单位");
                    bundle4.putInt("type", 1);
                    //bundle4.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) itemunitList);
                    CommonUtils.goActivityForResult(WGoodsDetailActivity.this, WBaseSelectActivity.class, bundle4, 10014, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //修改商品分类
            case R.id.goodsclassifyRL:
                if (power.getIndustryid() == 1) {
                    mItemtype = null;
                    //showChooseify("请选择分类", "0");
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("title_name", "选择分类");
                    bundle3.putInt("type", 0);
                    //bundle3.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) itemtypeList);
                    CommonUtils.goActivityForResult(WGoodsDetailActivity.this, WBaseSelectActivity.class, bundle3, 10015, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //修改商品名称
            case R.id.goodsnameRL:
                if (power.getName() == 1) {
                    showDialogs(0, "商品名称", "请输入商品名称", "取消", goodsnaneTx);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                //showDialogToast("请输入商品名称", "请输入商品名称", goodsnaneTx);
                break;
            //修改商品条码
            case R.id.goodsbarRL:
                if (power.getItemnumber() == 1) {
                    showDialogs(1, "商品条码", "请输入商品条码", "扫描", goodsbarTx);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                //showDialogToast2("请输入商品条码", "请输入商品条码", goodsbarTx);
                break;
            //修改商品规格
            case R.id.goodsnormsRL:
                if (power.getSpecs() == 1) {
                    //showDialogToast("请输入商品规格", "请输入商品规格", goodsnormsTx);
                    showDialogs(3, "商品规格", "请输入商品规格", "取消", goodsnormsTx);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //提交
            case R.id.submit:
                if (!check()) {
                    return;
                }
                if (NetUtils.isNetworkConnected(mContext)) {
//                    progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
//                    progressDialog.show();
                    submitGoodsImg();
                } else {
                    showToastShort("网络不给力");
                }
                break;
            //修改商品位置
            case R.id.goodsAddressRL:
                if (power.getLocalhost() == 1) {
                    CommonUtils.goActivity(mContext, GoodSheives.class, null, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //修改商品品牌
            case R.id.goodsbrand:
                if (power.getBrand() == 1) {
                    CommonUtils.goActivityForResult(mContext, WBrandActivity.class, new Bundle(), 10014, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //商品介绍
            case R.id.goodsdetailRL:
                if (power.getSpecialnote() == 1) {
                    showDialogs(5, "商品介绍", "请输入商品介绍", "取消", goodsdetaailTx);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //加入促销
            case R.id.goto_salesRL:
                if (power.getIs_sales() == 1) {
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("itemid", id);
                    bundle5.putString("saveid", lookitem.getSaveid());
                    bundle5.putString("price", lookitem.getSprice());
                    bundle5.putString("type", "add");
                    bundle5.putString("goodsname", goodsnaneTx.getText().toString());
                    CommonUtils.goActivity(mContext, WSalesDetailActivity.class, bundle5, false);
                } else {
                    showToastShort("您暂未有权限修改");
                }
                break;
            //纠错
            case R.id.id_right_btu:
                Bundle bundle = new Bundle();
                bundle.putString("itemid", lookitem.getId());
                CommonUtils.goActivity(this, ErrorCorrectionActivity.class, bundle);
                break;
        }
    }

    private void setbgcolor() {
        if (power.getSupplierid() == 0) {
            goodsupplierRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getIndustryid() == 0) {
            goodsclassifyRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getBrand() == 0) {
            goodsbrand.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getName() == 0) {
            goodsnameRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getItemnumber() == 0) {
            goodsbarRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getCustomnumber() == 0) {
            goodscodeRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getUnitid() == 0) {
            goodsunitRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getSpecs() == 0) {
            goodsnormsRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getSpecialnote() == 0) {
            goodsExplainRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getLocalhost() == 0) {
            goodsAddressRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }
        if (power.getIs_sales() == 0) {
            gotoSalesRL.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
        }

    }

    private void showDialogs(int type, String title, String hint, String lift_bt_text, TextView itemview) {

        dialog.show();
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(title);
        dialog_edit = dialog.findViewById(R.id.ecit_phone);
        dialog_edit.setText(itemview.getText().toString());
        dialog_edit.setSelection(itemview.getText().toString().length());
        KeybordS.openKeybord(dialog_edit, mContext);
        dialog_edit.setHint(hint);
        ((TextView) dialog.findViewById(R.id.dialog_cancel)).setText(lift_bt_text);
        dialog.setOnCenterItemClickListener((dialog, view) -> {
            switch (view.getId()) {
                case R.id.dialog_close:
                    KeybordS.closeKeybord(dialog_edit, mContext);
                    dialog.dismiss();
                    break;
                case R.id.dialog_cancel:
                    switch (type) {
                        case 1:
                            //扫描
                            Bundle bundle = new Bundle();
                            bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.original_type);
                            bundle.putString("type", "goodsAdd");
                            CommonUtils.goActivity(mContext, MipcaActivityCapture.class, bundle);
                            KeybordS.closeKeybord(dialog_edit, mContext);
                            dialog.dismiss();
                            break;
                        default:
                            KeybordS.closeKeybord(dialog_edit, mContext);
                            dialog.dismiss();
                            break;
                    }
                    break;
                case R.id.dialog_sure:
                    if (!dialog_edit.getText().toString().equals("")) {
                        itemview.setText(dialog_edit.getText().toString());
                        KeybordS.closeKeybord(dialog_edit, mContext);
                        dialog.dismiss();
                    } else {
                        showToastShort(hint);
                    }
                    break;
            }
        });
    }
//    @OnClick(R.id.changeLy)
//    public void onclichengbenpriceLay() {//修改成本价
////        showDialogToastM("成本价", "输入成本价", chengbenpriceTx);
//        Bundle bundle = new Bundle();
//        bundle.putString("costPrice", chengbenpriceTx.getText().toString()); //成本价
//        bundle.putString("wholesalePrice", goodspriceTx.getText().toString());//批发价
//        bundle.putString("inventory", goodinventoryTx.getText().toString()); //库存
//        bundle.putString("max", maxGoodinventoryTx.getText().toString()); //最大库存
//        bundle.putString("min", minGoodinventoryTx.getText().toString()); //最小库存
//        bundle.putString("minnum", itemMinNum.getText().toString());
//        bundle.putString("maxnum", itemMaxNum.getText().toString());
//        bundle.putString("stopnum", stopitemsum.getText().toString());
//        Log.d("m_tag", minnum + maxnum);
//        CommonUtils.goActivityForResult(mContext, WPriceEditActivity.class, bundle, WPriceEditActivity.EDIT_CODE, false);
//    }

//    @OnClick(R.id.goodinventoryTx)
//    public void onClickInventoryLay() { //修改库存
////        setDialogInput(goodspriceTx, goodinventoryTx);
//        setDialogInput(goodinventoryTx);
//    }

//    @OnClick(R.id.goodsAddressRL)
//    public void onViewClicked() {
//        CommonUtils.goActivity(mContext, GoodSheives.class, null, false);
//
//    }

//    @OnClick(R.id.simpleDraweeView)
//    public void choose() {
//        if (lookitem == null) {
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString("GoodsNumber", lookitem.getItemnoid());
//        CommonUtils.goActivityForResult(mContext, ChooseActivity.class, bundle, 0, false);
//    }


//    @OnClick(R.id.goodsunitRL)
//    public void showUnit() {
//        String goodstr = goodsunitTx.getText().toString();
//
//        String[] selectItemArr = new String[itemunitList.size()];
//
//        int goodsNum = -1;
//        int i = 0;
//        for (Itemunit itemunit : itemunitList) {
//            selectItemArr[i] = itemunit.getName();
//            if (goodstr.equals(itemunit.getName())) {
//                goodsNum = i;
//            }
//            i++;
//        }
//        showDialogList("请选择商品单位", selectItemArr, goodsNum, goodsunitTx);
//    }

//    @OnClick(R.id.goodsclassifyRL)
//    public void showGoodsClassIfy() {
//        mItemtype = null;
//        showChooseify("请选择分类", "0");
//    }

//    @OnClick(R.id.goodsnameRL)
//    public void showName() {
//        showDialogToast("请输入商品名称", "请输入商品名称", goodsnaneTx);
//    }
//
//    @OnClick(R.id.goodsbarRL)
//    public void showBar() {
//        showDialogToast2("请输入商品条码", "请输入商品条码", goodsbarTx);
//    }
//
//    @OnClick(R.id.goodsnormsRL)
//    public void showNorm() {
//        showDialogToast("请输入商品规格", "请输入商品规格", goodsnormsTx);
//    }
//
//    //跳到商品品牌
//    @OnClick(R.id.goodsbrand)
//    public void onClick() {
//        CommonUtils.goActivityForResult(mContext, WBrandActivity.class, new Bundle(), 10013, false);
//    }

//    @OnClick(R.id.goodsExplainRL)
//    public void goodsExplainRL() {
//        showDialogToastM("商品特别说明", "输入说明", goodExplainTx);
//    }


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

//    @OnClick(R.id.goodscodeRL)
//    public void goodscodeRL() {
//        showDialogToastM("请输入货号编码", "输入货号编码", goodsodeTx);
//    }

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

//    public void showDialogList(final String title, final String[] selectItemArr, final int goodsNum, final TextView tv) {
//
//        if (itemunitList.size() == 0) {
//            return;
//        }
//
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
//        builder.title(title);
//
//        builder.items(selectItemArr);
//
//        builder.itemsCallbackSingleChoice(goodsNum, new MaterialDialog.ListCallbackSingleChoice() {
//            @Override
//            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                tv.setText(text == null ? "" : text.toString());
//                lookitem.setUnitid(itemunitList.get(which).getId());
//                lookitem.setUnit(text == null ? "" : text.toString());
//
//                return true; // allow selection
//            }
//        });
//        if (title.equals("请选择商品单位")) {
//
//            builder.neutralText("手动输入");
//            builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    new MaterialDialog.Builder(mContext)
//
//
//                            .input("输入单位", null, new MaterialDialog.InputCallback() {
//                                @Override
//                                public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
//                                    if (input != null) {
//                                        tv.setText(input.toString());
//                                    }
//                                }
//                            })
//                            .positiveText("确定")
//                            .negativeText("取消")
//                            .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    // goodsclassifyTx.setText(inpustr);
//                                }
//                            })
//                            .show();
//
//                }
//            });
//        }
//
//
//        builder.negativeText("取消");
//        builder.positiveText("确定");
//
//        builder.autoDismiss(true);
//        builder.show();
//    }

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
                        maxGoodinventoryTx.setText(maxelementScale.getText().toString().trim());
                        minGoodinventoryTx.setText(minelementScale.getText().toString().trim());
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
        maxelementScale.setText(maxGoodinventoryTx.getText().toString().trim());
        minelementScale.setText(minGoodinventoryTx.getText().toString().trim());
        elementScale.setText(t1.getText().toString());
        System.out.println("==========" + t1.getText().toString());
        dialog.show();
    }

    Itemtype mItemtype;
    String inpustr = "";

//    public void showChooseify(String title, String id) {
//        final List<Itemtype> list = new ArrayList<>();
//        Itemtype item = new Itemtype();
//        for (Itemtype itemtype : itemtypeList) {
//            if (itemtype.getPid().equals(id)) {
//                list.add(itemtype);
//            }
//            if (itemtype.getId().equals(id)) {
//                item = itemtype;
//            }
//        }
//        if (list.size() == 0) {
//            lookitem.setTypeid(item.getId());
//            lookitem.setTypename(item.getName());
//            goodsclassifyTx.setText(item.getName());
//            return;
//        }
//        final String[] array;
//        if (mItemtype == null) {
//            array = new String[list.size()];
//            int i = 0;
//            for (Itemtype itemtype : list) {
//                array[i] = itemtype.getName();
//                i++;
//            }
//        } else {
//            array = new String[list.size() + 1];
//            int i = 1;
//            array[0] = mItemtype.getName();
//            for (Itemtype itemtype : list) {
//                array[i] = itemtype.getName();
//                i++;
//            }
//        }
//
//        new MaterialDialog.Builder(mContext)
//                .title(title)
//                .items(array)
//
//                .neutralText("手动输入")
//
//                .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                        new MaterialDialog.Builder(mContext)
//
//
//                                .input("输入类别", null, new MaterialDialog.InputCallback() {
//                                    @Override
//                                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
//                                        if (input != null) {
//                                            goodsclassifyTx.setText(input.toString());
//                                        }
//                                    }
//                                })
//                                .positiveText("确定")
//                                .negativeText("取消")
//                                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                    @Override
//                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                        // goodsclassifyTx.setText(inpustr);
//                                    }
//                                })
//                                .show();
//                    }
//                })
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        if (mItemtype != null && mItemtype.getName().equals(array[which])) {
//                            lookitem.setTypeid(mItemtype.getId());
//                            lookitem.setTypename(mItemtype.getName());
//                            goodsclassifyTx.setText(mItemtype.getName());
//                        } else {
//                            if (mItemtype == null) {
//                                mItemtype = list.get(which);
//                                showChooseify(text == null ? "" : text.toString(), list.get(which).getId());
//                            } else {
//                                mItemtype = list.get(which - 1);
//                                showChooseify(text == null ? "" : text.toString(), list.get(which - 1).getId());
//                            }
//
//                        }
//                    }
//                })
//                .negativeText("取消")
//                .show();
//    }

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
                goodsupplierTx.setText(agencyname);
                break;
            case 33:
                if (data.getStringExtra("costPrice") == null) {
                    chengbenpriceTx.setText("");
                } else {
                    chengbenpriceTx.setText(data.getStringExtra("costPrice"));
                }
                goodspriceTx.setText(data.getStringExtra("wholesalePrice"));
                goodinventoryTx.setText(data.getStringExtra("inventory"));
                maxGoodinventoryTx.setText(data.getStringExtra("max"));
                minGoodinventoryTx.setText(data.getStringExtra("min"));
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
                maxGoodinventoryTx.setText(data.getStringExtra("max"));
                minGoodinventoryTx.setText(data.getStringExtra("min"));
                itemMinNum.setText(data.getStringExtra("minnum"));
                itemMaxNum.setText(maxnum = data.getStringExtra("maxnum"));
                stopitemsum.setText(data.getStringExtra("stopnum"));
                retailPrice.setText(data.getStringExtra("vipprice"));
                break;
            case WBaseSelectActivity.ITEMTYPE:
                Itemtype itemtype = data.getParcelableExtra("item");
                ShowLog.e(itemtype.getName());
                goodsclassifyTx.setText(itemtype.getName());
                break;
            case WBaseSelectActivity.ITEMUNIT:
                Itemunit itemunit = data.getParcelableExtra("item");
                goodsunitTx.setText(itemunit.getName());
                ShowLog.e(itemunit.getName());
                break;
            default:
                break;

        }
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
        if (StringHelper.isEmpty(goodsbarTx.getText().toString().trim().replace(" ", "")) && StringHelper.isEmpty(goodsodeTx.getText().toString().trim().replace(" ", ""))) {
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
