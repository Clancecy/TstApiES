package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.Device;
import com.testyle.service.IDeviceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private IDeviceService deviceService;
    String charact="UTF-8";

    @RequestMapping("/add")
    public void addTypeAttr(Device device, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent =new ResContent();
        if(device.getDevName()==null||device.getDevName().equals("")){
            resContent.setCode(102);
            resContent.setMessage("参数错误或者为空");
        }else {
            int count=deviceService.insert(device);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("delete")
    public void deleteDevice(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        long devID = Long.parseLong(request.getParameter("devID"));
        int count =deviceService.delete(devID);
        ResContent resContent=new ResContent();
        Utils.dealForDel(count, resContent);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateTypeAttr(Device device, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        if(device.getDevID()!=-1) {
            int count = deviceService.update(device);
            Utils.dealForUpdate(count, resContent);
        }else {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/get")
    public void getDev(Device device, HttpServletResponse response )throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<Device> devices=deviceService.select(device);
        if(devices.size()==0){
            resContent.setMessage("没有数据");
            resContent.setCode(102);
        }
        else {
            resContent.setCode(101);
            resContent.setMessage("获取成功");
            resContent.setData(devices);
        }
    }

}
