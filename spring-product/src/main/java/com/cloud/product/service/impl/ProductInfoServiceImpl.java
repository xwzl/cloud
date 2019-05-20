package com.cloud.product.service.impl;

import com.cloud.product.mapper.ProductInfoMapper;
import com.cloud.product.model.ProductInfo;
import com.cloud.product.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}