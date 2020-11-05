package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.config.AppProjectConfig;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.po.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;

import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@Api(tags = "项目基本功能模块（创建、保存、项目信息获取)")
public class ProjectCreateController {

    @Autowired
    private ProjectCreateService projectCreateService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "初始化项目")
    @GetMapping("/init")
    public AppResponse<String> init(BaseVo vo) { //传入 用户登录后后的的 accesstoken
        //验证是否登录
        String mId =  stringRedisTemplate.opsForValue().get(vo.getAccessToken());
        if(StringUtils.isEmpty(mId)){
            return AppResponse.fail("请登录");
        }
        //创建项目 获得项目token
        String pToken =  projectCreateService.initCreateProject(Integer.parseInt(mId));
        //将项目token返回
        return AppResponse.ok(pToken);
    }


    @ApiOperation(value = "保存项目的基本信息")
    @PostMapping("/savebaseInfo")
    public AppResponse saveBaseInfo(ProjectBaseInfoVo baseInfoVo){
        //首先根据传入的 项目token  获取到初始化的项目对象
        String tmp = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+baseInfoVo.getProjectToken());
        if(StringUtils.isEmpty(tmp)){
            return AppResponse.fail("请登录");
        }
        //将json串转为对象
        ProjectRedisStorageVo redisStorageVo =  JSON.parseObject(tmp,ProjectRedisStorageVo.class);
        //然后将redis项目对象 的数据转移到 项目基本信息对象里面
        BeanUtils.copyProperties(baseInfoVo,redisStorageVo);

        //再将redis项目对象转为json穿, 存入到redis
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+baseInfoVo.getProjectToken(),
                JSON.toJSONString(redisStorageVo));

        return AppResponse.ok("保存完毕");
    }

    @ApiOperation(value = "完善项目回报信息")
    @PostMapping("/returnInfo")
    public AppResponse returnInfo(@RequestBody List<ProjectReturnVo> returnVos){
        //获取到 ptoken
        String pToken = returnVos.get(0).getProjectToken();
        //根据projectToken 获取到基本信息对象
        String temp = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+pToken);
        //转成对象 获取到基本信息对象
        ProjectRedisStorageVo redisVo = JSON.parseObject(temp,ProjectRedisStorageVo.class);
        //将回报信息 添加到基本信息对象里面
        List list = new ArrayList();
        for (ProjectReturnVo returnVo : returnVos) {
            //深克隆 复制
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(returnVo,tReturn);
            list.add(tReturn);
        }
        redisVo.setProjectReturns(list);

        //添加到redis 里面
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+pToken,JSON.toJSONString(redisVo));

        return AppResponse.ok("完善回报完成");
    }



    @ApiOperation(value = "保存完成,将项目保存到数据库")
    @PostMapping("/submit")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accessToken",value = "用户token",required = true),
            @ApiImplicitParam(name = "projectToken",value = "项目临时的token",required = true),
            @ApiImplicitParam(name = "ops",value = "操作类型",required = true) //0 为草稿  1 为提交审核
    })
    public AppResponse submit(String accessToken,String projectToken,String ops){
        //判断是否登录
        String mId = stringRedisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(mId)){
           return AppResponse.fail("请登录") ;
        }
        //获取到项目信息
        ProjectRedisStorageVo storageVo= JSON.parseObject( stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken),ProjectRedisStorageVo.class);
        //判断类型是否为空
        if(!StringUtils.isEmpty(ops)){
            if(ops.equalsIgnoreCase("0")){
                //为0 草稿
                ProjectStatusEnume enume = ProjectStatusEnume.DRAFT;
                projectCreateService.saveProjectInfo(enume,storageVo);
                return AppResponse.ok(null);
            }else if(ops.equals("1")){
                //提交审核
               ProjectStatusEnume enume =  ProjectStatusEnume.SUBMIT_AUTH;
               projectCreateService.saveProjectInfo(enume,storageVo);
                return AppResponse.ok(null);
            }else{
                AppResponse appResponse = AppResponse.fail(null);
                appResponse.setMsg("不支持该操作");
                return appResponse;
            }
        }
        //为空
        return AppResponse.fail("状态不存在");
    }
}
