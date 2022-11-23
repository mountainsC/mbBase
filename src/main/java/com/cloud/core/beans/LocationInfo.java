package com.cloud.core.beans;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/9
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class LocationInfo {

    /**
     * 经度
     */
    private double longitude = -1;
    /**
     * 纬度
     */
    private double latitude = -1;
    /**
     * 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息
     */
    private String address = "";
    /**
     * 国家信息
     */
    private String country = "";
    /**
     * 省信息
     */
    private String province = "";
    /**
     * 市信息
     */
    private String city = "";
    /**
     * 城区信息
     */
    private String district = "";
    /**
     * 街道信息
     */
    private String street = "";
    /**
     * 街道门牌号信息
     */
    private String streetNum = "";
    /**
     * 获取当前室内定位楼层
     */
    private String floor = "";
    /**
     * 获取GPS的当前状态
     */
    private int gpsAccuracyStatus = 0;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getGpsAccuracyStatus() {
        return gpsAccuracyStatus;
    }

    public void setGpsAccuracyStatus(int gpsAccuracyStatus) {
        this.gpsAccuracyStatus = gpsAccuracyStatus;
    }
}
