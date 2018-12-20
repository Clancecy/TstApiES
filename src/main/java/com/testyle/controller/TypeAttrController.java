package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.TypeAttr;
import com.testyle.service.ITypeAttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/typeAttr")
public class TypeAttrController {
    @Resource
    private ITypeAttrService typeAttrService;
    String charact="UTF-8";

    @RequestMapping("/add")
    public void addTypeAttr(TypeAttr typeAttr, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent =new ResContent();
        if(typeAttr.getAttrName()==null||typeAttr.getAttrName().equals("")){
            resContent.setCode(102);
            resContent.setMessage("参数错误或者为空");
        }else {
            int count=typeAttrService.insert(typeAttr);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("delete")
    public void deleteTypeAttr(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        long attrID = Long.parseLong(request.getParameter("attrID"));
        int count =typeAttrService.delete(attrID);
        ResContent resContent=new ResContent();
        Utils.dealForDel(count, resContent);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateTypeAttr(TypeAttr typeAttr, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        if(typeAttr.getAttrID()==-1){
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            int count = typeAttrService.update(typeAttr);
            Utils.dealForUpdate(count, resContent);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/get")
    public void getAttr(TypeAttr typeAttr, HttpServletResponse response )throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<TypeAttr> typeAttrs=typeAttrService.select(typeAttr);

        if (typeAttrs.size() != 0) {
            resContent.setCode(101);
            resContent.setMessage("获取成功");
            resContent.setData(typeAttrs);
        } else {
            resContent.setMessage("没有数据");
            resContent.setCode(102);
        }
    }

}
