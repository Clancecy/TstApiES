package com.testyle.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aspose.cells.SaveFormat;
import com.testyle.common.ExcelUtils;
import com.testyle.common.Utils;
import com.testyle.dao.IDataDao;
import com.testyle.dao.IItemDao;
import com.testyle.dao.IRecordDao;
import com.testyle.model.Data;
import com.testyle.model.Item;
import com.testyle.model.Project;
import com.testyle.model.Record;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class IDataDaoTest {
    @Autowired
    private IDataDao dataDao;
    @Autowired
    private IRecordDao recordDao;
    @Autowired
    private IItemDao itemDao;

    @Test
    public void testData() {
        Data data = new Data();
        data.setTestID(4);
        List<Data> dataList = dataDao.select(data);
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

//        System.out.println(JSON.toJSONString(projects));
    }

    private List<Record> getDbRecords(List<Data> dataList, long proID) {
        Record record = new Record();
        record.setProID(proID);
        List<Record> records = recordDao.select(record);
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
                changeItem(dataList, testOrder, recordID, itemList);
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
        System.out.println(JSON.toJSONString(resultRecords));
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
        List<Item> itemList = itemDao.select(item);
        return itemList;
    }


    @Test
    public void testUpdate() {
        List<Data> dataList = new ArrayList<>();
        Data data = new Data();
        data.setTestID(1);
        data.setDataID(323);
        data.setDataVal("tt");
        dataList.add(data);
        dataList.add(data);
        dataList.add(data);
        dataDao.updateList(dataList);
    }

    @Test
    public void testJson() {
        String jstr = "{\"records\":[{\"pRecID\":0,\"proID\":25,\"recordID\":57,\"recordName\":\"æ¸©åº¦\",\"records\":[{\"itemList\":[{\"itemID\":37,\"itemName\":\"æ¸©åº¦\",\"itemType\":\"STRING\",\"itemVal\":\"20â„ƒ\",\"recordID\":58}],\"pRecID\":57,\"proID\":25,\"recordID\":58,\"recordName\":\"æ¸©åº¦\"}]},{\"pRecID\":0,\"proID\":25,\"recordID\":59,\"recordName\":\"æ¹¿åº¦\",\"records\":[{\"itemList\":[{\"itemID\":38,\"itemName\":\"æ¹¿åº¦\",\"itemType\":\"NUMERIC\",\"itemVal\":\"0.0\",\"recordID\":60}],\"pRecID\":59,\"proID\":25,\"recordID\":60,\"recordName\":\"æ¹¿åº¦\"}]},{\"pRecID\":0,\"proID\":25,\"recordID\":61,\"recordName\":\"ä½ŽåŽ‹ç»•é˜»\",\"records\":[{\"itemList\":[{\"itemID\":39,\"itemName\":\"AB\",\"itemType\":\"NUMERIC\",\"itemVal\":\"2.0\",\"recordID\":62},{\"itemID\":40,\"itemName\":\"BC\",\"itemType\":\"NUMERIC\",\"itemVal\":\"3.0\",\"recordID\":62},{\"itemID\":41,\"itemName\":\"CA\",\"itemType\":\"NUMERIC\",\"itemVal\":\"4.0\",\"recordID\":62},{\"itemID\":42,\"itemName\":\"è¯¯å·® \",\"itemType\":\"FORMULA\",\"itemVal\":\"0.5\",\"recordID\":62}],\"pRecID\":61,\"proID\":25,\"recordID\":62,\"recordName\":\"æ•°æ\u008D®\"}]},{\"pRecID\":0,\"proID\":25,\"recordID\":63,\"recordName\":\"ä¸\u00ADåŽ‹ç»•é˜»\",\"records\":[{\"itemList\":[{\"itemID\":43,\"itemName\":\"AB\",\"itemType\":\"BLANK\",\"itemVal\":\"\",\"recordID\":64},{\"itemID\":44,\"itemName\":\"BC\",\"itemType\":\"BLANK\",\"itemVal\":\"\",\"recordID\":64},{\"itemID\":45,\"itemName\":\"CA\",\"itemType\":\"BLANK\",\"itemVal\":\"\",\"recordID\":64},{\"itemID\":46,\"itemName\":\"è¯¯å·® \",\"itemType\":\"FORMULA\",\"itemVal\":\"0.0\",\"recordID\":64}],\"pRecID\":63,\"proID\":25,\"recordID\":64,\"recordName\":\"æ•°æ\u008D®\"}]},{\"pRecID\":0,\"proID\":25,\"recordID\":65,\"recordName\":\"é«˜åŽ‹ç»•é˜»\",\"records\":[{\"itemList\":[{\"itemID\":47,\"itemName\":\"AB\",\"itemType\":\"NUMERIC\",\"itemVal\":\"1.0\",\"recordID\":66},{\"itemID\":48,\"itemName\":\"BC\",\"itemType\":\"NUMERIC\",\"itemVal\":\"2.0\",\"recordID\":66},{\"itemID\":49,\"itemName\":\"CA\",\"itemType\":\"NUMERIC\",\"itemVal\":\"3.0\",\"recordID\":66},{\"itemID\":50,\"itemName\":\"è¯¯å·® \",\"itemType\":\"FORMULA\",\"itemVal\":\"0.6666666666666666\",\"recordID\":66}],\"pRecID\":65,\"proID\":25,\"recordID\":66,\"recordName\":\"1.0\"},{\"itemList\":[{\"itemID\":47,\"itemName\":\"AB\",\"itemType\":\"NUMERIC\",\"itemVal\":\"5\",\"recordID\":66},{\"itemID\":48,\"itemName\":\"BC\",\"itemType\":\"NUMERIC\",\"itemVal\":\"6\",\"recordID\":66},{\"itemID\":49,\"itemName\":\"CA\",\"itemType\":\"NUMERIC\",\"itemVal\":\"7\",\"recordID\":66},{\"itemID\":50,\"itemName\":\"è¯¯å·® \",\"itemType\":\"FORMULA\",\"itemVal\":\"0.6666666666666666\",\"recordID\":66}],\"pRecID\":65,\"proID\":25,\"recordID\":66,\"recordName\":\"2\"},{\"itemList\":[{\"itemID\":47,\"itemName\":\"AB\",\"itemType\":\"NUMERIC\",\"itemVal\":\"5\",\"recordID\":66},{\"itemID\":48,\"itemName\":\"BC\",\"itemType\":\"NUMERIC\",\"itemVal\":\"6\",\"recordID\":66},{\"itemID\":49,\"itemName\":\"CA\",\"itemType\":\"NUMERIC\",\"itemVal\":\"7\",\"recordID\":66},{\"itemID\":50,\"itemName\":\"è¯¯å·® \",\"itemType\":\"FORMULA\",\"itemVal\":\"0.6666666666666666\",\"recordID\":66}],\"pRecID\":65,\"proID\":25,\"recordID\":66,\"recordName\":\"3\"}]}],\"url\":\"E:\\\\testFile\\\\ç›´æµ\u0081ç”µé˜»æ¨¡æ\u009D¿2525.xlsx\"}";
        Map<String, Object> map = JSON.parseObject(jstr, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(map);
    }

    @Test
    public void testPDF() {
        if (!Utils.getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            File pdfFile = new File("E:/testFile/test1.pdf");// 输出路径
            String fnameDist = "temp.xlsx";
            Workbook workbookDist = null;
            if (fnameDist.endsWith(".xls")) {
                workbookDist = new HSSFWorkbook();
            } else if (fnameDist.endsWith(".xlsx")) {
                workbookDist = new XSSFWorkbook();
            }
            Sheet sheetDist = workbookDist.createSheet();
            FileInputStream fis = new FileInputStream("E:/testFile/油浸式变压器test35task28pro50.xlsx");
            org.apache.poi.ss.usermodel.Workbook workbookSrc = WorkbookFactory.create(fis);
            workbookSrc.setForceFormulaRecalculation(true);
            Sheet sheetSrc = workbookSrc.getSheetAt(0);
            ExcelUtils excelUtils=new ExcelUtils();
            excelUtils.mergeSheet(sheetDist, sheetSrc);
            ExcelUtils.setColumnWidth(sheetDist);
            FileOutputStream fos = new FileOutputStream(fnameDist);
            workbookDist.write(fos);
            fos.flush();
            workbookDist.close();
            fos.close();
            com.aspose.cells.Workbook wb = new com.aspose.cells.Workbook(fnameDist);// 原始excel路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            for (int i = 1; i < wb.getWorksheets().getCount(); i++) {
                wb.getWorksheets().get(i).setVisible(false);
            }
            wb.save(fileOS, SaveFormat.PDF);
            fileOS.close();
            File temp=new File(fnameDist);
            if(temp.exists())
                temp.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
