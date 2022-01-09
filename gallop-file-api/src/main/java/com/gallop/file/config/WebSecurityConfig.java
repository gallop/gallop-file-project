package com.gallop.file.config;

import com.gallop.file.constant.UrlSecurityIgnoreConstant;
import com.gallop.core.security.filter.LoginAuthenticationFilter;
import com.gallop.core.security.handler.*;
import com.gallop.core.security.strategy.MyExpiredSessionStrategy;
import com.gallop.core.security.strategy.MyInvalidSessionStrategy;
import com.gallop.file.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * author gallop
 * date 2021-08-24 11:49
 * Description: 开启spring security的配置类
 * Modified By:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Resource
    private UrlAuthenticationSuccessHandler successHandler;
    @Resource
    private UrlAuthenticationFailureHandler failureHandler;
    @Resource
    private MyAccessDeineHandler myAccessDeineHandler;
    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Resource
    private MyInvalidSessionStrategy invalidSessionStrategy;
    @Resource
    private MyExpiredSessionStrategy expiredSessionStrategy;

    /**
     * 开启跨域访问拦截器
     *
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/css/**", "/images/**");
        //super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .antMatcher("/**").authorizeRequests();
            //.antMatchers("/", "/login**").permitAll()
        //放开一些接口的权限校验
        for (String notAuthResource : UrlSecurityIgnoreConstant.NONE_SECURITY_URL_PATTERNS) {
            http.authorizeRequests().antMatchers(notAuthResource).permitAll();
        }
        //其余的都需授权访问
        http.authorizeRequests().anyRequest().authenticated();

        //这里必须要写formLogin()，不然原有的UsernamePasswordAuthenticationFilter不会出现，也就无法配置我们重新的UsernamePasswordAuthenticationFilter
        http.formLogin()//.loginProcessingUrl("/login")//loginPage("/")
            .and().logout().logoutSuccessHandler(myLogoutSuccessHandler)
            .and().csrf().disable();

        //security session管理
        http.sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy) //session无效处理策略
                .maximumSessions(1) //允许最大的session(同一个账号只能登录一次)
                .expiredSessionStrategy(expiredSessionStrategy); //session过期处理策略，被顶号了
                // .maxSessionsPreventsLogin(true) //只允许一个地点登录，再次登陆报错

        //添加未授权处理
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        //权限不足处理
        http.exceptionHandling().accessDeniedHandler(myAccessDeineHandler);

        //用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
        http.addFilterAt(loginAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
        //super.configure(http);
        http.headers()
                .frameOptions().disable()
                .httpStrictTransportSecurity().disable();
    }

    //注册自定义的UsernamePasswordAuthenticationFilter
    @Bean
    protected LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler); //登录成功的handler
        filter.setAuthenticationFailureHandler(failureHandler); //登录失败的handler
        filter.setFilterProcessesUrl("/login");//登录的访问路径

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }
}
