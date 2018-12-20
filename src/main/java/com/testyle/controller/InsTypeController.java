package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.InsType;
import com.testyle.service.IInsTypeService;
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
@RequestMapping("/insType")
public class InsTypeController {
    @Resource
    private IInsTypeService insTypeService;
    String charact="UTF-8";

    public List<InsType> typeCommon;

    public List<Object> insTypeList(List<InsType> type){
        List<Object> list = new ArrayList<Object>();
        this.typeCommon = type;
        for (InsType x : type) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpTypeID()==0){
                mapArr.put("id", x.getInsTypeID());
                mapArr.put("label", x.getInsTypeName());
                mapArr.put("pid", x.getpTypeID());
                if(insTypeChild(x.getInsTypeID()).size()>0)
                    mapArr.put("children", insTypeChild(x.getInsTypeID()));
                list.add(mapArr);
            }
        }
        return list;
    }


    public List<?> insTypeChild(long id){
        List<Object> lists = new ArrayList<Object>();
        for(InsType a:typeCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpTypeID() == id){
                childArray.put("label", a.getInsTypeName());
                childArray.put("id", a.getInsTypeID());
                childArray.put("pid", a.getpTypeID());
                if(insTypeChild(a.getInsTypeID()).size()>0)
                    childArray.put("children", insTypeChild(a.getInsTypeID()));
                lists.add(childArray);
            }
        }
        return lists;

    }
    @RequestMapping("/list")
    public void selectAll(InsType insType, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<InsType> insTypeList = insTypeService.select(insType);
        List<Object> insTypes= insTypeList(insTypeList);
        Utils.dealResForSelall(resContent, insTypes);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();

    }
    @RequestMapping("/add")
    public void addInsType(InsType insType, HttpServletResponse response ) throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        if(insType.getInsTypeName()==null||insType.getInsTypeName().equals("")
        || insType.getpTypeID()==-1){
            resContent.setCode(102);
            resContent.setMessage("参数错误或者为空");
        }else {
            int count=insTypeService.insert(insType);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/delete")
    public void deleteInsType(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        long typeID = Long.parseLong(request.getParameter("insTypeID"));
        int count =insTypeService.delete(typeID);
        ResContent resContent=new ResContent();
        Utils.dealForDel(count, resContent);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateInsType(InsType insType, HttpServletResponse response)throws IOException{
        ResContent resContent = new ResContent();
        response.setCharacterEncoding(charact);
        if(insType.getInsTypeID()!=-1) {
            int count = insTypeService.update(insType);
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
