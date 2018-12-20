package com.testyle.model;

import java.util.Date;
import java.util.List;

public class Data {
    long dataID = -1;
    long testID = -1;
    long proID = -1;
    int testOrder=-1;
    long recordID = -1;
    long itemID = -1;
    String dataVal;
    Date addtime = new Date();

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    List<Project> projects;

    public long getDataID() {
        return dataID;
    }

    public void setDataID(long dataID) {
        this.dataID = dataID;
    }

    public long getTestID() {
        return testID;
    }

    public void setTestID(long testID) {
        this.testID = testID;
    }

    public long getProID() {
        return proID;
    }

    public void setProID(long proID) {
        this.proID = proID;
    }

    public int getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(int testOrder) {
        this.testOrder = testOrder;
    }

    public long getRecordID() {
        return recordID;
    }

    public void setRecordID(long recordID) {
        this.recordID = recordID;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getDataVal() {
        return dataVal;
    }

    public void setDataVal(String dataVal) {
        this.dataVal = dataVal;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
