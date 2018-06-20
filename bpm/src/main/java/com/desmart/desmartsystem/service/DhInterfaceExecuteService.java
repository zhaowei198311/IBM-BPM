package com.desmart.desmartsystem.service;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.util.Json;

public interface DhInterfaceExecuteService {
	
	public Json interfaceSchedule(JSONObject jsonObject) throws Exception;
	
	
	/**
	 * 
	 * @Title: executeSapInterface  
	 * @Description: Rpc类型接口执行方法
	 * @param @param jsonObject
	 * @param @param 
	 * @param @return      
	 * @return    Json  
	 * @throws 
	 */
	public Json executeRpcInterface(JSONObject jsonObject) throws Exception;
	
	
	/**
	 * 
	 * @Title: executeSapInterface  
	 * @Description: sap类型接口执行方法
	 * @param @param jsonObject
	 * @param @param 
	 * @param @return      
	 * @return Json
	 * @throws
	 */
	public Json executeWebServiceInterface(JSONObject jsonObject) throws Exception;
	
	
	/**
	 * 
	 * @Title: executeSapInterface  
	 * @Description: sap类型接口执行方法
	 * @param @param jsonObject
	 * @param @param 
	 * @param @return      
	 * @return Json
	 * @throws
	 */
	public Json executeRestInterface(JSONObject jsonObject) throws Exception;
}
