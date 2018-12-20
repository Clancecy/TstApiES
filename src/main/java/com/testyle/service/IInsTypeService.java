package com.testyle.service;

import com.testyle.model.InsType;

import java.util.List;

public interface IInsTypeService {
    List<InsType> select(InsType insType);
    int insert(InsType insType);
    int delete(long insTypeID);
    int update(InsType insType);
}
