package com.gallop.core.exception;

import com.gallop.core.exception.enums.AbstractBaseExceptionEnum;
import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;

/**
 * author gallop
 * date 2021-11-15 14:21
 * Description:
 * Modified By:
 */
@Getter
public class PermissionException extends AccessDeniedException {
    private final Integer code;

    private final String errorMessage;

    public PermissionException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }
}
