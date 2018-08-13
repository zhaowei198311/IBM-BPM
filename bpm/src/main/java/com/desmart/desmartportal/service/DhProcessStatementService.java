package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 流程报表Service</p>  
* <p>Description: </p>  
* @author xs 
* @date 2018年8月7日  
*/
public interface DhProcessStatementService {
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectAllTask(Map<String, String> parameter,Integer pageNum, Integer pageSize);
	List<DhTaskInstance> selectAllTask(Map<String, String> parameter);
}
