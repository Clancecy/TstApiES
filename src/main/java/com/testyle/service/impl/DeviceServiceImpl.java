package com.testyle.service.impl;

import com.testyle.dao.IDeviceDao;
import com.testyle.model.Device;
import com.testyle.service.IDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceService")
public class DeviceServiceImpl implements IDeviceService {

    @Resource
    private IDeviceDao deviceDao;
    @Override
    public List<Device> select(Device device) {
        return deviceDao.select(device);
    }

    @Override
    public int insert(Device device) {
        return deviceDao.insert(device);
    }

    @Override
    public int delete(long devID) {
        return deviceDao.delete(devID);
    }

    @Override
    public int update(Device device) {
        return deviceDao.update(device);
    }
}
