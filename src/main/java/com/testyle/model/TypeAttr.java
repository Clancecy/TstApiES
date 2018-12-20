package com.testyle.model;

import java.util.Date;

public class TypeAttr {
    long attrID=-1;
    String attrName;
    long typeID=-1;
    Date addtime =new Date();

    public long getAttrID() {
        return attrID;
    }

    public void setAttrID(long attrID) {
        this.attrID = attrID;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public long getTypeID() {
        return typeID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
