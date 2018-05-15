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
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.ProcessInstance;
import com.desmart.desmartportal.entity.TaskInstance;
import com.desmart.desmartportal.service.ProcessInstanceService;
import com.desmart.desmartportal.service.ProcessService;
import com.desmart.desmartportal.service.TaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;

/**  
* <p>Title: ProcessServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
@Service
public class ProcessServiceImpl implements ProcessService {
	
	private Logger log = Logger.getLogger(ProcessServiceImpl.class);
	
	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@Autowired
	private TaskInstanceService taskInstanceService;
	
	/**
	 * 发起流程
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
		log.info("发起流程结束......");
		if(result.getCode()==200) {
			// 保存数据信息
			log.info("掉用API返回过来的数据信息:"+result.getMsg());
			
			JSONObject jsonBody = JSONObject.parseObject(result.getMsg());
			JSONObject jsonBody2 = JSONObject.parseObject(String.valueOf(jsonBody.get("data")));
			JSONArray jsonBody3 = JSONArray.parseArray(String.valueOf(jsonBody2.get("tasks")));
			// 将流程数据 保存到 当前流程实例数据库中
			String InsUid = EntityIdPrefix.DH_PROCESS_INSTANCE + String.valueOf(UUID.randomUUID());
	      	ProcessInstance processInstance = new ProcessInstance();
	      	processInstance.setInsUid(InsUid);
	      	processInstance.setInsTitle(String.valueOf(jsonBody2.get("processAppName")));
	      	processInstance.setInsId(Integer.parseInt(String.valueOf(jsonBody2.get("piid"))));
	      	processInstance.setInsParent("");
	      	processInstance.setInsStatus(String.valueOf(jsonBody2.get("executionState")));
	      	processInstance.setInsStatusId(0);
	      	processInstance.setProAppId(String.valueOf(jsonBody2.get("processAppID")));
	      	processInstance.setProUid(String.valueOf(jsonBody2.get("processTemplateID")));
	      	processInstance.setProVerUid(String.valueOf(jsonBody2.get("snapshotID")));
	      	processInstance.setInsInitUser(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
	      	processInstance.setInsData(result.getMsg());
	      	processInstanceService.insertProcess(processInstance);
	      	// 将任务数据 保存到 当前任务实例数据库中
	      	TaskInstance taskInstance = new TaskInstance();
	      	taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + String.valueOf(UUID.randomUUID()));
	      	taskInstance.setInsUid(InsUid);
			for (int i = 0; i < jsonBody3.size(); i++) {
				JSONObject jsonObject=jsonBody3.getJSONObject(i);
		      	taskInstance.setTaskId(Integer.parseInt(String.valueOf(jsonObject.get("tkiid"))));
		      	taskInstance.setUsrUid(String.valueOf(jsonObject.get("assignedToDisplayName")));
		      	taskInstance.setActivityBpdId(String.valueOf(jsonObject.get("flowObjectID")));
		      	taskInstance.setTaskType(String.valueOf(jsonObject.get("clientTypes")));
		      	taskInstance.setTaskStatus(String.valueOf(jsonObject.get("status")));
		      	taskInstance.setTaskTitle(String.valueOf(jsonObject.get("name")));
		      	taskInstance.setTaskData(String.valueOf(jsonObject.get("data")));
		      	taskInstanceService.insertTask(taskInstance);
			}
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

}
