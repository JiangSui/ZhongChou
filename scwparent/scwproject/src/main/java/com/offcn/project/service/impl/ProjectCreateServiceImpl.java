package com.offcn.project.service.impl;
import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectTagMapper tagMapper;

    @Autowired
    private TProjectImagesMapper imagesMapper;


    @Autowired
    private TProjectTypeMapper typeMapper;

    @Autowired
    private TReturnMapper returnMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //初始化项目 , 生成项目token返回
    public String initCreateProject(Integer memberId) {

        String pToken = UUID.randomUUID().toString().replace("-", "");
        //redis储存项目对象
        ProjectRedisStorageVo redisStorageVo = new ProjectRedisStorageVo();
        redisStorageVo.setMemberid(memberId);
        //转成json字符串 存到redis
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+pToken, JSON.toJSONString(redisStorageVo));

        return pToken;
    }

    //保存信息
    public void saveProjectInfo(ProjectStatusEnume statusEnume, ProjectRedisStorageVo storageVo) {
        //1.先保存 项目主表
        TProject project = new TProject();
        BeanUtils.copyProperties(storageVo,project);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        project.setCreatedate(date);
        projectMapper.insertSelective(project);
        //获得项目的id
        Integer pId = project.getId();

        //插入从表  img tag type
        //2.保存图片 头部图片
        TProjectImages images = new TProjectImages(null,pId,storageVo.getHeaderImage(), ProjectImageTypeEnume.HEADER.getCode());
        imagesMapper.insert(images);

        //3.保存详细图
        for (String imgUrl : storageVo.getDetailsImage()) {
            TProjectImages projectImages = new TProjectImages(null,pId,imgUrl,ProjectImageTypeEnume.DETAILS.getCode());
            //查入
            imagesMapper.insert(projectImages);
        }

        //4.插入tag 标签
        for (Integer tagid : storageVo.getTagids()) {
            TProjectTag tag = new TProjectTag(null,pId,tagid);
            tagMapper.insert(tag);
        }

        //5.插入 类型
        for (Integer typeid : storageVo.getTypeids()) {
            TProjectType tProjectType = new TProjectType(null,pId,typeid);
            typeMapper.insert(tProjectType);
        }

        // 6. 回报信息
        for (TReturn projectReturn : storageVo.getProjectReturns()) {
            projectReturn.setProjectid(pId);
            returnMapper.insert(projectReturn);
        }

        //finally  删除redis中的缓存
        stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+storageVo.getProjectToken());
    }

}
