package com.testyle.model;

import java.util.Date;

public class Device {
    long devID=-1;
    String devName;
    String devCode;
    long devTypeID=-1;
    long staID=-1;
    Date addtime =new Date();

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public long getDevID() {
        return devID;
    }

    public void setDevID(long devID) {
        this.devID = devID;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public long getDevTypeID() {
        return devTypeID;
    }

    public void setDevTypeID(long devTypeID) {
        this.devTypeID = devTypeID;
    }

    public long getStaID() {
        return staID;
    }

    public void setStaID(long staID) {
        this.staID = staID;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
