package com.testyle.service;

import com.testyle.model.Test;

import java.util.List;

public interface ITestService {
    List<Test> select(Test test);
    int insert(Test test);
    int delete(long testID);
    int update(Test test);
}
