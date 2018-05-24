/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.RouteStatus;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.RestUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: ProcessInstanceServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月11日  
*/
@Service
public class DhProcessInstanceServiceImpl implements DhProcessInstanceService {
	
	private Logger log = Logger.getLogger(DhProcessInstanceServiceImpl.class);
	
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	
	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance, Integer pageNum, Integer pageSize) {
		log.info("查询所有process开始");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhProcessInstance> resultList = dhProcessInstanceMapper.selectAllProcess(processInstance);
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询所有process结束");
		return null;
	}
	
	/**
	 * 根据流程实例主键 查询流程
	 */
	@Override
	public DhProcessInstance selectByPrimaryKey(String insUid) {
		log.info("");
		try {
			return dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return null;
	}
	
	/**
	 * 根据流程实例主键 修改流程
	 */
	@Override
	public int updateByPrimaryKey(String insUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}
	
	/**
	 * 根据流程实例主键 删除流程
	 */
	@Override
	public int deleteByPrimaryKey(String insUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}
	
	/**
	 * 添加新的流程实例
	 */
	@Override
	public void insertProcess(DhProcessInstance processInstance) {
		log.info("添加新的流程实例 Start...");
		try {
			if(processInstance != null) {
				dhProcessInstanceMapper.insertProcess(processInstance);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("添加新的流程实例 End...");
	}

	/**
	 * 根据用户id 以及类型 查询用户所用有的流程 按条件 查询 
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectProcessByUserAndType(DhProcessInstance processInstance,Integer pageNum, Integer pageSize) {
		log.info("通过用户查询流程实例 Start...");
		List <DhProcessInstance> resultList = new ArrayList<DhProcessInstance>();
		try {
			PageHelper.startPage(pageNum, pageSize);
			DhTaskInstance taskInstance = new DhTaskInstance();
			taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
			List <DhTaskInstance> taskInstanceList = dhTaskInstanceMapper.selectAllTask(taskInstance);
			for (DhTaskInstance dhTaskInstance1 : taskInstanceList) {
				// 用户id
				processInstance.setInsUid(dhTaskInstance1.getInsUid());
				List <DhProcessInstance> processInstanceList = dhProcessInstanceMapper.selectAllProcess(processInstance);
				for (DhProcessInstance dhProcessInstance : processInstanceList) {
					resultList.add(dhProcessInstance);
				}
			}
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("通过用户查询流程实例 End...");
		return null;
	}

	/* 
	 * 模糊按条件查询
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,Integer pageNum, Integer pageSize) {
		log.info("模糊查询流程实例 Start...");
		List <DhProcessInstance> resultList = new ArrayList<DhProcessInstance>();
		try {
			PageHelper.startPage(pageNum, pageSize);
			DhTaskInstance taskInstance = new DhTaskInstance();
			taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
			List <DhTaskInstance> taskInstanceList = dhTaskInstanceMapper.selectAllTask(taskInstance);
			for (DhTaskInstance dhTaskInstance1 : taskInstanceList) {
				// 用户id
				paramMap.put("insUid", dhTaskInstance1.getInsUid());
				List <DhProcessInstance> processInstanceList = dhProcessInstanceMapper.queryByStausOrTitle(paramMap);
				for (DhProcessInstance dhProcessInstance : processInstanceList) {
					resultList.add(dhProcessInstance);
				}
			}
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("模糊查询流程实例 End...");
		return null;
	}
	
	@Override
	@Transactional
	public ServerResponse startProcess(String proUid, String proAppId, String verUid, String dataInfo,
			String approval) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(verUid)) {
            return ServerResponse.createByError();
        }
	    log.info("发起流程开始......");
		String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		HttpReturnStatus result = new HttpReturnStatus();
		
		// 掉用API 发起一个流程
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		CommonBusinessObject pubBo = new CommonBusinessObject();
		pubBo.setCreatorId(currentUserUid);
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		result = bpmProcessUtil.startProcess(proAppId, proUid, verUid, pubBo);
		log.info("掉用API状态码:" + result.getCode());
		
		
		// 如果获取API成功 将返回过来的流程数据 保存到 平台
		if (result.getCode() == 200) {
			// 保存数据信息
			log.info("掉用API返回过来的数据信息:" + result.getMsg());
			JSONObject jsonBody = JSONObject.parseObject(result.getMsg());
			JSONObject jsonBody2 = JSONObject.parseObject(String.valueOf(jsonBody.get("data")));
			JSONArray jsonBody3 = JSONArray.parseArray(String.valueOf(jsonBody2.get("tasks")));
			// 将流程数据 保存到 当前流程实例数据库中
			String InsUid = EntityIdPrefix.DH_PROCESS_INSTANCE + String.valueOf(UUID.randomUUID());
			// 查询用户信息
			SysUser sysUser = sysUserMapper.queryByPrimaryKey(String.valueOf(currentUserUid));
			// 
			DhProcessInstance processInstance = new DhProcessInstance();
			processInstance.setInsUid(InsUid);
			processInstance.setInsTitle(String.valueOf(jsonBody2.get("processAppName")));
			processInstance.setInsId(Integer.parseInt(String.valueOf(jsonBody2.get("piid"))));
			processInstance.setInsParent("");
			if (String.valueOf(jsonBody2.get("executionState")).equals("Active")) {
				processInstance.setInsStatus(String.valueOf(jsonBody2.get("executionState")));
				processInstance.setInsStatusId(Integer.parseInt(DhProcessInstance.STATUS_ACTIVE));
			}
			processInstance.setProAppId(String.valueOf(jsonBody2.get("processAppID")));
			processInstance.setProUid(String.valueOf(jsonBody2.get("processTemplateID")));
			processInstance.setProVerUid(String.valueOf(jsonBody2.get("snapshotID")));

			processInstance.setCompanyNumber(sysUser.getCompanynumber());
			processInstance.setInsInitUser(String.valueOf(currentUserUid));
			processInstance.setInsInitUser(currentUserUid);
			// 流程数据
			processInstance.setInsData(dataInfo);
			dhProcessInstanceMapper.insertProcess(processInstance);
			// 将任务数据 保存到 当前任务实例数据库中
			DhTaskInstance taskInstance = new DhTaskInstance();
			taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + String.valueOf(UUID.randomUUID()));
			taskInstance.setInsUid(InsUid);
			for (int i = 0; i < jsonBody3.size(); i++) {
				JSONObject jsonObject = jsonBody3.getJSONObject(i);
				taskInstance.setTaskId(Integer.parseInt(String.valueOf(jsonObject.get("tkiid"))));
				// 处理人
				taskInstance.setUsrUid(approval);
				taskInstance.setActivityBpdId(String.valueOf(jsonObject.get("flowObjectID")));
				// 任务类型
				taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
				taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
				taskInstance.setTaskTitle(String.valueOf(jsonObject.get("name")));
				// 发起流程上一环节默认 是自己
				taskInstance.setTaskPreviousUsrUid(
						String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
				taskInstance.setTaskPreviousUsrUsername(sysUser.getUserName());
				taskInstance.setTaskPreviousUsrUid(currentUserUid);
				SysUser sysUserName = sysUserMapper.queryByPrimaryKey(currentUserUid);
				taskInstance.setTaskPreviousUsrUsername(sysUserName.getUserName());
				// 任务数据
				taskInstance.setTaskData(dataInfo);
				
				// 流程发起结束后设置变量
				BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
				bpmActivityMeta.setProAppId(proAppId);
				bpmActivityMeta.setPoId(proUid);
				bpmActivityMeta.setSnapshotId(verUid);
				List<BpmActivityMeta> bpmActivityMeta2 = bpmActivityMetaMapper
						.queryByBpmActivityMetaSelective(bpmActivityMeta);
				// 环节名称 用于保存流转信息
				String activityName = "";
				for (BpmActivityMeta bpmActivityMeta3 : bpmActivityMeta2) {
					// 找到一个环节顺序号
					if (bpmActivityMeta3.getSortNum() == 1) {
						log.info("第一个环节ID:" + bpmActivityMeta3.getActivityId());
						dhTaskInstanceService.queryTaskSetVariable(bpmActivityMeta3.getActivityId(),
								String.valueOf(jsonObject.get("tkiid")));
						BpmActivityMeta bpmActivityMeta4 = bpmActivityMetaMapper
								.queryByPrimaryKey(bpmActivityMeta3.getActivityId());
						log.info("第一个环节ID:" + bpmActivityMeta4.getActivityName());
						activityName = bpmActivityMeta4.getActivityName();
					}
				}
				// 默认发起 提交第一个环节
				ServerResponse serverResponse = dhTaskInstanceService.perform(String.valueOf(jsonObject.get("tkiid")),String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
				if (!serverResponse.isSuccess()) {
				    throw new PlatformException("提交第一个任务失败");
				}
				if (dhTaskInstanceService.isTaskExists(taskInstance.getTaskId())) {
                    // 更新任务状态为完成
                    dhTaskInstanceMapper.updateTaskStatusByTaskId(taskInstance.getTaskId(), DhTaskInstance.STATUS_CLOSED);
                } else {
                    dhTaskInstanceService.insertTask(taskInstance);
                }
				// 任务完成后 保存到流转信息表里面
				DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
				dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
				dhRoutingRecord.setInsUid(InsUid);
				dhRoutingRecord.setActivityName(activityName);
				dhRoutingRecord.setRouteType(RouteStatus.ROUTE_STARTPROCESS);
				// 发起流程 第一个流转环节信息 的 用户id 是 自己
				dhRoutingRecord.setUserUid(
						String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
				dhRoutingRecordMapper.insert(dhRoutingRecord);
			}
			log.info("发起流程结束......");
			return ServerResponse.createBySuccess();
		} else {
			return ServerResponse.createByError();
		}
	}

	/**
	 * 查看流程图
	 */
	@Override
	public String viewProcessImage(String insId) {
		try {
			log.info("流程图查看......");
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			RestUtil restUtil = new RestUtil(bpmGlobalConfig);
			HttpClientUtils httpClientUtils = new HttpClientUtils();
			String result = httpClientUtils.checkLoginIbm("http://10.0.4.201:9080/rest/bpm/wle/v1/visual/processModel/instances?instanceIds=["+insId+"]&showCurrentActivites=true&showExecutionPath=true&showNote=true&showColor=true&image=true");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
