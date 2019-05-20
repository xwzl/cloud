package com.cloud.common.utils;


import com.cloud.common.VO.ResultVO;

/**
 * @author xuweizhi
 * @date 2019/05/20 19:27
 */
public class ResultVOUtil {

    public static ResultVO<Object> success(Object object) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
}
