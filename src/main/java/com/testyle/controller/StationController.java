package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.Station;
import com.testyle.service.IStationService;
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
@RequestMapping("/station")
public class StationController {
    @Resource
    private IStationService stationService;
    String charact="UTF-8";

    public List<Station> staCommon;

    public List<Object> staList(List<Station> sta){
        List<Object> list = new ArrayList<Object>();
        this.staCommon = sta;
        for (Station x : sta) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpStaID()==0){
                mapArr.put("name", x.getStaName());
                mapArr.put("id", x.getStaID());
//                mapArr.put("level", x.getDepth());
                mapArr.put("pid", x.getpStaID());
                if(staChild(x.getStaID()).size()>0)
                    mapArr.put("children", staChild(x.getStaID()));
                list.add(mapArr);
            }
        }
        return list;
    }


    public List<?> staChild(long id){
        List<Object> lists = new ArrayList<Object>();
        for(Station a:staCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpStaID() == id){
                childArray.put("id", a.getStaID());
                childArray.put("name", a.getStaName());
                childArray.put("pid", a.getpStaID());
//                childArray.put("level", a.getDepth());
                if(staChild(a.getStaID()).size()>0)
                    childArray.put("children", staChild(a.getStaID()));
                lists.add(childArray);
            }
        }
        return lists;

    }

    @RequestMapping("/all")
    public void selectAll(HttpServletResponse response)throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<Station> stationList = stationService.selectAll();
        List<Object>stas= staList(stationList);
        Utils.dealResForSelall(resContent, stas);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();

    }

    @RequestMapping("/children")
    public void selectChildren(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        long pStaID = Long.parseLong(request.getParameter("pStaID"));
        List<Station> childrenStas=stationService.selectChildren(pStaID);

        ResContent resContent=new ResContent();
        if(childrenStas.size()<=0){
            resContent.setCode(102);
            resContent.setMessage("没有数据");
        }else{
            resContent.setCode(101);
            resContent.setMessage("获取成功");
            resContent.setData(childrenStas);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/show")
    public void selectStation(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent =new ResContent();
        long staID = Long.parseLong(request.getParameter("staID"));
        Station station=stationService.selectStaton(staID);
        if(station==null){
            resContent.setCode(102);
            resContent.setMessage("没有该站点");
        }else {
            resContent.setCode(101);
            resContent.setMessage("获取成功");
            resContent.setData(station);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/add")
    public void insertStation(Station station , HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent =new ResContent();
        if(station.getStaName()==null||station.getStaName().equals("")){
            resContent.setCode(102);
            resContent.setMessage("参数错误或者为空");
        }else {
            int count=stationService.insertStation(station);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/delete")
    public void deleteStation(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        long staID = Long.parseLong(request.getParameter("staID"));
        int count =stationService.deleteStation(staID);
        ResContent resContent=new ResContent();
        Utils.dealForDel(count, resContent);
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateStation(Station station , HttpServletResponse response) throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        if(station.getStaID()!=-1) {
            int count = stationService.updateStation(station);
            Utils.dealForUpdate(count, resContent);
        }else {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }
}
