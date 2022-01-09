package com.gallop.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.core.pojo.SysUser;
import com.gallop.file.enums.CommonStatusEnum;
import com.gallop.file.mapper.RootFolderMapper;
import com.gallop.file.mapper.SysUserMapper;
import com.gallop.file.pojo.RootFolderPojo;
import com.gallop.file.service.RootFolderService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * author gallop
 * date 2022-01-03 18:00
 * Description:
 * Modified By:
 */
@Service
public class RootFolderServiceImpl extends ServiceImpl<RootFolderMapper, RootFolderPojo> implements RootFolderService {


    @Override
    public List<RootFolderPojo> findByUserId(Long userId) {
        LambdaQueryWrapper<RootFolderPojo> queryWrapper = new LambdaQueryWrapper<>();
        if(ObjectUtils.isNotEmpty(userId)){
            queryWrapper.eq(RootFolderPojo::getUserId,userId);
        }else {
            return null;
        }

        queryWrapper.eq(RootFolderPojo::getStatus, CommonStatusEnum.ENABLE.getCode());
        return this.list(queryWrapper);
    }

    @Override
    public Boolean saveRootFolder(RootFolderPojo folderPojo) {
        return this.save(folderPojo);
    }
}
