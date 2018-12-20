package com.testyle.model;

import java.util.Date;

public class DevAttrVal {
    long ID=-1;
    long attrID=-1;
    long devID=-1;
    String arrtVal;
    Date addtime=new Date();

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getAttrID() {
        return attrID;
    }

    public void setAttrID(long attrID) {
        this.attrID = attrID;
    }

    public long getDevID() {
        return devID;
    }

    public void setDevID(long devID) {
        this.devID = devID;
    }

    public String getArrtVal() {
        return arrtVal;
    }

    public void setValue(String arrtVal) {
        this.arrtVal = arrtVal;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
