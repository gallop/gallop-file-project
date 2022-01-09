package com.gallop.file.controller;

import com.gallop.core.annotation.Permissions;
import com.gallop.core.annotation.RequiresPermissionsDesc;
import com.gallop.core.base.BaseResultUtil;

import com.gallop.core.pojo.Role;
import com.gallop.core.security.bean.PermVo;
import com.gallop.core.util.PermissionUtil;
import com.gallop.core.util.PermissionWrap;
import com.gallop.file.exception.ServiceException;
import com.gallop.file.exception.enums.ParamExceptionEnum;
import com.gallop.file.service.PermissionService;
import com.gallop.file.service.RoleService;
import com.gallop.file.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;


/**
 * author gallop
 * date 2020-04-19 17:53
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/admin/role")
@Validated
@Slf4j
public class AdminRoleController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;



    @GetMapping("/options")
    public Object options(){
        List<Role> roleList = roleService.queryAll();

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (Role role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getName());
            options.add(option);
        }

        return BaseResultUtil.success(options);
    }

    //@PreAuthorize("@aps.hasPermission('admin:role:read')")
    //@Permissions("admin:role:read")
    //@RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        Role role = roleService.findById(id);
        return BaseResultUtil.success(role);
    }

    private void validate(Role role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            throw new ServiceException(ParamExceptionEnum.PARAM_ERROR);
        }

    }





    @Autowired
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    private List<PermVo> getSystemPermissions(){
        final String basicPackage = "com.gallop.file.controller";
        if(systemPermissions == null || systemPermissions.size() == 0){
            List<PermissionWrap> permissionWrapLists = PermissionUtil.listPermissionWrap(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissionWrapLists);
            systemPermissionsString = PermissionUtil.listPermissionString(permissionWrapLists);
        }
        return systemPermissions;
    }

    private Set<String> getAssignedPermissions(Integer roleId){
        // 这里需要注意的是，如果存在超级权限*，那么这里需要转化成当前所有系统权限。
        // 之所以这么做，是因为前端不能识别超级权限，所以这里需要转换一下。
        Set<String> assignedPermissions = null;
        if(permissionService.checkSuperPermission(roleId)){
            getSystemPermissions();
            assignedPermissions = systemPermissionsString;
        }else{
            assignedPermissions = permissionService.queryByRoleId(roleId);
        }

        return assignedPermissions;
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    //@PreAuthorize("@aps.hasPermission('admin:role:permission:get')")
    //@Permissions("admin:role:permission:get")
   // @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限详情")
    @GetMapping("/getPermissions")
    public Object getPermissions(Integer roleId) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return BaseResultUtil.success(data);
    }

    /**
     * 更新管理员的权限
     *
     * @param body
     * @return
     */
    //@PreAuthorize("@aps.hasPermission('admin:role:permission:update')")
    //@Permissions("admin:role:permission:update")
    //@RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限变更")
    @PostMapping("/updatePermissions")
    public Object updatePermissions(@RequestBody String body) {
       /* Integer roleId = JsonUtils.parseInteger(body, "roleId");
        List<String> permissionStrs = JsonUtils.parseStringList(body, "permissions");
        if(roleId == null || permissionStrs == null){
            return JSONResult.badArgument();
        }

        // 如果修改的角色是超级权限，则拒绝修改。
        if(permissionService.checkSuperPermission(roleId)){
            return JSONResult.errorMsg(ROLE_SUPER_SUPERMISSION, "当前角色的超级权限不能变更");
        }

        // 先删除旧的权限，再更新新的权限
        permissionService.deleteByRoleId(roleId);
        for(String permissionStr : permissionStrs){
            Permission permissionDb = new Permission();
            permissionDb.setRoleId(roleId);
            permissionDb.setPermission(permissionStr);
            permissionService.add(permissionDb);
        }*/
        return BaseResultUtil.success();
    }


}
