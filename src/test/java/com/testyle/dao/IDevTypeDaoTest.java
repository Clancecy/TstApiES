package com.testyle.dao;

import com.alibaba.fastjson.JSON;
import com.testyle.dao.IDevTypeDao;
import com.testyle.model.DevType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class IDevTypeDaoTest {
    @Autowired
    private IDevTypeDao devTypeDao;

    @Test
    public void testSelect(){
        DevType devType=new DevType();
        System.out.println(JSON.toJSONString(devTypeDao.select(devType)));
    }
}
