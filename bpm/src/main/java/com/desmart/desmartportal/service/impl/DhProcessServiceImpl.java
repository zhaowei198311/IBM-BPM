/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhProcessService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;

/**  
* <p>Title: ProcessServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
@Service
public class DhProcessServiceImpl implements DhProcessService {
	
	private Logger log = Logger.getLogger(DhProcessServiceImpl.class);
	
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	@Autowired
	private DhActivityConfMapper dhActivityConfMapper;
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
	/**
	 * 发起流程 掉用API 发起一个流程 然后 根据所选的 流程 去找下一环节审批人 以及 变量信息 
	 */
	@Override
	public ServerResponse startProcess(String proUid, String proAppId, String verUid) {
		log.info("发起流程开始......");
		HttpReturnStatus result = new HttpReturnStatus();
		// 判断
		if("".equals(proUid) && "".equals(proAppId) && "".equals(verUid)) {
			return ServerResponse.createByError();
		}
		Map<String,Object> params = new HashMap<>();
		params.put("snapshotId", verUid);
		params.put("processAppId", proAppId);
		params.put("action", "start");
		params.put("bpdId", proUid);
		// 掉用API 发起一个流程
		HttpClientUtils httpClientUtils = new HttpClientUtils();
		result = httpClientUtils.checkApiLogin("post", "http://10.0.4.201:9080/rest/bpm/wle/v1/process", params);
		log.info("掉用API状态码:"+result.getCode());		
		// 如果获取API成功  将返回过来的流程数据 保存到 平台
		if(result.getCode()==200) {
			// 保存数据信息
			log.info("掉用API返回过来的数据信息:"+result.getMsg());
			
			JSONObject jsonBody = JSONObject.parseObject(result.getMsg());
			JSONObject jsonBody2 = JSONObject.parseObject(String.valueOf(jsonBody.get("data")));
			JSONArray jsonBody3 = JSONArray.parseArray(String.valueOf(jsonBody2.get("tasks")));
			// 将流程数据 保存到 当前流程实例数据库中
			String InsUid = EntityIdPrefix.DH_PROCESS_INSTANCE + String.valueOf(UUID.randomUUID());
	      	DhProcessInstance processInstance = new DhProcessInstance();
	      	processInstance.setInsUid(InsUid);
	      	processInstance.setInsTitle(String.valueOf(jsonBody2.get("processAppName")));
	      	processInstance.setInsId(Integer.parseInt(String.valueOf(jsonBody2.get("piid"))));
	      	processInstance.setInsParent("");
	      	if(String.valueOf(jsonBody2.get("executionState")).equals("Active")) {
	      		processInstance.setInsStatus(String.valueOf(jsonBody2.get("executionState")));
	      		processInstance.setInsStatusId(Integer.parseInt(DhProcessInstance.STATUS_ACTIVE));
	      	}
	      	processInstance.setProAppId(String.valueOf(jsonBody2.get("processAppID")));
	      	processInstance.setProUid(String.valueOf(jsonBody2.get("processTemplateID")));
	      	processInstance.setProVerUid(String.valueOf(jsonBody2.get("snapshotID")));
	      	processInstance.setInsInitUser(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
	      	processInstance.setInsData(result.getMsg());
	      	dhProcessInstanceService.insertProcess(processInstance);
	      	// 将任务数据 保存到 当前任务实例数据库中
	      	DhTaskInstance taskInstance = new DhTaskInstance();
	      	taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + String.valueOf(UUID.randomUUID()));
	      	taskInstance.setInsUid(InsUid);
			for (int i = 0; i < jsonBody3.size(); i++) {
				JSONObject jsonObject=jsonBody3.getJSONObject(i);
		      	taskInstance.setTaskId(Integer.parseInt(String.valueOf(jsonObject.get("tkiid"))));
		      	taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		      	taskInstance.setActivityBpdId(String.valueOf(jsonObject.get("flowObjectID")));
		      	taskInstance.setTaskType(String.valueOf(jsonObject.get("clientTypes")));
		      	taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		      	taskInstance.setTaskTitle(String.valueOf(jsonObject.get("name")));
		      	taskInstance.setTaskData(String.valueOf(jsonObject.get("data")));
		      	dhTaskInstanceService.insertTask(taskInstance);
			}
			log.info("发起流程结束......");
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

	/**
	 * 寻找流程变量 更具activityid 去 找 meta下的 LoopType 知道是简单循环还是多循环 (3种方式)要判断
	 * if loopType 为 none 单 实例 情况下 就不需要 activityCofg 表下的 会签变量数据(sign_Count_varname)
	 * else 就需要  会签变量数据 
	 * activityCofg  下的 分派变量名称 是 必须要的
	 * 
	 * @param activityId 环节关联id
	 * @param tkkid 任务实例id (引擎)
	 */
	@Override
	public void queryProcessVariable(String activityId,String tkkid) {
		log.info("寻找流程变量开始......");
		try {
			BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
			String LoopType = bpmActivityMeta.getLoopType();
			DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
			log.info("循环类型:"+LoopType);
			HttpReturnStatus result = new HttpReturnStatus();
			HttpClientUtils httpClientUtils = new HttpClientUtils();
			Map<String,Object> params = new HashMap<>();
			params.put("action", "finish");
			params.put("parts", "all");
			if("none".equals(LoopType)) {
				// 单实例循环
				params.put("params", "all");
				result = httpClientUtils.checkApiLogin("put", "http://10.0.4.201:9080/rest/bpm/wle/v1/task/"+tkkid, params);
				log.info("掉用API状态码:"+result.getCode());		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("寻找流程变量结束......");
	}

	/* 
	 * 
	 */
	@Override
	public void setApprover() {
		// TODO Auto-generated method stub
		
	}

}
