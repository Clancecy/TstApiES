package com.testyle.service.impl;

import com.testyle.dao.IProDao;
import com.testyle.model.Project;
import com.testyle.service.IProService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("proService")
@Transactional
public class ProServiceImpl implements IProService {

    @Resource
    private IProDao proDao;

    @Override
    public List<Project> select(Project project) {
        return proDao.select(project);
    }

    @Override
    public int insert(Project project) {
        return proDao.insert(project);
    }

    @Override
    public int delete(long ID) {
        return proDao.delete(ID);
    }

    @Override
    public int update(Project project) {
        return proDao.update(project);
    }
}
