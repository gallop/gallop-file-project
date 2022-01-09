package com.gallop.file.security;

import com.alibaba.fastjson.JSON;
import com.gallop.core.pojo.SysUser;
import com.gallop.core.security.bean.SysLoginUser;
import com.gallop.file.service.PermissionService;
import com.gallop.file.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * author gallop
 * date 2021-08-24 15:21
 * Description:
 * Modified By:
 */
@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (null == sysUser) {
            log.warn("用户{}不存在", username);
            throw new UsernameNotFoundException(username);
        }
        System.out.println("------------------roleids="+Arrays.toString(sysUser.getRoleIds()));
        Set<String> permissionSet = permissionService.queryByRoleIds(sysUser.getRoleIds());

        //List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        SysLoginUser sysLoginUser = new SysLoginUser(sysUser,permissionSet);
        log.info("请求认证用户: {}", JSON.toJSONString(sysUser));
        return sysLoginUser;
    }
}
