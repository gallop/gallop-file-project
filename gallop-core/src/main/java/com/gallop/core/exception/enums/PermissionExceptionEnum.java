package com.gallop.core.exception.enums;

import com.gallop.core.annotation.ExpEnumType;
import com.gallop.core.constant.SysExpEnumConstant;

/**
 * author gallop
 * date 2021-11-15 14:25
 * Description:
 * Modified By:
 */
@ExpEnumType(module = SysExpEnumConstant.SYS_EXP_MODULE_CODE, category = SysExpEnumConstant.PERMISSION_EXCEPTION_ENUM)
public enum PermissionExceptionEnum implements AbstractBaseExceptionEnum{
    /**
     * 资源路径不存在
     */
    URL_NOT_EXIST(1, "资源路径不存在，请检查请求地址"),

    /**
     * 没有权限访问资源
     */
    NO_PERMISSION(2, "没有权限访问资源，请联系管理员");

    private final Integer code;

    private final String message;

    PermissionExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeCreateFactory.getExpEnumCode(this.getClass(), code);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
