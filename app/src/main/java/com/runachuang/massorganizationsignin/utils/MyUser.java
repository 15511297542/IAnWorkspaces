package com.runachuang.massorganizationsignin.utils;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/9/9/009.
 */
public class MyUser extends BmobUser {
    private String name;
    private String group;
    private String sex;
    private String qq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
