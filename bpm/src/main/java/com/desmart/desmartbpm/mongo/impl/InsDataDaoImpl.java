package com.desmart.desmartbpm.mongo.impl;

import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.util.DateUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessRetrieveMapper;
import com.desmart.desmartbpm.entity.DhProcessRetrieve;
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
	@Autowired
	private DhProcessRetrieveMapper dhProcessRetrieveMapper;

	@Override
	public List<JSONObject> queryInsData(String status, String processName, Date startTime, Date endTime, 
			Integer pageNum, Integer pageSize, 
			String usrUid, String proUid, String proAppId,JSONArray jsonArray) {
		// 将数据插入mongo insData 集合中
		try {
//			insertInsData();
			Query query = new Query();
			Criteria criteria = new Criteria();
			//组装动态条件
			criteria = this.assembleDynamicCondition(jsonArray,criteria,proAppId,proUid);
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
				criteria.and("insStatusId").is(Integer.parseInt(status));
			}
			// 流程实例标题
			if (!processName.isEmpty()) {
				criteria.and("insTitle").regex(".*" + processName + ".*");
			}
			// 流程实例创建时间查询范围
			if (startTime == null) {
				if (endTime != null) {
					criteria.and("insCreateDate").lt(endTime);
				}
			}else {
				if (endTime == null) {
					criteria.and("insCreateDate").gte(startTime);
				}else {
					criteria.and("insCreateDate").gte(startTime).lt(endTime);
				}
			}
			criteria.and("relationUsers").regex(".*" + usrUid + ".*");
			criteria.and("proUid").is(proUid);
			criteria.and("proAppId").is(proAppId);
			query.addCriteria(criteria);
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
	//组装动态的检索条件
	private Criteria assembleDynamicCondition(JSONArray jsonArray, Criteria criteria, String proAppId, String proUid) {
		DhProcessRetrieve selective = new DhProcessRetrieve();
		selective.setProAppId(proAppId);selective.setProUid(proUid);
		List<DhProcessRetrieve> list = dhProcessRetrieveMapper.getDhprocessRetrievesByCondition(selective);
		if(list!=null && list.size()>0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);//获得表单对象
				String fieldName = jsonObject.getString("name");//获得表单字段名
				String dataValue = jsonObject.getString("value");//获得表单字段的值
				String key = "insData$.formData."+fieldName+".value";//查询MongoDB字段key
				DhProcessRetrieve dhProcessRetrieve = null;
				for (DhProcessRetrieve item : list) {
					if(item.getFieldName().equals(fieldName)) {
						dhProcessRetrieve = item;
						break;
					}
				}
				if(dhProcessRetrieve!=null) {
					String elementType = dhProcessRetrieve.getElementType();
					switch (elementType) {
					case DhProcessRetrieve.TYPE_BY_INPUT:
						if(Const.Boolean.TRUE.equals(dhProcessRetrieve.getIsScope())) {
							String[] dataArr = dataValue.toString().split(" - ");
							if(dataArr!=null && dataArr.length>1) {
								Double minNumber = Double.valueOf(dataArr[0]);
								Double maxNumber = Double.valueOf(dataArr[1]);
								criteria.and(key).gte(minNumber).lte(maxNumber);
							}
						}else {
							criteria.and(key).regex(".*" + dataValue + ".*");
						}
						
						break;
					case DhProcessRetrieve.TYPE_BY_DATE:
						if(Const.Boolean.TRUE.equals(dhProcessRetrieve.getIsScope())) {
							String[] dataArr = dataValue.toString().split(" - ");
							if(dataArr!=null && dataArr.length>1) {
								Date startTime = DateUtil.stringtoDate(dataArr[0]);
								Date endTime = DateUtil.stringtoDate(dataArr[1]);
								criteria.and(key).gte(startTime).lte(endTime);
							}
						}else {
								//模糊查询选择日期当天的数据
							if(dataValue!=null&&!"".equals(dataValue)) {
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
								String retrieveTime = simpleDateFormat.format(dataValue);
								criteria.and(key).regex(".*"+retrieveTime+".*");
							}
						}
						break;
					case DhProcessRetrieve.TYPE_BY_SELECT:
							if(dataValue!=null&&!"".equals(dataValue)) {
								criteria.and(key).is(dataValue);
							}
						break;

					default:
						break;
					}
				}
			}
		}
		return criteria;
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
