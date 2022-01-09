package com.gallop.file.pojo;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author gallop
 * date 2021-08-23 11:32
 * Description:
 * Modified By:
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class FileTreeNode extends AbstractDocument {
    private boolean isFolder; //是否是目录
    private String ownId; //目录拥有者
    private String type; //文件类型
    private String value; //文件（目录）名称
    private String url;
    private Long size;
    private Integer readCount;
    private Integer downCount;
    private boolean open;
    private Date date;
    private ObjectId parentId; //上级id
    private List<FileTreeNode> data;
    private Integer depth; //递归深度，从0 开始

    public long getDate() {
        String timestamp = String.valueOf(date.getTime()/1000);
        return Long.valueOf(timestamp);
    }

    public void setDate(long time) {
        this.date = new Date(time);
    }

    public void appendChild(FileTreeNode fileTreeNode){
        if(data ==null){
            data = new ArrayList<>();
            data.add(fileTreeNode);
        }else {
            data.add(fileTreeNode);
        }
    }

    public void appendChildren(List<FileTreeNode> list){
        if(data ==null){
            data = new ArrayList<>();
            data.addAll(list);
        }else {
            data.addAll(list);
        }
    }

}
