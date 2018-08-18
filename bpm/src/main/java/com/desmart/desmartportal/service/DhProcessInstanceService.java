/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DatLaunchProcessResult;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.github.pagehelper.PageInfo;

public interface DhProcessInstanceService {
	
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
	int updateByPrimaryKeySelective(DhProcessInstance dhProcessInstance);
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,Integer pageNum, Integer pageSize);
	
	/**
	 * 发起流程
	 */
	ServerResponse<Map<String, Object>> startProcess(String data);


	/**
	 * 准备流程第一个环节的数据
	 * @return
	 */
	ServerResponse<Map<String, Object>> toStartProcess(String proAppId, String proUid, String insUid,String insBusinessKey);

	
	/**
	 * 根据流程定义创建草稿的流程实例
	 * @param dhProcessDefinition 流程定义
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
	 * 根据流程实例编号，和tokenId来确定一个子流程实例
	 * @param insId
	 * @param tokenId
	 * @return
	 */
	DhProcessInstance queryByInsIdAndTokenId(int insId, String tokenId);

	/**
	 * 创建一个子流程实例对象
	 * @param parentInstance 父流程实例
	 * @param processNode  代表子流程的节点
	 * @param tokenId  代表子流程的节点上停留的token
	 * @param creatorId   子流程创建人
	 * @param departNo    子流程创建人所属部门
	 * @param companyNumber 子流程创建人所属组织编号
	 * @return
	 */
	DhProcessInstance generateSubProcessInstanceByParentInstance(DhProcessInstance parentInstance, DhProcessInstance currProcessInstance, BpmActivityMeta processNode,
																 String tokenId, String creatorId);

	/**
	 *
	 * @param dhProcessInstance
	 * @return
	 */
	ServerResponse selectBusinessKeyToStartProcess(String proAppId, String proUid);

	/**
	 * 根据下一个环节的路由信息，关闭流程（主流程，子流程）<br/>
	 * @param insId  流程实例编号
	 * @param routingData  下个环节的路由信息
	 * @return
	 */
	ServerResponse closeProcessInstanceByRoutingData(int insId, BpmRoutingData routingData, JSONObject processDataJson);

	/**
	 * 根据当前任务所在的流程实例、下个环节的路由信息、提交任务pubBo来创建子流程<br/>
	 * 如果不需要创建返回sucess
	 * @param currProcessInstance 当前任务所在的流程实例
	 * @param bpmRoutingData 下个环节路由信息
	 * @param pubBo  向引擎传递的变量
	 * @param  processDataJson  流程实例信息，通过调用RESTful API获得
	 * @return
	 */
	ServerResponse createSubProcessInstanceByRoutingData(DhProcessInstance currProcessInstance, BpmRoutingData bpmRoutingData,
														 CommonBusinessObject pubBo, JSONObject processDataJson);
	
	/**
	 * 
	 * @Title: queryProcessInstanceByIds  
	 * @Description: TODO  
	 * @param @param key
	 * @param @param value
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @param usrUid
	 * @param @param proUid
	 * @param @param proAppId
	 * @param @param sign
	 * @param retrieveData
	 * @param @return  
	 * @return ServerResponse<List<JSONObject>>  
	 * @throws
	 */
	ServerResponse<Object> queryProcessInstanceByIds(String status, String processName, Date startTime, Date endTime,
															Integer pageNum, Integer pageSize, 
															String usrUid, String proUid, String proAppId,String retrieveData);


	/**
	 * 判断当前用户有没有发起指定流程的权限
	 * @param processDefintion  带版本的流程定义
	 * @return
	 */
	boolean checkPermissionStart(DhProcessDefinition processDefintion);

	/**
	 * 调用API发起一个流程
	 * @param dhProcessInstance 其中需要设置的值： <br/>
	 *                              proAppId: 应用库id<br/>
	 *								proUid: 流程id<br/>
	 *								insBusinessKey: 关键字<br/>
	 *								insInitUser: 流程发起人工号<br/>
	 *								departNo: 流程对应部门编号<br/>
	 *								companyNumber: 流程对应公司编号<br/>
	 *								insTitle: 流程标题<br/>
	 * @return
	 */
	ServerResponse<DatLaunchProcessResult> launchProcess(DhProcessInstance dhProcessInstance);
}
