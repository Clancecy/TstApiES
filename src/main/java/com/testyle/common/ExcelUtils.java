/**
 * @author Peng
 * @date 2018年11月26日
 */
package com.testyle.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * @author Peng
 *
 */
public class ExcelUtils {

    /**
     * 根据文件类型的不同选择不同的打开方式
     *
     * @param fis
     * @param fname
     * @return
     * @throws IOException
     */
    public Workbook getWorkbok(FileInputStream fis, String fname) throws IOException {
        Workbook wb = null;
        if (fname.endsWith("xls")) { // Excel 2003
            wb = new HSSFWorkbook(fis);
        } else if (fname.endsWith("xlsx")) { // Excel 2007/2010
            wb = new XSSFWorkbook(fis);
        }
        return wb;
    }

    /**
     * 根据Cell的类型读取并返回数据
     *
     * @param cell
     * @return
     */
    public Object getCellValue(Workbook wb, Cell cell) {
        FormulaEvaluator evaluator=wb.getCreationHelper().createFormulaEvaluator();
        Object value = null;
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case ERROR:
                value = cell.getErrorCellValue();
                break;
            case NUMERIC:
                value = cell.getNumericCellValue();
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case FORMULA:
                try {
                    CellValue cellValue=evaluator.evaluate(cell);
                    value = String.valueOf(cellValue.getNumberValue());
                } catch (IllegalStateException e) {
                    value = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 根据Cell的类型写入数据
     * @param cell
     * @return
     */
    public void setCellValue(Cell cell, String value) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                try {
                    cell.setCellValue(Boolean.getBoolean(value));
                } catch (Exception e) {
                    cell.setCellValue(value);
                }
                break;
            case ERROR:
                try {
                    cell.setCellErrorValue(Byte.parseByte(value));
                } catch (Exception e) {
                    cell.setCellValue(value);
                }
                break;
            case NUMERIC:
                try {
                    cell.setCellValue(Double.parseDouble(value));
                } catch (Exception e) {
                    cell.setCellValue(value);
                }
                break;
            case STRING:
                try {
                    cell.setCellValue(Double.parseDouble(value));
                    cell.setCellType(CellType.NUMERIC);
                } catch (Exception e) {
                    cell.setCellValue(value);
                }

                break;
            case FORMULA:
                // 公式不处理
                break;
            case BLANK:
                // 空白优先写入数值型
                try {
                    cell.setCellValue(Double.parseDouble(value));
                    cell.setCellType(CellType.NUMERIC);
                } catch (Exception e) {
                    cell.setCellValue(value);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 如果是合并的单元格行，返回合并单元格的最后一行
     *
     * @param sheet
     * @param i：当前单元格所在行
     * @param j：当前单元格所在列
     * @return i: 合并单元格最后一行的行号
     */
    public int skipMergeRowCell(Sheet sheet, int i, int j) {
        int numMergedRegions = sheet.getNumMergedRegions();
        if (numMergedRegions <= 0) {
            return i;
        }
        // 如果cell在合并区域内，直接跳到最后合并行
        for (int k = 0; k < numMergedRegions; k++) {
            CellRangeAddress address = sheet.getMergedRegion(k);
            if (i < address.getFirstRow() || i > address.getLastRow()) {
                continue;
            }
            if (j >= address.getFirstColumn() && j <= address.getLastColumn()) {
                return address.getLastRow();
            }
        }
        return i;
    }

    /**
     * 如果是合并的单元格列，返回合并单元格的最后一列
     *
     * @param sheet
     * @param i：当前单元格所在行
     * @param j：当前单元格所在列
     * @return j: 合并单元格最后一列列索引
     */
    public int skipMergeColCell(Sheet sheet, int i, int j) {
        int numMergedRegions = sheet.getNumMergedRegions();
        if (numMergedRegions <= 0) {
            return j;
        }
        // 如果cell在合并区域内，直接跳到最后合并行号，否则直接返回
        for (int k = 0; k < numMergedRegions; k++) {
            CellRangeAddress address = sheet.getMergedRegion(k);
            if (i < address.getFirstRow() || i > address.getLastRow()) {
                continue;
            }
            if (j >= address.getFirstColumn() && j <= address.getLastColumn()) {
                i = address.getLastColumn();
                return j;
            }
        }
        return j;
    }

//    /**
//     * 设置表格宽度
//     * @param sheetDist
//     * @param sheetSrc
//     */
//    public  void setColumnWidth(Sheet sheetDist, Sheet sheetSrc) {
//        int colCount = 0;
//        // 遍历获取列数
//        for (Row row : sheetSrc) {
//            colCount = Math.max(colCount, row.getPhysicalNumberOfCells());
//        }
//        // 设置宽度
//        for (int i = 0; i < colCount; i++) {
//            sheetDist.setColumnWidth(i, sheetSrc.getColumnWidth(i));
//        }
//    }

    /**
     * 合并sheet到sheet1
     * @param sheet1
     * @param sheet2
     */
    public  void mergeSheet(Sheet sheet1, Sheet sheet2) {
        int startRow = sheet1.getPhysicalNumberOfRows();
        int rowCount = sheet2.getPhysicalNumberOfRows();

        // 复制行, 将sheet2[i]拷贝到sheet1[startRow+i]
        for (int i = 0; i < rowCount; i++) {
            Row referRow = sheet2.getRow(i);
            Row newRow = sheet1.createRow(startRow+i);
            copyRow(newRow, referRow);
        }

        // 处理合并单元格
        megerCell(sheet1, sheet2, startRow);
    }


    /**
     * 合并行，通过复制的形式
     * @param newRow
     * @param referRow
     */
    private void copyRow(Row newRow, Row referRow) {
        FormulaEvaluator evaluator=referRow.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        newRow.setHeight(referRow.getHeight());
        for (int i = 0; i < referRow.getPhysicalNumberOfCells(); i++) {
            Cell newCell = newRow.createCell(i);
            Cell referCell = referRow.getCell(i);
            // 根据单元格类型写入数据
            switch (referCell.getCellTypeEnum()) {
                case BLANK:
                    newCell.setCellType(CellType.BLANK);
                    newCell.setCellValue(referCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellType(CellType.BOOLEAN);
                    newCell.setCellValue(referCell.getBooleanCellValue());
                    break;
                case ERROR:
                    newCell.setCellType(CellType.ERROR);
                    newCell.setCellValue(referCell.getErrorCellValue());
                    break;
                case NUMERIC:
                    newCell.setCellType(CellType.NUMERIC);
                    newCell.setCellValue(referCell.getNumericCellValue());
                    break;
                case FORMULA:
                    CellValue cellValue=evaluator.evaluate(referCell);
                    try {

                        newCell.setCellValue(cellValue.getNumberValue());
                    } catch (IllegalStateException e) {
                        newCell.setCellValue(cellValue.getStringValue());
                    }
                    break;
                case STRING:
                    newCell.setCellType(CellType.STRING);
                    newCell.setCellValue(referCell.getStringCellValue());
                    break;
                default:
                    try {
                        double value = referCell.getNumericCellValue();
                        newCell.setCellType(CellType.NUMERIC);
                        newCell.setCellValue(value);
                    } catch (Exception e) {
                        String value = referCell.getStringCellValue();
                        newCell.setCellType(CellType.STRING);
                        newCell.setCellValue(value);
                    }
                    break;
            }
            // 设置样式
            CellStyle newStyle = newCell.getRow().getSheet().getWorkbook().createCellStyle();
            newStyle.cloneStyleFrom(referCell.getCellStyle());
            newCell.setCellStyle(newStyle);
        }
    }

    /**
     * 将sheet2复制到sheet1后进行单元格的合并
     * @param sheet1
     * @param sheet2
     */
    private void megerCell(Sheet sheet1, Sheet sheet2, int startRow) {
        int num = sheet2.getNumMergedRegions();
        for (int i = 0; i < num; i++) {
            CellRangeAddress address = sheet2.getMergedRegion(i);
            sheet1.addMergedRegion(new CellRangeAddress(address.getFirstRow()+startRow, address.getLastRow()+startRow,
                    address.getFirstColumn(), address.getLastColumn()));
        }
    }

    /**
     * 设置表格宽度
     *
     * @param sheet
     */
    public static void setColumnWidth(Sheet sheet) {
        int colCount = 0;
        int width = 22000;	// pdf总宽度22000
        width = (int) ((75 + 0.72) * 256);
        // 遍历获取列数
        for (Row row : sheet) {
            colCount = Math.max(colCount, row.getPhysicalNumberOfCells());
        }
        width = width / colCount;
        System.out.println(sheet.getColumnWidth(0));
        System.out.println(width);
        // 设置宽度
        for (int i = 0; i < colCount; i++) {
            sheet.setColumnWidth(i, width);
        }
    }
}
