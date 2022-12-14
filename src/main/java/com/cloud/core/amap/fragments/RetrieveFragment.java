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
 * Description:??????fragment
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
    //??????????????????
    private int currentPage = 1;
    //????????????
    private String locationCity = "??????";
    private PoiSearch.Query query;// Poi???????????????
    private PoiSearch poiSearch;// POI??????
    private AmapViewUtils amapViewUtils = new AmapViewUtils();
    private Marker marker = null;
    private GeocodeSearch geocoderSearch = null;
    private RegeocodeQuery regeocodeQuery = null;

    /**
     * ??????amap????????????
     *
     * @param onRetrieveViewListener amap????????????
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
        //????????????
        boolean tipFlag = RxCache.getCacheFlag(getContext(), "cl_retrieve_tip_flag", true, false);
        if (!tipFlag) {
            tipLl.setVisibility(View.VISIBLE);
        }
        //??????????????????
        selectedCityTv.setText(locationCity);

        if (onRetrieveViewListener != null) {
            onRetrieveViewListener.onAmapRenderStart();
        }

        //?????????map
        initMap();

        mapUtils.getLocation(getContext());
    }

    private AMapUtils mapUtils = new AMapUtils() {
        @Override
        protected void onLocationSuccess(LocationInfo locationInfo) {
            LatLng latLng = new LatLng(locationInfo.getLatitude(), locationInfo.getLongitude());
            //????????????
            marker = amapViewUtils.addMarker(getContext(), mAMap, latLng, R.mipmap.cl_location_icon, true);
            //??????????????????????????????
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
        //??????hint
        if (bundle.containsKey("hint")) {
            hint = bundle.getString("hint");
        }
        //????????????
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

        //????????????marker????????????
        mAMap.setOnMarkerClickListener(this);
        //????????????infowindow????????????
        mAMap.setInfoWindowAdapter(this);
        //??????????????????
        mAMap.setOnMarkerDragListener(this);
        //????????????????????????
        mAMap.setOnCameraChangeListener(this);
        //??????????????????
        mAMap.setOnMapLoadedListener(this);
        //???????????????
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
                //????????????????????????
                RxCache.setCacheObject(getContext(), "cl_retrieve_tip_flag", true, 1, TimeUnit.DAYS);
                tipLl.setVisibility(View.GONE);
            } else if (id == R.id.search_vb) {
                SoftUtils.hideSoftInput(getContext(), searchEt);
                //??????
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
        // ????????????????????????????????????????????????????????????poi????????????????????????????????????poi??????????????????????????????????????????
        query = new PoiSearch.Query(keywords, "", locationCity);
        // ?????????????????????????????????poiitem
        query.setPageSize(30);
        // ??????????????????
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
                //????????????
                marker = amapViewUtils.addMarker(getContext(), mAMap, latLng, R.mipmap.cl_location_icon, true);
                //??????????????????????????????
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
            ToastUtils.showLong(getContext(), "??????????????????????????????");
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onPoiSearchEnd();
            }
            return;
        }
        if (!poiResult.getQuery().equals(query)) {
            //?????????????????????
            if (onRetrieveViewListener != null) {
                onRetrieveViewListener.onPoiSearchEnd();
            }
            return;
        }
        List<PoiItem> pois = poiResult.getPois();
        if (ObjectJudge.isNullOrEmpty(pois)) {
            ToastUtils.showLong(getContext(), "??????????????????????????????");
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
        //??????????????????????????????????????????
        if (marker != null && !marker.isRemoved() && marker.isDraggable()) {
            marker.remove();
        }
        LatLng latLng = cameraPosition.target;
        //????????????
        marker = amapViewUtils.addMarker(getContext(), mAMap, latLng, R.mipmap.cl_location_icon, true);
        //??????????????????????????????
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        //??????????????????
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
            //?????????????????????
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
        //address?????????-???-???
        //?????????
        locationInfo.setAddress(regeocodeAddress.getFormatAddress().replaceFirst(regeocodeAddress.getProvince(), ""));
        //?????????
        locationInfo.setAddress(locationInfo.getAddress().replaceFirst(regeocodeAddress.getCity(), ""));
        //?????????
        locationInfo.setAddress(locationInfo.getAddress().replaceFirst(regeocodeAddress.getDistrict(), ""));

        locationInfo.setLatitude(queryPoint.getLatitude());
        locationInfo.setLongitude(queryPoint.getLongitude());
        //?????????????????????????????????????????????
        StreetNumber streetNumber = regeocodeAddress.getStreetNumber();
        if (TextUtils.isEmpty(streetNumber.getStreet())) {
            locationInfo.setAddress(locationInfo.getAddress().replace(locationInfo.getProvince() + locationInfo.getCity() + locationInfo.getDistrict(), ""));
        } else {
            locationInfo.setStreet(streetNumber.getStreet());
            locationInfo.setStreetNum(streetNumber.getNumber());
        }
        //????????????????????????
        if (!TextUtils.equals(locationCity, locationInfo.getCity())) {
            locationCity = locationInfo.getCity();
            selectedCityTv.setText(locationCity);
        }
        //??????????????????
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
