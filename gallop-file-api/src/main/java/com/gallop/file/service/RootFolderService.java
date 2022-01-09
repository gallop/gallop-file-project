package com.gallop.file.service;

import com.gallop.core.pojo.SysUser;
import com.gallop.file.pojo.RootFolderPojo;

import java.util.List;

/**
 * author gallop
 * date 2022-01-03 17:58
 * Description:
 * Modified By:
 */
public interface RootFolderService {
    List<RootFolderPojo> findByUserId(Long userId);
    Boolean saveRootFolder(RootFolderPojo folderPojo);
}
