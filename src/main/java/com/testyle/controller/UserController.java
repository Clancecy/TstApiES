package com.testyle.controller;

import com.alibaba.fastjson.JSON;
import com.testyle.model.User;
import com.testyle.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/show")
    public void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        long userId = Long.parseLong(request.getParameter("userID"));
        User user = this.userService.selectUser(userId);
        response.getWriter().write(JSON.toJSONString(user));
        response.getWriter().close();
    }

    @RequestMapping("/check")
    public void checkUser(User user, HttpServletResponse response)
    throws IOException{
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> res = new HashMap<>();
        long userID=userService.checkUser(user);
        if(userID>0){
            res.put("code",101);
            res.put("message","存在该用户");
            res.put("data",null);
        }else {
            res.put("code",102);
            res.put("message","不存在该用户");
            res.put("data",null);
        }
        response.getWriter().write(JSON.toJSONString(res));
        response.getWriter().close();
    }

    @RequestMapping("/register")
    public void addUser(User user, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> res = new HashMap<>();
        if (isEmptyUser(user)) {
            res.put("code", 102);
            res.put("message", "参数为空或错误");
        }
        else if(checkUser(user)){
            res.put("code", 103);
            res.put("message", "用户名已存在");
        }else {
            int count = userService.addUser(user);
            if (count > 0) {
                res.put("code", 101);
                res.put("message", "添加成功");
            } else {
                res.put("code", 104);
                res.put("message", "添加失败");
            }
        }
        response.getWriter().write(JSON.toJSONString(res));
        response.getWriter().close();
    }

    @RequestMapping("/login")
    public void userLogin(User user, HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> res = new HashMap<>();
        Map<String,Object> data= new HashMap<>();
        if (isEmptyUser(user)) {
            res.put("code", 102);
            res.put("message", "参数为空或错误");
        }
        if(checkUser(user)){
            res.put("code",101);
            res.put("message","登录成功");
            long userID=userService.checkUser(user);
            HttpSession session = request.getSession();
            session.setAttribute("userName",user.getUserName());
            session.setAttribute("userID",userID);
        }else {
            res.put("code",103);
            res.put("message","用户名或密码错误");
        }
        response.getWriter().write(JSON.toJSONString(res));
        response.getWriter().close();
    }

    @RequestMapping("/getSession")
    public void getSession(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("UTF-8");
        HttpSession session=request.getSession();
        Map<String, Object> res = new HashMap<>();
        Map<String,Object> data= new HashMap<>();
        if(session.getAttribute("userID")==null){
            res.put("code",102);
            res.put("message","登录信息已过期");
        }else {
            long userID=(long)session.getAttribute("userID");
            String userName=(String)session.getAttribute("userName");
            res.put("code", 101);
            res.put("message", "获取成功");
            data.put("userID",userID);
            data.put("userName",userName);
            res.put("data",data);
        }
        response.getWriter().write(JSON.toJSONString(res));
        response.getWriter().close();
    }

    @RequestMapping("/logout")
    public void Logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("UTF-8");
        HttpSession session=request.getSession();
        session.invalidate();
        Map<String, Object> res = new HashMap<>();
        res.put("code",101);
        res.put("message","退出登录");
        response.getWriter().write(JSON.toJSONString(res));
        response.getWriter().close();

    }

    @RequestMapping("/delete")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response)throws IOException{
        response.setCharacterEncoding("UTF-8");
        long userId = Long.parseLong(request.getParameter("userID"));
        int delete=userService.deleteUser(userId);
        Map<String, Object> res = new HashMap<>();
        if(delete>0) {
            res.put("code", 101);
            res.put("message", "删除成功");
        }else {
            res.put("code", 102);
            res.put("message", "删除失败");
        }
    }

    private boolean checkUser(User user){
        long userID=userService.checkUser(user);
        if(userID>0)
            return true;
        else
            return false;
    }

    private boolean isEmptyUser(User user){
        if (user.getUserName() == null|| user.getUserName().isEmpty()
                ||user.getUserPassword()==null||user.getUserPassword().isEmpty()) {
            return true;
        }else
            return false;
    }
}
