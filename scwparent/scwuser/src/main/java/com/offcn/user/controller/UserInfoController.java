package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.resp.UserAddressVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@Api(tags = "查询用户信息")
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "根据id 查询用户信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "用户id",required = true)
    })
    //根据用户编号获取用户信息
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tmemberById = userService.findTmemberById(id);
        UserRespVo respVo = new UserRespVo();
        BeanUtils.copyProperties(tmemberById,respVo);
        return AppResponse.ok(respVo);
    }

    //获取用户地址
    @ApiOperation("获取用户地址")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accessToken",value = "用户token")
    })
    @GetMapping("/address")
    public AppResponse<List<UserAddressVo>> getAdderss(String accessToken){
        //判断是否登录
        String mId = stringRedisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(mId)){
            return  AppResponse.fail(null);
        }
        List<TMemberAddress> tMemberAddresses = userService.addressList(Integer.parseInt(mId));
        List<UserAddressVo> addressVos = new ArrayList<>();
        //将 po对象 的数据转移到vo  返回
        for (TMemberAddress tMemberAddress : tMemberAddresses) {
            UserAddressVo vo = new UserAddressVo();
            vo.setId(tMemberAddress.getId());
            vo.setAddress(tMemberAddress.getAddress());
            addressVos.add(vo);
        }
        return AppResponse.ok(addressVos);
    }
}
