package com.desmart.desmartbpm.mongo;

import java.util.List;

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
	 * @return  
	 * @return List<JSONObject>
	 */
	List<JSONObject> queryInsData(String key, String value, Integer pageNum, Integer pageSize, 
								String usrUid, String proUid, String proAppId);
}
