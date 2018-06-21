package com.desmart.desmartbpm.mongo.impl;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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
	public List<String> queryInsData(String key, String value) {
		Criteria criteria = Criteria.where("formData."+ key +".value").is(value);
		Query query = new Query(criteria);
		List<String> formData = mongoTemplate.find(query, String.class, "insData");
		return formData;
	}

	@Override
	public List<String> queryInsData(String key, String value, int page, int size) {
		Criteria criteria = new Criteria();
		Query query = new Query();
		if (!key.isEmpty()) {
			criteria = Criteria.where("formData."+ key +".value").is(value);
			query = new Query(criteria);
		}
		query.limit(size);
		query.skip(page * (size-1));
		List<String> formData = mongoTemplate.find(query, String.class, "insData");
		return formData;
	}

	@Override
	public void insertInsData() {
		try {
			List<DhProcessInstance> dhProcessInstanceList = dhProcessInstanceMapper.queryInsDataByDate();
			// 所有id集合
			List<DhProcessInstance> list = mongoTemplate.find(new BasicQuery("{}"), DhProcessInstance.class, "insData");
//			InsData insData = new InsData();
			String id = "";
			// 需要插入的数据
			List<DhProcessInstance> newDpiList = new LinkedList<>();
			// 需要更新的数据
			List<DhProcessInstance> updateDpiList = new LinkedList<>();
			boolean flag = false;
			mongoTemplate.insert(dhProcessInstanceList, "insData");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
