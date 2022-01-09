package com.gallop.file.controller;

import com.gallop.core.annotation.Permissions;
import com.gallop.core.annotation.RequiresPermissionsDesc;
import com.gallop.core.base.BaseResultUtil;
import com.gallop.file.param.SysUserParam;
import com.gallop.core.security.bean.SysLoginUser;
import com.gallop.file.service.SysUserService;
import com.gallop.core.base.PagedResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * author gallop
 * date 2021-08-25 13:52
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/list")
    @Permissions("admin:user:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="查询")
    public Object list(String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order) {
        PagedResult userList = sysUserService.querySelective(username, page, pageSize, sort, order);
        return BaseResultUtil.success(userList);
    }

    /**
     * 查看系统用户
     *
     */
    @GetMapping("/detail")
    @Permissions("admin:user:detail")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="详情")
    public Object detail(@Validated(SysUserParam.detail.class) SysUserParam sysUserParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysLoginUser loginUser = (SysLoginUser)authentication.getPrincipal();
        System.err.println("---------loginUser:"+loginUser.toString());
        System.err.println(">>>>>id:"+sysUserParam.getId());
        return BaseResultUtil.success(sysUserService.detail(sysUserParam));
    }

    /**
     * 增加系统用户
     *
     */
    @Permissions("admin:user:add")
    @PostMapping("/add")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="增加")
    public Object add(@RequestBody @Validated(SysUserParam.add.class) SysUserParam sysUserParam) {
        sysUserService.add(sysUserParam);
        return BaseResultUtil.success();
    }

    /**
     * 编辑系统用户
     *
     */
    @PostMapping("/edit")
    //@PreAuthorize("@aps.hasPermission('admin:user:edit')")
    @Permissions("admin:user:edit")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="编辑")
    public Object edit(@RequestBody @Validated(SysUserParam.edit.class) SysUserParam sysUserParam) {
        sysUserService.edit(sysUserParam);
        return BaseResultUtil.success();
    }

    /**
     * 修改状态
     */
    @Permissions("admin:user:changeStatus")
    @PostMapping("/changeStatus")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="修改状态")
    public Object changeStatus(@RequestBody @Validated(SysUserParam.changeStatus.class) SysUserParam sysUserParam) {
        System.err.println("=========修改状态==============");
        sysUserService.changeStatus(sysUserParam);
        return BaseResultUtil.success();
    }

    /**
     * 拥有角色
     *
     */
    @GetMapping("/ownRole")
    @Permissions("admin:user:ownRole")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="拥有角色")
    public Object ownRole(@Validated(SysUserParam.detail.class) SysUserParam sysUserParam) {
        return BaseResultUtil.success(sysUserService.ownRole(sysUserParam));
    }

    /**
     * 授权角色
     *
     */
    @PostMapping("/grantRole")
    @Permissions("admin:user:grantRole")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="授权角色")
    public Object grantRole(@RequestBody @Validated(SysUserParam.grantRole.class) SysUserParam sysUserParam) {
        sysUserService.grantRole(sysUserParam);
        return BaseResultUtil.success();
    }

    /**
     * 重置密码
     *
     */
    @PostMapping("/resetPwd")
    @Permissions("admin:user:resetPwd")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="重置密码")
    public Object resetPwd(@RequestBody @Validated(SysUserParam.resetPwd.class) SysUserParam sysUserParam) {
        sysUserService.resetPwd(sysUserParam);
        return BaseResultUtil.success();
    }

    /**
     * 删除系统用户
     *
     */
    @PostMapping("/delete")
    @Permissions("admin:user:delete")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="删除用户")
    public Object delete(@RequestBody @Validated(SysUserParam.delete.class) List<SysUserParam> sysUserParamList) {
        sysUserService.delete(sysUserParamList);
        return BaseResultUtil.success();
    }


}
