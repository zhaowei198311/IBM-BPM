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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.Const;
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
	public List<JSONObject> queryInsData(String key, String value, Integer pageNum, Integer pageSize, 
			String usrUid, String proUid, String proAppId, String sign) {
		// 将数据插入mongo insData 集合中
		try {
			if ("true".equals(sign)) {
				insertInsData(usrUid, proUid, proAppId);
			}
			Query query = new Query();
			if (!key.isEmpty()) {
				if (value.contains(",")) {
					String[] values = value.split(",");
					String reg = "";
					for (String string : values) {
						reg += ".*" + string;
					}
					query.addCriteria(Criteria.where("insData$.formData."+ key +".value").regex(reg));
				}else {
					Criteria criteria = Criteria.where("insData$.formData."+ key +".value").regex(".*" + value + ".*");
					query.addCriteria(criteria);
				}
			}
			query.addCriteria(Criteria.where("proUid").is(proUid));
			query.addCriteria(Criteria.where("proAppId").is(proAppId));
			// 查询数量
			int count = (int) mongoTemplate.count(query, Const.INS_DATA);
			query.limit(pageSize);
			query.skip(pageSize * (pageNum-1));
			List<JSONObject> insData = mongoTemplate.find(query, JSONObject.class, Const.INS_DATA);
			insData.add(JSONObject.parseObject("{'count':'"+ count +"'}"));
			return insData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insertInsData(String usrUid, String proUid, String proAppId) {
		try {
			List<DhProcessInstance> dhProcessInstanceList = dhProcessInstanceMapper.queryInsDataByUser(usrUid, proUid, proAppId);
//			// 测试插入查询性能
//			List<DhProcessInstance> test = new LinkedList<>();
//			for (int i = 0; i < 12; i++) {
//				test.addAll(dhProcessInstanceList);
//			}
//			mongoTemplate.insert(test, Const.INS_DATA);
			
			JSONObject insData = null;
			// 数据库已存数据
			List<JSONObject> idS = mongoTemplate.find(new BasicQuery("{}","{'_id':1}"), JSONObject.class, Const.INS_DATA);
			List<String> $ids = new LinkedList<>();
			for (JSONObject jsonObject : idS) {
				$ids.add(jsonObject.getString("_id"));
			}
			
			// 需要插入的数据
			List<DhProcessInstance> insertDhProcessInstance = new LinkedList<>();
			// 需要更新的数据
			List<DhProcessInstance> updateDhProcessInstance = new LinkedList<>();
			
			for (DhProcessInstance dhProcessInstance : dhProcessInstanceList) {
				insData = JSONObject.parseObject(dhProcessInstance.getInsData());
				dhProcessInstance.setinsData$(insData);
				if ($ids.contains(dhProcessInstance.getInsUid())) {
					updateDhProcessInstance.add(dhProcessInstance);
				}else {
					insertDhProcessInstance.add(dhProcessInstance);
				}
			}
			if (!insertDhProcessInstance.isEmpty()) {
				mongoTemplate.insert(insertDhProcessInstance, Const.INS_DATA);
			}
			if (!updateDhProcessInstance.isEmpty()) {
				Update update = new Update();
				for (DhProcessInstance dhProcessInstance : updateDhProcessInstance) {			
					update.set("insData$", JSONObject.parse(dhProcessInstance.getInsData()));
					mongoTemplate.updateMulti(new Query(new Criteria("_id").is(dhProcessInstance.getInsUid())), update, Const.INS_DATA);
				}
			}
			
//			for (JSONObject jsonObject : insDataList) {
//				if ($ids.contains(jsonObject.get("_id"))) {
//					updateInsDataList.add(jsonObject);
//				}else {
//					insertInsDataList.add(jsonObject);
//				}	
//			}
//			if (!insertInsDataList.isEmpty()) {
//				mongoTemplate.insert(insertInsDataList, Const.INS_DATA);
//			}
//			if (!updateInsDataList.isEmpty()) {
//				Update update = new Update();
//				for (JSONObject jsonObject : updateInsDataList) {
//					update.set("processData", jsonObject.get("processData"));
//					update.set("formData", jsonObject.get("formData"));
//					mongoTemplate.updateMulti(new Query(new Criteria("_id").is(jsonObject.get("_id"))), update, Const.INS_DATA);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
