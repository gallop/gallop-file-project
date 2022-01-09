package com.gallop.file.enums;

import org.springframework.util.ObjectUtils;

/**
 * author gallop
 * date 2021-08-03 9:32
 * Description:
 * Modified By:
 */
public enum FileStorageTypeEnum {
    LOCAL("local","本地存储"),
    FASTDFS("fastdfs","fast DFS存储"),
    MINIO("minio","minio 存储")
    ;

    private String value;
    private String note;

    FileStorageTypeEnum(String value, String note) {
        this.value = value;
        this.note = note;
    }

    public static FileStorageTypeEnum getFileStorageTypeEnum(String value){
        //返回不截取字段
        if (ObjectUtils.isEmpty(value)){
            return FileStorageTypeEnum.LOCAL;
        }
        for (FileStorageTypeEnum fileStorageTypeEnum: FileStorageTypeEnum.values()){
            if (fileStorageTypeEnum.value.equals(value)){
                return fileStorageTypeEnum;
            }
        }
        //返回不截取字段
        return FileStorageTypeEnum.LOCAL;
    }
}
