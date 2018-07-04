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
	 * 根据表单数据和环节找到接下来会流转到的环节，供路由工具栏展示使用
	 * @param sourceActivityMeta
	 * @param formData
	 * @return
	 */
	List<BpmActivityMeta> getNextActivitiesForRoutingBar(BpmActivityMeta sourceActivityMeta, JSONObject formData);


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
     * 获取环节可选处理人
     * @param activityUid
     * @return
     */
	public ServerResponse<List<SysUser>> choosableHandler(String insUid, String activityId, String departNo, String companyNum, String formData
			,HttpServletRequest request, String taskUid);

	/**
	 * 获得指定节点的默认处理人
	 * @param taskNode  指定节点
	 * @param preTaskOwner  上个环节任务处理人
	 * @param dhProcessInstance  流程实例
	 * @param mergedFormData  当前的formData信息
	 * @return
	 */
	List<String> getDefaultTaskOwnerOfTaskNode(BpmActivityMeta taskNode, String preTaskOwner, DhProcessInstance dhProcessInstance, JSONObject mergedFormData);

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
	boolean willFinishTaskMoveToken(DhTaskInstance currTask);

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
	 * @param routeData  下个环节路由数据
	 * @return
	 */
	List<DhTaskHandler> saveTaskHandlerOfLoopTask(int insId, JSONArray routeData);



	/**
	 * 为不能指定处理人的节点装配处理人
	 * 提交人是子流程的最后一个人时
	 * 1. 子流程衔接另一个子流程
	 * 2. 子流程衔接普通节点
	 * @param subProcessInstance
	 * @param pubBo
	 * @param routingData
	 * @return
	 */
	CommonBusinessObject assembleTaskOwnerForNodesCannotChoose(DhTaskInstance currTask, DhProcessInstance currProcessInstance,
															   CommonBusinessObject pubBo, BpmRoutingData routingData);

	/**
	 * 获得下个环节的路由信息
	 * @param sourceNode
	 * @param formData
	 * @return
	 */
	BpmRoutingData getBpmRoutingData(BpmActivityMeta sourceNode, JSONObject formData);
}
