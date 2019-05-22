package com.cloud.order.service.impl;

import com.cloud.order.dto.OrderDTO;
import com.cloud.order.enums.OrderStatusEnum;
import com.cloud.order.enums.PayStatusEnum;
import com.cloud.order.mapper.OrderDetailMapper;
import com.cloud.order.mapper.OrderMasterMapper;
import com.cloud.order.model.OrderDetail;
import com.cloud.order.model.OrderMaster;
import com.cloud.order.service.OrderService;
import com.cloud.order.utils.KeyUtil;
import com.cloud.prodcut.common.DecreaseStockInput;
import com.cloud.prodcut.common.ProductInfoOutput;
import com.cloud.product.client.ProductClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO create(@NotNull OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();

        //查询商品信息(调用商品服务)
        List<String> productIdList = orderDTO.getOrderDetailList().stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);

        //计算总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo : productInfoList) {
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    //单价*数量
                    //orderDetail.setProductQuantity(2);
                    orderAmount = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    //订单详情入库
                    orderDetailMapper.insert(orderDetail);
                }
            }
        }

        //扣库存(调用商品服务)
        /* List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());*/
        List<OrderDetail> detailList = orderDTO.getOrderDetailList();
        Stream<OrderDetail> detailStream = detailList.stream();
        DecreaseStockInput decreaseStockInput = new DecreaseStockInput();
        Stream<DecreaseStockInput> decreaseStockInputStream = detailStream.map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()));
        List<DecreaseStockInput> decreaseStockInputList = decreaseStockInputStream.collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);

        //订单入库
        OrderMaster orderMaster = new OrderMaster();

        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMaster.setCreateTime(LocalDateTime.now());
        orderMaster.setUpdateTime(LocalDateTime.now());
        orderMasterMapper.insert(orderMaster);
        return orderDTO;
    }


}
