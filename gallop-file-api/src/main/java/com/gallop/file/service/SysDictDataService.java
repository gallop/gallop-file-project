package com.gallop.file.service;

import cn.hutool.core.lang.Dict;

import java.util.List;

/**
 * author gallop
 * date 2021-11-05 9:23
 * Description: 系统字典值service接口
 */
public interface SysDictDataService {
    /**
     * 根据typeId下拉
     *
     * @param dictTypeId 字典类型id
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     */
    List<Dict> getDictDataListByDictTypeId(Long dictTypeId);
}
