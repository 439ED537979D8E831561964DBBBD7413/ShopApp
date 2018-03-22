package com.yj.shopapp.ui.activity.wholesale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ui.activity.ChooseActivity;
import com.yj.shopapp.ui.activity.adapter.WSetupPathAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Adver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/10/14.
 */

public class WSetupAdvertising extends BaseActivity implements GoodsRecyclerView {

    final int requestCode = 001;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edittag_edit)
    EditText editTagEdit;
    @BindView(R.id.word_edit)
    EditText word_edit;


    String id = "";
    List<String> paths = new ArrayList<>();


    int s;//
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.wacticity_setupadvertising;
    }

    @Override
    protected void initData() {
        title.setText("添加编辑广告");
        intiAdapter();
        getAdvertising();
    }

    private void intiAdapter() {
        WSetupPathAdapter sadapter = new WSetupPathAdapter(this, paths, this);
        layoutManager = new GridLayoutManager(mContext, 5);
        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, sadapter);


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void CardClick(int postion) {
        paths.remove(postion);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.addpath_btn)
    public void imgOnclick() {
        Bundle bundle = new Bundle();
        bundle.putString("funtion", "adver");
        CommonUtils.goActivityForResult(mContext, ChooseActivity.class, bundle, requestCode, false);
    }

    @OnClick(R.id.submit_tv)
    public void submit() {
        refreshRequest();

    }

    private void getAdvertising() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Advinfo, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    Adver adver = new Adver(json);
                    editTagEdit.setText(adver.getTag());
                    word_edit.setText(adver.getTitle());
                    paths.addAll(adver.getImgurls());
                    id = adver.getId();
                    adapter.notifyDataSetChanged();

                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });
    }

    private String pathsToString() {
        String pathsString = "";
        for (int i = 0; i < paths.size(); i++) {
            if (i > 0) {
                pathsString = pathsString + "," + paths.get(i);
            } else {
                pathsString = paths.get(i);
            }
        }
        return pathsString;
    }

    public void refreshRequest() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", id);
        params.put("title", word_edit.getText().toString().trim().replace(" ", ""));
        params.put("imgurl", pathsToString());
        params.put("tag", editTagEdit.getText().toString().trim().replace(" ", ""));

        HttpHelper.getInstance().post(mContext, Contants.PortA.Advsave, params, new OkHttpResponseHandler<String>(mContext) {

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
        if (resultCode == ChooseActivity.CHOOSE_IMAGE_WHAT) {
            paths.add(data.getStringExtra("chooseUrl"));
            adapter.notifyDataSetChanged();

        }
    }


}
