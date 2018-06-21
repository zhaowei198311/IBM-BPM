package com.desmart.desmartbpm.mongo.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.mongo.TaskDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class TaskDaoImpl implements TaskDao{
	
	@Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

	@Override
	public String queryTask(String myId) {
//		DBObject dbObject = new BasicDBObject();  
//		dbObject.put("_id", myId);  //查询条件  
//		  
//		BasicDBObject fieldsObject=new BasicDBObject();  
//		//指定返回的字段  
//		fieldsObject.put("value", true);       
//		  
//		Query query = new BasicQuery(dbObject,fieldsObject);
		String task = mongoTemplate.findById(myId, String.class, "task");
		task = JSONObject.parseObject(task).getString("value");
		return task;
	}

	@Override
	public void insertTask(String myId, String value) {
		Update update = new Update();
		update.set("value", value);
		mongoTemplate.upsert(new Query(new Criteria("_id").is(myId)), update, "task");	
	}
	
}
