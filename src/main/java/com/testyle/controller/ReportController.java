package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.testyle.common.ExcelUtils;
import com.testyle.common.ResContent;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {
    String reportPDF = "E:/reportPDF/";

    @RequestMapping("/create")
    public void createReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        ResContent resContent = new ResContent();
        String fnameStr = request.getParameter("fnameList");
        String reportName = request.getParameter("reportName");
        if (fnameStr == null || "".equals(fnameStr)
                || reportName == null || "".equals(reportName)) {
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        } else {
            try {
                System.out.println(fnameStr);
                List<String> fnameList = JSON.parseArray(fnameStr, String.class);
                int fsize = fnameList.size();
                if (fsize == 0) {
                    resContent.setCode(104);
                    resContent.setMessage("没有项目信息");
                } else {
                    ExcelUtils excelUtils = new ExcelUtils();
                    String fnameDist = reportPDF+reportName+".xlsx";
                    Workbook workbookDist = null;
                    File targetFile = new File(fnameDist);
                    if (targetFile.exists()) {
                        targetFile.delete();
                    }
                    if (fnameDist.endsWith(".xls")) {
                        workbookDist = new HSSFWorkbook();
                    } else if (fnameDist.endsWith(".xlsx")) {
                        workbookDist = new XSSFWorkbook();
                    }
                    Sheet sheetDist = workbookDist.createSheet();

                    // 合并
                    for (int i = 0; i < fnameList.size(); i++) {
                        FileInputStream fis = new FileInputStream(fnameList.get(i));
                        Workbook workbookSrc = WorkbookFactory.create(fis);
                        workbookSrc.setForceFormulaRecalculation(true);
                        Sheet sheetSrc = workbookSrc.getSheetAt(0);
                        excelUtils.mergeSheet(sheetDist, sheetSrc);
                        // 设置宽度
                        if (0 == i) {
                            excelUtils.setColumnWidth(sheetDist, sheetSrc);
                        }
                        workbookSrc.close();
                        fis.close();
                    }
                    FileOutputStream fos = new FileOutputStream(fnameDist);
                    workbookDist.write(fos);
                    fos.flush();
                    workbookDist.close();
                    fos.close();
                    System.out.println("end");
                    resContent.setCode(101);
                    resContent.setMessage("成功了");
                    resContent.setData(fnameDist);
                }
            } catch (JSONException e) {
                resContent.setCode(104);
                resContent.setMessage(e.getMessage());
            } catch (Exception e) {
                resContent.setCode(105);
                resContent.setMessage(e.getMessage());
            }
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

}
