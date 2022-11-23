package com.cloud.core.amap.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.StreetNumber;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cloud.core.ObjectJudge;
import com.cloud.core.R;
import com.cloud.core.amap.AMapUtils;
import com.cloud.core.amap.AmapErros;
import com.cloud.core.amap.AmapViewUtils;
import com.cloud.core.amap.LocationInfo;
import com.cloud.core.amap.events.OnRetrieveViewListener;
import com.cloud.core.cache.RxCache;
import com.cloud.core.ebus.EBus;
import com.cloud.core.ebus.SubscribeEBus;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.SoftUtils;
import com.cloud.core.utils.ToastUtils;
import com.cloud.core.view.VariableButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/30
 * Description:检索fragment
 * Modifier:
 * ModifyContent:
 */
public class RetrieveFragment extends Fragment implements View.OnClickListener,
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerDragListener, PoiSearch.OnPoiSearchListener,
        AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapLoadedListener {

    private String hint = "";
    private ImageView returnIv = null;
    private TextView selectedCityTv = null;
    private EditText searchEt = null;
    private LinearLayout tipLl = null;
    private ImageView tipCloseIv = null;
    private VariableButton searchVb = null;
    private OnRetrieveViewListener onRetrieveViewListener = null;
    private AMap mAMap;
    //检索分页索引
    private int currentPage = 1;
    //定位城市
    private String locationCity = "杭州";
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private AmapViewUtils amapViewUtils = new AmapViewUtils();
    private Marker marker = null;
    private GeocodeSearch geocoderSearch = null;
    private RegeocodeQuery regeocodeQuery = null;

    /**
     * 设置amap检索监听
     *
     * @param onRetrieveViewListener amap检索监听
     */
    public void setOnRetrieveViewListener(OnRetrieveViewListener onRetrieveViewListener) {
        this.onRetrieveViewListener = onRetrieveViewListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cl_retrieve_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindArguments();
        EBus.getInstance().registered(this);

        searchEt.setHint(hint);
        //显示提示
        boolean tipFlag = RxCache.getCacheFlag(getContext(), "cl_retrieve_tip_flag", true, false);
        if (!tipFlag) {
            tipLl.setVisibility(View.VISIBLE);
        }
        //显示定位城市
        selectedCityTv.setText(locationCity);

        if (onRetrieveViewListener != null) {
            onRetrieveViewListener.onAmapRenderStart();
        }

        //初始化map
        initMap();

        mapUtils.getLocation(getContext());
    }

    private AMapUtils mapUtils = new AMapUtils() {
        @Override
        protected void onLocationSuccess(LocationInfo locationInfo) {
            LatLng latLng = new LatLng(locationInfo.getLatitude(), locationInfo.getLongitude());
            //添加标记
            marker = amapViewUtils.addMarker(getContext(), mAMap, latLng, R.mipmap.cl_location_icon, true);
            //设置中心点和缩放比例
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onAmapRenderEnd();
            }
        }
        //locationByCityName(locationCity);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EBus.getInstance().unregister(this);
        mapUtils.destroy();
    }

    private void initView(View view) {
        returnIv = (ImageView) view.findViewById(R.id.return_iv);
        returnIv.setOnClickListener(this);
        selectedCityTv = (TextView) view.findViewById(R.id.selected_city_tv);
        selectedCityTv.setOnClickListener(this);
        searchEt = (EditText) view.findViewById(R.id.search_et);
        searchVb = (VariableButton) view.findViewById(R.id.search_vb);
        searchVb.setOnClickListener(this);
        tipLl = (LinearLayout) view.findViewById(R.id.tip_ll);
        tipCloseIv = (ImageView) tipLl.findViewById(R.id.tip_close_iv);
        tipCloseIv.setOnClickListener(this);
    }

    private void bindArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        //检索hint
        if (bundle.containsKey("hint")) {
            hint = bundle.getString("hint");
        }
        //定位城市
        if (bundle.containsKey("locationCity")) {
            locationCity = bundle.getString("locationCity");
        }
    }

    private void initMap() {
        Fragment fragment = this.getChildFragmentManager().findFragmentById(R.id.map_ft);
        if (fragment == null) {
            return;
        }
        SupportMapFragment supportMapFragment =SupportMapFragment.newInstance();
        mAMap = supportMapFragment.getMap();
        if (mAMap == null) {
            return;
        }

        //添加点击marker监听事件
        mAMap.setOnMarkerClickListener(this);
        //添加显示infowindow监听事件
        mAMap.setInfoWindowAdapter(this);
        //添加拖动监听
        mAMap.setOnMarkerDragListener(this);
        //设置地图拖动监听
        mAMap.setOnCameraChangeListener(this);
        //地图渲染监听
        mAMap.setOnMapLoadedListener(this);
        //地址逆解析
        try {
            geocoderSearch = new GeocodeSearch(getContext());
        } catch (AMapException e) {
            e.printStackTrace();
        }
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();
            if (id == R.id.return_iv) {
                if (onRetrieveViewListener != null) {
                    onRetrieveViewListener.onReturnClick();
                }
            } else if (id == R.id.selected_city_tv) {
                if (onRetrieveViewListener != null) {
                    onRetrieveViewListener.onSelectCityClick();
                }
            } else if (id == R.id.tip_close_iv) {
                //一天内无需再显示
                RxCache.setCacheObject(getContext(), "cl_retrieve_tip_flag", true, 1, TimeUnit.DAYS);
                tipLl.setVisibility(View.GONE);
            } else if (id == R.id.search_vb) {
                SoftUtils.hideSoftInput(getContext(), searchEt);
                //检索
                Editable searchEtText = searchEt.getText();
                String keywords = searchEtText.toString();
                if (!TextUtils.isEmpty(keywords)) {
                    if (onRetrieveViewListener != null) {
                        onRetrieveViewListener.onPoiSearchStart();
                    }
                    doSearchQuery(keywords);
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private void doSearchQuery(String keywords) {
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keywords, "", locationCity);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(30);
        // 设置查第一页
        query.setPageNum(currentPage);

        try {
            poiSearch = new PoiSearch(getContext(), query);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @SubscribeEBus(receiveKey = "cl_amap_selected_city_name")
    public void onSelectedCity(String cityName) {
        this.locationCity = cityName;
        selectedCityTv.setText(cityName);
        locationByCityName(cityName);
    }

    private void locationByCityName(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return;
        }
        GeocodeSearch geocodeSearch = null;
        try {
            geocodeSearch = new GeocodeSearch(getContext());
        } catch (AMapException e) {
            e.printStackTrace();
        }
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {
                if (code != 1000 || geocodeResult == null) {
                    return;
                }
                List<GeocodeAddress> addressList = geocodeResult.getGeocodeAddressList();
                if (ObjectJudge.isNullOrEmpty(addressList)) {
                    return;
                }
                GeocodeAddress address = addressList.get(0);
                LatLonPoint latLonPoint = address.getLatLonPoint();
                LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                //添加标记
                marker = amapViewUtils.addMarker(getContext(), mAMap, latLng, R.mipmap.cl_location_icon, true);
                //设置中心点和缩放比例
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));

                if (onRetrieveViewListener != null) {
                    onRetrieveViewListener.onAmapRenderEnd();
                }
            }
        });
        GeocodeQuery geocodeQuery = new GeocodeQuery(cityName.trim(), cityName.trim());
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (onRetrieveViewListener != null) {
            LatLng latLng = marker.getPosition();
            LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            regeocodeQuery = new RegeocodeQuery(latLonPoint, 25, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(regeocodeQuery);
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        if (code != 1000) {
            String message = AmapErros.getAmapErrosMessage(code);
            ToastUtils.showLong(getContext(), message);
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onPoiSearchEnd();
            }
            return;
        }
        if (poiResult == null || poiResult.getQuery() == null) {
            ToastUtils.showLong(getContext(), "没有搜索到相关数据！");
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onPoiSearchEnd();
            }
            return;
        }
        if (!poiResult.getQuery().equals(query)) {
            //搜索条件不一致
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onPoiSearchEnd();
            }
            return;
        }
        List<PoiItem> pois = poiResult.getPois();
        if (ObjectJudge.isNullOrEmpty(pois)) {
            ToastUtils.showLong(getContext(), "没有搜索到相关数据！");
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onPoiSearchEnd();
            }
            return;
        }
        if (onRetrieveViewListener != null) {
            onRetrieveViewListener.onPoiSearchSuccess(pois);
            onRetrieveViewListener.onPoiSearchEnd();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int code) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //拖动结束后将标记移到屏幕中心
        if (marker != null && !marker.isRemoved() && marker.isDraggable()) {
            marker.remove();
        }
        LatLng latLng = cameraPosition.target;
        //添加标记
        marker = amapViewUtils.addMarker(getContext(), mAMap, latLng, R.mipmap.cl_location_icon, true);
        //设置中心点和缩放比例
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        //查询详情信息
        if (onRetrieveViewListener != null) {
            LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            regeocodeQuery = new RegeocodeQuery(latLonPoint, 25, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(regeocodeQuery);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
        if (code != 1000) {
            return;
        }
        if (regeocodeResult == null || regeocodeResult.getRegeocodeQuery() == null) {
            return;
        }
        if (!regeocodeResult.getRegeocodeQuery().equals(regeocodeQuery)) {
            //搜索条件不一致
            return;
        }
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        if (regeocodeAddress == null) {
            return;
        }
        LatLonPoint queryPoint = regeocodeQuery.getPoint();
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setProvince(regeocodeAddress.getProvince());
        locationInfo.setCity(regeocodeAddress.getCity());
        locationInfo.setDistrict(regeocodeAddress.getDistrict());
        locationInfo.setCountry(regeocodeAddress.getCountry());
        //address去掉省-市-区
        //替换省
        locationInfo.setAddress(regeocodeAddress.getFormatAddress().replaceFirst(regeocodeAddress.getProvince(), ""));
        //替换市
        locationInfo.setAddress(locationInfo.getAddress().replaceFirst(regeocodeAddress.getCity(), ""));
        //替换区
        locationInfo.setAddress(locationInfo.getAddress().replaceFirst(regeocodeAddress.getDistrict(), ""));

        locationInfo.setLatitude(queryPoint.getLatitude());
        locationInfo.setLongitude(queryPoint.getLongitude());
        //如果未取到街道则从详情地址中取
        StreetNumber streetNumber = regeocodeAddress.getStreetNumber();
        if (TextUtils.isEmpty(streetNumber.getStreet())) {
            locationInfo.setAddress(locationInfo.getAddress().replace(locationInfo.getProvince() + locationInfo.getCity() + locationInfo.getDistrict(), ""));
        } else {
            locationInfo.setStreet(streetNumber.getStreet());
            locationInfo.setStreetNum(streetNumber.getNumber());
        }
        //重新设置定位城市
        if (!TextUtils.equals(locationCity, locationInfo.getCity())) {
            locationCity = locationInfo.getCity();
            selectedCityTv.setText(locationCity);
        }
        //当前信息回调
        onRetrieveViewListener.onCurrentLocationCall(locationInfo);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {

    }

    @Override
    public void onMapLoaded() {
        if (onRetrieveViewListener != null) {
            onRetrieveViewListener.onAmapRenderEnd();
        }
    }
}
