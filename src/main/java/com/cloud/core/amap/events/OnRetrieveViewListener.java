package com.cloud.core.amap.events;

import com.amap.api.services.core.PoiItem;
import com.cloud.core.amap.LocationInfo;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/30
 * Description:检索视图监听
 * Modifier:
 * ModifyContent:
 */
public interface OnRetrieveViewListener {
    /**
     * 地图渲染开始
     */
    public void onAmapRenderStart();

    /**
     * 地图渲染结束
     */
    public void onAmapRenderEnd();

    /**
     * 页面返回点击监听
     */
    public void onReturnClick();

    /**
     * 选择城市点击监听
     */
    public void onSelectCityClick();

    /**
     * 检索开始监听
     */
    public void onPoiSearchStart();

    /**
     * 检索结束监听
     */
    public void onPoiSearchEnd();

    /**
     * 检索成功
     *
     * @param poiItems 检索数据项
     */
    public void onPoiSearchSuccess(List<PoiItem> poiItems);

    /**
     * 当前定位回调
     *
     * @param locationInfo 位置信息
     */
    public void onCurrentLocationCall(LocationInfo locationInfo);
}
