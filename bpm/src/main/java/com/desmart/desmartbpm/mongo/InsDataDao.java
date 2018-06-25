package com.desmart.desmartbpm.mongo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface InsDataDao {
	/**
	 * 
	 * @Title: queryInsData
	 * @Description: 根据字段名和值查询Json数据  
	 * @param @param key
	 * @param @param value
	 * @param @return  
	 * @return List<InsData>
	 * @throws
	 */
	List<JSONObject> queryInsData(String key, String value, String usrUid, String insUid);
	
	/**
	 * 
	 * @Title: queryInsData
	 * @Description: 分页查询  
	 * @param @param key
	 * @param @param value
	 * @param @param start
	 * @param @param size
	 * @param @return  
	 * @return List<InsData>
	 * @throws
	 */
	List<JSONObject> queryInsData(String key, String value, int page, int size, 
								String usrUid, String insUid);
	
	
	/**
	 * 
	 * @Title: insertInsData  
	 * @Description: 定时查询InsData然后插入MongoDB中  
	 * @param @return  
	 * @return   
	 * @throws
	 */
//	void insertInsData(String usrUid, String insUid);
}
