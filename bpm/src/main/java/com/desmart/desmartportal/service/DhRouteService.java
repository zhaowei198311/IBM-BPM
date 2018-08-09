package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.entity.SysUser;

public interface DhRouteService {
	
	/**
	 * 展示当前环节的下个环节与默认处理人信息
	 * 未发起的流程通过insUid得到的流程实例id(insId)为:-1
	 * @param insUid 流程实例id
	 * @param  activityId 当前环节id
	 * @param  departNo 部门
	 * @param formData 表单数据
	 * @param 	companyNum 公司id
	 * @return  ServerResponse<List<BpmActivityMeta>>
	 */
	ServerResponse<List<BpmActivityMeta>> showRouteBar(String taskUid, String insUid, String activityId, 
												String departNo, String companyNum, String formData);

	/**
	 * 装配处理人信息
	 * @param pubBo
	 * @param routeData
	 * @return
	 */
	public ServerResponse<CommonBusinessObject> assembleCommonBusinessObject(CommonBusinessObject pubBo, JSONArray routeData);

	/**
	 * 根据表单数据和环节找到接下来会流转到的真实环节
	 * @param sourceActivityMeta
	 * @param formData
	 * @return
	 */
	Set<BpmActivityMeta> getActualNextActivities(BpmActivityMeta sourceActivityMeta, JSONObject formData);

	/**
	 * 获取指定环节的可选处理人
	 * @param insUid  流程实例主键
	 * @param activityId  指定环节
	 * @param departNo    部门编号
	 * @param companyNum  公司编码
	 * @param formData    表单数据
	 * @param request     HttpServletRequest
	 * @param taskUid     任务主键
	 * @return
	 */
	ServerResponse<List<SysUser>> getChoosableHandler(String insUid, String activityId, String departNo, String companyNum, String formData
			, HttpServletRequest request, String taskUid);

	/**
	 * 获得指定节点的默认处理人，如果没有处理人，不设置为管理员，返回空集合
	 * @param taskNode  指定节点
	 * @param preTaskOwner  上个环节任务处理人
	 * @param dhProcessInstance  流程实例
	 * @param mergedFormData  当前的formData信息
	 * @return 不会返回null
	 * 返回值map包含的内容：
	 * idList 处理人工号集合 不会为null
	 * nameList 处理人对象集合  不会为null
	 */
	List<SysUser> getDefaultTaskOwnerOfTaskNode(BpmActivityMeta taskNode, String preTaskOwner, DhProcessInstance dhProcessInstance, JSONObject mergedFormData);

	/**
	 * 更新网关决策的中间表
	 * @param currActivityMeta
	 * @param insId
	 * @param formData
	 * @return
	 */
	ServerResponse updateGatewayRouteResult(Integer insId, BpmRoutingData routingData);
	
    /**
     * 找流转到指定环节的上一环节（含有处理人信息）, 包含驳回的，跳转的
	 * 并将任务处理人放入： userUid属性 和 userName属性
     * @param dhProcessInstance
     * @param bpmActivityMeta
     * @return
     */
	ServerResponse<BpmActivityMeta> getPreActivity(DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta);

	/**
	 * 找到基于流程图的上个环节（含有处理人信息）， 基于流程图上的上个环节，不含取回的、驳回的
	 * @param sourceNode  目标节点
	 * @param dhProcessInstance  流程实例
	 * @return
	 */
	ServerResponse<BpmActivityMeta> getPreActivityByDiagram(DhProcessInstance dhProcessInstance, BpmActivityMeta sourceNode);


	/**
	 * 根据网关环节和表单内容给出唯一的输出连接线
	 * @param gatewayNode
	 * @param formData
	 * @return
	 */
	DhGatewayLine getUniqueOutLineByGatewayNodeAndFormData(BpmActivityMeta gatewayNode, JSONObject formData);

	/**
	 * 查询网关路由的结果
	 * @param insId
	 * @param activityBpdId
	 * @return
	 */
	ServerResponse<String> getDhGatewayRouteResult(Integer insId, String activityBpdId);

	/**
	 *	查看提交上来的选人信息是否完整
	 * @param currTaskNode  当前任务节点
	 * @param routeData     提交的路由信息
	 * @param routingData   下个环节路由结果
	 * @return  true 信息完整   false 信息缺失
	 */
	boolean checkRouteData(BpmActivityMeta currTaskNode, JSONArray routeData, BpmRoutingData routingData);

