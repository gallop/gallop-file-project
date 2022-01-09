package com.gallop.file.exception;

import com.gallop.core.base.BaseResultUtil;
import com.gallop.core.exception.AuthException;
import com.gallop.file.constant.AopSortConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author gallop
 * date 2021-10-05 10:27
 * Description: 全局异常处理器
 * Modified By:
 */
@Order(AopSortConstant.GLOBAL_EXP_HANDLER_AOP)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 拦截业务异常
     *
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Object businessError(ServiceException e) {
        log.error(">>> 业务异常，具体信息为：{}", e.getMessage());
        return BaseResultUtil.failure(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截认证失败异常
     *
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public Object authFail(AuthException e) {
        log.error(">>> 认证异常，具体信息为：{}", e.getMessage());
        return BaseResultUtil.failure(e.getCode(), e.getErrorMessage());
    }
}
