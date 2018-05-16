/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhAgentProInfo;
/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author 张志颖 
* @date 2018年5月8日  
*/
@Repository
public interface DhAgentMapper {
		
	int deleteByAgentId(String agentId);
	
	List <DhAgent> selectAgentList(@Param(value="currUser") String currUser,
			@Param(value="person") String person);
	
	int save(DhAgent agent);

	List<DhAgentProInfo> queryDhAgentProInfoByAgentId(String agentId);
}
