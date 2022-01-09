package com.gallop.file.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.core.base.PagedResult;
import com.gallop.file.enums.CommonStatusEnum;
import com.gallop.file.mapper.FileStorageMapper;
import com.gallop.file.pojo.FileStorage;
import com.gallop.file.service.FileStorageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author gallop
 * date 2021-12-29 17:40
 * Description:
 * Modified By:
 */
@Service
public class FileStorageServiceImpl extends ServiceImpl<FileStorageMapper, FileStorage> implements FileStorageService {
    @Override
    public void add(FileStorage fileStorage) {
        this.add(fileStorage);
    }

    @Override
    public void deleteByKey(String key) {
        this.deleteByKey(key);
    }

    @Override
    public FileStorage findByKey(String key) {
        LambdaQueryWrapper<FileStorage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileStorage::getKey, key);
        queryWrapper.ne(FileStorage::getDeleted, CommonStatusEnum.DELETED.getCode());
        return this.getOne(queryWrapper);
    }

    @Override
    public int update(FileStorage fileStorageInfo) {
        return this.update(fileStorageInfo);
    }

    @Override
    public FileStorage findById(Integer id) {
        LambdaQueryWrapper<FileStorage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileStorage::getId, id);
        queryWrapper.ne(FileStorage::getDeleted, CommonStatusEnum.DELETED.getCode());
        return this.getOne(queryWrapper);
    }

    @Override
    public PagedResult querySelective(String key, String name, Integer page, Integer pageSize, String sort, String order) {
        LambdaQueryWrapper<FileStorage> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq(FileStorage::getKey,key);
        }
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.likeRight(FileStorage::getName,name);
        }

        queryWrapper.eq(FileStorage::getDeleted, CommonStatusEnum.DELETED.getCode());
        //queryWrapper.orderByDesc(Role::getName);
        PageHelper.startPage(page, pageSize);
        List<FileStorage> list = this.list(queryWrapper);
        PageInfo<FileStorage> pageList = new PageInfo<>(list);
        return new PagedResult(pageList);
    }

    @Override
    public int count() {
        return this.count();
    }
}
