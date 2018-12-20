package com.testyle.dao;

import com.testyle.model.TypeAttr;

import java.util.List;

public interface ITypeAttrDao {
    List<TypeAttr> select(TypeAttr typeAttr);
    int insert(TypeAttr typeAttr);
    int delete(long attrID);
    int update(TypeAttr typeAttr);
}
