package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.util.AppDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.sound.midi.MidiChannel;
import java.util.List;
import java.util.UUID;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private TOrderMapper orderMapper;

    //保存订单
    public TOrder saveOrder(OrderInfoSubmitVo submitVo) {
       //创建一个订单对象
        TOrder order = new TOrder();
        //根据accessToken  获取到用户id
        String mId = stringRedisTemplate.opsForValue().get(submitVo.getAccessToken());
        order.setMemberid(Integer.parseInt(mId)); //用户id
        order.setProjectid(submitVo.getProjectid()); //项目id
        order.setReturnid(submitVo.getReturnid()); //huibaoid
        //生成订单编号
        String orderNum = UUID.randomUUID().toString().replace("-","");
        order.setOrdernum(orderNum);
        order.setCreatedate(AppDateUtils.getFormatTime()); //订单创建时间

        //调用project服务,获取到回报列表
        AppResponse<List<TReturn>> listAppResponse = projectServiceFeign.detailsReturn(submitVo.getProjectid());
        List<TReturn> returnList = listAppResponse.getData();
        //不选择了 直接那第一个
        TReturn tReturn = returnList.get(0);
        //计算用户的消费总金额
        Integer money = tReturn.getSupportmoney()*submitVo.getRtncount()+tReturn.getFreight();
        order.setMoney(money);

        order.setRtncount(submitVo.getRtncount()); //支持数量
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+""); //支付状态 未支付

        order.setRemark(submitVo.getRemark()); //备注
        order.setAddress(submitVo.getAddress()); //收货地址
        order.setInvoice(submitVo.getInvoice().toString()); //是否开发票
        order.setInvoictitle(submitVo.getInvoictitle()); //发票头

        //插入
        orderMapper.insert(order);
        return order;
    }
}
