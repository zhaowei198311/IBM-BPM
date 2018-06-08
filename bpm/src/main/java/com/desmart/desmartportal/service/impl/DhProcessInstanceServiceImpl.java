/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.*;

import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionObjType;
import com.desmart.desmartportal.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.common.util.FormDataUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.common.util.RestUtil;
import com.desmart.common.util.ExecutionTreeUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhActivityRejectMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhActivityReject;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.service.DhApprovalOpinionService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * Title: ProcessInstanceServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
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
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	@Autowired
	private SysUserDepartmentService sysUserDepartmentService;
	@Autowired
	private DhStepService dhStepService;
	@Autowired
	private BpmFormManageService bpmFormManageService;
	@Autowired
	private DhDraftsMapper dhDraftsMapper;

	@Autowired
	private DhActivityConfMapper dhActivityConfMapper;
	@Autowired
	private DhRouteService dhRouteService;
	@Autowired
	private BpmFormFieldService bpmFormFieldService;
	@Autowired
	private DhApprovalOpinionService dhApprovalOpinionService;
	@Autowired
	private DhActivityRejectMapper dhActivityRejectMapper;
	@Autowired
	private DhObjectPermissionMapper dhObjectPermissionMapper;
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	@Autowired
	private SysTeamMemberMapper sysTeamMemberMapper;

	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance,
			Integer pageNum, Integer pageSize) {
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
	public ServerResponse selectByPrimaryKey(String insUid) {
		log.info("");
		DhProcessInstance proInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		if (null == proInstance) {
			throw new PlatformException("找不到目标流程实例");
		}
		log.info("");
		return ServerResponse.createBySuccess(proInstance);
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
			if (processInstance != null) {
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
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectProcessByUserAndType(
			DhProcessInstance processInstance, Integer pageNum, Integer pageSize) {
		log.info("通过用户查询流程实例 Start...");
		List<DhProcessInstance> resultList = new ArrayList<DhProcessInstance>();
		try {
			PageHelper.startPage(pageNum, pageSize);
			DhTaskInstance taskInstance = new DhTaskInstance();
			taskInstance.setUsrUid(
					String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
			List<DhTaskInstance> taskInstanceList = dhTaskInstanceMapper.selectAllTask(taskInstance);
			for (DhTaskInstance dhTaskInstance1 : taskInstanceList) {
				// 用户id
				processInstance.setInsUid(dhTaskInstance1.getInsUid());
				List<DhProcessInstance> processInstanceList = dhProcessInstanceMapper.selectAllProcess(processInstance);
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
	public ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,
			Integer pageNum, Integer pageSize) {
		log.info("模糊查询流程实例 Start...");
		List<DhProcessInstance> resultList = new ArrayList<DhProcessInstance>();
		try {
			PageHelper.startPage(pageNum, pageSize);
			DhTaskInstance taskInstance = new DhTaskInstance();
			taskInstance.setUsrUid(
					String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
			List<DhTaskInstance> taskInstanceList = dhTaskInstanceMapper.selectAllTask(taskInstance);
			for (DhTaskInstance dhTaskInstance1 : taskInstanceList) {
				// 用户id
				paramMap.put("insUid", dhTaskInstance1.getInsUid());
				List<DhProcessInstance> processInstanceList = dhProcessInstanceMapper.queryByStausOrTitle(paramMap);
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
	public ServerResponse startProcess(String data) {
		if (StringUtils.isBlank(data)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		JSONObject dataJson = JSONObject.parseObject(data);
		JSONObject formDataFromTask = (JSONObject) dataJson.get("formData");
		JSONArray routeData = dataJson.getJSONArray("routeData");
		JSONObject processData = (JSONObject) dataJson.get("processData");
		String insTitle = processData.getString("insTitle");
		String companyNumber = processData.getString("companyNumber");
		String departNo = processData.getString("departNo");
		;
		String insUid = processData.getString("insUid");
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		if (dhProcessInstance == null || DhProcessInstance.STATUS_ID_DRAFT != dhProcessInstance.getInsStatusId()) {
			return ServerResponse.createByErrorMessage("流程实例状态异常");
		}
		String proAppId = dhProcessInstance.getProAppId();
		String proUid = dhProcessInstance.getProUid();
		String proVerUid = dhProcessInstance.getProVerUid();
		String insDataStr = dhProcessInstance.getInsData();
		// 混合提交的表单内容和流程实例中的表单内容
		JSONObject insData = JSON.parseObject(insDataStr);
		JSONObject formDataFromIns = insData.getJSONObject("formData");
		JSONObject mergedFromData = FormDataUtil.formDataCombine(formDataFromTask, formDataFromIns);

		DhProcessDefinition startableDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId,
				proUid);
		if (!dhProcessInstance.getProVerUid().equals(startableDefinition.getProVerUid())) {
			ServerResponse.createByErrorMessage("草稿版本不符合当前可发起版本，请重新起草");
		}

        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        SysUser currentUser = sysUserMapper.queryByPrimaryKey(currentUserUid);
		// 查看当前用户有没有发起流程的权限
        if (!checkPermissionStart(startableDefinition)) {
            return ServerResponse.createByErrorMessage("您没有发起该流程的权限");
        }

		BpmActivityMeta firstHumanActivity = dhProcessDefinitionService
				.getFirstHumanBpmActivityMeta(proAppId, proUid, proVerUid).getData();

		if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}

		// 传递第一个环节处理人信息
		CommonBusinessObject pubBo = new CommonBusinessObject();
		String firstUserVarname = firstHumanActivity.getDhActivityConf().getActcAssignVariable();
		List<String> tmpList = new ArrayList<>();
		tmpList.add(currentUserUid);
		CommonBusinessObjectUtils.setNextOwners(firstUserVarname, pubBo, tmpList);

		HttpReturnStatus result = new HttpReturnStatus();
		// 掉用API 发起一个流程
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();

		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);

		// 如果获取API成功 将返回过来的流程数据 保存到 平台
		if (!BpmClientUtils.isErrorResult(result)) {
			// 获得流流程编号,和第一个任务的编号
			int insId = getProcessId(result);
			int taskId = getFirstTaskId(result);
			pubBo.setInstanceId(String.valueOf(insId));

			// 获得路由信息
            BpmRoutingData routingData = dhRouteService.getNextActivityTo(firstHumanActivity, mergedFromData);

			Set<BpmActivityMeta> nextActivities = routingData.getNormalNodes();
            if (routingData.getGatewayNodes().size() > 0) {
				dhRouteService.updateGatewayRouteResult(insId, routingData);
            }


            // 封装下一环节的处理人
			ServerResponse<CommonBusinessObject> assembleResponse = dhRouteService.assembleCommonBusinessObject(pubBo,
					routeData);
			if (!assembleResponse.isSuccess()) {
				return assembleResponse;
			}

			// 完成第一个任务
			BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
			Map<String, HttpReturnStatus> commitTaskMap = bpmTaskUtil.commitTask(taskId, pubBo);
			Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(commitTaskMap);

			// 如果完成任务成功
			if (errorMap.get("errorResult") == null) {
				// 更新草稿流程实例的状态
				DhProcessInstance instanceSelective = new DhProcessInstance();
				instanceSelective.setInsUid(dhProcessInstance.getInsUid());
				instanceSelective.setInsTitle(insTitle);
				instanceSelective.setInsId(insId);
				instanceSelective.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
				instanceSelective.setInsInitDate(new Date());
				instanceSelective.setCompanyNumber(companyNumber);
				instanceSelective.setDepartNo(departNo);
				// 装配insData
				insData.put("formData", mergedFromData);
				processData.put("insInitUser", currentUserUid);
				insData.put("processData", processData);
				instanceSelective.setInsData(insData.toJSONString());
				dhProcessInstanceMapper.updateByPrimaryKeySelective(instanceSelective);

				DhTaskInstance taskInstance = new DhTaskInstance();
				taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
				taskInstance.setUsrUid(currentUserUid);
				taskInstance.setActivityBpdId(firstHumanActivity.getActivityBpdId());
				taskInstance.setTaskData(data);
				taskInstance.setTaskId(taskId);
				taskInstance.setTaskTitle(firstHumanActivity.getActivityName());
				taskInstance.setInsUid(dhProcessInstance.getInsUid());
				taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
				taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
				taskInstance.setTaskInitDate(new Date());
				taskInstance.setTaskFinishDate(new Date());
				dhTaskInstanceMapper.insertTask(taskInstance);

				// 任务完成后 保存到流转信息表里面
				DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
				dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
				dhRoutingRecord.setInsUid(dhProcessInstance.getInsUid());
				dhRoutingRecord.setActivityName(firstHumanActivity.getActivityName());
				dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_Type_START_PROCESS);
				dhRoutingRecord.setUserUid(currentUserUid);
				dhRoutingRecord.setActivityId(firstHumanActivity.getActivityId());
				if (nextActivities.size() > 0) {
					String activityTo = "";
					for (BpmActivityMeta nextMeta : nextActivities) {
						activityTo += nextMeta.getActivityId() + ",";
					}
					dhRoutingRecord.setActivityTo(activityTo.substring(0, activityTo.length() - 1));
				}
				// 发起流程 第一个流转环节信息 的 用户id 是 自己
				dhRoutingRecordMapper.insert(dhRoutingRecord);

				// 从草稿箱删除这个流程的草稿
				dhDraftsMapper.deleteByInsUid(insUid);
			} else {
				return ServerResponse.createByErrorMessage("发起流程失败");
			}
			return ServerResponse.createBySuccess();
		} else {
			return ServerResponse.createByErrorMessage("发起流程失败");
		}
	}

	/**
	 * 根据RESTfual调用返回值获得流程实例id
	 * 
	 * @param httpReturnStatus
	 */
	private int getProcessId(HttpReturnStatus httpReturnStatus) {
		JSONObject jsonBody = JSONObject.parseObject(httpReturnStatus.getMsg());
		JSONObject jsonBody2 = JSONObject.parseObject(String.valueOf(jsonBody.get("data")));
		return new Integer(jsonBody2.getString("piid"));
	}

	/**
	 * 从发起流程RESTful调用的结果中得到第一个人工任务
	 * 
	 * @param httpReturnStatus
	 * @return
	 */
	private int getFirstTaskId(HttpReturnStatus httpReturnStatus) {
		JSONObject jsoResult = JSONObject.parseObject(httpReturnStatus.getMsg());
		String taskId = jsoResult.getJSONObject("data").getJSONArray("tasks").getJSONObject(0).getString("tkiid");
		return new Integer(taskId);
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
			String result = httpClientUtils.checkLoginIbm(
					"http://10.0.4.201:9080/rest/bpm/wle/v1/visual/processModel/instances?instanceIds=[" + insId
							+ "]&showCurrentActivites=true&showExecutionPath=true&showNote=true&showColor=true&image=true");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DhProcessInstance generateDraftDefinition(DhProcessDefinition dhProcessDefinition) {
		DhProcessInstance processInstance = new DhProcessInstance();
		processInstance = new DhProcessInstance();
		processInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
		processInstance.setInsTitle("未命名");
		processInstance.setInsId(-1);
		processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_DRAFT);
		processInstance.setInsStatus(DhProcessInstance.STATUS_DRAFT);
		processInstance.setProAppId(dhProcessDefinition.getProAppId());
		processInstance.setProUid(dhProcessDefinition.getProUid());
		processInstance.setProVerUid(dhProcessDefinition.getProVerUid());
		processInstance.setInsInitDate(new Date());
		processInstance
				.setInsInitUser((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		// processInstance.setCompanyNumber(currentUser.getCompanynumber());
		JSONObject insData = new JSONObject();
		insData.put("formData", new JSONObject());
		JSONObject processData = new JSONObject();
		processData.put("insUid", processInstance.getInsUid());
		insData.put("processData", processData);

		processInstance.setInsData(insData.toJSONString());
		// processInstance.setInsStatus("run");
		return processInstance;
	}

	public DhProcessInstance getByInsUid(String insUid) {
		if (StringUtils.isBlank(insUid)) {
			return null;
		}
		return dhProcessInstanceMapper.selectByPrimaryKey(insUid);
	}

	@Transactional
	public ServerResponse<Map<String, Object>> toStartProcess(String proAppId, String proUid, String insUid) {
		Map<String, Object> resultMap = new HashMap<>();

		String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		SysUser currentUser = sysUserMapper.queryByPrimaryKey(currentUserUid);

		// 获得发起人部门信息和公司编码信息
		SysUserDepartment sysUserDepartment = new SysUserDepartment();
		sysUserDepartment.setUserUid(currentUserUid);
		List<SysUserDepartment> userDepartmentList = sysUserDepartmentService.selectAll(sysUserDepartment);

		DhProcessDefinition processDefintion = null;
		DhProcessInstance processInstance = null;

		String formData = "{}";
		if (StringUtils.isBlank(insUid)) {
			// 不是草稿箱来的
			processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
			if (processDefintion == null) {
				return ServerResponse.createByErrorMessage("当前流程没有可发起的版本");
			}
			if(checkPermissionStart(processDefintion)) {
				processInstance = this.generateDraftDefinition(processDefintion);
			}else {
				return ServerResponse.createByErrorMessage("无权限发起当前流程");
			}
		} else {
			// 是草稿箱来的
			processInstance = this.getByInsUid(insUid);
			if (processInstance == null) {
				return ServerResponse.createByErrorMessage("草稿中的流程实例不存在");
			}
			processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(processInstance.getProAppId(),
					processInstance.getProUid());
			if (processDefintion == null || !processDefintion.getProVerUid().equals(processInstance.getProVerUid())) {
				return ServerResponse.createByErrorMessage("不能用此草稿版本发起流程");
			}
			proAppId = processInstance.getProAppId();
			proUid = processInstance.getProUid();
			DhDrafts dhDrafts = dhDraftsMapper.queryDraftsByInsUid(insUid);
			JSONObject jsonObj = JSONObject.parseObject(dhDrafts.getDfsData());
			formData = jsonObj.getString("formData");
		}

		ServerResponse<BpmActivityMeta> metaResponse = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId,
				proUid, processDefintion.getProVerUid());
		if (!metaResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("找不到第一个人工环节");
		}
		BpmActivityMeta firstHumanMeta = metaResponse.getData();

		DhActivityConf dhActivityConf = dhActivityConfMapper
				.selectByPrimaryKey(firstHumanMeta.getDhActivityConf().getActcUid());

		// 获得默认步骤的列表
		List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(firstHumanMeta, "default");
		DhStep formStep = getFirstFormStepOfStepList(steps);
		if (formStep == null) {
			return ServerResponse.createByErrorMessage("找不到表单步骤");
		}
		ServerResponse<String> fieldPermissionResponse = bpmFormFieldService
				.queryFieldPermissionByStepUid(formStep.getStepUid());
		if (!fieldPermissionResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("缺少表单权限信息");
		}
		String fieldPermissionInfo = fieldPermissionResponse.getData();

		ServerResponse getFormResponse = bpmFormManageService.queryFormByFormUid(formStep.getStepObjectUid());
		if (!getFormResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("缺少表单");
		}
		BpmForm bpmForm = (BpmForm) getFormResponse.getData();

		if (StringUtils.isBlank(insUid)) {
			dhProcessInstanceMapper.insertProcess(processInstance);
		}

		// 是否显示环节权责控制
		if (StringUtils.isBlank(dhActivityConf.getActcResponsibility())
				|| "<br>".equals(dhActivityConf.getActcResponsibility())) {
			resultMap.put("showResponsibility", "FALSE");
		} else {
			resultMap.put("showResponsibility", "TRUE");
		}

		resultMap.put("currentUser", currentUser);
		resultMap.put("bpmForm", bpmForm);
		resultMap.put("fieldPermissionInfo", fieldPermissionInfo);
		resultMap.put("processDefinition", processDefintion);
		resultMap.put("formData", formData);
		resultMap.put("bpmActivityMeta", firstHumanMeta);
		resultMap.put("dhActivityConf", dhActivityConf);
		resultMap.put("userDepartmentList", userDepartmentList);
		resultMap.put("dhStep", formStep);
		resultMap.put("processInstance", processInstance);
		return ServerResponse.createBySuccess(resultMap);
	}

    /**
     * 查看当前用户有没有发起指定流程的权限
     * @param processDefintion
     * @return
     */
	private boolean checkPermissionStart(DhProcessDefinition processDefintion) {
		boolean flag = false;
		log.info("判断---当前用户权限 开始。。。");
		String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		log.info("当前用户为" + user);
		if(Const.Boolean.TRUE.equals(processDefintion.getIsAllUserStart())) {
			flag = true;
		}else {
			DhObjectPermission dhObjectPermission = new DhObjectPermission();
			dhObjectPermission.setProAppId(processDefintion.getProAppId());
			dhObjectPermission.setProUid(processDefintion.getProUid());
			dhObjectPermission.setProVerUid(processDefintion.getProVerUid());
			dhObjectPermission.setOpObjType(DhObjectPermissionObjType.PROCESS.getCode());
			dhObjectPermission.setOpAction(DhObjectPermissionAction.START.getCode());
			List<DhObjectPermission> list = dhObjectPermissionMapper.listByDhObjectPermissionSelective(dhObjectPermission);
			// 根据用户id 去 查询 角色id
			SysRoleUser sysRoleUser = new SysRoleUser();
			sysRoleUser.setUserUid(user);
			List<SysRoleUser> result = sysRoleUserMapper.selectAll(sysRoleUser);
			
			for (DhObjectPermission dhObjectPermission2 : list) {
				switch (dhObjectPermission2.getOpParticipateType()) {
				case "USER":
					if(user.equals(dhObjectPermission2.getOpParticipateUid())) {
						flag = true;
					}
					break;
				case "ROLE":
					for (SysRoleUser sysRoleUser2 : result) {
						if(sysRoleUser2.getRoleUid().equals(dhObjectPermission2.getOpParticipateUid())) {
							flag = true;
							break;
						}
					}
					break;
				case "TEAM":
					for (SysRoleUser sysRoleUser2 : result) {
						// 根据用户id 去找到 用户所在的角色组织
						SysTeamMember sysTeamMember = new SysTeamMember();
						sysTeamMember.setUserUid(sysRoleUser2.getUserUid());
						List<SysTeamMember> sysTeamList = sysTeamMemberMapper.selectAll(sysTeamMember);
						for (SysTeamMember sysTeamMember2 : sysTeamList) {
							//根据team
							if(sysTeamMember2.getTeamUid().equals(dhObjectPermission2.getOpParticipateUid())) {
								flag = true;
								break;
							}
						}
					}
					break;
				default:
					break;
				}
				
				if(flag==true) {
					break;
				}
			}
			
		}
		return flag;
	}

	private DhStep getFirstFormStepOfStepList(List<DhStep> stepList) {
		for (DhStep dhStep : stepList) {
			if (DhStep.TYPE_FORM.equals(dhStep.getStepType())) {
				return dhStep;
			}
		}
		return null;
	}

	@Override
	public ServerResponse<List<Map<String, Object>>> queryRejectByActivity(String activityId, String insUid) {
		List<Map<String, Object>> activitiMapList = new ArrayList<>();
		if (StringUtils.isBlank(activityId) || StringUtils.isBlank(insUid)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
		// 查询当前环节是否允许驳回 （TRUE,FALSE）
		String rejectboolean = dhActivityConf.getActcCanReject();
		// 查询当前环节驳回方式 (toProcessStart发起人,toPreActivity上个环节,toActivities选择环节)
		String rejectType = dhActivityConf.getActcRejectType();
		// 可以驳回

		if (Const.Boolean.TRUE.equals(rejectboolean)) {
			DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
			BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
			// 获得流程实例引擎id
			int insId = dhProcessInstance.getInsId();
			DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
			dhRoutingRecord.setInsUid(insUid);
			switch (rejectType) {
			case "toProcessStart":
				log.info("驳回到发起人");
				// 发起人
				// 获得流程发起人
				String insInitUser = dhProcessInstance.getInsInitUser();
				SysUser sysUser = new SysUser();
				sysUser.setUserUid(insInitUser);
				SysUser sysUser2 = sysUserMapper.findById(sysUser);
				String UserName = sysUser2.getUserName();
				// 获得流转数据中的发起流程的环节id
				Map<String, Object> toProcessStartMap = new HashMap<>();
				List<DhRoutingRecord> dhRoutingRecordList = dhRoutingRecordMapper
						.getDhRoutingRecordListByCondition(dhRoutingRecord);
				for (DhRoutingRecord dhRoutingRecord2 : dhRoutingRecordList) {
					// 过滤掉其他的数据信息 只需要 类型为 发起流程的环节信息
					BpmActivityMeta bpmActivityMeta3 = bpmActivityMetaMapper
							.queryByPrimaryKey(dhRoutingRecord2.getActivityId());
					if (DhRoutingRecord.ROUTE_Type_START_PROCESS.equals(dhRoutingRecord2.getRouteType())) {
						toProcessStartMap.put("insId", insId);
						toProcessStartMap.put("activityBpdId", bpmActivityMeta3.getActivityBpdId());
						toProcessStartMap.put("activityName", bpmActivityMeta3.getActivityName());
						toProcessStartMap.put("userId", dhRoutingRecord2.getUserUid());
						toProcessStartMap.put("userName", dhRoutingRecord2.getUserName());
						activitiMapList.add(toProcessStartMap);
					}
				}
				return ServerResponse.createBySuccess(activitiMapList);
			case "toPreActivity":
				log.info("驳回到上个环节");
				// 上个环节
				Map<String, Object> toPreActivityMap = new HashMap<>();
				ServerResponse<BpmActivityMeta> bpmActivityMeta2 = dhRouteService.getPreActivity(dhProcessInstance,
						bpmActivityMeta);
				toPreActivityMap.put("insInitUser", bpmActivityMeta2.getData().getUserUid());
				toPreActivityMap.put("insInitUserName", bpmActivityMeta2.getData().getUserName());
				toPreActivityMap.put("insId", insId);
				toPreActivityMap.put("activityBpdId", bpmActivityMeta2.getData().getActivityBpdId());
				toPreActivityMap.put("activityName", bpmActivityMeta2.getData().getActivityName());
				activitiMapList.add(toPreActivityMap);
				return ServerResponse.createBySuccess(activitiMapList);
			case "toActivities":
				log.info("驳回到指定环节");
				//查询可选配置
				List<DhActivityReject> dhActivityRejects = dhActivityRejectMapper.listByActivityId(activityId);
				
				// 选择环节
				List<DhRoutingRecord> dhRoutingRecordList2 = dhRoutingRecordMapper
						.getDhRoutingRecordListByCondition(dhRoutingRecord);
				for (DhRoutingRecord dhRoutingRecord3 : dhRoutingRecordList2) {
					// 过滤信息
					BpmActivityMeta bpmActivityMeta4 = bpmActivityMetaMapper
							.queryByPrimaryKey(dhRoutingRecord3.getActivityId());
					if(checkReject(bpmActivityMeta4,dhActivityRejects)) {
					if (DhRoutingRecord.ROUTE_Type_SUBMIT_TASK.equals(dhRoutingRecord3.getRouteType())
							|| DhRoutingRecord.ROUTE_Type_START_PROCESS.equals(dhRoutingRecord3.getRouteType())) {
						Map<String, Object> toActivitiesMap = new HashMap<>();
						toActivitiesMap.put("insId", insId);
						toActivitiesMap.put("activityBpdId", bpmActivityMeta4.getActivityBpdId());
						toActivitiesMap.put("activityName", bpmActivityMeta4.getActivityName());
						toActivitiesMap.put("userId", dhRoutingRecord3.getUserUid());
						toActivitiesMap.put("userName", dhRoutingRecord3.getUserName());
						activitiMapList.add(toActivitiesMap);
					}
					}
				}
				return ServerResponse.createBySuccess(activitiMapList);
			}
		}
		return null;
	}

	private boolean checkReject(BpmActivityMeta bpmActivityMeta4, List<DhActivityReject> dhActivityRejects) {
		boolean flag = false;
		for (DhActivityReject dhActivityReject : dhActivityRejects) {
			if(bpmActivityMeta4.getActivityId().equals(dhActivityReject.getActivityId())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public ServerResponse rejectProcess(String data) {
		JSONObject jsonBody = JSONObject.parseObject(data);
		JSONObject routeData = JSONObject.parseObject(String.valueOf(jsonBody.get("routeData")));
		JSONObject approvalData = JSONObject.parseObject(String.valueOf(jsonBody.get("approvalData")));// 获取审批信息
		JSONObject taskData = JSONObject.parseObject(String.valueOf(jsonBody.get("taskData")));
		int insId = routeData.getIntValue("insId");
		String activityBpdId = routeData.getString("activityBpdId");
		String userUid = routeData.getString("userUid");
		String taskUid = taskData.getString("taskUid");

		// 判断驳回的任务是否存在
		if (StringUtils.isBlank(taskUid)) {
		    return ServerResponse.createByErrorMessage("缺少任务信息");
        }
        DhTaskInstance sourceTask = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
        if (sourceTask == null || !"12".equals(sourceTask.getTaskStatus())) {
            return ServerResponse.createByErrorMessage("任务不存在，或状态异常");
        }

        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(sourceTask.getInsUid());

        int taskId = taskData.getIntValue("taskId");
		log.info("驳回流程开始......");
		log.info("流程标识为:" + routeData.getString("insId"));
		if (insId == 0 || StringUtils.isBlank(activityBpdId) || StringUtils.isBlank(userUid)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		// 通过activityBpdId 去查询 环节配置表 获取 activityId 然后去查询变量
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		// 获取树流程信息
		HttpReturnStatus returnStatus = bpmProcessUtil.getProcessData(insId);
		if(returnStatus.getCode()!=200) {
			return ServerResponse.createByErrorMessage("查询树流程信息出错");
		}
		// 查询token
		JSONObject jsonObject = JSONObject.parseObject(returnStatus.getMsg());
		Map<Object, Object> resultMap = ExecutionTreeUtil.queryTokenId(taskId, jsonObject);
		String tokenId = String.valueOf(resultMap.get("tokenId"));
		String parentTokenId = String.valueOf(resultMap.get("parentTokenId"));
		// 数据信息
		CommonBusinessObject pubBo = new CommonBusinessObject();
		List<String> dataList = new ArrayList<>();
		dataList.add(userUid);
		// todo  判断驳回到的这个环节，在配置中的处理人变量是哪个，pubBo设置对应的变量
		pubBo.setNextOwners_0(dataList);
		Map<String, HttpReturnStatus> httpMap = bpmProcessUtil.setDataAndMoveToken(insId, activityBpdId, pubBo,tokenId);
		if (httpMap.get("moveTokenResult").getCode() == 200) {

			// 驳回成功修改当前用户任务状态为 完成

			String insUid = dhProcessInstance.getInsUid();
			DhTaskInstance dhTaskInstance = new DhTaskInstance();
			dhTaskInstance.setInsUid(insUid);
			List<DhTaskInstance> DhTaskInstanceList = dhTaskInstanceMapper.selectAllTask(dhTaskInstance);
			String taskTitle = "";
			String currentUser = "";
			for (DhTaskInstance dhTaskInstance2 : DhTaskInstanceList) {
				/**
				 * 这里的用户判断 应该是获取当前登陆的用户 因为是我驳回的 ， 相反user是驳回给谁 哪一个人， 跟当前用户无关 ， 这里只是修改我本人的状态
				 * ----完成任务(注：如果是并行任务需要修改其他人的任务状态为-1废弃)
				 */
				currentUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
				taskTitle = dhTaskInstance2.getTaskTitle();
				if (currentUser.equals(dhTaskInstance2.getUsrUid())
						&& DhTaskInstance.STATUS_RECEIVED.equals(dhTaskInstance2.getTaskStatus())) {
					dhTaskInstanceMapper.updateOtherTaskStatusByTaskId(dhTaskInstance2.getUsrUid(),
							dhTaskInstance2.getTaskId(), DhTaskInstance.STATUS_CLOSED);
					/*
					 * List<String> insUids = new ArrayList<>(); insUids.add();
					 * dhTaskInstanceMapper.abandonTaskByInsUidList(insUids);
					 */
				}
			}
			// 保存流转信息
			DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
			dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
			dhRoutingRecord.setInsUid(insUid);
			dhRoutingRecord.setActivityName(taskTitle);
			dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_Type_REJECT_TASK);
			dhRoutingRecord.setUserUid(currentUser);
			BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
			bpmActivityMeta.setActivityBpdId(activityBpdId);
			List<BpmActivityMeta> bpmActivityMetaList = bpmActivityMetaMapper
					.queryByBpmActivityMetaSelective(bpmActivityMeta);
			String activityId = "";
			for (BpmActivityMeta bpmActivityMeta2 : bpmActivityMetaList) {
				activityId = bpmActivityMeta2.getActivityId();
			}
			dhRoutingRecord.setActivityId(activityId);
			dhRoutingRecordMapper.insert(dhRoutingRecord);
			// 保存审批意见
			String aprOpiComment = approvalData.getString("aprOpiComment");
			String aprStatus = "驳回";
			DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
			dhApprovalOpinion.setAprOpiId(EntityIdPrefix.DH_APPROVAL_OPINION + String.valueOf(UUID.randomUUID()));
			dhApprovalOpinion.setInsUid(insUid);
			dhApprovalOpinion.setTaskUid(taskUid);
			dhApprovalOpinion.setAprUserId(currentUser);
			dhApprovalOpinion.setAprOpiComment(aprOpiComment);
			dhApprovalOpinion.setAprStatus(aprStatus);
			dhApprovalOpinion.setActivityId(activityId);
			dhApprovalOpinionService.insertDhApprovalOpinion(dhApprovalOpinion);
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}
}
