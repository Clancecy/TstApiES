package com.testyle.model;

import java.util.Date;

public class DevType {
    long typeID=-1;
    String typeCode;
    String typeName;
    long pTypeID=-1;
    int depth;
    int order;
    Date addtime=new Date();

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public long getTypeID() {
        return typeID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getpTypeID() {
        return pTypeID;
    }

    public void setpTypeID(long pTypeID) {
        this.pTypeID = pTypeID;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
