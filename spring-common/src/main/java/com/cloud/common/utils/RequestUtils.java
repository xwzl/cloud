package com.cloud.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuweizhi
 * @since 2019/09/17 16:18
 */
public class RequestUtils {

    public static String getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        return request.getHeader("id");
    }
}
