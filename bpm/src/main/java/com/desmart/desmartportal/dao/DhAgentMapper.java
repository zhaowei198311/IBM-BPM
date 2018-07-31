/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhAgentProInfo;
/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author loser
* @date 2018年5月18日  
*/
@Repository
public interface DhAgentMapper {
		
	int deleteByAgentId(String agentId);
	
	/**
	 * 根据当前用户和代理委托人模糊查询代理信息
	 */
	List <DhAgent> selectAgentList(@Param(value="currUser") String currUser,
			@Param(value="person") String person);
	
	/**
	 * 根据代理Id查询代理流程信息集合
	 */
	List<DhAgentProInfo> queryDhAgentProInfoByAgentId(String agentId);

	/**
	 * 根据开始，结束时间以及流程库id，流程id找到对应的代理流程信息集合
	 */
	List<DhAgentProInfo> queryAgentProInfoBySelective(@Param("agentSdate")Date agentSdate,
			@Param("agentEdate")Date agentEdate, @Param("meta")DhProcessMeta meta, @Param("agentOperator")String agentOperator);

	/**
	 * 添加代理信息
	 */
	int addAgentInfo(DhAgent dhAgent);

	/**
	 * 添加代理流程信息
	 */
	int addAgentProInfo(DhAgentProInfo agentProInfo);

	/**
	 * 查询某段时间内当前用户的代理信息
	 */
	List<DhAgent> queryAgentInfoByUserAndDate(@Param("agentOperator")String agentOperator, 
			@Param("agentSdate")Date agentSdate, @Param("agentEdate")Date agentEdate);

	/**
	 * 查询某段时间内当前用户不包含某个代理的其他代理集合
	 */
	List<DhAgent> queryAgentInfoByUserAndDateNotSelf(@Param("agentId")String agentId, 
			@Param("agentOperator")String agentOperator, @Param("agentSdate")Date agentSdate,
			@Param("agentEdate")Date agentEdate);

	/**
	 * 根据开始，结束时间以及流程库id，流程id找到不包含某个代理的其它代理流程信息集合
	 */
	List<DhAgentProInfo> queryAgentProInfoBySelectiveNotSelf(@Param("agentId")String agentId,@Param("agentSdate")Date agentSdate,
			@Param("agentEdate")Date agentEdate, @Param("meta")DhProcessMeta meta, @Param("agentOperator")String agentOperator);

	/**
	 * 根据代理Id修改代理设置的信息
	 */
	int updateAgentById(DhAgent dhAgent);

	/**
	 * 根据代理Id删除代理流程信息
	 */
	int deleteAgentProById(String agentId);

	/**
	 * 修改代理状态信息
	 */
	int updateAgentStatus(DhAgent dhAgent);

	/**
	 * 根据用户id、流程库id以及流程定义id查询当前时间的代理信息
	 * @param proAppid 流程库id
	 * @param proUid 流程id
	 * @param userUid 用户id
	 * @return 代理信息
	 */
	DhAgent getDelegateResult(@Param("proAppId")String proAppId, 
			@Param("proUid")String proUid, @Param("userUid")String userUid);

	/**
	 * 通过用户Id查询用户当前代理信息
	 */
	List<DhAgent> getDelegateByUserId(String userUid);

	/**
	 * 批量插入代理信息
	 * @param agentId
	 * @param metaList
	 * @return
	 */
	int insertBatch(@Param("agentProInfoList")List<DhAgentProInfo> agentProInfoList);
}
