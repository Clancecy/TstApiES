package com.testyle.service.impl;

import com.testyle.dao.IDevTypeDao;
import com.testyle.model.DevType;
import com.testyle.service.IDevTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("devTypeService")
public class DevTypeServiceImpl implements IDevTypeService {
    @Resource
    private IDevTypeDao devTypeDao;
    @Override
    public List<DevType> select(DevType devType) {
        return devTypeDao.select(devType);
    }

    @Override
    public int insert(DevType devType) {
        return devTypeDao.insert(devType);
    }

    @Override
    public int delete(long typeID) {
        return devTypeDao.delete(typeID);
    }

    @Override
    public int update(DevType devType) {
        return devTypeDao.update(devType);
    }
}
