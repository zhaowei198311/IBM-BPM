/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 流程实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface DhProcessInstanceService {
	
	/**
	 * 根据流程实例对象 查询所有流程实例(分页查询)
	 * @param processInstance 流程实例对象
	 * @param pageNum 
	 * @param pageSize
	 * @return List<DhProcessInstance> 集合
	 */
	ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance,Integer pageNum, Integer pageSize);
	
	/**
	 * 根据流程实例主键 查询流程
	 * @param insUid
	 * @return DhProcessInstance 流程实例对象
	 */
	DhProcessInstance selectByPrimaryKey(String insUid);
	
	/**
	 * 根据流程实例主键 修改流程实例
	 * @param insUid
	 * @return int类型 判断修改
	 */
	int updateByPrimaryKey(String insUid);
	
	/**
	 * 根据流程实例主键 删除流程实例
	 * @param insUid
	 * @return int类型 判断删除
	 */
	int deleteByPrimaryKey(String insUid);
	
	/**
	 * 根据流程实例对象 插入流程实例数据
	 * @param processInstance 
	 */
	void insertProcess(DhProcessInstance processInstance);
	
	/**
	 * 根据流程实例对象中的 流程类型 和 用户 查询流程
	 * @param processInstance
	 * @param pageNum
	 * @param pageSize
	 * @return List<DhProcessInstance> 集合
	 */
	ServerResponse<PageInfo<List<DhProcessInstance>>> selectProcessByUserAndType(DhProcessInstance processInstance,Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,Integer pageNum, Integer pageSize);
	
	/**
	 * 发起流程
	 * @param proUid 流程id
	 * @param proAppId 流程应用库id
	 * @param verUid 流程版本id
	 * @param dataInfo 流程数据
	 * @param approval 审批意见
	 * @return
	 */
	ServerResponse startProcess(String proUid, String proAppId, String verUid, String dataInfo, String approval);	
	
	/**
	 * 根据流程实例id  查看流程图
	 * @param insId 流程实例id(引擎中的id)
	 * @return
	 */
	String viewProcessImage(String insId);
}
