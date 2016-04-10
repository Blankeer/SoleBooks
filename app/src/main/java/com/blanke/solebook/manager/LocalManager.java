package com.blanke.solebook.manager;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blanke.solebook.constants.Constants;
import com.socks.library.KLog;

/**
 * Created by blanke on 16-4-3.
 */
public class LocalManager {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private Context context;
    public AMapLocationClientOption mLocationOption = null;
    private int count = 0;//定位次数,重试N次

    public LocalManager(Context context) {
        this.context = context.getApplicationContext();
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
    }

    public void start(CallBack callBack) {
        if (callBack == null) {
            return;
        }
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        KLog.d("amap success:" + aMapLocation.toString());
                        callBack.onSuccess(aMapLocation);
                        count = 0;
                    } else {
                        count++;
                        KLog.d("amap error:" + aMapLocation.getErrorCode() + "," + aMapLocation.getErrorInfo());
                        if (count == Constants.TRY_LOCAL_COUNT) {
                            count = 0;
                            callBack.onError(aMapLocation.getErrorInfo());
                        } else {
                            mLocationClient.startLocation();
                        }
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    public void stop() {
        mLocationClient.stopAssistantLocation();
        mLocationClient.onDestroy();
    }

    public interface CallBack {
        void onSuccess(AMapLocation location);

        void onError(String msg);
    }
}
