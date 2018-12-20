package com.testyle.model;

import java.util.Date;

public class InsType {
    long insTypeID=-1;
    String insTypeName;
    long pTypeID=0;
    Date addtime=new Date();

    public long getInsTypeID() {
        return insTypeID;
    }

    public void setInsTypeID(long insTypeID) {
        this.insTypeID = insTypeID;
    }

    public String getInsTypeName() {
        return insTypeName;
    }

    public void setInsTypeName(String insTypeName) {
        this.insTypeName = insTypeName;
    }

    public long getpTypeID() {
        return pTypeID;
    }

    public void setpTypeID(long pTypeID) {
        this.pTypeID = pTypeID;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
