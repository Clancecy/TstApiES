package com.testyle.service;

import com.testyle.model.User;

public interface IUserService {
    User selectUser(long userId);
    int addUser(User user);
    long checkUser(User user);
    int deleteUser(long userID);
}
