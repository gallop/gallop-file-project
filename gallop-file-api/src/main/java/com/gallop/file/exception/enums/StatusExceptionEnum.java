package com.gallop.file.exception.enums;


import com.gallop.core.annotation.ExpEnumType;
import com.gallop.core.constant.SysExpEnumConstant;
import com.gallop.core.exception.enums.AbstractBaseExceptionEnum;
import com.gallop.core.exception.enums.ExpEnumCodeCreateFactory;

/**
 * 状态枚举
 *
 */
@ExpEnumType(module = SysExpEnumConstant.SYS_EXP_MODULE_CODE,category = SysExpEnumConstant.STATUS_EXCEPTION_ENUM)
public enum StatusExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 请求状态值为空
     */
    REQUEST_EMPTY(1, "请求状态值为空"),

    /**
     * 请求状值为非正确状态值
     */
    NOT_WRITE_STATUS(2, "请求状态值不合法"),

    /**
     * 更新状态失败，试图更新被删除的记录
     */
    UPDATE_STATUS_ERROR(3, "更新状态失败，您试图更新被删除的记录");

    private final Integer code;

    private final String message;

    StatusExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeCreateFactory.getExpEnumCode(this.getClass(),code);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
