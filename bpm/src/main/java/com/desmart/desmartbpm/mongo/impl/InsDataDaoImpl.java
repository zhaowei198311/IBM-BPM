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
	public List<JSONObject> queryInsData(String key, String value, String usrUid, String insUid) {
		Query query = new Query();
		if (value.contains(",")) {
			String[] values = value.split(",");
			String reg = "";
			for (String string : values) {
				reg += ".*" + string;
			}
			query.addCriteria(Criteria.where("formData."+ key +".value").regex(reg));
		}else {
			Criteria criteria = Criteria.where("formData."+ key +".value").regex(".*" + value + ".*");
			query.addCriteria(criteria);
		}
		List<JSONObject> insData = mongoTemplate.find(query, JSONObject.class, Const.INS_DATA);
		return insData;
	}

	@Override
	public List<JSONObject> queryInsData(String key, String value, int page, int size, 
										String usrUid, String insUid) {
		Query query = new Query();
		if (value.contains(",")) {
			String[] values = value.split(",");
			String reg = "";
			for (String string : values) {
				reg += ".*" + string;
			}
			query.addCriteria(Criteria.where("formData."+ key +".value").regex(reg));
		}else {
			Criteria criteria = Criteria.where("formData."+ key +".value").regex(".*" + value + ".*");
			query.addCriteria(criteria);
		}
		query.limit(size);
		query.skip(page * (size-1));
		List<JSONObject> insData = mongoTemplate.find(query, JSONObject.class, Const.INS_DATA);
		return insData;
	}
	
//	@Scheduled(cron = "0/20 * * * * ?")
	public void insertInsData(String usrUid, String insUid) {
		try {
			List<DhProcessInstance> dhProcessInstanceList = dhProcessInstanceMapper.queryInsDataByUser(usrUid, insUid);
			
			JSONObject insData = null;
			// 需要处理的数据
			List<JSONObject> insDataList = new LinkedList<>();
			for (DhProcessInstance dhProcessInstance : dhProcessInstanceList) {
				insData = JSONObject.parseObject(dhProcessInstance.getInsData());
				insData.put("_id", dhProcessInstance.getInsUid());
				insData.put("insTitle", dhProcessInstance.getInsTitle());
				insData.put("insStatus", dhProcessInstance.getInsStatus());
				insData.put("insCreateDate", dhProcessInstance.getInsCreateDate());
				insData.put("insFinishDate", dhProcessInstance.getInsFinishDate());
				insDataList.add(insData);
			}
			// 数据库已存数据
			List<JSONObject> idS = mongoTemplate.find(new BasicQuery("{}","{'_id':1}"), JSONObject.class, Const.INS_DATA);
			List<String> $ids = new LinkedList<>();
			for (JSONObject jsonObject : idS) {
				$ids.add(jsonObject.getString("_id"));
			}
			// 需要插入的数据
			List<JSONObject> insertInsDataList = new LinkedList<>();
			// 需要更新的数据
			List<JSONObject> updateInsDataList = new LinkedList<>();
			
			for (JSONObject jsonObject : insDataList) {
				if ($ids.contains(jsonObject.get("_id"))) {
					updateInsDataList.add(jsonObject);
				}else {
					insertInsDataList.add(jsonObject);
				}	
			}
			if (!insertInsDataList.isEmpty()) {
				mongoTemplate.insert(insertInsDataList, Const.INS_DATA);
			}
			if (!updateInsDataList.isEmpty()) {
				Update update = new Update();
				for (JSONObject jsonObject : updateInsDataList) {
					update.set("processData", jsonObject.get("processData"));
					update.set("formData", jsonObject.get("formData"));
					mongoTemplate.updateMulti(new Query(new Criteria("_id").is(jsonObject.get("_id"))), update, Const.INS_DATA);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
