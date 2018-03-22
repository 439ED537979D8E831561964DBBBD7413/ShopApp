package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Classise;
import com.yj.shopapp.ui.activity.ChooseActivity;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/11/3.
 */

public class WaddCategoryActivity extends BaseActivity {

    Classise classise;
    final int requestCode = 001;
    @BindView(R.id.word_edit)
    EditText word_edit;
    @BindView(R.id.choose_tv)
    TextView choose_tv;

    @BindView(R.id._img)
    ImageView _img;
    @BindView(R.id.choose)
    ImageView choose;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    String id = "";
    String supplierid="";
    String AgancyId;


    int ishot = 0;//如果选中==1不选中==0
    @BindView(R.id.title)
    TextView title;
    String imgUrl;
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_addcategory;
    }

    @Override
    protected void initData() {
        title.setText("添加分类");
        if (getIntent().hasExtra("classice")) {
            classise = (Classise) getIntent().getSerializableExtra("classice");
            title.setText("编辑分类");
        }
        if (classise != null) {
            word_edit.setText(classise.getName());
            imgUrl=classise.getImgurl();
            choose_tv.setText(classise.getName());
            Uri imageUri = Uri.parse(imgUrl);
            simpleDraweeView.setImageURI(imageUri);
            supplierid = classise.getSupplierid();
            if (classise.getIshot()!=null) {
                ishot = Integer.parseInt(classise.getIshot());
                if (ishot == 0) {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));


                } else {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));


                }
            }
            else
            {
                ishot=0;
                choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
            }

        }
    }


    @OnClick(R.id.chooseimg_tv)
    public void imgOnclick() {
        Bundle bundle = new Bundle();
        bundle.putString("funtion", "category");
        CommonUtils.goActivityForResult(mContext, ChooseActivity.class, bundle, requestCode, false);
    }

    @OnClick(R.id.submit_tv)
    public void submit() {
        if (checkDataIsOk()) {
            refreshRequest();
        }


    }

    @OnClick(R.id.choose_re)
    public void chooseNewGood() {
        if (ishot == 0) {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
            ishot = 1;

        } else {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
            ishot = 0;

        }
    }

    @OnClick(R.id.choose_tv)
    public void Choose_tvOnclick() {
        Bundle bundle = new Bundle();
        bundle.putString("choosetype", "0");
        CommonUtils.goActivityForResult(mContext, WAgencyActivity.class, bundle, requestCode, false);
    }

    public void refreshRequest() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);


        params.put("name", word_edit.getText().toString().trim().replace(" ", ""));
        params.put("id", id);
        params.put("supplierid", supplierid);
        params.put("imgurl", imgUrl);
        params.put("ishot", ishot + "");


        HttpHelper.getInstance().post(mContext, Contants.PortA.Savebigtype, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onBefore() {
                super.onBefore();
                showToastShort(Contants.Progress.SUMBIT_ING);

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
                System.out.println("response" + e.toString());

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == ChooseActivity.CHOOSE_IMAGE_WHAT) {

            Uri imageUri = Uri.parse(data.getStringExtra("chooseUrl"));
            simpleDraweeView.setImageURI(imageUri);
            imgUrl=data.getStringExtra("chooseUrl");
        } else if (requestCode == this.requestCode && resultCode == WAgencyActivity.CHOOSEAGENT_TYPE_WHAT) {
            choose_tv.setText(data.getStringExtra("agentuName"));
            supplierid = data.getStringExtra("agentuid");
        }
    }


    public boolean checkDataIsOk()
    {
        if (word_edit.getText().toString().trim().replace(" ", "").equals(""))
        {
            showToastShort("类别名称不能为空！");
            return false;
        }
        if (supplierid.equals(""))
        {
            showToastShort("供应商不能为空！");
            return false;
        }
        if(imgUrl.equals(""))

        {
            showToastShort("类别图片不能为空！");
            return false;
        }
        return true;
    }
}
