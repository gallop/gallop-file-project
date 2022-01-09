package com.gallop.file.controller;

import com.gallop.core.base.BaseResultUtil;
import com.gallop.core.security.bean.SysLoginUser;
import com.gallop.core.util.PermissionUtil;
import com.gallop.core.util.PermissionWrap;
import com.gallop.file.service.PermissionService;
import com.gallop.file.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * author gallop
 * date 2021-10-06 21:45
 * Description:
 * Modified By:
 */
@RestController
@Slf4j
public class LoginUserController {

    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;



    @GetMapping("/getLoginUser")
    public Object info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysLoginUser loginUser = (SysLoginUser)authentication.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("name", loginUser.getUsername());
        data.put("avatar", loginUser.getUser().getAvatar());

        log.info("==========adminUser:"+loginUser.toString());
        Integer[] roleIds = loginUser.getUser().getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        System.err.println("permissions=="+permissions.toString());
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        //todo
        data.put("permissions", toApi(permissions));
        return BaseResultUtil.success(data);
    }

    @Resource
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;

    private Collection<String> toApi(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.gallop.file.controller";
            List<PermissionWrap> systemPermissions = PermissionUtil.listPermissionWrap(context, basicPackage);
            for (PermissionWrap permissionWrap : systemPermissions) {
                //String perm = PermissionUtil.getPermissionStrByRegex(permissionWrap.getPermissions().value());
                String perm = permissionWrap.getPermissions().value();
                String api = permissionWrap.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
                // return systemPermissionsMap.values();
            }
        }
        return apis;
    }
}
