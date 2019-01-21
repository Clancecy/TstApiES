package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.testyle.common.ExcelUtils;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.*;
import com.testyle.service.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/data")
public class DataController {
    @Resource
    private IDataService dataService;
    @Resource
    private IProService proService;
    @Resource
    private IRecordService recordService;
    @Resource
    private IItemService itemService;
    @Resource
    private ITestService testService;


    String charact = "UTF-8";

    String testRoot="E:/testFile/";

    @RequestMapping("/add")
    public void addData(Data data, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        Utils utils = new Utils();
        ResContent resContent = new ResContent();
        String fname =request.getParameter("url");
        String defaultVal=request.getParameter("default");
        if (data.getDataVal() == null
        ||fname=="") {
            resContent.setCode(104);
            resContent.setMessage("参数错误:" + data.getDataVal());
        }else {
            List<Record> recordList = new ArrayList<>();
            try {
                recordList = (List<Record>) JSON.parseArray(data.getDataVal(), Record.class);
                FileInputStream fis = new FileInputStream(fname);
                Workbook workbook = WorkbookFactory.create(fis);
                // 写入新内容到字段表
                int addRecordNum = utils.writeToItemExcel(workbook, recordList,1);
                // 写入新内容到显示表
                utils.writeToReportExcel(workbook.getSheetAt(0), addRecordNum);

                //写入出厂值
                List<Data> dataList = (List<Data>) JSON.parseArray(defaultVal, Data.class);
                if(dataList.size()!=0&&readDefaultNum(fname)==1) {
                    List<Record> defaultRecords = toRecords(dataList);
                    utils.writeToItemExcel(workbook, defaultRecords, 4);
                }
                // 执行公式
                workbook.setForceFormulaRecalculation(true);
                // 写入文件并关闭流
                FileOutputStream fos = new FileOutputStream(fname);
                workbook.write(fos);
                workbook.close();
                fos.close();
                fis.close();
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                resContent.setCode(106);
                resContent.setMessage(jsone.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                resContent.setCode(103);
                resContent.setMessage("写入文件错误");
            }
            List<Record> excelRecords = utils.readExcel(fname);
            if (excelRecords.size() > 0) {
                resContent.setCode(101);
                resContent.setMessage("计算成功");
                getListComExcel(recordList, excelRecords);
                System.out.println("8080出来的："+JSON.toJSONString(recordList));
                resContent.setData(recordList);
            } else {
                resContent.setCode(105);
                resContent.setMessage("计算失败");
            }
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/get")
    public void getData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        String dataVal=request.getParameter("dataVal");
        String remark=request.getParameter("remark");
        String url=request.getParameter("url");
        if(url==null){
            url="";
        }
        if (dataVal == null || "".equals(dataVal)) {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            try {
                List<Data> dataList = (List<Data>) JSON.parseArray(dataVal, Data.class);
                List<Record> records= toRecords(dataList);
              //  List<Project> projects = toProjects(dataList);
                Map<String, Object> map = new HashMap<>();
                map.put("records", records);
                map.put("remark",remark);
                map.put("url",url);
                resContent.setCode(101);
                resContent.setMessage("获取成功");
                resContent.setData(map);
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                resContent.setCode(104);
                resContent.setMessage(jsone.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                resContent.setCode(102);
                resContent.setMessage("获取失败");
            }
        }
        System.out.println(JSON.toJSONString(resContent));
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/getDefault")
    public void getDefualtData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        String dataVal=request.getParameter("dataVal");
        if (dataVal == null || "".equals(dataVal)) {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            try {
                List<Data> dataList = (List<Data>) JSON.parseArray(dataVal, Data.class);
                List<Record> records= toRecords(dataList);
                resContent.setCode(101);
                resContent.setMessage("获取成功");
                resContent.setData(records);
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                resContent.setCode(104);
                resContent.setMessage(jsone.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                resContent.setCode(102);
                resContent.setMessage("获取失败");
            }
        }
        System.out.println(JSON.toJSONString(resContent));
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }
    private int readDefaultNum(String testUrl) {
        int num = 0;
        ExcelUtils excelUtils = new ExcelUtils();
        try {
            FileInputStream fis = new FileInputStream(testUrl);
            Workbook workbook = WorkbookFactory.create(fis);
            Row row = workbook.getSheetAt(2).getRow(1);
            Cell cell = row.getCell(1);
            String val = excelUtils.getCellValue(workbook, cell).toString();
            num = (int) Double.parseDouble(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }
    private List<Record> toRecords(List<Data> dataList) {
        long proID=dataList.get(0).getProID();
        return getDbRecords(dataList, proID);
    }


    private List<Project> toProjects(List<Data> dataList) {
        Map<Long, Integer> map = new HashMap<>();
        for (Data tempData : dataList) {
            if (map.containsKey(tempData.getProID())) {
                Integer num = map.get(tempData.getProID());
                map.put(tempData.getProID(), num + 1);
            } else {
                map.put(tempData.getProID(), 1);
            }
        }
        List<Project> projects = new ArrayList<>();
        for (long proID : map.keySet()) {
            Project project = new Project();
            project.setProID(proID);
            project.setRecords(getDbRecords(dataList, proID));
            projects.add(project);
        }
        return projects;
    }

    private List<Record> getDbRecords(List<Data> dataList, long proID) {
        Record record = new Record();
        record.setProID(proID);
        List<Record> records = recordService.select(record);
        List<Record> resultRecords = dealRecords(records);
        comDataList(resultRecords, dataList);
        return resultRecords;
    }

    private void comDataList(List<Record> resultRecords, List<Data> dataList) {
        for (int i = 0; i < resultRecords.size(); i++) {
            Record chunk = resultRecords.get(i);
            List<Record> records = chunk.getRecords();
            int size = records.size();
            int testOrder = 1;
            for (int j = 0; j < size; j++) {
                Record record = records.get(j);
                long recordID = record.getRecordID();
                List<Item> itemList = record.getItemList();
                List<Item> newitemList=changeItem(dataList, testOrder, recordID, itemList);
                record.setItemList(newitemList);
            }
            if (i == resultRecords.size() - 1) {
                for (int k = 2; ; k++) {
                    if (!findData(dataList, k)) {
                        break;
                    } else {
                        Record record = records.get(size - 1);
                        Record newRecord = new Record();
                        long recordID = record.getRecordID();
                        newRecord.setRecordID(recordID);
                        newRecord.setRecordName(String.valueOf(k));
                        newRecord.setProID(record.getProID());
                        newRecord.setpRecID(record.getpRecID());
                        newRecord.setItemList(record.getItemList());
                        List<Item> newItemList = changeItem(dataList, k, recordID, newRecord.getItemList());
                        newRecord.setItemList(newItemList);
                        records.add(newRecord);
                    }
                }
            }
        }
    }

    private List<Item> changeItem(List<Data> dataList, int testOrder, long recordID, List<Item> newItemList) {
        List<Item> resList = new ArrayList<>();
        for (Item item : newItemList) {
            long itemID = item.getItemID();
            for (Data data : dataList) {
                if (data.getRecordID() == recordID
                        && data.getItemID() == itemID
                        && data.getTestOrder() == testOrder) {
                    Item tempitem = new Item();
                    tempitem.setItemID(itemID);
                    tempitem.setRecordID(item.getRecordID());
                    tempitem.setItemName(item.getItemName());
                    tempitem.setItemType(item.getItemType());
                    tempitem.setItemVal(data.getDataVal());
                    resList.add(tempitem);
                    break;
                }
            }
        }
        return resList;
    }

    private boolean findData(List<Data> dataList, int testOrder) {
        boolean flag = false;
        for (Data data : dataList) {
            if (data.getTestOrder() == testOrder) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    private List<Record> dealRecords(List<Record> records) {
        List<Record> recordList = new ArrayList<Record>();
        for (Record x : records) {
            if (x.getpRecID() == 0) {
                Record record = x;
                record.setRecords(getRecords(records, x.getRecordID()));
                recordList.add(record);
            }
        }
        return recordList;
    }

    private List<Record> getRecords(List<Record> records, long ID) {
        List<Record> recordList = new ArrayList<Record>();
        for (Record x : records) {
            if (x.getpRecID() == ID) {
                Record record = x;
                record.setItemList(getItems(x.getRecordID()));
                recordList.add(record);
            }
        }
        return recordList;
    }

    private List<Item> getItems(long ID) {
        Item item = new Item();
        item.setRecordID(ID);
        List<Item> itemList = itemService.select(item);
        return itemList;
    }

    private void getListComExcel(List<Record> reqRecords, List<Record> excelRecords) {
        int chunkCount = reqRecords.size();
        for (int i = 0; i < chunkCount; i++) {
            List<Record> records = reqRecords.get(i).getRecords();
            int recordCount = records.size();
            for (int j = 0; j < recordCount; j++) {
                List<Item> itemList = records.get(j).getItemList();
                int itemCount = itemList.size();
                for (int k = 0; k < itemCount; k++) {
                    Item item = itemList.get(k);
                    String excelVal = excelRecords.get(i).getRecords()
                            .get(j).getItemList().get(k).getItemVal();
                    item.setItemVal(excelVal);
                }
            }
        }
    }

    private void addDb(List<Record> records, Data data, ResContent resContent) {
        List<Data> List = new ArrayList<>();
        boolean isExt = false;
        for (Record chunk : records) {
            List<Record> recordList = chunk.getRecords();
            int recCount = recordList.size();
            for (int i = 0; i < recCount; i++) {
                Record record = recordList.get(i);
                long recordID = record.getRecordID();
                int testOrder = i + 1;
                for (Item item : record.getItemList()
                ) {
                    Data tempData = new Data();
                    tempData.setTestID(data.getTestID());
                    tempData.setProID(data.getProID());
                    long itemID = item.getItemID();
                    String dataVal = item.getItemVal();
                    tempData.setRecordID(recordID);
                    tempData.setTestOrder(testOrder);
                    tempData.setItemID(itemID);
                    tempData.setDataVal(dataVal);
                    long dataID = dataService.selOne(tempData);
                    if (dataID == 0) {
                        List.add(tempData);
                    } else {
                        isExt = true;
                        tempData.setDataID(dataID);
                        List.add(tempData);
                    }
                }
            }
        }
        if (!isExt) {
            int count = dataService.insertList(List);
            if (count > 0) {
                resContent.setCode(101);
                resContent.setMessage("新增成功，新增" + count + "条数据");
            } else {
                resContent.setCode(104);
                resContent.setMessage("新增失败");
            }
        } else {
            int count = dataService.updateList(List);
            if (count > 0) {
                resContent.setCode(105);
                resContent.setMessage("更新成功，更新" + count + "条数据");
            } else {
                resContent.setCode(106);
                resContent.setMessage("更新失败");
            }
        }

    }
}
