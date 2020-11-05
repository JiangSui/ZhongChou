package com.offcn.project.controller;


import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.po.*;
import com.offcn.project.service.impl.ProjectInfoServiceImpl;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.util.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Api(tags = "文件上传、项目信息获取等")
@RestController
@RequestMapping("/project")
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;


    @Autowired
    private ProjectInfoServiceImpl infoService;
    //日志记录
    Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public AppResponse<Map> upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        //创建返回对象
        Map map = new HashMap();
        //存放返回的文件的url
        List<String> urls = new ArrayList<>();
        //判断是否为空
        if(files!=null && files.length>0){
            //遍历每一个Mfile 对象
            for (MultipartFile file : files) {
                //判断每一个file是否为空
                if(!file.isEmpty()){
                    //不为空 存储
                    String url = ossTemplate.upload(file.getInputStream(),file.getOriginalFilename());
                    urls.add(url);
                }
            }
        }
        map.put("urls",urls);
        //日志打印
        log.debug("ossTemplate信息：{},文件上传成功访问路径{}",ossTemplate,urls);
        return AppResponse.ok(map);
    }


    //根据项目id 获取到回报信息
    @ApiOperation("获取到项目的回报信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pId",value = "项目id",required = true)
    })
    @GetMapping("/returns/{pId}")
    public AppResponse<List<TReturn>> detailsReturn(@PathVariable("pId") Integer pId){
        List<TReturn> projectReturns = infoService.getProjectReturns(pId);
        return AppResponse.ok(projectReturns);
    }


    //获取所有的项目信息
    @ApiOperation(value = "获取所有项目的信息")
    @GetMapping("/getAll")
    public AppResponse<List<ProjectVo>> getAllProject(){
        List<TProject> allProjects = infoService.getAllProjects();
        //po 复制进vo
        List<ProjectVo> vo = new ArrayList<>();
        for (TProject allProject : allProjects) {
            ProjectVo pVo = new ProjectVo();
            BeanUtils.copyProperties(allProject,pVo);

            //便利imagetype
            //图片 根据项目id
            List<TProjectImages> projectImages = infoService.getProjectImages(allProject.getId());
            for (TProjectImages projectImage : projectImages) {
               if(projectImage.getImgtype() == ProjectImageTypeEnume.HEADER.getCode()){
                   pVo.setHeaderImage(projectImage.getImgurl());
               }
            }

            vo.add(pVo);
        }
        return AppResponse.ok(vo);
    }


    @ApiOperation("获取项目信息详细")
    @GetMapping("/details/info/{projectId}")
    public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId) {
        //首先根据项目id  获取项目对象
        TProject projectInfo = infoService.getProjectInfo(projectId);
        //创建项目vo 对象
        ProjectDetailVo vo = new ProjectDetailVo();
        //将查出来项目信息 copy到vo
        BeanUtils.copyProperties(projectInfo,vo);

        //1.查询该项目所有的图片 再放进到detailvo 里面
        //详细图
        List<String> detailImage = new ArrayList<>();
        List<TProjectImages> projectImages = infoService.getProjectImages(projectId);
        for (TProjectImages projectImage : projectImages) {
            if(projectImage.getImgtype() == ProjectImageTypeEnume.HEADER.getCode()){
                //头图
                vo.setHeaderImage(projectImage.getImgurl());
            }else{
                //详细图
                detailImage.add(projectImage.getImgurl());
            }
        }
        //详细图放入到vo里面
        vo.setDetailsImage(detailImage);

        //2. 查询回报放入到vo
        List<TReturn> projectReturns = infoService.getProjectReturns(projectId);
        //放入到vo
        vo.setProjectReturns(projectReturns);


        //返回
        return AppResponse.ok(vo);
    }



    @ApiOperation("获取所有的标签")
    @GetMapping("/allTag")
    public AppResponse<List<TTag>> getAllTag(){
        List<TTag> allProjectTags = infoService.getAllProjectTags();
        return AppResponse.ok(allProjectTags);
    }

    @ApiOperation("获取所有的项目类型")
    @GetMapping("/types")
    public AppResponse<List<TType>> types() {
        List<TType> projectTypes = infoService.getProjectTypes();
        return AppResponse.ok(projectTypes);
    }


    @ApiOperation("根据rid 获取到详细回报信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "returnId",value = "回报id")
    })
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> getTReturn(@PathVariable("returnId") Integer returnId){
        TReturn projectReturns = infoService.getReturnInfo(returnId);
        return AppResponse.ok(projectReturns);
    }
}
