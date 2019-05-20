package com.cloud.order.service.impl;

import com.cloud.order.dataobject.OrderMaster;
import com.cloud.order.dto.OrderDTO;
import com.cloud.order.enums.OrderStatusEnum;
import com.cloud.order.enums.PayStatusEnum;
import com.cloud.order.mapper.OrderMasterMapper;
import com.cloud.order.service.OrderService;
import com.cloud.order.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务实现类
 *
 * @author xuweizhi
 * @since 2019-05-20
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMasterMapper, OrderMaster> implements OrderService {

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        //订单入库
        OrderMaster orderMaster = new OrderMaster();

        orderDTO.setOrderId(KeyUtil.genUniqueKey());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(new BigDecimal(5));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMaster.setCreateTime(LocalDateTime.now());
        orderMaster.setUpdateTime(LocalDateTime.now());
        orderMasterMapper.insert(orderMaster);
        return orderDTO;
    }


}
