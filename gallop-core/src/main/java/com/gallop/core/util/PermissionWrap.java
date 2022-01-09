package com.gallop.core.util;

import com.gallop.core.annotation.Permissions;
import com.gallop.core.annotation.RequiresPermissionsDesc;
import lombok.Data;



/**
 * author gallop
 * date 2020-04-19 16:42
 * Description:
 * Modified By:
 */
@Data
public class PermissionWrap {
    private Permissions permissions;
    private RequiresPermissionsDesc requiresPermissionsDesc;
    private String api;
}
