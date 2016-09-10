package com.runachuang.massorganizationsignin.utils;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/9/10/010.
 */
public class LocationDB extends BmobObject {
    private String location;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
