package com.gallop.file.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.file.constant.CommonConstant;
import com.gallop.file.enums.CommonStatusEnum;
import com.gallop.file.mapper.SysDictDataMapper;
import com.gallop.file.pojo.SysDictDataPojo;
import com.gallop.file.service.SysDictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author gallop
 * date 2021-11-05 9:24
 * Description:
 * Modified By:
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictDataPojo> implements SysDictDataService {
    @Override
    public List<Dict> getDictDataListByDictTypeId(Long dictTypeId) {
        //构造查询条件
        LambdaQueryWrapper<SysDictDataPojo> queryWrapper = new LambdaQueryWrapper<SysDictDataPojo>();
        queryWrapper.eq(SysDictDataPojo::getTypeId, dictTypeId).ne(SysDictDataPojo::getStatus, CommonStatusEnum.DELETED.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(SysDictDataPojo::getSort);
        //查询dictTypeId下所有的字典项
        List<SysDictDataPojo> results = this.list(queryWrapper);

        //抽取code和value封装到map返回
        List<Dict> dictList = CollectionUtil.newArrayList();
        results.forEach(sysDictData -> {
            Dict dict = Dict.create();
            dict.put(CommonConstant.CODE, sysDictData.getCode());
            dict.put(CommonConstant.VALUE, sysDictData.getValue());
            dictList.add(dict);
        });

        return dictList;
    }
}
