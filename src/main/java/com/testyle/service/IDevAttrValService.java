package com.testyle.service;

import com.testyle.model.DevAttrVal;

import java.util.List;

public interface IDevAttrValService {
    List<DevAttrVal> select(DevAttrVal devAttrVal);
    int insert(DevAttrVal devAttrVal);
    int delete(long ID);
    int update(DevAttrVal devAttrVal);
}
