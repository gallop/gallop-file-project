package com.gallop.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.core.base.BaseResultUtil;
import com.gallop.core.base.PagedResult;
import com.gallop.core.context.login.LoginContextHolder;
import com.gallop.core.exception.enums.RoleExceptionEnum;
import com.gallop.core.pojo.Role;
import com.gallop.core.pojo.SysUser;
import com.gallop.file.constant.CommonConstant;
import com.gallop.file.enums.CommonStatusEnum;
import com.gallop.file.exception.ServiceException;
import com.gallop.file.mapper.RoleMapper;
import com.gallop.file.param.SysRoleParam;
import com.gallop.file.service.PermissionService;
import com.gallop.file.service.RoleService;
import com.gallop.file.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * author gallop
 * date 2021-10-04 21:54
 * Description:
 * Modified By:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private PermissionService permissionService;

    @Override
    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if (roleIds.length == 0) {
            return roles;
        }
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId, roleIds).eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());
        List<Role> roleList = this.list(queryWrapper);
        for (Role role : roleList) {
            roles.add(role.getName());
        }
        return roles;
    }

    @Override
    public PagedResult querySelective(SysRoleParam sysRoleParam) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysRoleParam.getName())) {
            queryWrapper.likeRight(Role::getName,sysRoleParam.getName());
        }
        if (!StringUtils.isEmpty(sysRoleParam.getCode())) {
            queryWrapper.likeRight(Role::getCode,sysRoleParam.getCode());
        }

        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());
        //queryWrapper.orderByDesc(Role::getName);
        PageHelper.startPage(sysRoleParam.getPage(), sysRoleParam.getPageSize());
        List<Role> list = this.list(queryWrapper);
        PageInfo<Role> pageList = new PageInfo<>(list);
        return new PagedResult(pageList);
    }

    @Override
    public List<Dict> options() {
        List<Dict> dictList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        //????????????????????????????????????????????????????????????????????????
        if (!LoginContextHolder.getContext().isSuperAdmin()) {

            //?????????????????????
            List<Integer> loginUserRoleIds = Arrays.asList(LoginContextHolder.getContext().getSysLoginUser().getUser().getRoleIds());
            if (ObjectUtil.isEmpty(loginUserRoleIds)) {
                return dictList;
            }
            queryWrapper.in(Role::getId, loginUserRoleIds);
        }
        //?????????????????????
        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());
        this.list(queryWrapper)
                .forEach(sysRole -> {
                    Dict dict = Dict.create();
                    dict.put(CommonConstant.ID, sysRole.getId());
                    dict.put(CommonConstant.CODE, sysRole.getCode());
                    dict.put(CommonConstant.NAME, sysRole.getName());
                    dictList.add(dict);
                });
        return dictList;
    }

    @Override
    public Role findById(Integer id) {
        Role role = this.getById(id);
        return role;
    }

    @Override
    public void add(SysRoleParam sysRoleParam) {
        //?????????????????????????????????????????????????????????
        checkRoleRepeat(sysRoleParam, false);
        Role sysRole = new Role();
        BeanUtil.copyProperties(sysRoleParam, sysRole);
        sysRole.setStatus(CommonStatusEnum.ENABLE.getCode());
        this.save(sysRole);
    }

    @Override
    public void deleteById(SysRoleParam sysRoleParam) {
        // ????????????????????????????????????????????????????????????????????????
        List<SysUser> sysUserList = sysUserService.findAll();
        for(SysUser sysUser : sysUserList){
            Integer[] roleIds = sysUser.getRoleIds();
            for(Integer roleId : roleIds){
                if(sysRoleParam.getId()== roleId.intValue()){
                    throw new ServiceException(RoleExceptionEnum.ROLE_HAS_USER);
                }
            }
        }

        Role sysRole = this.querySysRole(sysRoleParam.getId());
        sysRole.setStatus(CommonStatusEnum.DELETED.getCode());
        this.updateById(sysRole);
    }

    @Override
    public void edit(SysRoleParam sysRoleParam) {
        Role sysRole = this.querySysRole(sysRoleParam.getId());
        //?????????????????????????????????????????????????????????
        checkRoleRepeat(sysRoleParam, true);
        BeanUtil.copyProperties(sysRoleParam, sysRole);
        //??????????????????????????????????????????????????????
        sysRole.setStatus(null);
        this.updateById(sysRole);
    }


    @Override
    public boolean checkExist(String name) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq(Role::getName,name);
        }

        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());

        return this.count(queryWrapper)!= 0;
    }

    @Override
    public List<Role> queryAll() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());

        return this.list(queryWrapper);
    }

    @Override
    public void grantMenu(SysRoleParam sysRoleParam) {
        this.querySysRole(sysRoleParam.getId());
        permissionService.saveRolePermission(sysRoleParam.getId().toString(),sysRoleParam.getGrantMenuIdList(),sysRoleParam.getLastPermissionIds());
    }


    /**
     * ??????????????????
     *
     */
    private Role querySysRole(long roleId) {
        Role sysRole = this.getById(roleId);
        if (ObjectUtil.isNull(sysRole)) {
            throw new ServiceException(RoleExceptionEnum.ROLE_NOT_EXIST);
        }
        return sysRole;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     */
    private void checkRoleRepeat(SysRoleParam sysRoleParam, boolean isExcludeSelf) {
        Integer id = sysRoleParam.getId();
        String name = sysRoleParam.getName();
        String code = sysRoleParam.getCode();

        LambdaQueryWrapper<Role> queryWrapperByName = new LambdaQueryWrapper<>();
        queryWrapperByName.eq(Role::getName, name)
                .ne(Role::getStatus, CommonStatusEnum.DELETED.getCode());

        LambdaQueryWrapper<Role> queryWrapperByCode = new LambdaQueryWrapper<>();
        queryWrapperByCode.eq(Role::getCode, code)
                .ne(Role::getStatus, CommonStatusEnum.DELETED.getCode());

        //????????????????????????????????????????????????????????????id
        if (isExcludeSelf) {
            queryWrapperByName.ne(Role::getId, id);
            queryWrapperByCode.ne(Role::getId, id);
        }
        int countByName = this.count(queryWrapperByName);
        int countByCode = this.count(queryWrapperByCode);

        if (countByName >= 1) {
            throw new ServiceException(RoleExceptionEnum.ROLE_NAME_REPEAT);
        }
        if (countByCode >= 1) {
            throw new ServiceException(RoleExceptionEnum.ROLE_CODE_REPEAT);
        }
    }
}
