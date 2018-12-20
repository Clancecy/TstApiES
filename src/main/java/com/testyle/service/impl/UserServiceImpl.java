package com.testyle.service.impl;

import com.testyle.dao.IUserDao;
import com.testyle.model.User;
import com.testyle.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao userDao;

    public User selectUser(long userId) {
        return this.userDao.selectUser(userId);
    }

    public int addUser(User user) {
        return this.userDao.addUser(user);
    }

    public long checkUser(User user) {
        return userDao.checkUser(user);
    }

    public int deleteUser(long userID) {
        return userDao.deleteUser(userID);
    }


}
