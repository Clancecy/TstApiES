package com.testyle.service.impl;

import com.testyle.dao.IDataDao;
import com.testyle.model.Data;
import com.testyle.service.IDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("dataService")
@Transactional
public class DataServiceImpl implements IDataService {

    @Resource
    private IDataDao dataDao;

    @Override
    public List<Data> select(Data data) {
        return dataDao.select(data);
    }

    @Override
    public int insert(Data data) {
        return dataDao.insert(data);
    }

    @Override
    public int delete(long dataID) {
        return dataDao.delete(dataID);
    }

    @Override
    public int update(Data data) {
        return dataDao.update(data);
    }

    @Override
    public int insertList(List<Data> dataList) {
        return dataDao.insertList(dataList);
    }

    @Override
    public long selOne(Data data) {
        return dataDao.selOne(data);
    }

    @Override
    public int updateList(List<Data> dataList) {
        return dataDao.updateList(dataList);
    }


}
