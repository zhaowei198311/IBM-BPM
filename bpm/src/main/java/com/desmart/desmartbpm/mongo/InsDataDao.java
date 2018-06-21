package com.desmart.desmartbpm.mongo;

import java.util.List;

import com.desmart.desmartbpm.entity.InsData;

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
	List<InsData> queryInsData(String key, String value);
	
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
	List<InsData> queryInsData(String key, String value, int page, int size);
	/**
	 * 
	 * @Title: insertInsData  
	 * @Description: 定时查询InsData然后插入MongoDB中  
	 * @param @return  
	 * @return   
	 * @throws
	 */
	void insertInsData();
	
	/**
	 * 
	 * @Title: queryTask  
	 * @Description: 判断集合是否有该字段，如果有则返回其值  
	 * @param @param task
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	String queryTask(String task);
}
