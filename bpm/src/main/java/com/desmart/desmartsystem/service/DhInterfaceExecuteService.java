package com.desmart.desmartsystem.service;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.util.Json;

import java.util.Map;

public interface DhInterfaceExecuteService {

	/**
	 * 调用使用指定参数调用接口
	 * @param jsonObject
	 * @return
	 * status:0 成功, data的key: "dilUid":接口调用日志主键 "responseBody": 响应的字符串<br/>
	 * status:1 失败，未调用接口<br/>
	 * status:2 调用该接口返回值异常，data的key: "dilUid":接口调用日志主键 <br/>
	 * @throws Exception
	 */
	ServerResponse<Map<String, String>> interfaceSchedule(JSONObject jsonObject) throws Exception;
	
	
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
	Json executeRpcInterface(JSONObject jsonObject) throws Exception;
	
	
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
	Json executeWebServiceInterface(JSONObject jsonObject) throws Exception;
	
	
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
	Json executeRestInterface(JSONObject jsonObject) throws Exception;
}
