package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.yj.shopapp.ui.activity.baidu.mapapi.overlayutil.OverlayManager;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BottomDialog;
import com.yj.shopapp.util.GpsUtil;

import java.io.File;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.OnClick;

public class MapActivity extends BaseActivity implements SensorEventListener, OnGetGeoCoderResultListener, BottomDialog.OnCenterItemClickListener
        , OnGetRoutePlanResultListener {

    @BindView(R.id.mMapView)
    MapView mMapView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.storename)
    TextView storename;
    @BindView(R.id.storeaddress)
    TextView storeaddress;
    private BaiduMap mbaiduMap;
    private String name;
    private String address;
    //获取传感器
    private SensorManager mSensorManager;
    // 定位相关
    LocationClient mLocClient;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    private int mCurrentDirection = 0;
    boolean isFirstLoc = true; // 是否首次定位
    private Double lastX = 0.0;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    public MyLocationListenner myListener = new MyLocationListenner();
    GeoCoder mGeoSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mPosition;
    private BottomDialog bottomDialog;
    private LatLng endposition;
    // 搜索相关
    RoutePlanSearch mSearch = null;
    int nodeIndex = -1;
    DrivingRouteResult nowResultdrive = null;
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initData() {
        title.setText("商铺位置");
        if (getIntent().hasExtra("storename")) {
            name = getIntent().getStringExtra("storename");
            storename.setText(name);
        }
        if (getIntent().hasExtra("address")) {
            address = getIntent().getStringExtra("address");
            storeaddress.setText(address);

        }
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        }
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mbaiduMap = mMapView.getMap();
        // 开启定位图层
        mbaiduMap.setMyLocationEnabled(true);
        mbaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mbaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, null));
        initLocation();
// 初始化搜索模块，注册事件监听
        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);
        bottomDialog = new BottomDialog(mContext, R.layout.nav_dialog, new int[]{R.id.baiduMap, R.id.gaodeMap});
        bottomDialog.setOnCenterItemClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }


    public void initLocation() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @OnClick({R.id.checkRoute, R.id.navigation, R.id.mPosition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkRoute:
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("东莞", "东莞市里仁路祺胜酒店");
                PlanNode enNode = PlanNode.withCityNameAndPlaceName("东莞", address);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
                break;
            case R.id.navigation:
                bottomDialog.show();
                break;
            case R.id.mPosition:
                animation2Pos(mPosition);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mbaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            showToastShort("抱歉，未能找到结果");
            return;
        }
        mbaiduMap.addOverlay(new MarkerOptions().position(geoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_position)));
        animation2Pos(geoCodeResult.getLocation());
        endposition = geoCodeResult.getLocation();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            showToastShort("抱歉，未能找到结果");
            return;
        }
        showToastShort("geo");
    }

    @Override
    public void OnCenterItemClick(BottomDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.baiduMap:
                invokingBD();
                break;
            case R.id.gaodeMap:
                invokingGD();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转至高德地图
     */
    public void invokingGD() {
        //  com.autonavi.minimap这是高德地图的包名

        double[] lt = GpsUtil.bd09_To_Gcj02(endposition.latitude, endposition.longitude);
        Intent intent = new Intent();
        intent.setData(Uri.parse("androidamap://route?" +
                "sourceApplication=softname" +
                "&sname=我的位置" +
                "&dlat=" + lt[0] + "&dlon=" + lt[1] +
                "&dname=" + address +
                "&dev=0&m=0&t=0"));
        if (isInstallByread("com.autonavi.minimap")) {
            startActivity(intent);
            Log.e("GasStation", "高德地图客户端已经安装");
        } else {
            Toast.makeText(mContext, "没有安装高德地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 启动百度地图驾车路线规划
     */
    public void invokingBD() {

        //  com.baidu.BaiduMap这是百度地图的包名
        Intent intent = null;
        try {
            intent = Intent.getIntent("intent://map/direction?" +
                    //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                    "destination=latlng:" + endposition.latitude + "," + endposition.longitude + "|name:" + address +        //终点
                    "&mode=driving&" +          //导航路线方式
                    //                            "region=北京" +           //
                    //                            "&src=慧医" +
                    "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                startActivity(intent); //启动调用
                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
                Toast.makeText(mContext, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
//步行
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
//公交
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
//地铁
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
//驾车
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showToastShort("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            if (result.getRouteLines().size() > 1) {
                nowResultdrive = result;
                route = nowResultdrive.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mbaiduMap);
                mbaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(nowResultdrive.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mbaiduMap);
                routeOverlay = overlay;
                mbaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
//室内
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
//自行车
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mbaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                ShowLog.e(ll.latitude + "|" + ll.longitude + "");
                mPosition = ll;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mbaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

    }

    /**
     * 动画到定位位置
     *
     * @param ll
     */
    private void animation2Pos(LatLng ll) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mbaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    protected void onPause() {
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
        // Geo搜索
        mGeoSearch.geocode(new GeoCodeOption().city(
                "东莞").address(address));
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }



    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // activity 销毁时同时销毁地图控件
        MapView.setMapCustomEnable(false);
        // 当不需要定位图层时关闭定位图层
        mbaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView = null;
        mGeoSearch.destroy();
        super.onDestroy();
    }

    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
}
