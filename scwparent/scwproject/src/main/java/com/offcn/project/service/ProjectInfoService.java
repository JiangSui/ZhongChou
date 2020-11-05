package com.offcn.project.service;

import java.util.List;

import com.offcn.project.po.*;

//获取项目信息
public interface ProjectInfoService {
    //通过项目id  获取到回报信息
    public List<TReturn> getProjectReturns(Integer projectId);

    /**
     * 获取系统中所有项目
     * @return
     */
    public List<TProject> getAllProjects();


    /**
     * 获取项目图片
     * @param id
     * @return
     */
    List<TProjectImages> getProjectImages(Integer id);

    /**
     * 获取项目信息
     * @param projectId
     * @return
     */
    TProject getProjectInfo(Integer projectId);


    /**
     * 获得项目标签
     * @return
     */
    List<TTag> getAllProjectTags();


    /**
     * 获取项目分类
     * @return
     */
    List<TType> getProjectTypes();

    /**
     * 获取回报信息
     * @param returnId
     * @return
     */
    TReturn getReturnInfo(Integer returnId);
}