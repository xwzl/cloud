package com.cloud.product.service.impl;

import com.cloud.product.common.DecreaseStockInput;
import com.cloud.product.enums.ResultEnum;
import com.cloud.product.exception.ProductException;
import com.cloud.product.mapper.ProductInfoMapper;
import com.cloud.product.model.ProductInfo;
import com.cloud.product.service.ProductInfoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author xuweizhi
 */
@Service
public class ProductInfoServiceImpl extends BaseServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoMapper.selectByMap(new HashMap<>());
    }

    @Override
    @Transactional
    public void decreaseStock(@NotNull List<DecreaseStockInput> decreaseStockInputs) {
        for (DecreaseStockInput stockInput : decreaseStockInputs) {
            ProductInfo productInfo = productInfoMapper.selectById(stockInput.getProductId());

            //判断商品是否存在
            if (productInfo == null) {
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //库存是否足够
            Integer result = productInfo.getProductStock() - stockInput.getProductQuantity();
            if (result < 0) {
                throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
            productInfoMapper.updateById(productInfo);
        }

    }
}