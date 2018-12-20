package com.testyle.dao;

import com.testyle.model.User;

public interface IUserDao {
    User selectUser(long userID);
    int addUser(User user);
    long checkUser(User user);
    int deleteUser(long userID);
}
