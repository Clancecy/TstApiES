package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.DevAttrVal;
import com.testyle.service.IDevAttrValService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/devAttrVal")
public class DevAttrValController {
    @Resource
    private IDevAttrValService devAttrValService;
    String charact="UTF-8";

    @RequestMapping("/add")
    public void addDevAttrVal(DevAttrVal devAttrVal, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent =new ResContent();
        if(devAttrVal.getArrtVal()==null||devAttrVal.getArrtVal().equals("")){
            resContent.setCode(102);
            resContent.setMessage("参数错误或者为空");
        }else {
            int count=devAttrValService.insert(devAttrVal);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("delete")
    public void deleteDevAttrVal(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        long ID = Long.parseLong(request.getParameter("ID"));
        int count =devAttrValService.delete(ID);
        ResContent resContent=new ResContent();
        Utils.dealForDel(count, resContent);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateDevAttrVal(DevAttrVal devAttrVal, HttpServletResponse response)throws IOException{

        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        if(devAttrVal.getAttrID()!=-1) {
            int count = devAttrValService.update(devAttrVal);
            Utils.dealForUpdate(count, resContent);
        }else {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }

        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/get")
    public void getDevAttrVal(DevAttrVal devAttrVal, HttpServletResponse response )throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<DevAttrVal> devices=devAttrValService.select(devAttrVal);
        if(devices.size()==0){
            resContent.setCode(102);
            resContent.setMessage("没有数据");
        }
        else {
            resContent.setCode(101);
            resContent.setMessage("获取成功");
            resContent.setData(devices);
        }
    }
}
