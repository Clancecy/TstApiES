package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.Project;
import com.testyle.model.Test;
import com.testyle.service.IProService;
import com.testyle.service.ITestService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @Resource
    private ITestService testService;
    @Resource
    private IProService proService;
    String charact = "UTF-8";

    @RequestMapping("/add")
    public void addTest(Test test, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        if(test.getTestName()==null||
        test.getTestName().equals("")){
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            int count = testService.insert(test);
            Utils.dealForAdd(resContent, count);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/update")
    public void updateTest(Test test,HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        if(test.getTestID()==-1){
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            int count=testService.update(test);
            Utils.dealForUpdate(count,resContent);
        }
    }
}
