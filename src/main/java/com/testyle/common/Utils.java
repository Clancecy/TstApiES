package com.testyle.common;

import com.aspose.cells.License;
import com.testyle.model.Item;
import com.testyle.model.Record;
import com.testyle.service.IItemService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Utils {
    @Autowired
    private IItemService itemService;

    public static Utils utils;
    @PostConstruct
    public void init(){
        utils=this;
    }
    public static void dealResForSelall(ResContent resContent, List<Object> devTypes) {
        if(devTypes.size()<=0){
            resContent.setMessage("没有数据");
            resContent.setCode(102);
        }
        else {
            resContent.setCode(101);
            resContent.setMessage("获取成功");
            resContent.setData(devTypes);
        }
    }

    public static void dealForAdd(ResContent resContent, int count) {
        if(count>0) {
            resContent.setCode(101);
            resContent.setMessage("添加成功");
        }
        else {
            resContent.setCode(103);
            resContent.setMessage("添加失败");
        }
    }

    public static void dealForDel(int count, ResContent resContent) {
        if(count>0){
            resContent.setCode(101);
            resContent.setMessage("删除成功");
        }else {
            resContent.setCode(102);
            resContent.setMessage("删除失败");
        }
    }

    public static void dealForUpdate(int count, ResContent resContent) {
        if(count>0){
            resContent.setCode(101);
            resContent.setMessage("更新成功");
        }else {
            resContent.setCode(102);
            resContent.setMessage("更新失败");
        }
    }

    public List<Record> readExcel(String fname) {
        List<Record> records=new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(fname);
            Workbook workbook = WorkbookFactory.create(fis);
//            CreationHelper crateHelper = workbook.getCreationHelper();
//            FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
//            evaluator.clearAllCachedResultValues();
            Sheet sheet = workbook.getSheetAt(1);
            records=getChunk(workbook);
            workbook.close();
            fis.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return records;
    }


    private List<Record> getChunk(Workbook wb) {
        Sheet sheet=wb.getSheetAt(1);
        ExcelUtils excelUtils =new ExcelUtils();
        List<Record> chuckList = new ArrayList<>();
        int rowCount = sheet.getPhysicalNumberOfRows();
        final int chunkCol = 0;
        int chunkEnd = 0;

        for(int chunkStart=1; chunkStart<rowCount; chunkStart++) {
            chunkEnd = excelUtils.skipMergeRowCell(sheet, chunkStart, chunkCol);	// 列0对应chunk
            Row row = sheet.getRow(chunkStart);
            Cell cell = row.getCell(chunkCol);
            // 如果行无效，跳过
            if (row.getPhysicalNumberOfCells()<4 || cell.getCellTypeEnum().equals(CellType.BLANK)) {
                chunkStart = chunkEnd;
                continue;
            }
            Record chunk = new Record();
            chunk.setRecordName(excelUtils.getCellValue(wb,cell).toString());
            chunk.setRecords(getRecord(wb,chunkStart,chunkEnd));
            chuckList.add(chunk);
            chunkStart = chunkEnd;
        }
        return chuckList;
    }

    /**
     * @param wb
     * @param chunkStart
     * @param chunkEnd
     * @return
     */
    private List<Record> getRecord(Workbook wb, int chunkStart, int chunkEnd) {
        Sheet sheet=wb.getSheetAt(1);
        ExcelUtils excelUtils =new ExcelUtils();
        List<Record> recordList = new ArrayList<>();
        final int recordCol = 1;
        int recordEnd = 0;

        for (int recordStart = chunkStart; recordStart <= chunkEnd; recordStart++) {
            recordEnd = excelUtils.skipMergeRowCell(sheet, recordStart, recordCol);		// 列1对应记录名
            Record record = new Record();
            Row row = sheet.getRow(recordStart);
            Cell cell = row.getCell(recordCol);
            record.setRecordName(excelUtils.getCellValue(wb,cell).toString());
            record.setItemList(getItems(wb, recordStart, recordEnd));
            recordList.add(record);
            recordStart = recordEnd;
        }
        return recordList;
    }

    /**
     * @param wb
     * @param recordStart
     * @param recordEnd
     * @return
     */
    private List<Item> getItems(Workbook wb, int recordStart, int recordEnd) {
        Sheet sheet=wb.getSheetAt(1);
        ExcelUtils excelUtils =new ExcelUtils();
        List<Item> itemBeanList = new ArrayList<>();
        final int nameCol = 2;
        final int valueCol = 3;
        for (int i = recordStart; i <= recordEnd; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(nameCol);
            Item item = new Item();
            item.setItemName(excelUtils.getCellValue(wb,cell).toString());
            cell = row.getCell(valueCol);
            item.setItemVal(excelUtils.getCellValue(wb,cell).toString());
            item.setItemType(String.valueOf(cell.getCellTypeEnum()));
            itemBeanList.add(item);
        }
        return itemBeanList;
    }


    public List<Record> dealRecords(List<Record> records){
        List<Record> recordList = new ArrayList<Record>();
        for (Record x : records) {
            if(x.getpRecID()==0){
                Record record=x;
                record.setRecords(getRecords(records,x.getRecordID()));
                recordList.add(record);
            }
        }
        return recordList;
    }

    private List<Record> getRecords(List<Record> records,long ID) {
        List<Record> recordList = new ArrayList<Record>();
        for (Record x : records) {
            if(x.getpRecID()==ID){
                Record record=x;
                record.setItemList(getItems(x.getRecordID()));
                recordList.add(record);
            }
        }
        return recordList;
    }
    private List<Item>getItems(long ID){
        Item item=new Item();
        item.setRecordID(ID);
        List<Item> itemList=utils.itemService.select(item);
        return itemList;
    }

    public int writeToItemExcel(Workbook wb, List<Record> newList) {
        ExcelUtils excelUtils=new ExcelUtils();
        Sheet sheet=wb.getSheetAt(1);
        int oldRowCount = sheet.getPhysicalNumberOfRows();
        int rowCount = sheet.getPhysicalNumberOfRows();
        int chunkCount = newList.size();
        int startRow = 1;
        int addRecordNum = 0;    // 动态添加的记录数
        for (int i = 1; i < oldRowCount; i++) {
            Row row = sheet.getRow(i);
            if (row.getPhysicalNumberOfCells() < 4 || row.getCell(0).getCellTypeEnum().equals(CellType.BLANK)) {
                sheet.removeRow(row);
            }
            i = excelUtils.skipMergeRowCell(sheet, i, 0);
        }
        oldRowCount = sheet.getPhysicalNumberOfRows();
        rowCount = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < chunkCount; i++) {
            Record chunk = newList.get(i);

            // 写入记录
            for (Record record : chunk.getRecords()) {
                // 将记录值写入
                if (startRow < rowCount) {
                    startRow = writeOnlyValue(sheet,wb, record, startRow);
                }
                // 添加行并写入相关信息
                else {
                    addRecordNum++;
                    startRow = writeAddLine(sheet, record, startRow);
                    rowCount = startRow;
                }
            }
        }

        // 合并chunk
        mergeChunk(sheet, oldRowCount, rowCount);

        return addRecordNum;
    }

    /**
     * 合并chunk
     * @param oldRowCount
     * @param rowCount
     */
    private void mergeChunk(Sheet sheet, int oldRowCount, int rowCount) {
        for(int i=0; i<sheet.getMergedRegions().size(); i++){
            CellRangeAddress address = sheet.getMergedRegion(i);
            if (address.getLastRow()== oldRowCount-1 && address.getFirstColumn()==0){
                int si = address.getFirstRow();
                sheet.removeMergedRegion(i);
                sheet.addMergedRegionUnsafe(new CellRangeAddress(si, rowCount-1, 0,0));
            }
        }
    }

    /**
     * 只将值写入excel
     *
     * @param sheet
     * @param record
     * @param startRow
     * @return 新的起始行
     */
    private int writeOnlyValue(Sheet sheet, Workbook wb, Record record, int startRow) {
        ExcelUtils excelUtils = new ExcelUtils();
        final int nameCol = 2, valueCol = 3;
        List<Item> Items = record.getItemList();
        // 处理记录内的条目，对应一行
        for (int tem = startRow; startRow < tem + Items.size(); startRow++) {
            Row row = sheet.getRow(startRow);
            Cell cell = row.getCell(valueCol);
            // 如果是公式，不写入
            if (cell.getCellTypeEnum() == CellType.FORMULA) {
                continue;
            }
            // 找到对应条目写入值
            String name = String.valueOf(excelUtils.getCellValue(wb,row.getCell(nameCol)));
            for (Item item : Items) {
                if (item.getItemName().equals(name)) {
                    excelUtils.setCellValue(cell, item.getItemVal());
//                    excelUtils.setCellValue(cell, startRow + "");
                    break;
                }
            }
        }
        return startRow;
    }

    /**
     * 添加行并写入相关信息
     *
     * @param sheet
     * @param record
     * @param startRow
     */
    private int writeAddLine(Sheet sheet, Record record, int startRow) {
        ExcelUtils excelUtils = new ExcelUtils();
        final int nameCol = 2, valueCol = 3;
        List<Item> Items = record.getItemList();
        int itemCount = Items.size();
        int recordStartRow = startRow;

        // 处理记录内的条目
        for (; startRow < recordStartRow + itemCount; startRow++) {
            Row row = sheet.createRow(startRow);
            Row referRow = sheet.getRow(startRow - itemCount);
            Item Item = Items.get(startRow - recordStartRow);

            for (int i = 0; i < referRow.getPhysicalNumberOfCells(); i++) {
                Cell cell = row.createCell(i);
                Cell referCell = referRow.getCell(i);
                cell.setCellStyle(referCell.getCellStyle());
                cell.setCellType(referCell.getCellTypeEnum());

                // 根据单元格所属列写入不同数据
                if (i == 1 && startRow == recordStartRow) {
                    excelUtils.setCellValue(cell, record.getRecordName());
                } else if (referCell.getCellTypeEnum() == CellType.FORMULA) {
                    cell.setCellFormula(referCell.getCellFormula());
                } else if (i == nameCol) {
                    excelUtils.setCellValue(cell, Item.getItemName());
                } else if (i == valueCol) {
                    excelUtils.setCellValue(cell, Item.getItemVal());
                } else {
                    excelUtils.setCellValue(cell, referCell.getStringCellValue());
                }
            }
        }

        // 合并记录名单元格
        sheet.addMergedRegionUnsafe(new CellRangeAddress(recordStartRow, startRow - 1, 1, 1));
        // 合并chunk名单元格
        // sheet.addMergedRegionUnsafe(new CellRangeAddress(recordStartRow, startRow - 1, 0, 0));

        return startRow;
    }


    /**
     * 添加内容到显示表
     *
     * @param sheet
     * @param addRecordNum 需要添加的行数
     */
    public static void writeToReportExcel(Sheet sheet, int addRecordNum) {
        int rowCount = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < addRecordNum; i++) {
            Row row = sheet.createRow(rowCount + i);
            Row referRow = sheet.getRow(rowCount - 1);
            for (int j = 0; j < referRow.getPhysicalNumberOfCells(); j++) {
                Cell cell = row.createCell(j);
                Cell referCell = referRow.getCell(j);
                cell.setCellStyle(referCell.getCellStyle());
                cell.setCellType(referCell.getCellTypeEnum());
                cell.setCellFormula(referCell.getCellFormula());
            }
        }
    }

    public static boolean getLicense() {
        boolean result = false;
        try {
            String licenseurl = Utils.class.getClassLoader().getResource("licenseforcell.xml").getPath();;// license路径
            InputStream license=new FileInputStream(licenseurl);
            License a=new License();
            a.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 生成 日期+随机数的流水号
     * */
    public static String getNumberForPK(){
        String id="";
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        String temp = sf.format(new Date());
        int random=(int) (Math.random()*10000);
        id=temp+random;
        return id;
    }
}
