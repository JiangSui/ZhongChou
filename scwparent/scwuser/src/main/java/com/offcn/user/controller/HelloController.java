package com.offcn.user.controller;


import com.offcn.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//swagger2 测试接口类
@Api(value = "测试接口")
@RestController
public class HelloController {

    @ApiOperation("测试方法hello")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "姓名",required = true)
    })
    @GetMapping("/hello")
    public String test1(String name){
        return "你好,"+name;
    }

    @ApiOperation("保存用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "姓名",required = true),
            @ApiImplicitParam(name = "email",value = "邮件",required = true)
    })
    //保存用户 用到了model 实体User
    @PostMapping("/save")
    public User save(String name,String email){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
