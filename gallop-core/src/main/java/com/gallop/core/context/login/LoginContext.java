package com.gallop.core.context.login;

import com.gallop.core.enums.Logical;
import com.gallop.core.security.bean.SysLoginUser;

/**
 * author gallop
 * date 2021-11-15 11:36
 * Description: 登录用户上下文
 * Modified By:
 */
public interface LoginContext {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     */
    SysLoginUser getSysLoginUser();

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     * @return 当前登录用户信息
     */
    SysLoginUser getSysLoginUserWithoutException();

    /**
     * 获取当前登录用户的id
     *
     * @return 当前登录用户的id
     */
    Long getSysLoginUserId();

    /**
     * 获取当前登录用户的账户
     *
     * @return 当前登陆用户的账户account
     */
    String getSysLoginUserAccount();

    /**
     * 判断当前登录用户是否有某资源的访问权限
     *
     * @param requestUri 请求的url
     * @return 是否有访问权限，true是，false否
     */
    boolean hasPermission(String requestUri, Logical logical);

    /**
     * 判断当前登录用户是否是超级管理员
     *
     * @return 当前登录用户是否是超级管理员
     */
    boolean isSuperAdmin();
}
