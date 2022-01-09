package com.gallop.file.enums;

import org.springframework.util.ObjectUtils;

/**
 * author gallop
 * date 2021-07-26 17:16
 * Description:
 * Modified By:
 */

public enum FileActionEnum {
    ACTION_LIST("list","列表"),
    ACTION_RENAME("rename","改名"),
    ACTION_MOVE("move","移动"),
    ACTION_REMOVE("remove","删除")
    ;

    private String value;

    private String note;

    FileActionEnum(String value, String note) {
        this.value = value;
        this.note = note;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static FileActionEnum getFileActionEnum(String value){
        //返回不截取字段
        if (ObjectUtils.isEmpty(value)){
            return FileActionEnum.ACTION_LIST;
        }
        for (FileActionEnum fileActionEnum: FileActionEnum.values()){
            if (fileActionEnum.value.equals(value)){
                return fileActionEnum;
            }
        }
        //返回不截取字段
        return FileActionEnum.ACTION_LIST;
    }
}