	/**
	 * 预判提交任务动作会不会使token移动到下个人员服务
	 * @param currTask
	 * @return
	 */
	boolean willFinishTaskMoveToken(DhTaskInstance currTask, int insId);

	/**
	 * 判断token是否真实移动——移动到下个人员任务节点，或者导致流程结束
	 * @param insId  流程实例id
	 * @param routingData  预测的执行情况
	 * @return 当结果是会移动，返回success， data中存放 JSONObject processData
	 *         当结果是没有移动，返回success， data中存放null
	 */
	ServerResponse<JSONObject> didTokenMove(int insId, BpmRoutingData routingData);

	/**
	 * 根据流程实例编号和下个环节路由信息得到简单循环任务的处理人列表
	 * @param insId  流程实例编号
	 * @param bpmRoutingData  下个环节路由数据
	 * @param pubBo
	 * @return
	 */
	List<DhTaskHandler> saveTaskHandlerOfLoopTask(int insId, BpmRoutingData bpmRoutingData, CommonBusinessObject pubBo);



	/**
	 * 为不能指定处理人的节点装配处理人<br/>
	 * 提交人是子流程的最后一个人时<br/>
	 * 1. 子流程衔接另一个子流程<br/>
	 * 2. 子流程衔接普通节点<br/>
	 * @param subProcessInstance
	 * @param pubBo
	 * @param routingData
	 * @return
	 */
	CommonBusinessObject assembleTaskOwnerForNodesCannotChoose(DhTaskInstance currTask, DhProcessInstance currProcessInstance,
															   CommonBusinessObject pubBo, BpmRoutingData routingData);

	/**
	 * 为系统任务装配下一步的默认处理人, 与assembleTaskOwnerForNodesCannotChoose互补<br/>
	 * 1. firstTaskNodesOfStartProcessOnSameDeepLevel<br/>
	 * 2. taskNodesOnSameDeepLevel<br/>
	 * @param currTask
	 * @param currProcessInstance
	 * @param pubBo
	 * @param routingData
	 * @return
	 */
	CommonBusinessObject assembleTaskOwnerForSystemTask(DhTaskInstance currTask, DhProcessInstance currProcessInstance,
														CommonBusinessObject pubBo, BpmRoutingData routingData);

	/**
	 * 获得下个环节的路由信息
	 * @param sourceNode  作为起始点的节点
	 * @param formData  JSONObject格式的表单数据
	 * @return
	 */
	BpmRoutingData getBpmRoutingData(BpmActivityMeta sourceNode, JSONObject formData);

	/**
	 * 异步查询符合条件的用户
	 * @param insUid
	 * @param activityId
	 * @param departNo
	 * @param companyNum
	 * @param formData
	 * @param request
	 * @param taskUid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse choosableHandlerMove(String insUid, String activityId, String departNo, String companyNum, String formData
			,HttpServletRequest request, String taskUid, String userUidArrStr,String condition);

	/**
	 * 根据当前流程和代表子流程的节点得到子流程的父流程实例
	 * @param currProcessInstance  当前流程实例
	 * @param nodeIdentifyProcess  代表子流程的节点
	 * @return
	 */
	DhProcessInstance getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(DhProcessInstance currProcessInstance,
																						  BpmActivityMeta nodeIdentifyProcess);

	/**
	 * 判断任务节点是否是子流程的第一个节点，而且是回退回来的
	 * @param taskNode  任务节点
	 * @param processInstance  任务节点所属流程
	 * @return
	 */
	boolean isFirstTaskOfSubProcessAndWasRejected(BpmActivityMeta taskNode, DhProcessInstance processInstance);

	/**
	 * 判断任务节点是否是子流程的第一个节点
	 * @param taskNode 任务节点
	 * @param processInstance 任务节点所属流程
	 * @return
	 */
	boolean isFirstTaskOfSubProcess(BpmActivityMeta taskNode, DhProcessInstance processInstance);

	/**
	 * 获得主流程真实的第一个人工环节，会检索子流程节点<br/>
	 * 与 BpmActivityMetaService.getFirstUserTaskNodeOfMainProcess()区别
	 * @param proAppId
	 * @param proUid
	 * @param proVerUid
	 * @return
	 */
	ServerResponse<BpmActivityMeta> getActualFirstUserTaskNodeOfMainProcess(String proAppId, String proUid, String proVerUid);
}
