package com.testyle.model;

import java.util.List;

public class Project {
    long proID=-1;
    String proName;
    String Url;
    long devTypeID=-1;
    List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public long getProID() {
        return proID;
    }

    public void setProID(long proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getDevTypeID() {
        return devTypeID;
    }

    public void setDevTypeID(long devTypeID) {
        this.devTypeID = devTypeID;
    }
}
