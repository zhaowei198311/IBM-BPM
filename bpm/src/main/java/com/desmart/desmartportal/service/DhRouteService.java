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
	 * 查询获取环节处理人
	 * @param insUid 流程实例id
	 * @param  activityId 当前环节id
	 * @param  departNo 部门
	 * @param formData 表单数据
	 * @param 	companyNum 公司id
	 * @param request
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
	 * 为创建路由记录，获得下个环节的信息
	 * @param sourceActivityMeta
	 * @param routingData
	 * @return
	 */
	List<BpmActivityMeta> getNextActiviesForRoutingRecord(BpmActivityMeta sourceActivityMeta, BpmRoutingData routingData);

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
			,HttpServletRequest request);

	/**
	 * 更新网关决策的中间表
	 * @param currActivityMeta
	 * @param insId
	 * @param formData
	 * @return
	 */
	ServerResponse updateGatewayRouteResult(Integer insId, BpmRoutingData routingData);
	
    /**
     * 找到流程中指定环节的上一环节
     * @param dhProcessInstance
     * @param bpmActivityMeta
     * @return
     */
	ServerResponse<BpmActivityMeta> getPreActivity(DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta);

	/**
	 * 根据源环节与表单数据，获得下个环节的信息
	 * @param sourceNode
	 * @param formData
	 * @return
	 */
	BpmRoutingData getRoutingDataOfNextActivityTo(BpmActivityMeta sourceNode, JSONObject formData);

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
	 * 判断提交任务动作会不会使token移动到下个环节
	 * @param currTask
	 * @return
	 */
	boolean willFinishTaskMoveToken(DhTaskInstance currTask);

	/**
	 * 根据流程实例编号和下个环节路由信息得到简单循环任务的处理人列表
	 * @param insId  流程实例编号
	 * @param routeData  下个环节路由数据
	 * @return
	 */
	List<DhTaskHandler> getTaskHandlerOfSimpleLoopTask (int insId, JSONArray routeData);

	/**
	 * 更新流程实例下，简单循环任务的处理人信息
	 * @param list
	 * @return
	 */
	int updateDhTaskHandlerOfSimpleLoopTask(List<DhTaskHandler> list);
}
