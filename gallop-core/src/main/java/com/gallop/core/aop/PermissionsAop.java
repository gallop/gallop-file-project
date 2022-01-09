package com.gallop.core.aop;

import com.gallop.core.annotation.Permissions;
import com.gallop.core.context.login.LoginContextHolder;
import com.gallop.core.enums.Logical;
import com.gallop.core.exception.PermissionException;
import com.gallop.core.exception.enums.PermissionExceptionEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * author gallop
 * date 2021-11-15 11:18
 * Description: 权限过滤Aop切面
 * Modified By:
 */
@Aspect
@Order(-100)
public class PermissionsAop {

    /**
     * 权限切入点
     *
     */
    @Pointcut("@annotation(com.gallop.core.annotation.Permissions)")
    private void getPermissionPointCut() {
    }

    /**
     * 执行权限过滤
     *
     */
    @Before("getPermissionPointCut()")
    public void doPermission(JoinPoint joinPoint) {
        // 如果是超级管理员，直接放过权限校验
        boolean isSuperAdmin = LoginContextHolder.getContext().isSuperAdmin();
        if (isSuperAdmin) {
            return;
        }

        // 如果不是超级管理员，则开始进行权限校验
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Permissions permission = method.getAnnotation(Permissions.class);

        // 当前方法需要的角色集合
        String requirePermissions = permission.value();

        // 逻辑类型
        Logical logicType = permission.logical();

        if (requirePermissions != null) {
            boolean hasSpecialPermission = true;
            hasSpecialPermission = LoginContextHolder.getContext().hasPermission(requirePermissions,logicType);

            if (!hasSpecialPermission) {
                throw new PermissionException(PermissionExceptionEnum.NO_PERMISSION);
            }
        }
    }

}
