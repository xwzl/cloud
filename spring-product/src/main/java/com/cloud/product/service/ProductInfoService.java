package com.cloud.product.service;


import com.cloud.product.model.ProductInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xuweizhi
 * @since 2019-05-20
 */
public interface ProductInfoService extends BaseService<ProductInfo> {

    /**
     * 查询所有上架商品列表
     */
    List<ProductInfo> findUpAll();

}
