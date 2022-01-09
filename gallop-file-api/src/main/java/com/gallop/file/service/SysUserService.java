package com.gallop.file.service;


import com.gallop.core.base.PagedResult;
import com.gallop.core.pojo.SysUser;
import com.gallop.file.param.SysUserParam;


import java.util.List;

/**
 * author gallop
 * date 2021-06-24 16:01
 * Description:
 * Modified By:
 */
public interface SysUserService {
    SysUser getByUsername(String username);
    List<SysUser> findAll();
    PagedResult querySelective(String username, Integer page, Integer pageSize, String sort, String order);
    SysUser detail(SysUserParam sysUserParam);
    /**
     * 增加系统用户
     */
    void add(SysUserParam sysUserParam);
    /**
     * 编辑系统用户
     */
    void edit(SysUserParam sysUserParam);
    /**
     * 修改状态
     */
    void changeStatus(SysUserParam sysUserParam);

    /**
     * 拥有角色
     *
     */
    List<Integer> ownRole(SysUserParam sysUserParam);

    /**
     * 授权角色
     * @param sysUserParam 授权参数
     */
    void grantRole(SysUserParam sysUserParam);

    /**
     * 重置密码
     */
    void resetPwd(SysUserParam sysUserParam);

    /**
     * 删除系统用户
     *
     */
    void delete(List<SysUserParam> sysUserParamList);
}
