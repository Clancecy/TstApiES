package com.testyle.service;

import com.testyle.model.Record;

import java.util.List;

public interface IRecordService {
    List<Record> select(Record record);
    int insert(Record record);
    int delete(long ID);
    int update(Record record);

}
