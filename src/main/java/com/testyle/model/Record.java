package com.testyle.model;

import java.util.List;

public class Record {
    long recordID=-1;
    String recordName;
    long pRecID=-1;
    long proID=-1;
    List<Record> records;
    List<Item> itemList;


    public long getProID() {
        return proID;
    }

    public void setProID(long proID) {
        this.proID = proID;
    }

    public long getRecordID() {
        return recordID;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setRecordID(long recordID) {
        this.recordID = recordID;
    }

    public long getpRecID() {
        return pRecID;
    }

    public void setpRecID(long pRecID) {
        this.pRecID = pRecID;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
