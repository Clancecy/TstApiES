package com.testyle.dao;

import com.testyle.model.Test;

import java.util.List;

public interface ITestDao {
    List<Test> select(Test test);
    int insert(Test test);
    int delete(long testID);
    int update(Test test);
}
