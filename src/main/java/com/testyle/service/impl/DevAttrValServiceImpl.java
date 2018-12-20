package com.testyle.service.impl;

import com.testyle.dao.IDevAttrValDao;
import com.testyle.model.DevAttrVal;
import com.testyle.service.IDevAttrValService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("devAttrValService")
public class DevAttrValServiceImpl implements IDevAttrValService {
    @Resource
    private IDevAttrValDao devAttrValDao;
    @Override
    public List<DevAttrVal> select(DevAttrVal devAttrVal) {
        return devAttrValDao.select(devAttrVal);
    }

    @Override
    public int insert(DevAttrVal devAttrVal) {
        return devAttrValDao.insert(devAttrVal);
    }

    @Override
    public int delete(long ID) {
        return devAttrValDao.delete(ID);
    }

    @Override
    public int update(DevAttrVal devAttrVal) {
        return devAttrValDao.update(devAttrVal);
    }
}
