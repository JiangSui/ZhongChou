package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

//熔断服务
@Component
public class ProjectServiceFeignImpl  implements ProjectServiceFeign {

    public AppResponse<List<TReturn>> detailsReturn(Integer pId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务器失败【订单】");
        return fail;
    }
}
