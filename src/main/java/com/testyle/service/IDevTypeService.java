package com.testyle.service;

import com.testyle.model.DevType;

import java.util.List;

public interface IDevTypeService {
    List<DevType> select(DevType devType);
    int insert(DevType devType);
    int delete(long typeID);
    int update(DevType devType);
}
