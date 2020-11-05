package com.offcn.order.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.impl.ProjectServiceFeignImpl;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//服务调用,调用project服务
@FeignClient(value = "SCWPROJECT" ,fallback = ProjectServiceFeignImpl.class) //服务名字,熔断回调
public interface ProjectServiceFeign {
    //通过项目id  获取到回报信息
    @GetMapping("/project/returns/{pId}")
    public AppResponse<List<TReturn>> detailsReturn(@PathVariable("pId") Integer pId);
}
