package com.testyle.service.impl;

import com.testyle.dao.ITypeAttrDao;
import com.testyle.model.TypeAttr;
import com.testyle.service.ITypeAttrService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("typeAttrService")
public class TypeAttrServiceImpl implements ITypeAttrService {
    @Resource
    private ITypeAttrDao typeAttrDao;
    @Override
    public List<TypeAttr> select(TypeAttr typeAttr) {
        return null;
    }

    @Override
    public int insert(TypeAttr typeAttr) {
        return 0;
    }

    @Override
    public int delete(long attrID) {
        return 0;
    }

    @Override
    public int update(TypeAttr typeAttr) {
        return 0;
    }
}
