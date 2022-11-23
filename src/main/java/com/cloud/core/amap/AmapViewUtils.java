package com.cloud.core.amap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cloud.core.ObjectJudge;
import com.cloud.core.events.Action3;
import com.cloud.core.okrx.OkRxManager;
import com.cloud.core.okrx.properties.ReqQueueItem;
import com.cloud.core.utils.PixelUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/28
 * @Description:map视图工具类
 * @Modifier:
 * @ModifyContent:
 */
public class AmapViewUtils {

    private AMap aMap = null;
    private MapView mapView = null;

    protected void onMapLoadSuccess() {
        //加载成功回调
    }

    protected void onMarkerClickCall(Marker marker) {
        //在标记上单击回调
    }

    protected void onInfoWindowClickCall(Marker marker) {
        //在info window 单击回调
    }

    protected View onBuildInfoWindow(Marker marker) {
        return null;
    }

    public void initializeMap(MapView mapView, Bundle savedInstanceState) {
        if (mapView == null) {
            return;
        }
        this.mapView = mapView;
        aMap = mapView.getMap();
        //设置marker可拖拽事件监听器
        aMap.setOnMarkerDragListener(markerDragListener);
        //设置amap加载成功事件监听器
        aMap.setOnMapLoadedListener(mapLoadedListener);
        //设置点击marker事件监听器
        aMap.setOnMarkerClickListener(markerClickListener);
        //设置点击infoWindow事件监听器
        aMap.setOnInfoWindowClickListener(infoWindowClickListener);
        //设置自定义InfoWindow样式
        aMap.setInfoWindowAdapter(infoWindowAdapter);
        mapView.onCreate(savedInstanceState);
    }

    private AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            onMarkerClickCall(marker);
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                return true;
            }
            return false;
        }
    };

    private AMap.OnMapLoadedListener mapLoadedListener = new AMap.OnMapLoadedListener() {
        @Override
        public void onMapLoaded() {
            onMapLoadSuccess();
        }
    };

    private AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
            //开始拖动
        }

        @Override
        public void onMarkerDrag(Marker marker) {
//        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
//                + marker.getPosition().latitude + ","
//                + marker.getPosition().longitude + ")";
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            //结束拖动
        }
    };

    private AMap.OnInfoWindowClickListener infoWindowClickListener = new AMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            }
            onInfoWindowClickCall(marker);
        }
    };

    private AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return onBuildInfoWindow(marker);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };

    public void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    public AMap getMap() {
        return this.aMap;
    }

    /**
     * 获取图片BitmapDescriptor对象
     *
     * @param bitmap
     * @return
     */
    public BitmapDescriptor getBitmapDescriptor(Bitmap bitmap) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return bitmapDescriptor;
    }

    /**
     * 获取图片BitmapDescriptor对象
     *
     * @param context
     * @param resId
     * @return
     */
    public BitmapDescriptor getBitmapDescriptor(Context context, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return getBitmapDescriptor(bitmap);
    }

    /**
     * 清空标记
     */
    public void clearMarker() {
        if (this.aMap == null) {
            return;
        }
        this.aMap.clear();
    }

    private class AddRemoteIconMarker implements Runnable {

        private Context context = null;
        private MarkerInfo markerInfo = null;

        public AddRemoteIconMarker(Context context, MarkerInfo markerInfo) {
            this.context = context;
            this.markerInfo = markerInfo;
        }

        @Override
        public void run() {
            OkRxManager.getInstance().getBitmap(context,
                    markerInfo.getIconUrl(),
                    null,
                    null,
                    new Action3<Bitmap, String, HashMap<String, ReqQueueItem>>() {
                        @Override
                        public void call(Bitmap bitmap, String s, HashMap<String, ReqQueueItem> stringReqQueueItemHashMap) {
                            BitmapDescriptor bitmapDescriptor = getBitmapDescriptor(bitmap);
                            if (bitmapDescriptor == null) {
                                return;
                            }
                            addMarkerFromBD(aMap, bitmapDescriptor, markerInfo);
                        }
                    },
                    null,
                    "",
                    null);
        }
    }

    /**
     * 添加标记
     *
     * @param markerInfo 标记属性
     */
    public void addMarker(MarkerInfo markerInfo) {
        if (this.mapView == null ||
                this.aMap == null ||
                markerInfo == null ||
                (markerInfo.getIcon() == 0 && TextUtils.isEmpty(markerInfo.getIconUrl())) ||
                markerInfo.getPosition() == null ||
                (TextUtils.isEmpty(markerInfo.getTitle()) && TextUtils.isEmpty(markerInfo.getDes()))) {
            return;
        }
        if (markerInfo.getIcon() == 0) {
            AddRemoteIconMarker remoteIconMarker = new AddRemoteIconMarker(mapView.getContext(), markerInfo);
            remoteIconMarker.run();
        } else {
            BitmapDescriptor bitmapDescriptor = getBitmapDescriptor(mapView.getContext(), markerInfo.getIcon());
            if (bitmapDescriptor == null) {
                return;
            }
            addMarkerFromBD(aMap, bitmapDescriptor, markerInfo);
        }
    }

    private Marker addMarkerFromBD(AMap aMap, BitmapDescriptor bitmapDescriptor, MarkerInfo markerInfo) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.anchor(0.5f, 0.5f);
        markerOption.setFlat(false);
        markerOption.position(markerInfo.getPosition());
        markerOption.title(markerInfo.getTitle());
        markerOption.snippet(markerInfo.getDes());
        markerOption.icon(bitmapDescriptor);
        return aMap.addMarker(markerOption);
    }

    /**
     * 在地图上添加标记
     *
     * @param context     上下文
     * @param aMap        地图对象
     * @param position    经纬度
     * @param icon        位置图标
     * @param isDraggable 此标记是否可拖动
     */
    public Marker addMarker(Context context, AMap aMap, LatLng position, int icon, boolean isDraggable) {
        MarkerInfo markerInfo = new MarkerInfo();
        markerInfo.setIcon(icon);
        markerInfo.setPosition(position);
        markerInfo.setDraggable(isDraggable);
        BitmapDescriptor bitmapDescriptor = getBitmapDescriptor(context, markerInfo.getIcon());
        if (bitmapDescriptor == null) {
            return null;
        }
        return addMarkerFromBD(aMap, bitmapDescriptor, markerInfo);
    }

    /**
     * 设置地址围栏
     * 在规定屏幕范围内的地图经纬度范围
     * 一般在地图加载成功后onMapLoadSuccess中使用
     *
     * @param latLngs 经纬度列表
     */
    public void setLatLngBounds(List<LatLng> latLngs) {
        if (this.mapView == null || this.aMap == null) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (ObjectJudge.isNullOrEmpty(latLngs)) {
            return;
        }
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds build = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build, PixelUtils.dip2px(mapView.getContext(), 60)));
    }
}
