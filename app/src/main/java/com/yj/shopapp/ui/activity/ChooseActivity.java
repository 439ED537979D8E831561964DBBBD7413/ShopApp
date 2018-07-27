package com.yj.shopapp.ui.activity;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.adapter.ChooseAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.ImgUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PhotoUtil;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Imglist;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/26.
 * 图片选择页面
 */
public class ChooseActivity extends BaseActivity implements BaseRecyclerView {


    public static final int CHOOSE_IMAGE_WHAT = 9;


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    String cameraPath;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    String function;//其他页面传过来的功能参数
    String UploadURL;
    String getImgURL;
    String UploadcCategoryImgURL = Contants.PortA.Upbigtypeimg;
    String UploadAdvert = Contants.PortA.Upadving;
    String getCategoryImgURL = Contants.PortA.Bigtypeimglist;

    int UploadcCategoryImgURLRequestCode = 1005;

    GridLayoutManager staggeredGridLayoutManager;
    List<Imglist> imglists = new ArrayList<>();
    @BindView(R.id.title_view)
    RelativeLayout titleView;

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新或者加载

    private int mCurrentPage = 0;

    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    Intent intent;
    String number;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("cameraPath", cameraPath);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose;
    }

    @Override
    protected void initData() {
        intent = getIntent();
        title.setText("图片选择");
        if (getIntent().hasExtra("GoodsNumber")) {
            number = getIntent().getStringExtra("GoodsNumber");
        }
        imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_p, "", ""));
        imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_x, "", ""));
        if (getBundle() != null) {
            cameraPath = getBundle().getString("cameraPath");
        }

        init();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(ChooseActivity.this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    public void setUploadURL() {
        if (intent.hasExtra("funtion")) {
            if (intent.getStringExtra("funtion").equals("category")) {
                UploadURL = UploadcCategoryImgURL;
            } else if (intent.getStringExtra("funtion").equals("adver")) {
                UploadURL = UploadAdvert;
            }
        }

    }

    private void init() {
        setUploadURL();
        setGetImgURL();
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        layoutManager = new StaggeredGridLayoutManager(Contants.Column.THREE, StaggeredGridLayoutManager.VERTICAL);

        ChooseAdapter chooseAdapter = new ChooseAdapter(mContext, imglists, this);
        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, chooseAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);
                        if (intent.hasExtra("GoodsNumber")) {
                            refreshRequest();
                        } else {
                            if (intent.getStringExtra("funtion").equals("category")) {
                                getImg();
                            } else {

                                swipeRefreshLayout.setRefreshing(false);
                            }


                        }
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }

    }

    @OnClick(R.id.id_right_btu)
    public void onclickRight() {
        CommonUtils.goActivityForResult(mContext, PicasaActivity.class, null, 0, false);
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(false);
            isRequesting = false;
            //getImg();

        }
    };

    public void submitGoodsImg() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("imgbase", ImgUtils.getCompressImage(cameraPath, 310, 310));


        HttpHelper.getInstance().post(mContext, UploadURL, params, new OkHttpResponseHandler<String>(mContext) {

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
                    showToastShort("提交成功");
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("imgurl")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("chooseUrl", jsonObject.getString("imgurl"));

                            CommonUtils.goResult(mContext, bundle, CHOOSE_IMAGE_WHAT);
                        }

                    } catch (JSONException e) {
                    }


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

    public void setGetImgURL() {
        if (intent.hasExtra("funtion")) {
            if (intent.getStringExtra("funtion").equals("category")) {
                if (getCategoryImgURL != null) {
                    getImgURL = getCategoryImgURL;
                }

            }
        }

    }

    public void getImg() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, getImgURL, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                imglists.clear();
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_p, "", ""));
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_x, "", ""));
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Imglist> jsonHelper = new JsonHelper<Imglist>(Imglist.class);

                    imglists.addAll(jsonHelper.getDatas(json));

                    if (jsonHelper.getDatas(json).size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                imglists.clear();
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_p, "", ""));
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_x, "", ""));
                adapter.notifyDataSetChanged();
            }
        });


    }

    public void refreshRequest() {

        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("number", number);

        HttpHelper.getInstance().post(mContext, Contants.PortA.IMGLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                imglists.clear();
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_p, "", ""));
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_x, "", ""));
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Imglist> jsonHelper = new JsonHelper<Imglist>(Imglist.class);

                    imglists.addAll(jsonHelper.getDatas(json));

                    if (jsonHelper.getDatas(json).size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                imglists.clear();
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_p, "", ""));
                imglists.add(new Imglist("", "res://com.yj.shopapp/" + R.drawable.select_image_x, "", ""));
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void loadMoreRequest() {
        if (isRequesting) {
            return;
        }
        if (imglists.size() < 20) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("number", number);

        HttpHelper.getInstance().post(mContext, Contants.PortA.IMGLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Imglist> jsonHelper = new JsonHelper<Imglist>(Imglist.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        imglists.addAll(jsonHelper.getDatas(json));
                    }
                } else {
//                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
                iLoadView.showErrorView(loadMoreView);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            File cameraFile = PhotoUtil.camera(ChooseActivity.this);
            if (cameraFile != null) {
                cameraPath = cameraFile.getAbsolutePath();
            }
        } else {
            showToastShort("您未获取手机权限，请点击重试");
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0) {//拍照
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            } else {
                File cameraFile = PhotoUtil.camera(ChooseActivity.this);
                if (cameraFile != null) {
                    cameraPath = cameraFile.getAbsolutePath();
                }
            }

        } else if (position == 1) {//本地相册

            PhotoUtil.OpenFinder(ChooseActivity.this);

        } else {
            Bundle bundle = new Bundle();
            bundle.putString("chooseUrl", imglists.get(position).getImgurl());
            bundle.putString("imgid", imglists.get(position).getId());
            CommonUtils.goResult(mContext, bundle, CHOOSE_IMAGE_WHAT);
        }
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {
            loadMoreRequest();
        }
    }

    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

    Uri photoUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Contants.Photo.REQUEST_PHOTO_CODE
                && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//            cameraPath=selectedImage.getPath();
            //调用系统相机成功
            // cameraPath = PhotoUtil.getPhotoPath(this, data);
