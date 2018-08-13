package com.desmart.desmartportal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhTaskInstance;

/**  
* <p>Title: 流程报表dao</p>  
* <p>Description: </p>  
* @author xsf  
* @date 2018年8月7日  
*/

@Repository
public interface DhProcessStatementMapper {
	List <DhTaskInstance> selectAllTask(Map<String,String> parameter);
}
