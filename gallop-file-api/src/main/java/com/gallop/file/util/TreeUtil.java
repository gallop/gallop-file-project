package com.gallop.file.util;


import com.gallop.file.pojo.FileTreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author gallop
 * date 2021-08-23 14:48
 * Description:
 * Modified By:
 */
public class TreeUtil {

    public static List<FileTreeNode> buildTreeNode(List<FileTreeNode> treeNodeList){

        Map<Integer, List<FileTreeNode>> treeNodeMap = treeNodeList.stream().collect(Collectors.groupingBy(FileTreeNode::getDepth));
        List<Integer> keyList=new ArrayList(treeNodeMap.keySet());
        Collections.reverse(keyList);//key 倒序排序
        Integer maxKey = keyList.get(0);
        Integer depth = maxKey;
        while (depth > 0){
            List<FileTreeNode> parentNodeList = treeNodeMap.get(depth-1);
            treeNodeMap.get(depth).forEach(e->{
                for (int i = 0; i < parentNodeList.size() ; i++) {
                    FileTreeNode parentNodeItem = parentNodeList.get(i);
                    if(parentNodeItem.getId().equals(e.getParentId().toHexString())){
                        parentNodeItem.appendChild(e);
                        break;
                    }
                }
            });
            depth--;
        }
        return treeNodeMap.get(0);
    }
}
