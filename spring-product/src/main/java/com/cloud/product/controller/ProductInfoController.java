package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.common.VO.ResultVO;
import com.cloud.common.utils.ResultVOUtil;
import com.cloud.product.model.ProductCategory;
import com.cloud.product.model.ProductInfo;
import com.cloud.product.service.ProductCategoryService;
import com.cloud.product.service.ProductInfoService;
import com.cloud.product.vo.ProductInfoVO;
import com.cloud.product.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xuweizhi
 * @since 2019-05-20
 */
@RestController
@RequestMapping("/product")
public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 4. 构造数据
     */
    @GetMapping("/list")
    public ResultVO getProductInfo() {

        // 1. 查询所有再架的商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();

        // 2. 查取类目type列表
        List<Integer> collect = productInfoList.stream().map(ProductInfo::getCategoryType).filter(integer -> integer == 1).collect(Collectors.toList());

        // 3. 查询类目
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("category_type", collect);
        List<ProductCategory> productCategories = productCategoryService.list(queryWrapper);

        //4. 构造数据
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategories) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo: productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
