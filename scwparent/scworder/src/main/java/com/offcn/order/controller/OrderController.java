package com.offcn.order.controller;


import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//订单
@Api(tags = "保存订单模块")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "保存订单")
    @PostMapping("/createOrder")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoSubmitVo vo){
        try{
            TOrder order = orderService.saveOrder(vo);
            AppResponse<TOrder> response = AppResponse.ok(order);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return AppResponse.fail(null);
        }

    }
}
