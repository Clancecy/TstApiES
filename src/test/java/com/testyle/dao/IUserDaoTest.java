package com.testyle.dao;

import com.alibaba.fastjson.JSON;
import com.testyle.dao.IUserDao;
import com.testyle.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 加载spring配置文件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class IUserDaoTest {

    @Autowired
    private IUserDao dao;

    @Test
    public void testSelectUser() throws Exception {
        long id = 1;
        User user = dao.selectUser(id);
        System.out.println(user.getUserName());
    }

    @Test
    public void testJsonString(){
        Map<String,Object> res=new HashMap<>();
        User user =new User();
        user.setUserID(1);
        List<User> users=new ArrayList<>();
        users.add(user);
        User user2 =new User();
        user2.setUserID(2);
        users.add(user2);

        res.put("code",101);
        res.put("message","添加成功");
        res.put("data",users);

        System.out.println(JSON.toJSONString(res));

    }

    @Test
    public void testAddUser(){
        User user=new User();
        user.setUserName("cc");
        long id=dao.addUser(user);
        System.out.println(user.getUserID());
    }

    @Test
    public void testSession(){
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userName","234");
        session.setAttribute("userID","235");
        System.out.println(session.getAttribute("userName"));

    }

}
