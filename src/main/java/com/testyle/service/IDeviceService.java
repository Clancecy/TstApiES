package com.testyle.service;

import com.testyle.model.Device;

import java.util.List;

public interface IDeviceService {
    List<Device> select(Device device);
    int insert(Device device);
    int delete(long devID);
    int update(Device device);
}
