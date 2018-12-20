package com.testyle.controller;

import com.testyle.service.IItemService;
import com.testyle.service.IRecordService;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/record")
public class RecordController {
    @Resource
    private IRecordService recordService;
    private IItemService itemService;

    String charact = "UTF-8";
    String root="E:/file/";

    private void addRecord(Sheet sheet, long proID, long pRecID){

    }
}
