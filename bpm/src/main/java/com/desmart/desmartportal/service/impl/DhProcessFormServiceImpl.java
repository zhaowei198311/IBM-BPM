/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.desmart.desmartportal.service.DhRouteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartportal.service.DhProcessFormService;

/**  
* <p>Title: ProcessFormServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月10日  
*/
@Service
public class DhProcessFormServiceImpl implements DhProcessFormService{
	
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	
	@Autowired
	private DhActivityConfMapper dhActivityConfMapper;
	
	@Autowired
	private DhStepMapper dhStepMapper;
	
	@Autowired
	private DhRouteService dhRouteService;
	
	private Logger log = Logger.getLogger(DhProcessFormServiceImpl.class);
	
	/**
	 * 查找表单
	 * @param proAppId 流程应用库id
	 * @param proUid 流程id	
	 * @param verUid 版本id
	 * 
	 * 1.首先获取 流程元数据表下 的 流程图上元素id
	 * 2.然后通过元素id 去 查找bpm流程下的 一些 有用配置信息
	 * 3.流程图上元素id 获取 步骤表里面的 步骤id  表单id
	 * 4. 通过步骤id  去参数表里面获取 表单字段
	 * 1个环节对应多个步骤 
	 */
	@Override
	public Map<String,Object> queryProcessForm(String proAppId, String proUid, String verUid) {
		log.info("获取表单 Start...");
		Map<String,Object> stepMap = new HashMap<>();
		try {
			// 找到第一个环节的 流程bpdId 和 主键id
			ServerResponse<BpmActivityMeta> bpmActivityMeta = dhRouteService.getActualFirstUserTaskNodeOfMainProcess(proAppId, proUid, verUid);
			BpmActivityMeta bpmActivityMeta2 = bpmActivityMeta.getData();
			String activityBpdId = bpmActivityMeta2.getActivityBpdId();
			String activityId = bpmActivityMeta2.getActivityId();
			log.info("流程元素id:"+activityBpdId);
			log.info("Bpm主键id:"+activityId);
			// 通过主键id  获取 配置信息
			DhActivityConf  dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
			log.info("流程配置主键id:"+dhActivityConf.getActcUid());
			// 通过activityBpdId 找到步骤
			DhStep stepSelective = new DhStep();
			stepSelective.setActivityBpdId(activityBpdId);
			stepSelective.setStepType(DhStep.TYPE_FORM);
			List<DhStep> dhStepList = dhStepMapper.listBySelective(stepSelective);
			if (dhStepList.size() == 0) {
			    
			}
			DhStep currentStep = dhStepList.get(0);
			log.info("表单id:"+currentStep.getStepObjectUid());
			stepMap.put("activityBpdId", activityBpdId);
			stepMap.put("activityId", activityId);
			stepMap.put("actcUid", dhActivityConf.getActcUid());
			stepMap.put("formId", currentStep.getStepObjectUid());
			stepMap.put("stepId", currentStep.getStepUid());
			return stepMap;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
