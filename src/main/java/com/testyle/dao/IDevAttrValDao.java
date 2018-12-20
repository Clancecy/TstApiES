package com.testyle.dao;

import com.testyle.model.DevAttrVal;

import java.util.List;

public interface IDevAttrValDao {
    List<DevAttrVal> select(DevAttrVal devAttrVal);
    int insert(DevAttrVal devAttrVal);
    int delete(long ID);
    int update(DevAttrVal devAttrVal);
}
