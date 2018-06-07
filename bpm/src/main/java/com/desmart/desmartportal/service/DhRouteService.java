package com.desmart.desmartportal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
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
	 * 根据表单数据和环节找到接下来会流转到的环节
	 * @param sourceActivityMeta
	 * @param formData
	 * @return
	 */
	List<BpmActivityMeta> getNextActivities(BpmActivityMeta sourceActivityMeta, JSONObject formData);
	/**
	 * 装配处理人信息
	 * @param pubBo
	 * @param routeData
	 * @return
	 */
	public ServerResponse<CommonBusinessObject> assembleCommonBusinessObject(CommonBusinessObject pubBo, JSONArray routeData);


	
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
	BpmRoutingData getNextActivityTo(BpmActivityMeta sourceNode, JSONObject formData);

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
}
