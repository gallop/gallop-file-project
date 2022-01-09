package com.gallop.core.annotation;


import com.gallop.core.enums.Logical;

import java.lang.annotation.*;

/**
 * 权限注解，用于检查权限
 * 使用方式：@Permission表示检查是否有权限访问该资源
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {
    /**
     * 多个值用逗号隔开
     */
    String value() default "";

    Logical logical() default Logical.AND;

}
