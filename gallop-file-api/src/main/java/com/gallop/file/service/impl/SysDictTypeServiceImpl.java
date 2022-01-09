package com.gallop.file.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.file.exception.ServiceException;
import com.gallop.file.exception.enums.ParamExceptionEnum;
import com.gallop.file.mapper.SysDictTypeMapper;
import com.gallop.file.param.SysDictTypeParam;
import com.gallop.file.pojo.SysDictTypePojo;
import com.gallop.file.service.SysDictDataService;
import com.gallop.file.service.SysDictTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * author gallop
 * date 2021-11-02 11:13
 * Description:
 * Modified By:
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictTypePojo> implements SysDictTypeService {

    @Resource
    private SysDictDataService sysDictDataService;

    @Override
    public List<Dict> dropDown(SysDictTypeParam sysDictTypeParam) {
        LambdaQueryWrapper<SysDictTypePojo> queryWrapper = new LambdaQueryWrapper<SysDictTypePojo>()
                .eq(SysDictTypePojo::getCode, sysDictTypeParam.getCode());

        SysDictTypePojo sysDictType = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(sysDictType)) {
            throw new ServiceException(ParamExceptionEnum.DICT_TYPE_NOT_EXIST);
        }
        return sysDictDataService.getDictDataListByDictTypeId(sysDictType.getId());
    }
}
