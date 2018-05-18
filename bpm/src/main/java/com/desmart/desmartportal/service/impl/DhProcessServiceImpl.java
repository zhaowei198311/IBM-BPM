/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.IBMApiUrl;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhProcessService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;

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
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	/**
	 * 发起流程 掉用API 发起一个流程 然后 根据所选的 流程 去找下一环节审批人 以及 变量信息 
	 */
	@Override
	public ServerResponse startProcess(String proUid, String proAppId, String verUid,String dataInfo) {
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
		String str = "{\"pubBo\":{\"creatorId\":\"00011178\"}}";
		params.put("params", str);
		// 掉用API 发起一个流程
		HttpClientUtils httpClientUtils = new HttpClientUtils();
		result = httpClientUtils.checkApiLogin("post", IBMApiUrl.IBM_API_PROCESS, params);
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
	      	// 流程数据
	      	processInstance.setInsData(dataInfo);
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
		      	// 任务类型 
		      	taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
		      	taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		      	taskInstance.setTaskTitle(String.valueOf(jsonObject.get("name")));
		      	// 发起流程上一环节默认 是自己
		      	taskInstance.setTaskPreviousUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		      	SysUser sysUser = new SysUser();
		      	sysUser.setUserId(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		      	SysUser sysUserName = sysUserMapper.findById(sysUser);
		      	taskInstance.setTaskPreviousUsrUsername(sysUserName.getUserName());
		      	// 任务数据 
		      	taskInstance.setTaskData(dataInfo);
		      	dhTaskInstanceService.insertTask(taskInstance);
				// 流程发起结束后设置变量
				BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
				bpmActivityMeta.setProAppId(proAppId);
				bpmActivityMeta.setPoId(proUid);
				bpmActivityMeta.setSnapshotId(verUid);
		      	List<BpmActivityMeta> bpmActivityMeta2 = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
		      	for (BpmActivityMeta bpmActivityMeta3 : bpmActivityMeta2) {
		      		// 找到一个环节顺序号
		      		if(bpmActivityMeta3.getSortNum() == 1) {
		      			System.err.println(bpmActivityMeta3.getActivityId());
		      			dhTaskInstanceService.queryTaskSetVariable(bpmActivityMeta3.getActivityId(), String.valueOf(jsonObject.get("tkiid")));	
		      		}
				}
				// 默认发起 提交第一个环节
				dhTaskInstanceService.perform(String.valueOf(jsonObject.get("tkiid")));
			}
			log.info("发起流程结束......");
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

}
