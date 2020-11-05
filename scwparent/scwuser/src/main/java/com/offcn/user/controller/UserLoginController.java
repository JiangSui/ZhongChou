package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.po.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

//用户登录
@RestController
@RequestMapping("/login")
@Api(tags = "用户登录模块/注册模块")
public class UserLoginController {
    @Autowired
    private SmsTemplate smsTemplate;  //短信发送

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //redis模板

    @Autowired
    private UserService userService;

    //日志
    Logger logger = LoggerFactory.getLogger(UserLoginController.class);
    //发送验证码
    @ApiOperation(value = "发送验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "phoneNo",value = "手电号码",required = true,dataType = "String")
    })
    @GetMapping("/sendCode")
    public AppResponse<Object> sendCode(String phoneNo) {
        //使用 UUID生成验证码
        String code =  UUID.randomUUID().toString().substring(0,4);
        //验证码保存到redis 里面 以手机号为键  保存时间
        stringRedisTemplate.opsForValue().set(phoneNo,code, 10,TimeUnit.MINUTES);

        //封装参数
        Map querys = new HashMap();
        querys.put("mobile",phoneNo);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", "TP1711063");//短信模板

        //调用方法 发送短信
        String sendCode = smsTemplate.sendCode(querys);
        if(sendCode.equals("") || sendCode.equals("fail")){
            //发送失败
            return AppResponse.fail("发送失败");
        }else{
            return AppResponse.ok(sendCode);
        }

    }



    //注册
    @ApiOperation("用户注册")
    @PostMapping("/regist")
    public AppResponse userRegister(UserRegistVo registVo){
        //校验验证码
        String code = stringRedisTemplate.opsForValue().get(registVo.getLoginacct());
        //判断 不为空
        if(!StringUtils.isEmpty(code)){
            if(code.equalsIgnoreCase(registVo.getCode())){//忽略大小写
                //将vo 数据转到 po 里面
                TMember member = new TMember();
                BeanUtils.copyProperties(registVo,member);
                //注册
                try{
                    userService.registerUser(member);
                    //将验证码从redis里面删除
                    stringRedisTemplate.delete(registVo.getLoginacct());
                    return AppResponse.ok("注册成功");
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("用户信息注册失败：{}", member.getLoginacct());
                    return AppResponse.fail(e.getMessage());
                }

            }else{
                return AppResponse.fail("验证码错误");
            }
        }else{
            //验证码不存在
            return AppResponse.fail("验证码已经过期....");
        }
    }


    //登录
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username,String password){
        //1.登录
        TMember login = userService.login(username, password);
        if(login==null){
            //没登录上
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或者密码错误");
            return fail;
        }else{
            //登录成功 生成令牌  token  放在redis 里面
            String token = UUID.randomUUID().toString().replace("-","");
            UserRespVo respVo = new UserRespVo();
            respVo.setAccessToken(token);
            //将令牌放到redis 里面
            stringRedisTemplate.opsForValue().set(token,login.getId()+"",2,TimeUnit.HOURS);
            return AppResponse.ok(respVo);
        }
    }
}