//            imglists.add(new Imglist("uadd","file://" + cameraPath,"",""));
            if (intent.hasExtra("funtion")) {
                if (intent.getStringExtra("funtion").equals("category")) {
                    submitGoodsImg();
                }
                if (intent.getStringExtra("funtion").equals("adver")) {
                    submitGoodsImg();
                }
            } else {
                System.out.println(cameraPath);
                adapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putString("chooseUrl", cameraPath);
                bundle.putString("imgid", "");
                CommonUtils.goResult(mContext, bundle, CHOOSE_IMAGE_WHAT);
            }

        } else if (requestCode == Contants.Photo.REQUEST_FILE_CODE
                && resultCode == Activity.RESULT_OK) {
            //调用系统本地相册成功
//            Uri selectedImage = data.getData();
//            cameraPath=selectedImage.getPath();
            cameraPath = PhotoUtil.getPhotoPath(this, data);
            imglists.add(new Imglist("uadd", "file://" + cameraPath, "", ""));
            if (intent.hasExtra("funtion")) {
                try {
                    if (intent.getStringExtra("funtion").equals("category")) {
                        submitGoodsImg();
                    }
                    if (intent.getStringExtra("funtion").equals("adver")) {
                        submitGoodsImg();
                    }
                } catch (Exception e) {
                    String estr = e.toString();
                }

            } else {
                adapter.notifyDataSetChanged();
                System.out.println(cameraPath);
                Bundle bundle = new Bundle();
                bundle.putString("imgid", "");
                bundle.putString("chooseUrl", cameraPath);
                CommonUtils.goResult(mContext, bundle, CHOOSE_IMAGE_WHAT);
            }
        } else if (resultCode == 10) {
            if (NetUtils.isNetworkConnected(mContext)) {
                if (null != swipeRefreshLayout) {

                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            swipeRefreshLayout.setRefreshing(true);

                            refreshRequest();
                        }
                    }, 200);
                }
            } else {
                showToastShort("网络不给力");
            }
        }

    }
}
