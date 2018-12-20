package com.testyle.dao;

import com.testyle.model.Record;

import java.util.List;

public interface IRecordDao {
    List<Record> select(Record record);
    int insert(Record record);
    int delete(long ID);
    int update(Record record);

}
