package com.testyle.service.impl;

import com.testyle.dao.IRecordDao;
import com.testyle.model.Record;
import com.testyle.service.IRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("recordService")
@Transactional
public class RecordServiceImpl implements IRecordService {

    @Resource
    private IRecordDao recordDao;
    @Override
    public List<Record> select(Record record) {
        return recordDao.select(record);
    }

    @Override
    public int insert(Record record) {
        return recordDao.insert(record);
    }

    @Override
    public int delete(long ID) {
        return recordDao.delete(ID);
    }

    @Override
    public int update(Record record) {
        return recordDao.update(record);
    }

}
