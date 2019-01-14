package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Worksheet;
import com.testyle.common.ExcelUtils;
import com.testyle.common.ResContent;
import com.testyle.common.Utils;
import com.testyle.model.Item;
import com.testyle.model.Project;
import com.testyle.model.Record;
import com.testyle.service.IItemService;
import com.testyle.service.IProService;
import com.testyle.service.IRecordService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/project")
public class ProController {
    @Resource
    private IProService proService;
    @Resource
    private IRecordService recordService;
    @Resource
    private IItemService itemService;

    String charact = "UTF-8";
    String root = "E:/file/";
    @Value("${testPath}")
    String testRoot;
    @Value("${repPath}")
    String repPath;
    @Value("${repUrl}")
    String repUrl;

    @RequestMapping("/index")
    public void index(HttpServletResponse response) throws IOException {
        response.getWriter().write(System.getProperty("root") + "/file");
        response.getWriter().close();
    }

    @RequestMapping("/upload")
    public void doUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        if (!file.isEmpty()) {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
                    root,
                    file.getOriginalFilename()));
        }
        String url = root + file.getOriginalFilename();
        long devTypeID = Long.parseLong(request.getParameter("devTypeID"));
        String temp=file.getOriginalFilename();
        String fileName=temp.split("\\.")[0];
        Project project = new Project();
        project.setUrl(url);
        project.setProName(fileName);
        project.setDevTypeID(devTypeID);
        project.setProType(0);
        long ID = proService.insert(project);
        if (ID > 0) {
            readExcel(url,project.getProID(),1);
            Project projectTemp = new Project();
            projectTemp.setDevTypeID(devTypeID);
            projectTemp.setProType(1);
            List<Project> list=proService.select(projectTemp);
            if(readDefaultNum(url)==1&&list.size()==0) {
                project.setProType(1);
                project.setProID(-1);
                ID = proService.insert(project);
                if (ID > 0) {
                    readExcel(url, project.getProID(), 3);
                    resContent.setCode(101);
                    resContent.setMessage("上传成功");
                }else {
                    resContent.setCode(102);
                    resContent.setMessage("上传失败");
                }
            }
            resContent.setCode(101);
            resContent.setMessage("上传成功");
        } else {
            resContent.setCode(102);
            resContent.setMessage("上传失败");
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/list")
    public void getProList(Project project,HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        List<Project> projects=proService.select(project);
//        List< Map<String,Object>> mapList=new ArrayList<>();
//        for(Project pro :projects){
//            Map<String,Object>map =new HashMap<>();
//            map.put("id",pro.getProID());
//            map.put("label",pro.getProName());
//            mapList.add(map);
//        }
        if(projects.size()==0){
            resContent.setCode(102);
            resContent.setMessage("没有数据");
        }else {
            resContent.setMessage("获取成功");
            resContent.setCode(101);
            resContent.setData(projects);
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/get")
    public void getPro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        long proID = Long.parseLong(request.getParameter("proID"));
        long testID = Long.parseLong(request.getParameter("testID"));
        long taskID = Long.parseLong(request.getParameter("taskID"));
        if(request.getParameter("proID")==null
        ||request.getParameter("testID")==null
        ||request.getParameter("taskID")==null){
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            Project project=new Project();
            project.setProID(proID);
            List <Project> projectList= proService.select(project);
            if(projectList==null||projectList.size()==0){
                resContent.setCode(104);
                resContent.setMessage("没有该项目");
            }else {
                project = projectList.get(0);
                String url = project.getUrl();
                int num = readRecordNum(url);
                String code="test"+testID+"task"+taskID+"pro"+proID;
                Record record = new Record();
                record.setProID(proID);
                List<Record> records = recordService.select(record);
                List<Record> objects = dealRecords(records);
                Record chunk = objects.get(objects.size() - 1);
                for(int i=0;i<num-1;i++){
                    addRecord(chunk,i);
                }
                String testUrl=addExcel(project,code,num,url,objects,resContent);
                if(resContent.getCode()!=105) {
                    if (objects.size() > 0) {
                        resContent.setCode(101);
                        resContent.setMessage("获取成功");
                            Map<String, Object> map = new HashMap<>();
                            map.put("url", testUrl);
                            map.put("records", objects);
                            map.put("remark","");
                        resContent.setData(map);
                    } else {
                        resContent.setCode(102);
                        resContent.setMessage("获取失败");
                    }
                }
            }
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    @RequestMapping("/default")
    public void getDefault(Project project,HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        if(project.getDevTypeID()==-1){
            resContent.setCode(103);
            resContent.setMessage("参数错误");
        }else {
            project.setProType(1);
            List<Project> projectList=proService.select(project);
            if(projectList.size()==0){
                resContent.setCode(104);
                resContent.setMessage("没有出厂值");
            }else {
                project =projectList.get(0);
                Record record = new Record();
                record.setProID(project.getProID());
                List<Record> records = recordService.select(record);
                List<Record> objects = dealRecords(records);
                if(objects.size()>0){
                    resContent.setCode(101);
                    resContent.setMessage("获取成功");
                    resContent.setData(objects);
                }else {
                    resContent.setCode(102);
                    resContent.setMessage("获取失败");
                }
            }
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    private String addExcel(Project project, String code, int num, String url, List<Record> objects, ResContent resContent){
        String testUrl="";
        Utils utils=new Utils();
        try {
            File soure=new File(url);
            File target=new File(testRoot,project.getProName()+code+".xlsx");
            testUrl=target.getPath();
//            if(target.exists()){
//                return testUrl;
//            }
            FileUtils.copyFile(soure,target);

            FileInputStream fis = new FileInputStream(testUrl);
            Workbook workbook = WorkbookFactory.create(fis);
            int addRecordNum = utils.writeToItemExcel(workbook,objects);
            utils.writeToReportExcel(workbook.getSheetAt(0), addRecordNum);
            FileOutputStream fos = new FileOutputStream(testUrl);
            workbook.write(fos);
            workbook.close();
            fos.close();
            fis.close();

        }catch (Exception e){
            resContent.setCode(105);
            resContent.setMessage("文件操作失败");
            e.printStackTrace();
        }
        return testUrl;
    }

    private int readRecordNum(String testUrl){
        int num=0;
        ExcelUtils excelUtils=new ExcelUtils();
        try {
            FileInputStream fis = new FileInputStream(testUrl);
            Workbook workbook = WorkbookFactory.create(fis);
            Row row = workbook.getSheetAt(2).getRow(0);
            Cell cell = row.getCell(1);
            String val=excelUtils.getCellValue(workbook,cell).toString();
            num= (int)Double.parseDouble(val);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    private int readDefaultNum(String testUrl){
        int num=0;
        ExcelUtils excelUtils=new ExcelUtils();
        try {
            FileInputStream fis = new FileInputStream(testUrl);
            Workbook workbook = WorkbookFactory.create(fis);
            Row row = workbook.getSheetAt(2).getRow(1);
            Cell cell = row.getCell(1);
            String val=excelUtils.getCellValue(workbook,cell).toString();
            num= (int)Double.parseDouble(val);
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    public void addRecord(Record chunkBean,int num) {
        List<Record> recordBeans = chunkBean.getRecords();
        Record rBean1 = recordBeans.get(0);
        Record rBean2 = JSON.parseObject(JSON.toJSONString(rBean1), Record.class);
        rBean2.setRecordName(String.valueOf(num+2));
        List<Item> itemBeans = rBean2.getItemList();
        itemBeans.get(0).setItemVal("5");
        itemBeans.get(1).setItemVal("6");
        itemBeans.get(2).setItemVal("7");
        recordBeans.add(rBean2);
    }
    private List<Record> dealRecords(List<Record> records){
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
        List<Item> itemList=itemService.select(item);
        return itemList;
    }
    private void readExcel(String fname, long proID,int sheetAt) {
        try {
            FileInputStream fis = new FileInputStream(fname);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(sheetAt);
            addFirstRecord(sheet,workbook, proID, 0);
            workbook.close();
            fis.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }

    }

    private void addFirstRecord(Sheet sheet, Workbook wb, long proID, long pRecID) {
        ExcelUtils excelUtils = new ExcelUtils();
        int rowCount = sheet.getPhysicalNumberOfRows();
        final int chunkCol = 0;
        int chunkEnd = 0;
        Record record = new Record();
        for (int chunkStart = 1; chunkStart < rowCount; chunkStart++) {
            chunkEnd = excelUtils.skipMergeRowCell(sheet, chunkStart, chunkCol);    // 列0对应chunk
            Row row = sheet.getRow(chunkStart);
            Cell cell = row.getCell(chunkCol);
            if (excelUtils.getCellValue(wb,cell).toString().equals(""))
                break;
            record.setProID(proID);
            record.setpRecID(pRecID);
            record.setRecordName(excelUtils.getCellValue(wb,cell).toString());
            int count = recordService.insert(record);
            if (count > 0) {
                addSecondRecord(sheet,wb, chunkStart, chunkEnd, proID, record.getRecordID());
            }
            chunkStart = chunkEnd;
        }
    }

    private void addSecondRecord(Sheet sheet, Workbook wb, int Start, int End, long proID, long pRecID) {
        final int recordCol = 1;
        int recordEnd = 0;
        ExcelUtils excelUtils = new ExcelUtils();
        Record record = new Record();
        for (int recordStart = Start; recordStart <= End; recordStart++) {
            recordEnd = excelUtils.skipMergeRowCell(sheet, recordStart, recordCol);        // 列1对应记录名
            Row row = sheet.getRow(recordStart);
            Cell cell = row.getCell(recordCol);
            record.setProID(proID);
            record.setpRecID(pRecID);
            record.setRecordName(excelUtils.getCellValue(wb,cell).toString());
            int count = recordService.insert(record);
            if (count > 0) {
                addItems(sheet,wb, recordStart, recordEnd, record.getRecordID());
            }
            recordStart = recordEnd;
        }
    }

    private void addItems(Sheet sheet, Workbook wb, int Start, int End, long recordID) {
        List<Item> itemList = new ArrayList<>();
        final int nameCol = 2;
        final int valueCol = 3;
        ExcelUtils excelUtils = new ExcelUtils();
        for (int i = Start; i <= End; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(nameCol);
            Item item = new Item();
            item.setItemName(excelUtils.getCellValue(wb,cell).toString());
            cell = row.getCell(valueCol);
            item.setItemVal(excelUtils.getCellValue(wb,cell).toString());
            item.setItemType(String.valueOf(cell.getCellTypeEnum()));
            item.setRecordID(recordID);
            itemList.add(item);
        }
        itemService.insertList(itemList);
    }
    @RequestMapping("/down")
    public ResponseEntity<byte[]> filedownload(HttpServletRequest request) throws Exception{
        File file = new File("E:\\file\\直流电阻模板.xlsx");
        String filename = "直流电阻模板";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
    }

    @RequestMapping("/report")
    public void report(HttpServletRequest request,HttpServletResponse response)throws IOException{
        response.setCharacterEncoding(charact);
        ResContent resContent=new ResContent();
        try {
            String path=request.getParameter("path");
            String fname=request.getParameter("fname");
            String pdfUrl=toPDF(path,fname);
            if(pdfUrl!=null) {
                resContent.setCode(101);
                resContent.setMessage("获取成功");
                resContent.setData(pdfUrl);
            }else {
                resContent.setCode(102);
                resContent.setMessage("PDF获取失败");
            }
        }catch (Exception e){
            resContent.setCode(103);
            resContent.setMessage("获取失败");
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }

    private String toPDF(String path, String fname) {
        if (false) {
            return null;
        }else {
            try {
                String pdfPath = repPath + fname + ".pdf";
                String pdfUrl = repUrl + fname + ".pdf";
                File pdfFile = new File(pdfPath);// 输出路径
                if (pdfFile.exists()) {
                    pdfFile.delete();
                }
                com.aspose.cells.Workbook wb = new com.aspose.cells.Workbook(path);// 原始excel路径
                FileOutputStream fileOS = new FileOutputStream(pdfFile);
                for (int i = 1; i < wb.getWorksheets().getCount(); i++)
                {
                    wb.getWorksheets().get(i).setVisible(false);
                }
                wb.save(fileOS, SaveFormat.PDF);
                fileOS.close();
                return pdfUrl;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    @RequestMapping("/uploadCover")
    public void doUploadCover(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(charact);
        ResContent resContent = new ResContent();
        if (!file.isEmpty()) {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
                    root,
                    file.getOriginalFilename()));
        }
        String url = root + file.getOriginalFilename();
        long devTypeID = Long.parseLong(request.getParameter("devTypeID"));
        String temp=file.getOriginalFilename();
        String fileName=temp.split("\\.")[0];
        Project project = new Project();
        project.setUrl(url);
        project.setProName(fileName);
        project.setDevTypeID(devTypeID);
        project.setProType(3);
        long ID = proService.insert(project);
        if (ID > 0) {
            resContent.setCode(101);
            resContent.setMessage("上传成功");
        } else {
            resContent.setCode(102);
            resContent.setMessage("上传失败");
        }
        response.getWriter().write(JSON.toJSONString(resContent));
        response.getWriter().close();
    }
}
