package com.gallop.core.security.bean;

import lombok.Data;

import java.util.List;

/**
 * author gallop
 * date 2020-04-19 16:41
 * Description:
 * Modified By:
 */
@Data
public class PermVo {
    private String id;
    private String title;
    private String api;
    private List<PermVo> children;
}
