package com.gallop.file.controller;

import com.gallop.core.base.BaseResult;
import com.gallop.core.base.BaseResultUtil;
import com.gallop.file.param.SysDictTypeParam;
import com.gallop.file.service.SysDictTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author gallop
 * date 2021-11-02 10:38
 * Description: 系统字典控制器
 */
@RestController
public class SysDictTypeController {
    @Resource
    private SysDictTypeService sysDictTypeService;

    /**
     * 获取字典类型下所有字典，举例，返回格式为：[{code:"M",value:"男"},{code:"F",value:"女"}]
     *
     *
     */
    @GetMapping("/sysDictType/dropDown")
    public BaseResult dropDown(@Validated(SysDictTypeParam.dropDown.class) SysDictTypeParam sysDictTypeParam) {
        return BaseResultUtil.success(sysDictTypeService.dropDown(sysDictTypeParam));
    }
}
