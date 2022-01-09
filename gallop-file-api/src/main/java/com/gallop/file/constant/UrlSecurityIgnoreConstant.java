package com.gallop.file.constant;

/**
 * author gallop
 * date 2021-10-05 12:34
 * Description:
 * Modified By:
 */
public interface UrlSecurityIgnoreConstant {
    /**
     * 放开权限校验的接口
     */
    String[] NONE_SECURITY_URL_PATTERNS = {

            //前端的
            "/favicon.ico",

            //swagger相关的
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/configuration/ui",
            "/configuration/security",

            //后端的
            "/login",
            "/logout",
            "/login**",

            //文件的
            "/sysFileInfo/upload",
            "/sysFileInfo/download",
            "/sysFileInfo/preview",

            //druid的
            "/druid/**",

            //获取验证码
            "/captcha/**",
            "/getCaptchaOpen",
            //静态资源
            //"/static/**",
            //"/templates/**",
            "/**/*.js",
            "/**/*.css",
            "/fonts/**"

    };
}
