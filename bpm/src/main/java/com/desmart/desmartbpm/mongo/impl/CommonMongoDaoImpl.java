package com.desmart.desmartbpm.mongo.impl;

import com.alibaba.fastjson.JSON;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.mongo.CommonMongoDao;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CommonMongoDaoImpl implements CommonMongoDao {
    

    @Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public Integer getIntValue(String key) {
        String itemStr = mongoTemplate.findById(key, String.class, Const.COMMON_COLLECTION_NAME);
        if (itemStr == null) return null;
        return JSON.parseObject(itemStr).getIntValue("value");
    }

    @Override
    public String getStringValue(String key) {
        String itemStr = mongoTemplate.findById(key, String.class, Const.COMMON_COLLECTION_NAME);
        if (itemStr == null) return null;
        return JSON.parseObject(itemStr).getString("value");
    }

    @Override
    public int set(String key, Object value) {
        Query qurey = new Query(new Criteria("_id").is(key));
        Update update = new Update();
        update.set("value", value);
        WriteResult result = mongoTemplate.upsert(qurey, update, Const.COMMON_COLLECTION_NAME);
        return result.getN();
    }

    @Override
    public int remove(String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(key));
        WriteResult result = mongoTemplate.remove(query, Const.COMMON_COLLECTION_NAME);
        return result.getN();
    }

    @Override
    public Integer getIntValue(String key, String collectionName) {
        String itemStr = mongoTemplate.findById(key, String.class, collectionName);
        if (itemStr == null) return null;
        return JSON.parseObject(itemStr).getIntValue("value");
    }

    @Override
    public String getStringValue(String key, String collectionName) {
        String itemStr = mongoTemplate.findById(key, String.class, collectionName);
        if (itemStr == null) return null;
        return JSON.parseObject(itemStr).getString("value");
    }

    @Override
    public int set(String key, Object value, String collectionName) {
        Query qurey = new Query(new Criteria("_id").is(key));
        Update update = new Update();
        update.set("value", value);
        WriteResult result = mongoTemplate.upsert(qurey, update, collectionName);
        return result.getN();
    }

    @Override
    public int remove(String key, String collectionName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(key));
        WriteResult result = mongoTemplate.remove(query, collectionName);
        return result.getN();
    }



}