package com.desmart.desmartbpm.mongo;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface InsDataDao {
	
	/**
	 * 
	 * @Title: queryInsData  
	 * @Description: 分页查询mongo缓存  
	 * @param key
	 * @param value
	 * @param pageNum
	 * @param pageSize
	 * @param usrUid
	 * @param proUid
	 * @param proAppId
	 * @param jsonArray
	 * @return  
	 * @return List<JSONObject>
	 */
	List<JSONObject> queryInsData(String status, String processName, Date startTime, Date endTime,
								Integer pageNum, Integer pageSize, 
								String usrUid, String proUid, String proAppId,JSONArray jsonArray);
	
	/**
	 * 
	 * @Title: insertInsData  
	 * @Description: 定时将查询的流程实例数据同步到MongoDB insData集合    
	 * @return void
	 */
	void insertInsData();
}
