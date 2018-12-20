package com.testyle.dao;

import com.alibaba.fastjson.JSON;
import com.testyle.common.Utils;
import com.testyle.dao.IStationDao;
import com.testyle.model.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class IStationDaoTest {
    @Autowired
    private IStationDao stationDao;

    @Test
    public void testSeleteAll(){
        System.out.println(JSON.toJSONString(stationDao.selectAll()));
    }

    @Test
    public void testRead(){
        Utils utils=new Utils();
        List<Record> excelRecords= utils.readExcel("E:\\file2\\直流电阻模板20180301.xlsx");
        System.out.println(JSON.toJSONString(excelRecords));
    }

}
