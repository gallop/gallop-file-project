package com.gallop.file.service;

import cn.hutool.core.lang.Dict;
import com.gallop.file.param.SysDictTypeParam;

import java.util.List;

/**
 * author gallop
 * date 2021-11-02 11:13
 * Description:
 * Modified By:
 */
public interface SysDictTypeService {
    /**
     * 系统字典类型下拉
     *
     * @param sysDictTypeParam 下拉参数
     * @return 增强版hashMap，格式：[{"code:":"1", "value":"正常"}]
     */
    List<Dict> dropDown(SysDictTypeParam sysDictTypeParam);
}
