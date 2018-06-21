package com.desmart.desmartbpm.mongo.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.InsData;
import com.desmart.desmartbpm.mongo.InsDataDao;

@Repository
public class InsDataDaoImpl implements InsDataDao{
	
	@Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;
	
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

}
