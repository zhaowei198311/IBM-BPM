package com.desmart.desmartbpm.mongo.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
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
	public List<JSONObject> queryInsData(String status, String processName, Date startTime, Date endTime, 
			Integer pageNum, Integer pageSize, 
			String usrUid, String proUid, String proAppId) {
		// 将数据插入mongo insData 集合中
		try {
//			insertInsData();
			Query query = new Query();
//			if (!key.isEmpty()) {
//				if (value.contains("，")) {
//					String[] values = value.split("，");
//					String reg = "";
//					for (String string : values) {
//						reg += ".*" + string;
//					}
//					query.addCriteria(Criteria.where("insData$.formData."+ key +".value").regex(reg));
//				}else {
//					query.addCriteria(Criteria.where("insData$.formData."+ key +".value").regex(".*" + value + ".*"));
//				}
//			}
			// 流程实例状态
			if (!status.isEmpty()) {
				query.addCriteria(Criteria.where("insStatusId").is(Integer.parseInt(status)));
			}
			// 流程实例标题
			if (!processName.isEmpty()) {
				query.addCriteria(Criteria.where("insTitle").regex(".*" + processName + ".*"));
			}
			// 流程实例创建时间查询范围
			if (startTime == null) {
				if (endTime != null) {
					query.addCriteria(Criteria.where("insCreateDate").lt(endTime));
				}
			}else {
				if (endTime == null) {
					query.addCriteria(Criteria.where("insCreateDate").gte(startTime));
				}else {
					query.addCriteria(Criteria.where("insCreateDate").gte(startTime).lt(endTime));
				}
			}
			query.addCriteria(Criteria.where("relationUsers").regex(".*" + usrUid + ".*"));
			query.addCriteria(Criteria.where("proUid").is(proUid));
			query.addCriteria(Criteria.where("proAppId").is(proAppId));
			// 查询数量
			int count = (int) mongoTemplate.count(query, Const.INS_DATA);
			query.with(new Sort(new Order(Direction.DESC,"insCreateDate")));
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
	
	/**
	 * 
	 * @Title: insertInsData  
	 * @Description: 定时将查询的数据同步到mongo insData集合 ,间隔为10分钟   
	 * @return void
	 */
//	@Scheduled(cron="0 0/10 * * * ? ")
	@Override
	public void insertInsData() {
		System.out.println("========开始同步mongo insData集合数据=========");
		Date startTime = null;
		try {
			JSONObject lastTime = mongoTemplate.findOne(new Query(Criteria.where("_id").is("insFinishDate")), JSONObject.class, Const.COMMON_COLLECTION_NAME);			
			if (lastTime != null) {
				long $lastTime = Long.parseLong(lastTime.getString("value"));
				startTime = new Date($lastTime);
			}
			List<DhProcessInstance> dhProcessInstanceList = dhProcessInstanceMapper.queryInsDataByUser(startTime, new Date());
			// 测试插入查询性能
//			System.err.println("========开始a " + new Date().getTime());
//			mongoTemplate.insert(dhProcessInstanceList, Const.INS_DATA);
//			System.err.println("========结束 " + new Date().getTime());
			
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
				dhProcessInstance.setInsData$(insData);
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
			mongoTemplate.save("{'_id':'insFinishDate','value':'"+new Date().getTime()+"'}", Const.COMMON_COLLECTION_NAME);
			System.out.println("========结束同步mongo insData集合数据=========");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
