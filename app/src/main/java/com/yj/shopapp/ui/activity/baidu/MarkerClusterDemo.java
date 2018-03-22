/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.yj.shopapp.ui.activity.baidu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Agents;
import com.yj.shopapp.ui.activity.baidu.mapapi.clusterutil.clustering.Cluster;
import com.yj.shopapp.ui.activity.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.yj.shopapp.ui.activity.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.wbeen.Client;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 此Demo用来说明点聚合功能
 */
public class MarkerClusterDemo extends BaseActivity implements OnMapLoadedCallback {

    MapView mMapView;
    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<MyItem> mClusterManager;
    View smarkView;
    TextView shopnameTv;
    List<Client> clientList = new ArrayList<>();
    List<Agents> sclientList;
    Intent intent;
    int mCurrentPage = 1;
    private LocationManager locationManager;
    private String provider;
    private boolean isFirstLocate = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_marker_cluster_demo;
    }

    @Override
    protected void initData() {
        smarkView = LayoutInflater.from(this).inflate(R.layout.marck_layout, null);
        shopnameTv = (TextView) smarkView.findViewById(R.id.shopname_tv);

        mMapView = (MapView) findViewById(R.id.bmapView);

        refreshRequest();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initMap() {
        intent = getIntent();
        double lat = 0;
        double lng = 0;
        // if (intent.hasExtra("clientList")) {
        // clientList = (List<Client>) this.getIntent().getSerializableExtra("clientList");
        for (Client client : clientList) {
            if (client.getLocation() != null) {
                if (!client.getLocation().equals("")) {
                    String[] latlog = client.getLocation().split(",");
                    lat = Double.parseDouble(latlog[0]);
                    lng = Double.parseDouble(latlog[1]);
                }

            }
        }

        // }
//        if (intent.hasExtra("sclientList"))
//        {
//
//            sclientList = (List<Agents>) this.getIntent().getSerializableExtra("sclientList");
//            for (Agents agents:sclientList)
//            {
//                if (agents.getLocation()!=null)
//                {
//                    if (!agents.getLocation().equals(""))
//                    {
//                        string [] latlog=agents.getLocation().split(",");
//                        lat=Double.parseDouble(latlog[0]);
//                        lng=Double.parseDouble(latlog[1]);
//                    }
//
//                }
//            }
//        }


        if (lat != 0) {
            ms = new MapStatus.Builder().target(new LatLng(lat, lng)).zoom(8).build();
        } else {
            ms = new MapStatus.Builder().target(new LatLng(113.31, 22.39)).zoom(8).build();
        }

        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(MarkerClusterDemo.this,
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {


//                Uri mUri = Uri.parse("geo:"+item.getPosition().latitude+","+item.getPosition().longitude+"?q= ");
//                Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
//                startActivity(mIntent);
               //  CommonUtils.goActivity(MarkerClusterDemo.this,BNDemoMainActivity.class,null);
                if(isAvilible(mContext,"com.baidu.BaiduMap")) {
                    Intent intent1 = null;
                    try {
                        intent1 = Intent.getIntent("intent://map/marker?location=" + item.getPosition().latitude + "," + item.getPosition().longitude + "&title=我的位置&content" +
                                "=百度奎科大厦&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    } catch (URISyntaxException e) {

                    }
                    startActivity(intent1); //启动调用
                }
                else{//未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                return false;
            }
        });

    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    private boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    private void navigateTo(Location location) {
        //如果是第一次创建，就获取位置信息并且将地图移到当前位置
        //为防止地图被反复移动，所以就只在第一次创建时执行
        if (isFirstLocate) {
            //LatLng对象主要用来存放经纬度
            //zoomTo是用来设置百度地图的缩放级别，范围为3~19，数值越大越精确
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }

        //封装设备当前位置并且显示在地图上
        //由于设备在地图上显示的位置会根据我们当前位置而改变，所以写到if外面
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }



    public void refreshRequest() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("username", "");

        HttpHelper.getInstance().post(mContext, Contants.PortA.USERS, params, new OkHttpResponseHandler<String>(mContext) {

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


//                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Client> jsonHelper = new JsonHelper<Client>(Client.class);
                    if (jsonHelper.getDatas(json).size() != 0) {
                        clientList.addAll(jsonHelper.getDatas(json));
                        mCurrentPage++;
                        refreshRequest();

                    } else {
                        initMap();
                    }


                } else if (JsonHelper.getRequstOK(json) == 6) {
                    initMap();
                } else {
                    showToastShort(Contants.NetStatus.NETLOADERROR);
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                clientList.clear();

            }
        });

    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    public LatLng convertGPSToBaidu(LatLng srLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        converter.coord(srLatLng);
        return converter.convert();
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        // 添加Marker点
        List<MyItem> items = new ArrayList<MyItem>();
        //if (intent.hasExtra("clientList")) {
        for (Client client : clientList) {
            String location = client.getLocation();
            if (location != null) {
                if (!location.equals("")) {
                    String[] latlog = location.split(",");
                    //Gps gps=GpsUtil.gcj02_To_Bd09(Double.parseDouble(latlog[0]),Double.parseDouble(latlog[1]));
                    LatLng latLng = new LatLng(Double.parseDouble(latlog[0]), Double.parseDouble(latlog[1]));
                    LatLng finallatlng = convertGPSToBaidu(latLng);
                    double lat = finallatlng.latitude;
                    double lng = finallatlng.longitude;

                    items.add(new MyItem(new LatLng(lat, lng), client.getShopname()));
                }

            }

        }
        //}

        if (intent.hasExtra("sclientList")) {
            for (Agents agents : sclientList) {
                String location = agents.getLocation();
                if (location != null) {
                    if (!location.equals("")) {
                        String[] latlog = location.split(",");
                        LatLng latLng = new LatLng(Double.parseDouble(latlog[0]), Double.parseDouble(latlog[1]));
                        LatLng finallatlng = convertGPSToBaidu(latLng);
                        double lat = finallatlng.latitude;
                        double lng = finallatlng.longitude;

                        items.add(new MyItem(new LatLng(lat, lng), agents.getShopname()));
                    }

                }

            }
        }

        mClusterManager.addItems(items);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MarkerClusterDemo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        String shopName;


        public MyItem(LatLng latLng, String shopName) {
            mPosition = latLng;
            this.shopName = shopName;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {

            shopnameTv.setText(shopName);
            return BitmapDescriptorFactory.fromView(smarkView);
        }

    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

}
