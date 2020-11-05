package com.offcn.project.service;

import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {

    //初始化项目
    /**
     *  传入用户id  , 创建一个projecttokent , 项目token
     *  存入到redis
     */
    public String initCreateProject(Integer memberId);


    /**
     * 保存项目信息
     * 状态 和 信息
     */
    public  void  saveProjectInfo(ProjectStatusEnume statusEnume, ProjectRedisStorageVo storageVo);

}
