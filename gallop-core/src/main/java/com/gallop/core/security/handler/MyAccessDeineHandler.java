package com.gallop.core.security.handler;

import com.alibaba.fastjson.JSON;
import com.gallop.core.base.BaseResult;
import com.gallop.core.base.BaseResultUtil;
import com.gallop.core.exception.PermissionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author gallop
 * date 2021-08-25 14:28
 * Description: 用来解决认证过的用户访问无权限资源时的异常  ——  也就是权限不足的问题
 * Modified By:
 */
@Component
public class MyAccessDeineHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        //httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        e.printStackTrace();
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Integer errCode = 401;
        String errMsg = "没有访问权限!";
        if(e instanceof PermissionException){
            PermissionException exceptionExp = (PermissionException)e;
            errCode = exceptionExp.getCode();
            errMsg = exceptionExp.getErrorMessage();
        }
        BaseResult resp = BaseResultUtil.build(errCode,errMsg,null);
        httpServletResponse.getWriter().print(JSON.toJSONString(resp));
    }
}
