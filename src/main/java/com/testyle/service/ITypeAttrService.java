package com.testyle.service;

import com.testyle.model.TypeAttr;

import java.util.List;

public interface ITypeAttrService {
    List<TypeAttr> select(TypeAttr typeAttr);
    int insert(TypeAttr typeAttr);
    int delete(long attrID);
    int update(TypeAttr typeAttr);
}
