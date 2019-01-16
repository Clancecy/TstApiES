package com.testyle.dao;

import com.testyle.model.Project;

import java.util.List;

public interface IProDao {
    List<Project> select(Project project);
    int insert(Project project);
    int delete(long ID);
    int update(Project project);
    List<Project> selectList(List<Long> proIDs);
}
