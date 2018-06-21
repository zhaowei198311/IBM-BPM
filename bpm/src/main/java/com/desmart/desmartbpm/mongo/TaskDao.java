package com.desmart.desmartbpm.mongo;

public interface TaskDao {
	
	/**
	 * 
	 * @Title: queryTask  
	 * @Description: 判断集合是否有该字段，如果有则返回其值  
	 * @param @param task
	 * @param @return  
	 * @return String  
	 * @throws
	 */
	String queryTask(String myId);
	/**
	 * 
	 * @Title: insertTask  
	 * @Description: 插入  
	 * @param @param myId
	 * @param @param value  
	 * @return void  
	 * @throws
	 */
	void insertTask(String myId, String value);
}
