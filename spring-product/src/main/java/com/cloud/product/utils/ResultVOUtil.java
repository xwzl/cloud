package com.cloud.product.utils;

import com.cloud.product.vo.ResultVO;

/**
 * @author xuweizhi
 * @date 2019/05/20 19:27
 */
public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
}
