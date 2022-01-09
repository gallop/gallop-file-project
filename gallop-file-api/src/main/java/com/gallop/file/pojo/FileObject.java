package com.gallop.file.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author gallop
 * date 2021-07-22 16:26
 * Description:
 * Modified By:
 */
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileObject {
    @Id
    private String id;
    private Long ownId; //文档拥有者userId
    private boolean isFolder;
    private String type;
    private String value;
    private String url;
    private Long size;
    private Integer readCount;
    private Integer downCount;
    private boolean open;
    private Date date;
    private List<FileObject> data;

    public long getDate() {
        String timestamp = String.valueOf(date.getTime()/1000);
        return Long.valueOf(timestamp);
    }

    public void setDate(long time) {
        this.date = new Date(time);
    }

     /**
      * 2date 2021-07-26 17:32
      * Description: 追加子节点
      * Param:
      * return:
      **/
    public void append(FileObject fileObject){
        if(this.data != null){
            this.data.add(fileObject);
        }else {
            List<FileObject> list = new ArrayList<>();
            list.add(fileObject);
            this.setData(list);
        }
    }
}
