/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;

/**
 * <p>
 * Title: ProcessInstanceController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年5月9日
 */
@Controller
@RequestMapping(value = "/processInstance")
public class DhProcessInstanceController {
	private Logger log = Logger.getLogger(DhProcessInstanceController.class);

	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;

    /**
     * 发起流程的方法
     * @param data
     * @return
     */
	@RequestMapping(value = "/startProcess")
	@ResponseBody
	public ServerResponse startProcess(String data) {

		try {
            // 1. 发起流程
            ServerResponse<Map<String, Object>> startProcessResponse = dhProcessInstanceService.startProcess(data);
            if (!startProcessResponse.isSuccess()) {
                return startProcessResponse;
            }
            // 2. 提交第一个任务
            Map<String, Object> map = startProcessResponse.getData();
            int taskId = (Integer)map.get("taskId");
            BpmActivityMeta firstHumanActivity = (BpmActivityMeta)map.get("firstHumanActivity");
            DhProcessInstance dhProcessInstance = (DhProcessInstance)map.get("dhProcessInstance");
            BpmRoutingData routingData = (BpmRoutingData)map.get("routingData");
            CommonBusinessObject pubBo = (CommonBusinessObject)map.get("pubBo");
            JSONObject dataJson =(JSONObject)map.get("dataJson");
            return dhTaskInstanceService.commitFirstTask(taskId, firstHumanActivity, dhProcessInstance, routingData, pubBo, dataJson);
        } catch (Exception e) {
            log.error("发起流程失败", e);
            return ServerResponse.createByErrorMessage("发起流程失败");
        }
	}
	
	@RequestMapping(value = "/queryProcessByUser")
	@ResponseBody
	public ServerResponse queryProcessByUser() {
		
		return null;	
	}

    /**
     * 返回引擎提供的展示指定流程实例图的url
     * @param insId
     * @return
     */
	@RequestMapping(value = "/viewProcess")
	@ResponseBody
	public String viewProcess(String insId) {
		// 查看流程图 需要流程实例  这一步目的是 去 掉用IBM 做一次 登陆验证
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		String url = bpmGlobalConfig.getBpmServerHost() + "teamworks/executecf?" +
                "modelID=1.36bdcc65-8d6a-4635-85cf-57cab68a7e45&branchID=2063.34a0ce6e-631b-465d-b0dc-414c39fb893f" +
                "&tw.local.processInstanceId="+insId;
		return url;
	}

	/**
	 * 根据userId查询instuid,再根据instuid查processInstanceList
	 * 
	 * @param userId
	 *            用户id
	 * @param proUid
	 *            流程id
	 * @param proAppId
	 *            流程应用库id
	 * @param verUid
	 *            流程版本id
	 * @return
	 */
	@RequestMapping(value = "/getInfo")
	@ResponseBody
	public ServerResponse getInfo(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "proAppId") String proAppId, @RequestParam(value = "proUid") String proUid,
			@RequestParam(value = "verUid") String verUid,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		// userId = "00011178";
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance.setUsrUid(userId);
		return dhTaskInstanceService.selectTaskByUser(taskInstance, pageNum, pageSize);// 根据userId查询taskList
	}

	@RequestMapping(value = "/queryProcessByUserAndType")
	@ResponseBody
	public ServerResponse queryProcessByUserAndType(@RequestParam(value = "insTitle", required = false) String insTitle,
			@RequestParam(value = "insStatusId", required = false) String insStatusId,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("insTitle", insTitle);
		paramsMap.put("insStatusId", insStatusId);
		return dhProcessInstanceService.queryByStausOrTitle(paramsMap, pageNum, pageSize);
	}

	@RequestMapping(value = "/queryProcessByActive")
	@ResponseBody
	public ServerResponse queryProcessByActive(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		// 根据当前用户查询 他有哪些流程
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance
				.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		return dhTaskInstanceService.selectTaskByUser(taskInstance, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/queryRejectByActivity")
	@ResponseBody
	public ServerResponse queryRejectByActivity(@RequestParam(value = "activityId", required = false) String activityId,
			@RequestParam(value = "insUid", required = false) String insUid) {
		return dhProcessInstanceService.queryRejectByActivity(activityId,insUid);
	}
	

	@RequestMapping(value = "/queryProInstanceByInsUid")
	@ResponseBody
	public ServerResponse queryProInstanceByInsUid(@RequestParam(value = "insUid")String insUid) {
		try {
			return dhProcessInstanceService.selectByPrimaryKey(insUid);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	@RequestMapping(value = "/checkedBusinesskey")
	@ResponseBody
	public ServerResponse checkedBusinesskey(String proAppId, String proUid) {
		try {
			return dhProcessInstanceService.selectBusinessKeyToStartProcess(proAppId, proUid);
		}catch (Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 
	 * @Title: queryProcessInstanceByIds  
	 * @Description: 根据proUid，proAppId以及当前用户id查询流程实例  
	 * @param @param dhProcessInstance
	 * @param @return  
	 * @return ServerResponse  
	 */
	@RequestMapping(value = "/queryProcessInstanceByIds")
	@ResponseBody
	public ServerResponse<?> queryProcessInstanceByIds(@RequestParam(value = "proUid", required = true) String proUid,
													@RequestParam(value = "proAppId", required = true) String proAppId,
													@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
													@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
													@RequestParam(value = "status", required = true) String status,
													@RequestParam(value = "processName", required = true) String processName,
													@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date startTime,
													@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date endTime,
													@RequestParam(value="retrieveData",required=false)String retrieveData){
		String usrUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER); 
		return dhProcessInstanceService.queryProcessInstanceByIds(status, processName, startTime, endTime, pageNum, pageSize, usrUid, proUid, proAppId,retrieveData);
	}
}
