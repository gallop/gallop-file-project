package com.gallop.file.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author gallop
 * date 2022-01-03 17:50
 * Description:
 * Modified By:
 */
@TableName("root_folder")
@Data
public class RootFolderPojo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Long userId;
    @TableField("file_id")
    private String fileId;

    /**
     * 创建时间
     */
    @TableField("add_time")
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;

}
