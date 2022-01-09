package com.gallop.file.controller;

import cn.hutool.core.util.ObjectUtil;
import com.gallop.core.annotation.Permissions;
import com.gallop.core.annotation.RequiresPermissionsDesc;
import com.gallop.core.base.BaseResultUtil;
import com.gallop.core.security.bean.PermVo;
import com.gallop.core.util.PermissionUtil;
import com.gallop.core.util.PermissionWrap;
import com.gallop.file.param.SysRoleParam;
import com.gallop.file.service.PermissionService;
import com.gallop.file.service.RoleService;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * author gallop
 * date 2021-11-21 11:34
 * Description: 系统角色控制器
 * Modified By:
 */
@RestController
public class SysRoleController {
    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    /**
     * 查询系统角色
     *
     */
    @GetMapping("/sysRole/page")
    @Permissions("admin:sysRole:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="列表")
    public Object page(SysRoleParam sysRoleParam) {
        return BaseResultUtil.success(roleService.querySelective(sysRoleParam));
    }

    /**
     * 系统角色下拉（用于授权角色时选择）
     *
     */
    @GetMapping("/sysRole/dropDown")
    @Permissions("admin:sysRole:options")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色下拉选项")
    public Object options() {
        return BaseResultUtil.success(roleService.options());
    }

    /**
     * 添加系统角色
     */
    @PostMapping("/sysRole/add")
    @Permissions("admin:sysRole:add")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="增加")
    public Object add(@RequestBody @Validated(SysRoleParam.add.class) SysRoleParam sysRoleParam) {
        roleService.add(sysRoleParam);
        return BaseResultUtil.success();
    }

    /**
     * 删除系统角色
     *
     */
    @PostMapping("/sysRole/delete")
    @Permissions("admin:sysRole:delete")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="删除")
    public Object delete(@RequestBody @Validated(SysRoleParam.delete.class) SysRoleParam sysRoleParam) {
        Integer id = sysRoleParam.getId();
        if (id == null) {
            return BaseResultUtil.badArgument();
        }
        roleService.deleteById(sysRoleParam);
        return BaseResultUtil.success();
    }

    /**
     * 编辑系统角色
     */
    @PostMapping("/sysRole/edit")
    @Permissions("admin:sysRole:edit")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="编辑")
    public Object edit(@RequestBody @Validated(SysRoleParam.edit.class) SysRoleParam sysRoleParam) {
        roleService.edit(sysRoleParam);
        return BaseResultUtil.success();
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    @Permissions("admin:role:permission:get")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限详情")
    @GetMapping("/sysRole/getPermissions")
    public Object getPermissions(@Validated(SysRoleParam.detail.class) SysRoleParam sysRoleParam) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(sysRoleParam.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return BaseResultUtil.success(data);
    }

    private List<PermVo> getSystemPermissions(){
        final String basicPackage = "com.gallop.file.controller";
        if(systemPermissions == null || systemPermissions.size() == 0){
            List<PermissionWrap> permissionWrapLists = PermissionUtil.listPermissionWrap(context, basicPackage);
            System.err.println("permissionWrapLists-size="+permissionWrapLists.size());
            permissionWrapLists.forEach(item->{
                System.err.println(">>>>button-str="+item.getRequiresPermissionsDesc().button());
            });
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
     * 授权菜单
     *
     */
    @PostMapping("/sysRole/grantMenu")
    @Permissions("admin:role:grantMenu")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="授权菜单")
    public Object grantMenu(@RequestBody @Validated(SysRoleParam.grantMenu.class) SysRoleParam sysRoleParam) {
        roleService.grantMenu(sysRoleParam);
        return BaseResultUtil.success();
    }


}
