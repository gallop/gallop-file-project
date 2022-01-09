package com.gallop.core.context.login;

import cn.hutool.extra.spring.SpringUtil;

/**
 * author gallop
 * date 2021-11-15 11:39
 * Description: 当前登录用户信息获取的接口
 * Modified By:
 */
public class LoginContextHolder {

    public static LoginContext getContext() {
        return SpringUtil.getBean(LoginContext.class);
    }
}
