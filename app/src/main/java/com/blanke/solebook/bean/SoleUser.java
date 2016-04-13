package com.blanke.solebook.bean;

import android.os.Parcel;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;

/**
 * Created by blanke on 16-2-21.
 */
public class SoleUser extends AVUser {
    public static final String NICKNAME = "nickname";
    public static final String ICONURL = "iconurl";
    public static final String CITY = "city";
    public static final String LOCATION = "location";
    public static final String DISTRICT = "district";
    public static final String DEVICEID = "deviceId";

    public String getNickname() {
        return getString(NICKNAME);
    }

    public static String getNickname(AVUser user) {
        return user.getString(NICKNAME);
    }

    public static String getCity(AVUser user) {
        return user.getString(CITY);
    }

    public void setNickname(String nickname) {
        put(NICKNAME, nickname);
    }

    public String getIconurl() {
        return getString(ICONURL);
    }

    public static String getIconurl(AVUser user) {
        return user.getString(ICONURL);
    }

    public void setIconurl(String iconurl) {
        put(ICONURL, iconurl);
    }

    public void setDeviceId(String deviceId) {
        put(DEVICEID, deviceId);
    }

    public void setCity(String city) {
        put(CITY, city);
    }

    public void setLocation(AVGeoPoint point) {
        put(LOCATION, point);
    }

    public String getCity() {
        return getString(CITY);
    }

    public AVGeoPoint getLocation() {
        return getAVGeoPoint(LOCATION);
    }

    public SoleUser() {
    }

    public SoleUser(Parcel in) {
        super(in);
    }

    public void setDistrict(String district) {
        put(DISTRICT, district);
    }

    public String getDistrict() {
        return getString(DISTRICT);
    }
}
