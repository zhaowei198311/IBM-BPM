/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartportal.entity.CommonBusinessObject;
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
	ServerResponse selectByPrimaryKey(String insUid);
	
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
	 */
	ServerResponse startProcess(String data);	
	
	/**
	 * 准备流程第一个环节的数据
	 * @return
	 */
	ServerResponse<Map<String, Object>> toStartProcess(String proAppId, String proUid, String insUid);
	
	/**
	 * 根据流程实例id  查看流程图
	 * @param insId 流程实例id(引擎中的id)
	 * @return
	 */
	String viewProcessImage(String insId);
	
	/**
	 * 根据流程定义创建草稿的流程实例
	 * @param dhProcessDefinition
	 * @return
	 */
	DhProcessInstance generateDraftDefinition(DhProcessDefinition dhProcessDefinition);
	
	/**
	 * 根据流程实例唯一主键获得流程实例
	 * @param insUid
	 * @return
	 */
	DhProcessInstance getByInsUid(String insUid);
	
	/**
	 * 查询可供驳回的环节
	 */
	ServerResponse<List<Map<String,Object>>> queryRejectByActivity(String activityId,String insUid);

	
	/**
	 * 驳回流程
	 * @param insId 引擎实例id
	 * @param activityId 环节id
	 * @param user 用户id
	 * @return
	 */
	ServerResponse rejectProcess(int insId,String activityId, String user);

}
