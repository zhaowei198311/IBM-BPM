/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartportal.entity.BpmRoutingData;
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
	ServerResponse<Map<String, Object>> startProcess(String data);

	/**
	 * 发起流程成功后，提交第一个任务
	 * @param taskId
	 * @param firstHumanActivity
	 * @param dhProcessInstance
	 * @param routingData
	 * @param pubBo
	 * @param dataJson
	 * @return
	 */
	ServerResponse commitFirstTask(int taskId, BpmActivityMeta firstHumanActivity, DhProcessInstance dhProcessInstance,
								   BpmRoutingData routingData,
								   CommonBusinessObject pubBo, JSONObject dataJson);

	/**
	 * 准备流程第一个环节的数据
	 * @return
	 */
	ServerResponse<Map<String, Object>> toStartProcess(String proAppId, String proUid, String insUid,String insBusinessKey);
	
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
	DhProcessInstance generateDraftProcessInstance(DhProcessDefinition dhProcessDefinition
			,String insBusinessKey);
	
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
	ServerResponse rejectProcess(String data);

	/**
	 * 根据流程实例编号，和tokenId来确定一个子流程实例
	 * @param insId
	 * @param tokenId
	 * @return
	 */
	DhProcessInstance queryByInsIdAndTokenId(int insId, String tokenId);

	/**
	 * 根据父流程实例来创建子流程
	 * @param parentInstance 父流程实例
	 * @param processNode  代表子流程的节点
	 * @param tokenId  代表子流程的节点上停留的token
	 * @param creatorId   子流程创建人
	 * @param departNo    子流程创建人所属部门
	 * @param companyNumber 子流程创建人所属组织编号
	 * @return
	 */
	DhProcessInstance generateSubProcessInstanceByParentInstance(DhProcessInstance parentInstance, BpmActivityMeta processNode,
																 String tokenId, String creatorId, String departNo, String companyNumber);
	ServerResponse checkedBusinesskey(DhProcessInstance dhProcessInstance);

	/**
	 * 根据下一个环节的路由信息，关闭流程（主流程，子流程）
	 * @param insId  流程实例编号
	 * @param routingData  下个环节的路由信息
	 * @return
	 */
	ServerResponse closeProcessInstanceByRoutingData(int insId, BpmRoutingData routingData);

	/**
	 * 根据下个环节的路由信息和pubBo来创建子流程，如果不需要创建返回sucess
	 * @param insId 流程实例编号
	 * @param routingData 下个环节路由信息
	 * @param pubBo  向引擎传值的对象
	 * @return
	 */
	ServerResponse createSubProcessInstanceByRoutingData(int insId, BpmRoutingData routingData, CommonBusinessObject pubBo);
}
