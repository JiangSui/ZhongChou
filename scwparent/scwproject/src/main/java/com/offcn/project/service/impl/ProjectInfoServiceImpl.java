package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private TReturnMapper returnMapper;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper imagesMapper;

    @Autowired
    private TTypeMapper tTypeMapper;
    @Autowired
    private TTagMapper tagMapper;


    //获取回报信息 根据项目id
    public List<TReturn> getProjectReturns(Integer projectId) {
        TReturnExample example  = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);
    }

    //获取所有的项目信息
    public List<TProject> getAllProjects() {
        return projectMapper.selectByExample(null);
    }

    //获取指定项目的图片
    public List<TProjectImages> getProjectImages(Integer id) {
        TProjectImagesExample example = new TProjectImagesExample();
        example.createCriteria().andProjectidEqualTo(id);
        return imagesMapper.selectByExample(example);
    }

    //项目详情
    public TProject getProjectInfo(Integer projectId) {
        TProject project = projectMapper.selectByPrimaryKey(projectId);
        return project;
    }


    //获取所有的标签
    public List<TTag> getAllProjectTags() {
        return tagMapper.selectByExample(null);
    }

    //获取所有的类型
    public List<TType> getProjectTypes() {
        return tTypeMapper.selectByExample(null);
    }

    @Override
    public TReturn getReturnInfo(Integer returnId) {
        //根据回报id  获取到回报详细信息
        return  returnMapper.selectByPrimaryKey(returnId);

    }
}
