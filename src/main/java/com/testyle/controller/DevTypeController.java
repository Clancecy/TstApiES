package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.DevType;
import com.testyle.service.IDevTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/devType")
public class DevTypeController {
    @Resource
    private IDevTypeService devTypeService;
    String charact="UTF-8";

    public List<DevType> typeCommon;

    public List<Object> devTypeList(List<DevType> type){
        List<Object> list = new ArrayList<Object>();
        this.typeCommon = type;
        for (DevType x : type) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpTypeID()==0){
                mapArr.put("id", x.getTypeID());
                mapArr.put("label", x.getTypeName());
                mapArr.put("level", x.getDepth());
                mapArr.put("pid", x.getpTypeID());
                if(devTypeChild(x.getTypeID()).size()>0)
                    mapArr.put("children", devTypeChild(x.getTypeID()));
                list.add(mapArr);
            }
        }
        return list;
    }


    public List<?> devTypeChild(long id){
        List<Object> lists = new ArrayList<Object>();
        for(DevType a:typeCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpTypeID() == id){
                childArray.put("label", a.getTypeName());
                childArray.put("id", a.getTypeID());
                childArray.put("level", a.getDepth());
                childArray.put("pid", a.getpTypeID());
                if(devTypeChild(a.getTypeID()).size()>0)
                    childArray.put("children", devTypeChild(a.getTypeID()));
                lists.add(childArray);
            }
        }
        return lists;

    }
    @RequestMapping("/list")
    public void selectAll(DevType devType, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<DevType> devTypeList = devTypeService.select(devType);
        List<Object> devTypes= devTypeList(devTypeList);
        Utils.dealResForSelall(resContent, devTypes);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();

    }
    @RequestMapping("/add")
    public void addDevType(DevType devType, HttpServletResponse response ) throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        if(devType.getTypeName()==null||devType.getTypeName().equals("")
        || devType.getpTypeID()==-1){
            resContent.setCode(102);
            resContent.setMessage("参数错误或者为空");
        }else {
            int count=devTypeService.insert(devType);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/delete")
    public void deleteDevType(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        long typeID = Long.parseLong(request.getParameter("typeID"));
        int count =devTypeService.delete(typeID);
        ResContent resContent=new ResContent();
        Utils.dealForDel(count, resContent);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateDevType(DevType devType, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        if(devType.getTypeID()!=-1) {
            int count = devTypeService.update(devType);
            Utils.dealForUpdate(count, resContent);
        }
        else {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }




}
