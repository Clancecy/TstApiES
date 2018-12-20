package com.testyle.service.impl;

import com.testyle.dao.ITestDao;
import com.testyle.model.Test;
import com.testyle.service.ITestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("testService")
@Transactional
public class TestServiceImpl implements ITestService {

    @Resource
    private ITestDao testDao;

    @Override
    public List<Test> select(Test test) {
        return testDao.select(test);
    }

    @Override
    public int insert(Test test) {
        return testDao.insert(test);
    }

    @Override
    public int delete(long testID) {
        return testDao.delete(testID);
    }

    @Override
    public int update(Test test) {
        return testDao.update(test);
    }
}
