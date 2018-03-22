package com.yj.shopapp.baidu;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by jm on 2016/5/30.
 * 百度定位
 */
public class BaiduTool {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    BaiduClient mBaiduClient;

    public BaiduTool(Context context, BaiduClient baiduClient){
        mLocationClient = new LocationClient(context);     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        mBaiduClient = baiduClient;
        initLocation();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }

    public void start(){
        mLocationClient.start();
    }

    public void over(){
        mLocationClient.stop();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);

            if(location.getLocType()==61||location.getLocType()==161){ //成功
                String address = location.getAddress().address;
                mBaiduClient.report(0,address);
                mBaiduClient.getLocation(location);
            }else{
                mBaiduClient.report(1,location.getLocType()+"");
            }
            over();
        }
    }


    public interface BaiduClient{
        void report(int status,String value);
        void getLocation(BDLocation location);
    }

}
