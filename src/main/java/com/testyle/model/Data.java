package com.testyle.model;

import java.util.Date;
import java.util.List;

public class Data {
    String table="data";
    long dataID = -1;
    long testID=-1;
    long taskID=-1;
    long proID = -1;
    String proName;
    int testOrder=-1;
    long recordID = -1;
    String recordName;
    long itemID = -1;
    String itemName;
    long devID=-1;
    String devName;
    int status=-1;
    String dataVal;
    Date addtime = new Date();

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getDevID() {
        return devID;
    }

    public void setDevID(long devID) {
        this.devID = devID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTestID() {
        return testID;
    }

    public void setTestID(long testID) {
        this.testID = testID;
    }

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

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
