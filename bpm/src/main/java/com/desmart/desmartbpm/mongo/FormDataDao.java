package com.desmart.desmartbpm.mongo;

import java.util.List;

import com.desmart.desmartbpm.entity.FormData;

public interface FormDataDao{
	/**
	 * 
	 * @Title: queryFormData  
	 * @Description: 根据字段名和值查询Json数据  
	 * @param @param key
	 * @param @param value
	 * @param @return  
	 * @return List<FormData>  
	 * @throws
	 */
	List<FormData> queryFormData(String key, String value);
	
	/**
	 * 
	 * @Title: queryFormData  
	 * @Description: TODO  
	 * @param @param key
	 * @param @param value
	 * @param @param start
	 * @param @param size
	 * @param @return  
	 * @return List<FormData>  
	 * @throws
	 */
	List<FormData> queryFormData(String key, String value, int page, int size);
}
