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

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ResponseCode;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.ProcessInstanceDao;
import com.desmart.desmartportal.entity.ProcessInstance;
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
			JSONObject jsonBody2 = JSONObject.parseObject(jsonBody.get("data").toString());
	      	ProcessInstance processInstance = new ProcessInstance();
	      	processInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
	      	processInstance.setInsTitle(jsonBody2.get("processAppName").toString());
	      	processInstance.setInsId(Integer.parseInt(jsonBody2.get("piid").toString()));
	      	processInstance.setInsParent("");
	      	processInstance.setInsStatus(jsonBody2.get("state").toString());
	      	processInstance.setInsStatusId(0);
	      	processInstance.setProAppId(jsonBody2.get("processAppID").toString());
	      	processInstance.setProUid(jsonBody2.get("processTemplateID").toString());
	      	processInstance.setProVerUid(jsonBody2.get("snapshotID").toString());
	      	processInstance.setInsInitUser(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
	      	processInstance.setInsData(result.getMsg());
	      	processInstanceService.insertProcess(processInstance);
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

}
