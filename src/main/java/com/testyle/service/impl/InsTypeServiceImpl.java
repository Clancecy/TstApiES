package com.testyle.service.impl;

import com.testyle.dao.IInsTypeDao;
import com.testyle.model.InsType;
import com.testyle.service.IInsTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("insTypeService")
public class InsTypeServiceImpl implements IInsTypeService {
    @Resource
    private IInsTypeDao insTypeDao;
    @Override
    public List<InsType> select(InsType insType) {
        return insTypeDao.select(insType);
    }

    @Override
    public int insert(InsType insType) {
        return insTypeDao.insert(insType);
    }

    @Override
    public int delete(long insTypeID) {
        return insTypeDao.delete(insTypeID);
    }

    @Override
    public int update(InsType insType) {
        return insTypeDao.update(insType);
    }
}
