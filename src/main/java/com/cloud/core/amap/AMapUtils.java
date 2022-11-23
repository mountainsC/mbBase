package com.cloud.core.amap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.model.LatLng;
import com.cloud.core.ObjectJudge;
import com.cloud.core.cache.RxCache;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.JsonUtils;

import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/9
 * @Description:高德定位工具类
 * @Modifier:
 * @ModifyContent:
 */
public class AMapUtils {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;
    private static boolean isGetedLocation = false;
    private String locCacheKey = "d641c307-9eb9-48c6-885a-26fccbad34ef";
    private Context context = null;

    protected void onLocationSuccess(LocationInfo locationInfo) {

    }

    public void getLocation(Context context) {
        try {
            //如果本地经纬度不为空则不重新初始化
            //下面会每隔一定时间重新定位
            this.context = context;
            String locJson = RxCache.getCacheData(context, locCacheKey, true);
            if (TextUtils.isEmpty(locJson)) {
                amapLocation(context);
            } else {
                LocationInfo locationInfo = JsonUtils.parseT(locJson, LocationInfo.class);
                if (locationInfo == null ||
                        locationInfo.getLongitude() == -1 ||
                        locationInfo.getLatitude() == -1) {
                    amapLocation(context);
                } else {
                    onLocationSuccess(locationInfo);
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private void amapLocation(Context context) {
        isGetedLocation = false;
        AMapLocationClient.updatePrivacyShow(context,true,true);
        AMapLocationClient.updatePrivacyAgree(context,true);
        try {
            locationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        locationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        // 设置定位监听
        locationClient.setLocationListener(aMapLocationListener);
        // 创建Intent对象，action为LOCATION
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();
        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        start();
    }

    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation == null) {
                return;
            }
            bindLocationInfo(aMapLocation);
        }
    };

    private void bindLocationInfo(AMapLocation aMapLocation) {
        try {
            if (aMapLocation.getLatitude() == 0 || aMapLocation.getLongitude() == 0) {
                return;
            }
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatitude(aMapLocation.getLatitude());
            locationInfo.setLongitude(aMapLocation.getLongitude());
            locationInfo.setAddress(aMapLocation.getAddress());
            locationInfo.setCountry(aMapLocation.getCountry());
            locationInfo.setProvince(aMapLocation.getProvince());
            locationInfo.setCity(aMapLocation.getCity());
            locationInfo.setDistrict(aMapLocation.getDistrict());
            locationInfo.setStreet(aMapLocation.getStreet());
            locationInfo.setStreetNum(aMapLocation.getStreetNum());
            locationInfo.setFloor(aMapLocation.getFloor());
            locationInfo.setGpsAccuracyStatus(aMapLocation.getGpsAccuracyStatus());
            RxCache.setCacheData(context, locCacheKey, JsonUtils.toStr(locationInfo), 5, TimeUnit.MINUTES);
            if (!isGetedLocation) {
                stop();
                onLocationSuccess(locationInfo);
                isGetedLocation = true;
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private void start() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        locationOption.setInterval(5000);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        if (alarm != null) {
            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2 * 1000,
                    50000, alarmPi);
        }
    }

    private void stop() {
        // 停止定位
        if (locationClient != null) {
            locationClient.stopLocation();
        }
        //停止定位的时候取消闹钟
        if (alarm != null) {
            alarm.cancel(alarmPi);
        }
    }

    public void destroy() {
        try {
            stop();
            if (locationClient != null) {
                locationClient.onDestroy();
                locationClient = null;
                locationOption = null;
            }
            if (alarmReceiver != null && context != null) {
                if (ObjectJudge.isRegisterBroadcast(context, "LOCATION")) {
                    context.unregisterReceiver(alarmReceiver);
                }
                alarmReceiver = null;
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOCATION")) {
                if (null != locationClient) {
                    locationClient.startLocation();
                }
            }
        }
    };

    /**
     * 坐标转换,支持GPS/Mapbar/Baidu经纬度
     *
     * @param context
     * @param sourceLatLng GPS/Mapbar/Baidu经纬度
     * @return
     */
    public static LatLng toDPoint(Context context, LatLng sourceLatLng) {
        try {
            CoordinateConverter converter = new CoordinateConverter(context);
            // CoordType.GPS 待转换坐标类型
            converter.from(CoordinateConverter.CoordType.GPS);
            // sourceLatLng待转换坐标点 DPoint类型
            DPoint dPoint = new DPoint();
            dPoint.setLatitude(sourceLatLng.latitude);
            dPoint.setLongitude(sourceLatLng.longitude);
            converter.coord(dPoint);
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            LatLng latLng = new LatLng(desLatLng.getLatitude(), desLatLng.getLongitude());
            return latLng;
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return null;
    }

    /**
     * 判断是否有效的坐标
     *
     * @param context
     * @param latitude  纬度
     * @param longitude 经度
     * @return
     */
    public static boolean isAMapDataAvailable(Context context, double latitude, double longitude) {
        //返回true代表当前位置在大陆、港澳地区，反之不在。
        CoordinateConverter converter = new CoordinateConverter(context);
        //第一个参数为纬度，第二个为经度，纬度和经度均为高德坐标系。
        boolean isAMapDataAvailable = converter.isAMapDataAvailable(latitude, longitude);
        return isAMapDataAvailable;
    }

    /**
     * 计算两点间距离,单位：米
     *
     * @param startLatlng 开始经纬度
     * @param endLatlng   结束经纬度
     * @return 返回n米
     */
    public static float getDistance(LatLng startLatlng, LatLng endLatlng) {
        DPoint start = new DPoint();
        start.setLatitude(startLatlng.latitude);
        start.setLongitude(startLatlng.longitude);
        DPoint end = new DPoint();
        end.setLatitude(endLatlng.latitude);
        end.setLongitude(endLatlng.longitude);
        float distance = CoordinateConverter.calculateLineDistance(start, end);
        return distance;
    }
}
