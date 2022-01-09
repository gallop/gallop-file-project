package com.gallop.file.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.gallop.core.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author gallop
 * date 2021-11-02 11:06
 * Description:
 * Modified By:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict_type")
public class SysDictTypePojo extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;
}
