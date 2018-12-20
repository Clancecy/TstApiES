package com.testyle.service;

import com.testyle.model.Project;

import java.util.List;

public interface IProService {
    List<Project> select(Project project);
    int insert(Project project);
    int delete(long ID);
    int update(Project project);
}
