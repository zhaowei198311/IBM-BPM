/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.IBMApiUrl;
import com.desmart.common.constant.RouteStatus;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.util.JsonUtil;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhApprovalOpinionService;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.MenusService;
import com.desmart.desmartportal.service.SysHolidayService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * Title: TaskInstanceServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年5月11日
 */
@Service
public class DhTaskInstanceServiceImpl implements DhTaskInstanceService {

	private Logger log = Logger.getLogger(DhTaskInstanceServiceImpl.class);

	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;

	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;

	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;

	@Autowired
	private DhActivityConfMapper dhActivityConfMapper;

	@Autowired
	private DhProcessFormService dhProcessFormService;

	@Autowired
	private MenusService menusService;

	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;

	@Autowired
	private SysHolidayService sysDateService;

	@Autowired
	private BpmFormManageService bpmFormManageService;

	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;

	@Autowired
	private DhApprovalOpinionService dhapprovalOpinionServiceImpl;

	@Autowired
	private DhRouteService dhRouteServiceImpl;
	
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private DhStepService dhStepService;
	
	
	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectAllTask(DhTaskInstance taskInstance, Integer pageNum,
			Integer pageSize) {
		log.info("查询taskInstance开始......");
		try {
			// 查询用户
			/*
			 * // 根据用户id 查询用户信息 SysUser sysUser = new SysUser();
			 * sysUser.setUserId(String.valueOf(SecurityUtils.getSubject().getSession().
			 * getAttribute(Const.CURRENT_USER))); SysUser sysUser2 =
			 * sysUserMapper.findById(sysUser); sysUser2.getUserName();
			 */
			PageHelper.startPage(pageNum, pageSize);
			List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectAllTask(taskInstance);
			PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询taskInstance结束......");
		return null;
	}

	/**
	 * 根据实例任务id 查询任务
	 */
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectByPrimaryKey(String taskUid, Integer pageNum,
			Integer pageSize) {
		log.info("");
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return null;
	}

	/**
	 * 根据实例任务id 修改任务
	 */
	@Override
	public int updateByPrimaryKey(String taskUid) {
		log.info("");
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}

	/**
	 * 根据实例任务id 删除任务
	 */
	@Override
	public int deleteByPrimaryKey(String taskUid) {
		log.info("");
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}

	/**
	 * 新增任务实例
	 */
	@Override
	public void insertTask(DhTaskInstance taskInstance) {
		log.info("");
		try {
			dhTaskInstanceMapper.insertTask(taskInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
	}

	/*
	 * 根据用户id 查询 有多少代办任务 js前台定时 任务 每隔一分钟 去 查询一次用户待办
	 */
	@Override
	public int selectByusrUid(String usrUid) {
		log.info("======定时查询用户有多少代办=======");
		return dhTaskInstanceMapper.selectByusrUid(usrUid);
	}

	/*
	 * 根据用户id 查询 他有哪些流程
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectTaskByUser(DhTaskInstance taskInstance,
			Integer pageNum, Integer pageSize) {
		log.info("根据用户id查询有哪些流程.开始......");
		List<DhProcessInstance> resultList = new ArrayList<DhProcessInstance>();
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhTaskInstance> taskInstanceList = dhTaskInstanceMapper.selectAllTask(taskInstance);// 根据userId查询taskList
			if (taskInstanceList.size() > 0) {
				for (DhTaskInstance taskInstance1 : taskInstanceList) {
					DhProcessInstance processInstance = new DhProcessInstance();
					int id = Integer.parseInt(DhProcessInstance.STATUS_ACTIVE);
					processInstance.setInsUid(taskInstance1.getInsUid());// 获取taskList里的insUid
					processInstance.setInsStatusId(id);
					List<DhProcessInstance> processInstanceList = dhProcessInstanceMapper
							.selectAllProcess(processInstance);// 根据instUid查询processList
					for (DhProcessInstance p : processInstanceList) {
						resultList.add(p);
					}
				}
			}
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public int getMaxTaskIdInDb() {
		return dhTaskInstanceMapper.getMaxSynNumber();
	}

	public int insertBatch(List<DhTaskInstance> list) {
		return dhTaskInstanceMapper.insertBatch(list);
	}

	/**
	 * 寻找 并 设置流程变量 更具activityid 去 找 meta下的 LoopType 知道是简单循环还是多循环 (3种方式)要判断 if
	 * loopType 为 none 单 实例 情况下 就不需要 activityCofg 表下的 会签变量数据(sign_Count_varname)
	 * else 就需要 会签变量数据 activityCofg 下的 分派变量名称 是 必须要的
	 * 
	 * @param activityId
	 *            环节关联id
	 * @param tkkid
	 *            任务实例id (引擎)
	 */
	@Override
	public ServerResponse queryTaskSetVariable(String activityId, String tkkid) {

		log.info("寻找流程变量开始......");
		try {
			BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
			String LoopType = bpmActivityMeta.getLoopType();
			log.info("循环类型:" + LoopType);
			HttpReturnStatus result = new HttpReturnStatus();
			HttpClientUtils httpClientUtils = new HttpClientUtils();
			Map<String, Object> params = new HashMap<>();
			params.put("action", "setData");
			if ("none".equals(LoopType)) {
				// 单实例循环
				DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(bpmActivityMeta.getActivityId());
				// 获取变量并赋值 {"pubBo":{"nextOwners_0":["XXXXXXXXX"]}}
				String variable = dhActivityConf.getActcAssignVariable();
				String jsonstr = "{\"pubBo\":{\"" + variable + "\":[\"00011178\"],\"creatorId\":\"00011178\"}}";
				// JSONObject jsonObj = JSONObject. .parseObject(jsonstr);
				params.put("params", jsonstr);
				result = httpClientUtils.checkApiLogin("put", IBMApiUrl.IBM_API_TASK + tkkid, params);
				log.info("掉用API状态码:" + result.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("寻找流程变量结束......");
		return null;
	}

	/* 
	 *
	 */
	@Override
	@Transactional
	public ServerResponse perform(String data) {
		log.info("完成任务开始......");
		try {

			if (StringUtils.isBlank(data)) {
				return ServerResponse.createByErrorMessage("缺少必要参数");
			}

			JSONObject jsonBody = JSONObject.parseObject(data);
			JSONObject taskData = JSONObject.parseObject(String.valueOf(jsonBody.get("taskData")));
			JSONObject formData = JSONObject.parseObject(String.valueOf(jsonBody.get("formData")));
			JSONObject routeData = JSONObject.parseObject(String.valueOf(jsonBody.get("routeData")));
			String userId = routeData.getString("userUid").substring(0, 8);
			Integer taskId = Integer.parseInt(taskData.getString("taskId"));
			String taskUid = taskData.getString("taskUid");
			// 根据任务标识和用户 去查询流程 实例
			DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
			if (dhTaskInstance==null) {
				return ServerResponse.createByErrorMessage("当前任务不存在!");
			}
			if ("12".equals(dhTaskInstance.getTaskStatus())) {// 检查任务是否重复提交
				String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
				if (checkTaskUser(dhTaskInstance, currentUserUid)) {// 检查任务是否有权限提交

					// 装配处理人信息
					CommonBusinessObject pubBo = new CommonBusinessObject();
					ServerResponse<CommonBusinessObject> serverResponse = dhRouteServiceImpl
							.assembleCommonBusinessObject(pubBo, JSONObject.parseArray(routeData.toJSONString()));
					if (!serverResponse.isSuccess()) {
						return serverResponse;
					}
					/*
					 * List<String> userList = new ArrayList<>(); userList.add(userId);
					 * pubBo.setNextOwners_0(userList);
					 */

						JSONObject approvalData = JSONObject.parseObject(String.valueOf(jsonBody.get("approvalData")));// 获取审批信息
						/*String insUid = approvalData.getString("insUid");
						String apr_taskUid = approvalData.getString("taskUid");// 存储的是环节id->activity_id*/
						String aprOpiComment = approvalData.getString("aprOpiComment");
						String aprStatus = approvalData.getString("aprStatus");
						String insUid = dhTaskInstance.getInsUid();
						DhProcessInstance dhProcessInstance = dhProcessInstanceMapper
								.selectByPrimaryKey(insUid);
						String proAppId = dhProcessInstance.getProAppId();
						String activityBpdId = dhTaskInstance.getActivityBpdId();
						String snapshotId = dhProcessInstance.getProVerUid();
						String bpdId = dhProcessInstance.getProUid();
						BpmActivityMeta bpmActivityMeta = bpmActivityMetaServiceImpl.getBpmActivityMeta(proAppId, activityBpdId, snapshotId, bpdId);
						
						
						// 审批信息
						DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
						dhApprovalOpinion.setInsUid(insUid);
						dhApprovalOpinion.setTaskUid(bpmActivityMeta.getActivityId());
						dhApprovalOpinion.setAprOpiComment(aprOpiComment);
						dhApprovalOpinion.setAprStatus(aprStatus);
						ServerResponse serverResponse2 = dhapprovalOpinionServiceImpl
								.insertDhApprovalOpinion(dhApprovalOpinion);
						if (!serverResponse2.isSuccess()) {
							return serverResponse2;
						}
						
						// 整合formdata
						JSONObject formJson = new JSONObject();
						if (StringUtils.isNotBlank(formData.toJSONString())) {
							formJson = FormDataUtil.formDataCombine(formData,
									JSONObject.parseObject(dhProcessInstance.getInsData()));
						}
						dhProcessInstance.setInsUpdateDate(DateUtil.format(new Date()));
						dhProcessInstance.setInsData(formJson.toJSONString());
						//判断流程是否结束
						List<BpmActivityMeta> nextBpmActivityMetas = dhRouteServiceImpl.getNextActivities(bpmActivityMeta, formData);
						dhRouteServiceImpl.updateGatewayRouteResult(bpmActivityMeta, dhProcessInstance.getInsId(), formData);
						
						if(nextBpmActivityMetas==null || nextBpmActivityMetas.size()==0) {
							dhProcessInstance.setInsStatus(DhProcessInstance.STATUS_COMPLETED);
							dhProcessInstance.setInsStatusId(DhProcessInstance.STATUS_ID_COMPLETED);
						}
						//修改流程实例信息
						dhProcessInstanceMapper.updateByPrimaryKeySelective(dhProcessInstance);

						// 任务完成后 保存到流转信息表里面
						DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
						dhRoutingRecord
								.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
						dhRoutingRecord.setInsUid(dhTaskInstance.getInsUid());
						dhRoutingRecord.setActivityName(dhTaskInstance.getTaskTitle());
						dhRoutingRecord.setRouteType(RouteStatus.ROUTE_SUBMITTASK);
						dhRoutingRecord.setUserUid(userId);
						dhRoutingRecord.setActivityId(bpmActivityMeta.getActivityId());
						if(nextBpmActivityMetas!=null&&nextBpmActivityMetas.size()>0) {
							setActivityToValues(nextBpmActivityMetas,dhRoutingRecord);
						}
						dhRoutingRecordMapper.insert(dhRoutingRecord);
						// 修改当前任务实例状态为已完成
						if(dhTaskInstanceMapper.updateTaskStatusByTaskUid(taskUid)>0) {
						//如果任务为类型为normal，则将其它相同任务id的任务废弃
							if(DhTaskInstance.TYPE_NORMAL.equals(dhTaskInstance.getTaskType())) {
								dhTaskInstanceMapper.updateOtherTaskStatusByTaskId(taskUid,taskId, DhTaskInstance.STATUS_DISCARD);
							}
						}
						BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
						BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
						
						// 调用方法完成任务
						Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(taskId, pubBo);
						
						if (resultMap.get("commitTaskResult").getCode() == 200) {// 任务完成，修改数据信息
							log.info("完成任务结束......");
							return ServerResponse.createBySuccess();
						}else {
							log.info("任务完成失败！");
							throw new RuntimeException("任务完成失败！");
						}
				} else {
					return ServerResponse.createByErrorMessage("提交失败，用户权限验证失败!");
				}
			} else {
				return ServerResponse.createByErrorMessage("请不要重复提交!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("完成任务出现异常......");
			return ServerResponse.createByError();
		}
	}

	/**
	 *	设置流转到的节点activityId  逗号分隔
	 * @param nextBpmActivityMetas
	 * @param dhRoutingRecord
	 */
	private void setActivityToValues(List<BpmActivityMeta> nextBpmActivityMetas, DhRoutingRecord dhRoutingRecord) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < nextBpmActivityMetas.size(); i++) {
			if(i==nextBpmActivityMetas.size()-1) {
				stringBuffer.append(nextBpmActivityMetas.get(i));
			}else {
				stringBuffer.append(nextBpmActivityMetas.get(i)+",");
			}
		}
	}

	/**
	 * 判断用户是否有权限提交审批
	 * 
	 * @param currDhTaskInstance
	 * @param currentUser
	 * @return
	 */
	private boolean checkTaskUser(DhTaskInstance currDhTaskInstance, String currentUser) {
		if (currDhTaskInstance.getTaskDelegateUser() != null && !"".equals(currDhTaskInstance.getTaskDelegateUser())) {
			if (currDhTaskInstance.getTaskDelegateUser().equals(currentUser)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (currDhTaskInstance.getUsrUid().equals(currentUser)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isTaskExists(int taskId) {
		if (taskId == 0) {
			return false;
		}
		return dhTaskInstanceMapper.countByTaskId(taskId) > 0;
	}
	
	public ServerResponse<Map<String, Object>> toDealTask(String taskUid) {
	    Map<String, Object> resultMap = new HashMap<>();
	    
	    if (StringUtils.isBlank(taskUid)) {
	        return ServerResponse.createByErrorMessage("缺少必要的参数");
	    }
	    DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
	    if (dhTaskInstance == null || !DhTaskInstance.STATUS_RECEIVED.equals(dhTaskInstance.getTaskStatus())) {
	        return ServerResponse.createByErrorMessage("任务不存在或任务状态异常");
	    }
	    DhProcessInstance dhprocessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
	    if (dhprocessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }
	    resultMap.put("processInstance", dhprocessInstance);
	    
	    // 获得当前环节
	    BpmActivityMeta currMeta = bpmActivityMetaMapper.queryByFourElement(dhprocessInstance.getProAppId(), dhprocessInstance.getProUid(), 
	            dhprocessInstance.getProVerUid(), dhTaskInstance.getActivityBpdId());
	    
	    List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(currMeta, "default");
	    DhStep formStep = getFirstFormStepOfStepList(steps);
	    if (formStep == null) {
            return ServerResponse.createByErrorMessage("找不到表单步骤");
        }
        resultMap.put("dhStep", formStep);
        // 获得表单文件内容
        ServerResponse formResponse = bpmFormManageService.getFormFileByFormUid(formStep.getStepObjectUid());
        if (!formResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("获得表单数据失败");
        }
        resultMap.put("formHtml", formResponse.getData());
	    resultMap.put("taskInstance", dhTaskInstance);
	    return ServerResponse.createBySuccess(resultMap);
	    
	    
	}
	

	@Override
	public List<DhTaskInstance> selectByInsUidAndTaskTypeCondition(String insUid) {
		return dhTaskInstanceMapper.selectByInsUidAndTaskTypeCondition(insUid);
	}

	/**
	 * 已办任务
	 */
	@Override
	public int selectByusrUidFinsh(String usrUid) {
		log.info("已办任务总数查询开始......");
		try {
			return dhTaskInstanceMapper.selectByusrUidFinsh(usrUid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("已办任务总数查询结束......");
		return 0;
	}

	/**
	 * 根据任务实例查询任务数据和流程数据
	 */
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectTaskAndProcessInfo(DhTaskInstance taskInstance,
			Integer pageNum, Integer pageSize) {
		log.info("根据任务实例查询任务数据和流程数据 Start......");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectTaskAndProcessInfo(taskInstance);
			// 返回流程创建者 名称 而不是id 这里做处理 好直接返给前台
			PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据任务实例查询任务数据和流程数据 END......");
		return null;
	}

	@Override
	public ServerResponse<?> queryProgressBar(String proUid, String proVerUid, String proAppId, String taskUid) {
		// 创建时间
		Date createDate = new Date();
		// 时间个数
		Double timeAmount = null;
		// 时间单位
		String timeType = "";
		// 审批剩余时间
		int hour = 0;
		// 百分比
		int percent = 0;
		// 根据taskUid查询单个DH_TASK_INSTANCE对象
		DhTaskInstance dti = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		createDate = dti.getTaskInitDate();
		// 根据proUid，proVerUid，proAppId，activityBpdId查询单个BPM_ACTIVITY_META对象
		String activityBpdId = dti.getActivityBpdId();
		BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
		bpmActivityMeta.setBpdId(proUid);
		bpmActivityMeta.setSnapshotId(proVerUid);
		bpmActivityMeta.setProAppId(proAppId);
		bpmActivityMeta.setActivityBpdId(activityBpdId);
		List<BpmActivityMeta> bpmActivityMeta_1 = bpmActivityMetaMapper
				.queryByBpmActivityMetaSelective(bpmActivityMeta);
		for (BpmActivityMeta bam : bpmActivityMeta_1) {
			// DH_ACTIVITY_CONF对象
			DhActivityConf dhActivityConf = bam.getDhActivityConf();
			timeAmount = dhActivityConf.getActcTime();
			timeType = dhActivityConf.getActcTimeunit();
		}
		
		// 审批最后日期
		Date lastDate = sysDateService.calculateDueDate(createDate, timeAmount, timeType);
		if (timeAmount == null) {
			timeAmount = 24.0;
		} else {
			if (DhActivityConf.TIME_UNIT_DAY.equals(timeType)) {
				timeAmount = timeAmount * 24;
			}
			if (DhActivityConf.TIME_UNIT_MONTH.equals(timeType)) {
				timeAmount = timeAmount * 30 * 24;
			}
		}
		// 最大剩余时间
		long remainingTime = lastDate.getTime() - createDate.getTime();
		hour = (int) (remainingTime / (1000 * 60 * 60));
		// 如果最大剩余时间大于配置时间，则百分比为0，剩余时间为配置时间
		if (hour > timeAmount) {
			hour = timeAmount.intValue();
		} else {
			hour = (int) (lastDate.getTime() - new Date().getTime()) / (1000 * 60 * 60);
			// 剩余时间百分比
			long reTime = new Date().getTime() - createDate.getTime();
			percent = (int) (((double) reTime / (1000 * 60 * 60)) / timeAmount * 100);
		}
		Map<String, Object> map = new HashMap<>();
		if (hour < 0) {
			hour = -1;
			percent = 100;
		}
		map.put("percent", percent);
		map.put("hour", hour);
		return ServerResponse.createBySuccess(map);
	}

	@Override
	public ServerResponse<?> addSure(DhTaskInstance dhTaskInstance) {
		// 说明 dhTaskInstance.getActivityBpdId() 实际值为 activityId
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(dhTaskInstance.getActivityBpdId());
		dhTaskInstance.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
		dhTaskInstance.setTaskType("normal");
		dhTaskInstance.setTaskStatus("12");
		dhTaskInstance.setTaskTitle(bpmActivityMeta.getActivityName());
		dhTaskInstance.setTaskInitDate(new Date());
		String[] usrUid = dhTaskInstance.getUsrUid().split(";");
		SysUser sysUser = new SysUser();
		for (String string : usrUid) {
			sysUser.setUserName(string);
			List<SysUser> sysUserList = sysUserMapper.selectAll(sysUser);
			dhTaskInstance.setTaskUid("task_instance:"+UUID.randomUUID());
			dhTaskInstance.setUsrUid(sysUserList.get(0).getUserId());
			dhTaskInstanceMapper.insertTask(dhTaskInstance);
		}
		return ServerResponse.createBySuccess();
	}
    private DhStep getFirstFormStepOfStepList(List<DhStep> stepList) {
        for (DhStep dhStep : stepList) {
            if (DhStep.TYPE_FORM.equals(dhStep.getStepType())) {
                return dhStep;
            }
        }
        return null;
    }
}
