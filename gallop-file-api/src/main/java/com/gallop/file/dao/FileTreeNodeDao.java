package com.gallop.file.dao;

import com.gallop.file.pojo.FileObject;
import com.gallop.file.pojo.FileTreeNode;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * author gallop
 * date 2021-08-23 11:41
 * Description:
 * Modified By:
 */
@Component
public class FileTreeNodeDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public  FileTreeNode getFileTreeNode(String id){
        return this.mongoTemplate.findById(id,FileTreeNode.class);
    }
    public FileTreeNode saveTreeNode(FileTreeNode treeNode) {
        return this.mongoTemplate.save(treeNode);
    }
    public UpdateResult updateFileTreeNode(FileTreeNode node){
        Query query = Query.query(Criteria.where("id").is(node.getId()));
        Update update = Update.update("value", node.getValue());
        return this.mongoTemplate.updateFirst(query, update, FileTreeNode.class);
    }
     /**2
      * date 2021-08-23 16:48
      * Description: 对应的数据库查询脚本语句如下：
      * db.fileTreeNode.aggregate( [
      *    { $match: {
      *         _id: ObjectId("61231c4b559d6043d620189e")
      *     }},
      *    {
      *       $graphLookup: {
      *          from: "fileTreeNode",
      *          startWith: "$_id",
      *          connectFromField: "_id",
      *          connectToField: "parentId",
      *          depthField: "depth",
      *          maxDepth: 0,
      *          as: "children"
      *       }
      *    }
      * ] )
      **/
    public List<FileTreeNode> getTreeNodeOfAllChildren(String ...ids){
        Aggregation agg = newAggregation(
                match(Criteria.where("_id").in(ids)), //筛选符合条件的记录
                graphLookup("fileTreeNode")
                        .startWith("$_id")
                        .connectFrom("_id")
                        .connectTo("parentId")
                        .depthField("depth")
                        .as("data")
        );
        AggregationResults<FileTreeNode> results = mongoTemplate.aggregate(agg, "fileTreeNode", FileTreeNode.class);
        List<FileTreeNode> fileTreeNodeList = results.getMappedResults();

        return fileTreeNodeList;
    }

    public void deleteById(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, FileTreeNode.class);
    }
}
