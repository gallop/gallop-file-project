package com.gallop.core.security.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.gallop.core.context.login.LoginContext;
import com.gallop.core.enums.AdminTypeEnum;
import com.gallop.core.enums.Logical;
import com.gallop.core.exception.AuthException;
import com.gallop.core.exception.enums.AuthExceptionEnum;
import com.gallop.core.security.bean.SysLoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * author gallop
 * date 2021-11-15 11:40
 * Description: 登录用户上下文实现类
 * Modified By:
 */
@Component
public class LoginContextSpringSecurityImpl implements LoginContext {
    @Override
    public SysLoginUser getSysLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            throw new AuthException(AuthExceptionEnum.NO_LOGIN_USER);
        } else {
            return (SysLoginUser) authentication.getPrincipal();
        }
    }

    @Override
    public SysLoginUser getSysLoginUserWithoutException() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            return null;
        } else {
            return (SysLoginUser) authentication.getPrincipal();
        }
    }

    @Override
    public Long getSysLoginUserId() {
        return this.getSysLoginUser().getUser().getId();
    }

    @Override
    public String getSysLoginUserAccount() {
        return this.getSysLoginUser().getUser().getUsername();
    }

    @Override
    public boolean hasPermission(String requestPermission, Logical logical) {
        boolean flag = false;
        String[] permissionCodeArr = StrUtil.split(requestPermission, ",");
        if (Logical.AND.equals(logical)) {
            for (String permissionCode : permissionCodeArr) {
                if (!this.getSysLoginUser().getPermissions().contains(permissionCode)) {
                    flag = false;
                    break;
                }
            }

        }else if(Logical.OR.equals(logical)) {
            for (String permissionCode : permissionCodeArr) {
                if (this.getSysLoginUser().getPermissions().contains(permissionCode)) {
                    flag = true;
                    break;
                }
            }

        }
        return flag;
    }

    @Override
    public boolean isSuperAdmin() {
        Integer adminType = this.getSysLoginUser().getUser().getAdminType();
        boolean flag = false;
        if (adminType.equals(AdminTypeEnum.SUPER_ADMIN.getCode())) {
            flag = true;
        }
        return flag;

    }
}
