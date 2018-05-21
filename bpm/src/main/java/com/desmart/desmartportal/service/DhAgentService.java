/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartportal.entity.DhAgent;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author loser_wu
* @date 2018年5月18日  
*/
public interface DhAgentService {
	/**
	 * 根据参数组合查询某个用户的代理设置信息
	 */
	ServerResponse<PageInfo<List<DhAgent>>> selectAgentList(Integer pageNum, Integer pageSize, String person);
	
	/**
	 * 获得指定任务的代理人和代理,没有代理人时返回null
	 * @param proAppid
	 * @param proUid
	 * @param userUid
	 * @return  Map中的键：  delegateUser  代理人    agentId 代理主键
	 */
	Map<String, String> getDelegateResult(String proAppid, String proUid, String userUid);

	/**
	 * 根据分类集合查询分类下所有的流程元数据
	 */
	ServerResponse<List<DhProcessMeta>> listDhProcessMetaByCategoryList(List<DhProcessCategory> categoryList, String proName);

	/**
     * 添加代理信息
     */
	ServerResponse addAgentInfo(Date agentSdate, Date agentEdate, List<DhProcessMeta> metaList, String agentPerson, String agentIsAll);

	/**
	 * 根据传入的流程元数组Id，判断这些流程在某个时间段是否已经被分配，符合要求返回流程元集合
	 */
	ServerResponse<List<DhProcessMeta>> queryConformProMeta(Date agentSdate, Date agentEdate,
			String[] agentProMetaUidArr);

	/**
	 * 根据传入的流程元数组Id，判断这些流程在某个时间段是否已经被分配，符合要求返回流程元集合
	 */
	ServerResponse<List<DhProcessMeta>> queryConformProMetaNotSelf(DhAgent dhAgent, String[] agentProMetaUidArr);

	/**
	 * 修改代理信息
	 */
	ServerResponse updateAgentInfo(DhAgent dhAgent, List<DhProcessMeta> metaList);

	/**
	 * 查询批量的流程元数据
	 */
	ServerResponse<List<DhProcessMeta>> listProMeta(String[] agentProMetaUidArr);

	/**
	 * 修改代理的状态
	 */
	ServerResponse updateAgentStatus(DhAgent dhAgent);

	/**
	 * 根据代理Id删除代理信息
	 */
	ServerResponse deleteAgentByAgentId(String agentId);
}
