package com.cloud.core.amap;

import com.amap.api.maps.model.LatLng;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/29
 * @Description:标记信息
 * @Modifier:
 * @ModifyContent:
 */
public class MarkerInfo {
    /**
     * 标题
     */
    private String title = "";
    /**
     * 描述信息
     */
    private String des = "";
    /**
     * 标记坐标
     */
    private LatLng position = null;
    /**
     * 标记图标
     */
    private int icon = 0;
    /**
     * iconurl地址
     */
    private String iconUrl = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private boolean isDraggable = false;

    public MarkerInfo() {

    }

    public MarkerInfo(String title, String des, LatLng position, int icon, int id) {
        this.title = title;
        this.des = des;
        this.position = position;
        this.icon = icon;
        this.id = id;
    }

    public MarkerInfo(String title, String des, LatLng position, String iconUrl) {
        this.title = title;
        this.des = des;
        this.position = position;
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public void setDraggable(boolean draggable) {
        isDraggable = draggable;
    }
}
