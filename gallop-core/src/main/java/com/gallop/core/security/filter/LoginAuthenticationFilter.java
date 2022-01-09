package com.gallop.core.security.filter;

import com.alibaba.fastjson.JSON;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gallop.core.exception.AuthException;
import com.gallop.core.exception.enums.AuthExceptionEnum;
import com.gallop.core.security.bean.LoginBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * author gallop
 * date 2021-08-24 11:44
 * Description: 重写filter以支持用户登录以json 格式提交
 * Modified By:
 */
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Lazy
    @Resource
    private CaptchaService captchaService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        //如果是application/json类型，做如下处理
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                ||request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){

            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                LoginBean loginBean = mapper.readValue(is, LoginBean.class);
                //验证码验证
                verificationCode(loginBean.getCode());
                authRequest = new UsernamePasswordAuthenticationToken(
                        loginBean.getUsername(), loginBean.getPassword());
                log.info("------------authenticationBean={}", JSON.toJSONString(loginBean));
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } else {
            //transmit it to UsernamePasswordAuthenticationFilter
            return super.attemptAuthentication(request, response);
        }
    }

    /**
     * 校验验证码
     *
     */
    private boolean verificationCode(String code) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(code);
        if (!captchaService.verification(vo).isSuccess()) {
            throw new AuthException(AuthExceptionEnum.VALID_CODE_ERROR);
        }
        return true;
    }

}
