package com.gallop.file.dao;

import com.gallop.file.pojo.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author gallop
 * date 2021-07-22 16:54
 * Description:
 * Modified By:
 */
@Component
public class FileObjectDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public FileObject savePerson(FileObject fileObject){
        return this.mongoTemplate.save(fileObject);
    }

    public List<FileObject> findAll() {
        return mongoTemplate.findAll(FileObject.class);
    }
    public List<FileObject> findByIds(List<String> ids){
        if (ids == null || ids.size()<=0){
            return null;
        }
        Query query = new Query(Criteria.where("id").in(ids));
        return mongoTemplate.find(query, FileObject.class);
    }

    public FileObject findById(String id){
        return mongoTemplate.findById(id,FileObject.class);
    }

    public void updateName(FileObject fileObject){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(fileObject.getId()));
        Update update = new Update();
        update.set("value",fileObject.getValue());
        update.set("data", fileObject.getData());
        mongoTemplate.upsert(query, update,FileObject.class);
    }

    public void deleteById(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,FileObject.class);
    }
}
