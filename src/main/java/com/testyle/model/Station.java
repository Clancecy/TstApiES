package com.testyle.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Station {
    private long staID=-1;
    private String staName;
    private long pStaID=-1;
    private int depth;
    private int order;
    private float longitude;
    private float latitude;
    private Date addtime =new Date();
    private List<Station> childSta=new ArrayList<Station>();


    public long getStaID() {
        return staID;
    }

    public void setStaID(long staID) {
        this.staID = staID;
    }

    public String getStaName() {
        return staName;
    }

    public void setStaName(String staName) {
        this.staName = staName;
    }

    public long getpStaID() {
        return pStaID;
    }

    public void setpStaID(long pStaID) {
        this.pStaID = pStaID;
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

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Date getAddTime() {
        return addtime;
    }

    public void setAddTime(Date addTime) {
        this.addtime = addTime;
    }

    public List<Station> getChildSta() {
        return childSta;
    }

    public void setChildSta(List<Station> childSta) {
        this.childSta = childSta;
    }
}
