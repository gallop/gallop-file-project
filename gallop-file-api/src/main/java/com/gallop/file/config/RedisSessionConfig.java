package com.gallop.file.config;

import com.gallop.core.session.CustomedSessionIdResolver;
import com.gallop.file.constant.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * author gallop
 * date 2021-08-25 11:08
 * Description: 开启spring session redis缓存,并设置sessionId 存储于Header中
 * Modified By:
 */
@Configuration
//设置session失效时间为30分钟
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800)
public class RedisSessionConfig {

     /**
      * @date 2021-08-25 11:12
      * Description: 如果不设置这个bean spring默认的是 CookieHttpSessionIdResolver,
      * cookie 保存的是经过base64 的sessionId，
      * 如果是Header 的方式，则是直接在header 保存指定名字的sessionId (未经base64)
      * Param:
      * https://blog.csdn.net/chunzhenzyd/article/details/105141670
      * return:
      **/
    @Bean
    public CustomedSessionIdResolver headerHttpSessionIdResolver() {
        //return new CookieHttpSessionIdResolver(); --cookie 方式
        //return new HeaderHttpSessionIdResolver(CommonConstant.X_AUTH_TOKEN); --header 方式
        return new CustomedSessionIdResolver(CommonConstant.X_AUTH_TOKEN);
    }
}
