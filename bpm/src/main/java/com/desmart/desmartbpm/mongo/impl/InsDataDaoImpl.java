package com.desmart.desmartbpm.mongo.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.InsData;
import com.desmart.desmartbpm.mongo.InsDataDao;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;

@Repository
public class InsDataDaoImpl implements InsDataDao{
	
	@Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;
	
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	
	@Override
	public List<InsData> queryInsData(String key, String value) {
		Criteria criteria = Criteria.where("formData."+ key +".value").is(value);
		Query query = new Query(criteria);
		List<InsData> formData = mongoTemplate.find(query, InsData.class);
		return formData;
	}

	@Override
	public List<InsData> queryInsData(String key, String value, int page, int size) {
		Criteria criteria = new Criteria();
		Query query = new Query();
		if (!key.isEmpty()) {
			criteria = Criteria.where("formData."+ key +".value").is(value);
			query = new Query(criteria);
		}
		query.limit(size);
		query.skip(page * (size-1));
		List<InsData> formData = mongoTemplate.find(query, InsData.class);
		return formData;
	}

	@Override
	public void insertInsData() {
		try {
			List<DhProcessInstance> dhProcessInstanceList = dhProcessInstanceMapper.queryInsDataByDate();
//			List<InsData> insDataList = new LinkedList<>();
//			InsData insData = new InsData();
			Update update = new Update();
			JSONObject $insData = null;
			for (DhProcessInstance dhProcessInstance : dhProcessInstanceList) {
				$insData = JSONObject.parseObject(dhProcessInstance.getInsData());
				update.set("processData", $insData.getJSONObject("processData"));
				update.set("formData", $insData.getJSONObject("formData"));
//				insData.setInsUid(dhProcessInstance.getInsUid());
//				insData.setProcessData();
//				insData.setFormData($insData.getString("formData"));
//				insDataList.add(insData);
				mongoTemplate.upsert(Query.query(Criteria.where("_id").is(dhProcessInstance.getInsUid())), 
						update, InsData.class, "test");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
