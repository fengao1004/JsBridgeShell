package com.dayang.cmtools.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by 冯傲 on 2017/3/21.
 * e-mail 897840134@qq.com
 */

public class LocationUtil {
    Context context;
    static LocationUtil locationUtil;
    private double longitude;
    private String address;
    private String userName;
    private String url;
    private String userId;
    private String tenantId;
    private double latitude;
    int locationModel;
    static final int LOCATIONING = 213;
    static final int NOTLOCATION = 12433;
    static final int LOCATIONSTOP = 41;
    static final int ONCELOCATIONING = 124;
    static final int ONCELOCATIONED = 1241;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                    address = aMapLocation.getAddress();
                    if (locationListener != null) {
                        locationModel = ONCELOCATIONED;
                        locationListener.onLocationListener(latitude, longitude, address);
                        locationListener = null;
                    } else {
                        //TODO 发送GPS数据 后期需更换网络请求框架
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                        String deviceId = tm.getDeviceId();
                        UserLocation user = new UserLocation(deviceId, userName, longitude + "", latitude + "", "Android", userId, tenantId);
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        try {
                            OkHttpUtil okHttpUtil = new OkHttpUtil();
                            okHttpUtil.call(url, json, new OkHttpUtil.OkHttpCallBack() {
                                @Override
                                public void success(Response response) throws Exception {
                                    response.body().close();
                                }

                                @Override
                                public void error(Request request, IOException e) {

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    LogUtils.e("location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    if (locationListener != null) {
                        locationListener.onLocationListener(0, 0, "");
                        locationListener = null;
                    }
                }
            }
        }
    };
    private LocationListener locationListener;


    private LocationUtil(Context context) {
        this.context = context;
        mLocationClient = new AMapLocationClient(context);
    }

    public static LocationUtil getInstance(Context context, String username, String url, String tenantId, String userId) {
        if (locationUtil == null) {
            locationUtil = new LocationUtil(context);
            locationUtil.locationModel = NOTLOCATION;
        }
        locationUtil.userName = username;
        locationUtil.url = url;
        locationUtil.tenantId = tenantId;
        locationUtil.userId = userId;
        return locationUtil;
    }
    public static LocationUtil getInstance(Context context) {
        if (locationUtil == null) {
            locationUtil = new LocationUtil(context);
            locationUtil.locationModel = NOTLOCATION;
        }
        return locationUtil;
    }
    public boolean startLocation() {
        if (mLocationClient != null && (locationModel == NOTLOCATION || locationModel == ONCELOCATIONED)) {
            mLocationClient.setLocationListener(mLocationListener);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(false);
            mLocationOption.setHttpTimeOut(30000);
            mLocationOption.setInterval(60 * 1000);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
            locationModel = LOCATIONING;
            return true;
        } else if (mLocationClient != null && locationModel == LOCATIONSTOP) {
            mLocationClient.startLocation();
            locationModel = LOCATIONING;
            return true;
        }
        return false;
    }

    public void stopLocation() {
        if (mLocationClient != null && locationModel == LOCATIONING) {
            mLocationClient.stopLocation();
            locationModel = LOCATIONSTOP;
        }
    }

    public void getLocations(LocationListener locationListener) {
        if (mLocationClient != null && locationModel == LOCATIONING) {
            locationListener.onLocationListener(latitude, longitude, address);
        } else if (mLocationClient != null && (locationModel == LOCATIONSTOP || locationModel == NOTLOCATION || locationModel == ONCELOCATIONED)) {
            locationModel = ONCELOCATIONING;
            this.locationListener = locationListener;
            mLocationClient.setLocationListener(mLocationListener);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            mLocationOption.setHttpTimeOut(30000);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    public interface LocationListener {
        void onLocationListener(double latitude, double longitude, String address);
    }

    class UserLocation {
        public UserLocation(String deviceId, String userName, String longitude, String latitude, String osInfos, String userId, String tenantId) {
            this.deviceId = deviceId;
            this.userId = userId;
            this.userName = userName;
            this.lantitude = latitude;
            this.longitude = longitude;
            this.osInfos = osInfos;
            this.tenantId = tenantId;
        }

        String tenantId;
        String deviceId;
        String userName;
        String longitude;
        String lantitude;
        String osInfos;
        String userId;
    }
}
