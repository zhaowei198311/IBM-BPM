/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import com.desmart.common.util.ProcessDataUtil;
import com.desmart.common.util.FormDataUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.common.util.RestUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhActivityRejectMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhActivityReject;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionObjType;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
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
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhApprovalOpinionService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhRoutingRecordService;
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
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
	private DhRoutingRecordService dhRoutingRecordService;


	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance,
			Integer pageNum, Integer pageSize) {
		log.info("查询所有process开始");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhProcessInstance> resultList = dhProcessInstanceMapper.queryBySelective(processInstance);
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
				List<DhProcessInstance> processInstanceList = dhProcessInstanceMapper.queryBySelective(processInstance);
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
	public ServerResponse<Map<String, Object>> startProcess(String data) {
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
		String insUid = processData.getString("insUid");

		DhProcessInstance mainProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		if (mainProcessInstance == null || DhProcessInstance.STATUS_ID_DRAFT != mainProcessInstance.getInsStatusId().intValue()) {
			return ServerResponse.createByErrorMessage("流程实例状态异常");
		}
		String proAppId = mainProcessInstance.getProAppId();
		String proUid = mainProcessInstance.getProUid();
		String proVerUid = mainProcessInstance.getProVerUid();
		String insDataStr = mainProcessInstance.getInsData();

		// 混合提交的表单内容和流程实例中的表单内容
		JSONObject insData = JSON.parseObject(insDataStr);
		JSONObject formDataFromIns = insData.getJSONObject("formData");
		JSONObject mergedFromData = FormDataUtil.formDataCombine(formDataFromTask, formDataFromIns);

		// 查看可供发起的流程定义版本
		DhProcessDefinition startableDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
		if (!mainProcessInstance.getProVerUid().equals(startableDefinition.getProVerUid())) {
			ServerResponse.createByErrorMessage("草稿版本不符合当前可发起版本，请重新起草");
		}

        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        // SysUser currentUser = sysUserMapper.queryByPrimaryKey(currentUserUid);
		// 查看当前用户有没有发起流程的权限
        if (!checkPermissionStart(startableDefinition)) {
            return ServerResponse.createByErrorMessage("您没有发起该流程的权限");
        }

        // 获得主流程开始节点
        BpmActivityMeta startNodeOfMainProcess = bpmActivityMetaService.getStartMetaOfMainProcess(proAppId, proUid, proVerUid);
        // 获得开始节点往后的路由信息
        BpmRoutingData routingDataOfMainStartNode = dhRouteService.getRoutingDataOfNextActivityTo(startNodeOfMainProcess, mergedFromData);
        // 获得第一个人工节点
        BpmActivityMeta firstHumanActivity = routingDataOfMainStartNode.getNormalNodes().iterator().next();

        // 获得第一个人工环节后的路由信息
        BpmRoutingData routingData = dhRouteService.getRoutingDataOfNextActivityTo(firstHumanActivity, mergedFromData);

        // 检查用户传递的选人信息是否全面
        if (!dhRouteService.checkRouteData(firstHumanActivity, routeData, routingData)) {
            return ServerResponse.createByErrorMessage("缺少下个环节的用户信息");
        }

		// 传递第一个环节处理人信息
		CommonBusinessObject pubBo = new CommonBusinessObject();
		String firstUserVarname = firstHumanActivity.getDhActivityConf().getActcAssignVariable();
		List<String> creatorIdList = new ArrayList<>();
		creatorIdList.add(currentUserUid);
		CommonBusinessObjectUtils.setNextOwners(firstUserVarname, pubBo, creatorIdList);

		// 调用API 发起一个流程
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);

		// 如果获取API成功 将返回过来的流程数据
		if (!BpmClientUtils.isErrorResult(result)) {
			JSONObject startProcessDataJson = JSON.parseObject(result.getMsg());
            int insId = Integer.parseInt(startProcessDataJson.getJSONObject("data").getString("piid"));
            int taskId = Integer.parseInt(startProcessDataJson.getJSONObject("data").getJSONArray("tasks").getJSONObject(0).getString("tkiid"));
			// 在pubBo中设置实例编号，供网关环节决策使用
			pubBo.setInstanceId(String.valueOf(insId));

			// 更新网关决策服务中间表数据
            dhRouteService.updateGatewayRouteResult(insId, routingData);

			// 如果有循环任务记录处理人信息
			List<DhTaskHandler> taskHandlerList = dhRouteService.saveTaskHandlerOfLoopTask(insId, routeData);

            // 更新草稿流程实例的状态
            DhProcessInstance instanceSelective = new DhProcessInstance();
            instanceSelective.setInsUid(mainProcessInstance.getInsUid());
            instanceSelective.setInsTitle(insTitle);
            instanceSelective.setInsId(insId);
            instanceSelective.setInsStatus(DhProcessInstance.STATUS_ACTIVE);
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
            mainProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(mainProcessInstance.getInsUid());

            // 由于流程的第一个环节约定为起草，发起主流程不提交时，不会进入子流程
            // 从草稿箱删除这个流程的草稿
            dhDraftsMapper.deleteByInsUid(insUid);

            Map<String, Object> map = new HashMap<>();
            map.put("taskId", taskId);
            map.put("firstHumanActivity", firstHumanActivity);
            map.put("dhProcessInstance", mainProcessInstance);
            map.put("routingData", routingData);
            map.put("pubBo", pubBo);
            map.put("dataJson", dataJson);
			return ServerResponse.createBySuccess(map);
		} else {
			return ServerResponse.createByErrorMessage("发起流程失败");
		}
	}

	@Override
    @Transactional
	public ServerResponse commitFirstTask(int taskId, BpmActivityMeta firstHumanActivity, DhProcessInstance mainProcessInstance,
                                          BpmRoutingData routingData,
                                          CommonBusinessObject pubBo, JSONObject dataJson) {
        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        // 获得流流程编号,和第一个任务的编号
        int insId = mainProcessInstance.getInsId();

        pubBo.setInstanceId(String.valueOf(insId));

        JSONArray routeDataArr = dataJson.getJSONArray("routeData");
        // 设置后续任务处理人
        ServerResponse<CommonBusinessObject> assembleResponse = dhRouteService.assembleCommonBusinessObject(pubBo,
                routeDataArr);
        if (!assembleResponse.isSuccess()) {
            return assembleResponse;
        }

        // 创建第一个任务实例，第一个任务一定属于主流程
        DhTaskInstance taskInstance = new DhTaskInstance();
        taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
        taskInstance.setUsrUid(currentUserUid);
        taskInstance.setActivityBpdId(firstHumanActivity.getActivityBpdId());
        taskInstance.setTaskData(dataJson.toJSONString());
        taskInstance.setTaskId(taskId);
        taskInstance.setTaskTitle(firstHumanActivity.getActivityName());
        taskInstance.setInsUid(mainProcessInstance.getInsUid());
        taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
        taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
        taskInstance.setTaskInitDate(new Date());
        taskInstance.setTaskFinishDate(new Date());
        taskInstance.setTaskActivityId(firstHumanActivity.getActivityId());
        dhTaskInstanceMapper.insertTask(taskInstance);

        // 任务完成后 保存到流转信息表里面
        ServerResponse<DhRoutingRecord> generateRountingRecordResponse =
                dhRoutingRecordService.generateSubmitTaskRoutingRecordByTaskAndRoutingData(taskInstance, routingData, true);
        dhRoutingRecordMapper.insert(generateRountingRecordResponse.getData());

        // 完成第一个任务
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(globalConfig);
        Map<String, HttpReturnStatus> commitTaskMap = bpmTaskUtil.commitTask(taskId, pubBo);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(commitTaskMap);
        if (errorMap.get("errorResult") != null) {
            throw new PlatformException("提交第一个任务失败，流程实例id：" + insId);
        }

        // 如果需要创建子流程，就创建
        createSubProcessInstanceByRoutingData(mainProcessInstance, routingData, pubBo, null);

        return ServerResponse.createBySuccess();
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

	@Override
	public DhProcessInstance generateDraftProcessInstance(DhProcessDefinition dhProcessDefinition,String insBusinessKey) {
		DhProcessInstance processInstance = new DhProcessInstance();
		processInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
		processInstance.setInsTitle(dhProcessDefinition.getProName());
		processInstance.setInsId(-1);
		processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_DRAFT);
		processInstance.setInsStatus(DhProcessInstance.STATUS_DRAFT);
		processInstance.setProAppId(dhProcessDefinition.getProAppId());
		processInstance.setProUid(dhProcessDefinition.getProUid());
		processInstance.setProVerUid(dhProcessDefinition.getProVerUid());
		processInstance.setInsInitDate(new Date());
		processInstance.setInsParent("0");
		processInstance.setInsBusinessKey(insBusinessKey);
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
	public ServerResponse<Map<String, Object>> toStartProcess(String proAppId, String proUid, String insUid,String insBusinessKey) {
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
				processInstance = this.generateDraftProcessInstance(processDefintion, insBusinessKey);
			}else {
				return ServerResponse.createByErrorMessage("无权限发起当前流程");
			}
		} else {
			// 是草稿箱转来的
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
			JSONObject processData = jsonObj.getJSONObject("processData");
			resultMap.put("departNo", processData.getString("departNo"));
			resultMap.put("companyNumber", processData.getString("companyNumber"));
			resultMap.put("dhDrafts", dhDrafts);
		}

		ServerResponse<BpmActivityMeta> metaResponse = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId,
				proUid, processDefintion.getProVerUid());
		if (!metaResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("找不到第一个人工环节");
		}
		BpmActivityMeta firstHumanMeta = metaResponse.getData();

		DhActivityConf dhActivityConf = dhActivityConfMapper
				.selectByPrimaryKey(firstHumanMeta.getDhActivityConf().getActcUid());

		// 根据步骤关键字，得到所有这个环节的步骤
		List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(firstHumanMeta, processInstance.getInsBusinessKey());

		// 获得表单步骤
		DhStep formStep = getFirstFormStepOfStepList(steps);
		if (formStep == null) {
			return ServerResponse.createByErrorMessage("找不到表单步骤");
		}

		// todo 调用表单步骤前的触发器

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
		BpmActivityMeta currentbpmActivityMeta3 = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(currentbpmActivityMeta3.getSourceActivityId());
		// 查询当前环节是否允许驳回 （TRUE,FALSE）
		String rejectboolean = dhActivityConf.getActcCanReject();
		// 查询当前环节驳回方式 (toProcessStart发起人,toPreActivity上个环节,toActivities选择环节)
		String rejectType = dhActivityConf.getActcRejectType();
		// 可以驳回

		if (Const.Boolean.TRUE.equals(rejectboolean)) {
			DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
			//BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
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
				
				//for (DhRoutingRecord dhRoutingRecord2 : dhRoutingRecordList) {
					// 过滤掉其他的数据信息 只需要 类型为 发起流程的环节信息---此处如果进入到子流程驳回，那么应该只会驳回到子流程的发起节点才正确
					//BpmActivityMeta bpmActivityMeta3 = bpmActivityMetaMapper
							//.queryByPrimaryKey(dhRoutingRecord2.getActivityId());
					//if (DhRoutingRecord.ROUTE_Type_START_PROCESS.equals(dhRoutingRecord2.getRouteType())) {//满足不了应该
					BpmActivityMeta toBpmActivityMeta = new BpmActivityMeta();
					if("0".equals(currentbpmActivityMeta3.getParentActivityId())) {//主流程
						// 获得主流程开始节点
				        BpmActivityMeta startNodeOfMainProcess = bpmActivityMetaService.getStartMetaOfMainProcess(
				        		currentbpmActivityMeta3.getProAppId(), currentbpmActivityMeta3.getBpdId()
				        		, currentbpmActivityMeta3.getSnapshotId());

				        // 获得开始节点往后的路由信息
				        BpmRoutingData routingDataOfMainStartNode = dhRouteService.getRoutingDataOfNextActivityTo(startNodeOfMainProcess, null);

				        // 获得第一个人工节点
				        toBpmActivityMeta = routingDataOfMainStartNode.getNormalNodes().iterator().next();	
					}else {//子流程
						BpmActivityMeta parentBpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(currentbpmActivityMeta3.getParentActivityId());
						toBpmActivityMeta = bpmActivityMetaService.getFirstUserTaskMetaOfSubProcess(parentBpmActivityMeta);	
					}
						toProcessStartMap.put("insId", insId);
						toProcessStartMap.put("activityBpdId", toBpmActivityMeta.getActivityBpdId());
						toProcessStartMap.put("activityName", toBpmActivityMeta.getActivityName());
						
						dhRoutingRecord.setActivityId(toBpmActivityMeta.getActivityId());
						List<DhRoutingRecord> dhRoutingRecordList = dhRoutingRecordMapper
								.getDhRoutingRecordListByCondition(dhRoutingRecord);//排序方式是create_time asc
						String userId = dhRoutingRecordList.get(dhRoutingRecordList.size()-1).getUserUid();
						String userName = dhRoutingRecordList.get(dhRoutingRecordList.size()-1).getUserName();
						toProcessStartMap.put("userId", userId);
						toProcessStartMap.put("userName", userName);
						activitiMapList.add(toProcessStartMap);
					//}
				//}
				return ServerResponse.createBySuccess(activitiMapList);
			case "toPreActivity":
				log.info("驳回到上个环节");
				// 上个环节
				Map<String, Object> toPreActivityMap = new HashMap<>();
				ServerResponse<BpmActivityMeta> bpmActivityMeta2 = dhRouteService.getPreActivity(dhProcessInstance,
						currentbpmActivityMeta3);
				toPreActivityMap.put("userId", bpmActivityMeta2.getData().getUserUid());
				toPreActivityMap.put("userName", bpmActivityMeta2.getData().getUserName());
				toPreActivityMap.put("insId", insId);
				toPreActivityMap.put("activityBpdId", bpmActivityMeta2.getData().getActivityBpdId());
				toPreActivityMap.put("activityName", bpmActivityMeta2.getData().getActivityName());
				activitiMapList.add(toPreActivityMap);
				return ServerResponse.createBySuccess(activitiMapList);
			case "toActivities":
				log.info("驳回到指定环节");
				//查询可选配置
				List<DhActivityReject> dhActivityRejects = dhActivityRejectMapper.listByActivityId(currentbpmActivityMeta3.getSourceActivityId());
				
				// 选择环节
				List<DhRoutingRecord> dhRoutingRecordList2 = dhRoutingRecordMapper
						.getDhRoutingRecordListByCondition(dhRoutingRecord);
				for (DhActivityReject dhActivityReject : dhActivityRejects) {
					// 过滤信息
					BpmActivityMeta bpmActivityMeta4 = null;
					DhRoutingRecord dhRoutingRecord3 = null;
					for (DhRoutingRecord dhRoutingRecordCheck : dhRoutingRecordList2) {
						BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(dhRoutingRecordCheck.getActivityId());
						if(bpmActivityMeta.getActivityBpdId().equals(dhActivityReject.getActrRejectActivity())) {
							bpmActivityMeta4 = bpmActivityMeta;
							dhRoutingRecord3 = dhRoutingRecordCheck;
						}
					}
					if(bpmActivityMeta4!=null) {
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

	@Override
	public ServerResponse rejectProcess(String data) {
		JSONObject jsonBody = JSONObject.parseObject(data);
		JSONObject routeData = JSONObject.parseObject(String.valueOf(jsonBody.get("routeData")));
		JSONObject approvalData = JSONObject.parseObject(String.valueOf(jsonBody.get("approvalData")));// 获取审批信息
		JSONObject taskData = JSONObject.parseObject(String.valueOf(jsonBody.get("taskData")));
        // 目标环节
		String targetActivityBpdId = routeData.getString("activityBpdId");
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

		String currActivityId = sourceTask.getTaskActivityId();
		BpmActivityMeta currActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(currActivityId);
		// 判断当前节点能否驳回
		if (!"TRUE".equals(currActivityMeta.getDhActivityConf().getActcCanReject())) {
			return ServerResponse.createByErrorMessage("当前节点不允许驳回");
		}

        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(sourceTask.getInsUid());
		if (dhProcessInstance == null) return ServerResponse.createByErrorMessage("流程实例不存在");

        int taskId = sourceTask.getTaskId();
		int insId = dhProcessInstance.getInsId();
		
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		// 获取树流程信息
		HttpReturnStatus returnStatus = bpmProcessUtil.getProcessData(insId);
		if(HttpReturnStatusUtil.isErrorResult(returnStatus)) {
			return ServerResponse.createByErrorMessage("查询树流程信息出错");
		}

		// 获得任务的token
		JSONObject jsonObject = JSONObject.parseObject(returnStatus.getMsg());
		String tokenId = ProcessDataUtil.getTokenIdOfTask(taskId, jsonObject);

		// 获得目标节点
		BpmActivityMeta targetActivityMeta = bpmActivityMetaService
				.getByActBpdIdAndParentActIdAndProVerUid(targetActivityBpdId, currActivityMeta.getParentActivityId()
								, currActivityMeta.getSnapshotId());
		String actcAssignVariable = targetActivityMeta.getDhActivityConf().getActcAssignVariable();

		HttpReturnStatus httpReturnStatus = bpmProcessUtil.moveToken(insId, targetActivityBpdId, tokenId);
		if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
			return ServerResponse.createByErrorMessage("驳回失败");
		}

		JSONObject processData = JSON.parseObject(httpReturnStatus.getMsg());
		// 驳回节点上产生的任务
        List<Integer> taskIdList = ProcessDataUtil.getActiveTaskIdByFlowObjectId(targetActivityBpdId, processData);
        // todo 重新分配任务

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

			}
		}
		// 保存流转信息
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
		dhRoutingRecord.setInsUid(insUid);
		dhRoutingRecord.setActivityName(taskTitle);
		dhRoutingRecord.setRouteType(DhRoutingRecord.ROUTE_Type_REJECT_TASK);
		dhRoutingRecord.setUserUid(currentUser);

		String activityTo = targetActivityMeta.getActivityId();
		dhRoutingRecord.setActivityTo(activityTo);
		dhRoutingRecord.setActivityId(currActivityId);

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
		dhApprovalOpinion.setActivityId(currActivityId);
		dhApprovalOpinionService.insertDhApprovalOpinion(dhApprovalOpinion);
		return ServerResponse.createBySuccess();

	}

	public DhProcessInstance queryByInsIdAndTokenId(int insId, String tokenId) {
		DhProcessInstance selective = new DhProcessInstance();
		selective.setTokenId(tokenId);
		selective.setInsId(insId);
        List<DhProcessInstance> list = dhProcessInstanceMapper.queryBySelective(selective);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }



	@Override
	public ServerResponse checkedBusinesskey(DhProcessInstance dhProcessInstance) {

		DhProcessDefinition processDefintion = dhProcessDefinitionService
				.getStartAbleProcessDefinition(dhProcessInstance.getProAppId(),
						dhProcessInstance.getProUid());
		if (processDefintion == null) {
			return ServerResponse.createByErrorMessage("当前流程没有可发起的版本");
		}
		if (checkPermissionStart(processDefintion)) {
			DhStep selective = new DhStep(processDefintion.getProAppId()
	        		, processDefintion.getProUid(), processDefintion.getProVerUid());
			//selective.setStepType("form");
			ServerResponse<List<DhStep>> serverResponse = dhStepService.getStepInfoByCondition(selective);
			if(serverResponse.isSuccess()) {
				List<DhStep> list = serverResponse.getData();
				if(list.size()>0) {
					Set<String> set = new HashSet<>();
					for (DhStep dhStep : list) {
						set.add(dhStep.getStepBusinessKey());
					}
					if(set.size()==1) {
						Map<String, Object> map = new HashMap<>();
						map.put("flag", 1);
						map.put("stepBusinessKey", list.get(0).getStepBusinessKey());
						return ServerResponse.createBySuccess(map);
					}else {
						return ServerResponse.createBySuccess(set);
					}
				}else {
					return ServerResponse.createByErrorMessage("请先配置流程步骤");
				}
			}else {
				return ServerResponse.createByErrorMessage("获取流程步骤配置异常");
			}
		} else {
			return ServerResponse.createByErrorMessage("无权限发起当前流程");
		}
	}

	@Override
    public ServerResponse closeProcessInstanceByRoutingData(int insId, BpmRoutingData routingData) {
        Set<BpmActivityMeta> endProcessNodes = routingData.getEndProcessNodes();
        for (Iterator<BpmActivityMeta> it = endProcessNodes.iterator(); it.hasNext();) {
            BpmActivityMeta item = it.next();
            DhProcessInstance subInstance = dhProcessInstanceMapper.getByInsIdAndTokenActivityId(insId, item.getActivityId());
            DhProcessInstance selective = new DhProcessInstance(subInstance.getInsUid());
            selective.setInsFinishDate(new Date());
            selective.setInsStatusId(DhProcessInstance.STATUS_ID_COMPLETED);
            // todo 完成流程触发器
            dhProcessInstanceMapper.updateByPrimaryKeySelective(selective);
        }
        Set<BpmActivityMeta> mainEndNodes = routingData.getMainEndNodes();
        if (mainEndNodes.size() > 0) {
            DhProcessInstance mainProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);
            DhProcessInstance selective = new DhProcessInstance(mainProcessInstance.getInsUid());
            selective.setInsFinishDate(new Date());
            selective.setInsStatusId(DhProcessInstance.STATUS_ID_COMPLETED);
            dhProcessInstanceMapper.updateByPrimaryKeySelective(selective);
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse createSubProcessInstanceByRoutingData(DhProcessInstance currProcessInstance, BpmRoutingData routingData,
																CommonBusinessObject pubBo, JSONObject processDataJson) {
        Set<BpmActivityMeta> startProcessNodes = routingData.getStartProcessNodes();
        if (startProcessNodes.isEmpty()) {
	        return ServerResponse.createBySuccess();
        }
        int insId = currProcessInstance.getInsId();
        DhProcessInstance mainProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);

        if (processDataJson == null) { // 如果没有传入流程实例信息, 主动获取
            BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
            BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
            HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
            if (HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
                return ServerResponse.createByErrorMessage("通过RESTful API获得流程实例信息失败");
            }
            processDataJson = JSON.parseObject(processDataReturnStatus.getMsg());
        }

        for (Iterator<BpmActivityMeta> it = startProcessNodes.iterator(); it.hasNext();) {
            // 代表子流程的节点
            BpmActivityMeta startProcessNode = it.next();
            // 子流程的第一个人工环节
            BpmActivityMeta firstUserTaskNode = bpmActivityMetaService.getFirstUserTaskMetaOfSubProcess(startProcessNode);
            // 获得子流程的tokenId
            String tokenId = ProcessDataUtil.getTokenIdIdentifySubProcess(processDataJson, startProcessNode.getActivityBpdId(),
                    firstUserTaskNode.getActivityBpdId());
            // todo 如果tokenId找不到，不创建流程

			// 找到子流程的上级流程
			DhProcessInstance parentProcessInstance = null;
			if ("0".equals(startProcessNode.getParentActivityId())) {
				parentProcessInstance = mainProcessInstance;
			} else {
				DhProcessInstance insSelective = new DhProcessInstance();
				insSelective.setInsId(insId);
				insSelective.setTokenActivityId(startProcessNode.getParentActivityId());
				List<DhProcessInstance> list = dhProcessInstanceMapper.queryBySelective(insSelective);
				parentProcessInstance = list.get(0);
			}
			
            // 从pubBo中获得流程发起人id
            String assignVariable = firstUserTaskNode.getDhActivityConf().getActcAssignVariable();
            List<String> owners = CommonBusinessObjectUtils.getNextOwners(assignVariable, pubBo);
            DhProcessInstance subProcessInstacne = generateSubProcessInstanceByParentInstance(parentProcessInstance, currProcessInstance, startProcessNode,
                    tokenId, owners.get(0), mainProcessInstance.getDepartNo(), mainProcessInstance.getCompanyNumber());
            // todo 发起流程的触发器
            dhProcessInstanceMapper.insertProcess(subProcessInstacne);
        }

	    return ServerResponse.createBySuccess();
    }

    @Override
    public DhProcessInstance generateSubProcessInstanceByParentInstance(DhProcessInstance parentInstance,  DhProcessInstance currProcessInstance, BpmActivityMeta processNode,
                                                                        String tokenId, String creatorId, String departNo, String companyNumber) {
        DhProcessInstance subInstance = new DhProcessInstance();
        subInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
        subInstance.setInsTitle(parentInstance.getInsTitle() + "-" + processNode.getActivityName()); // 流程标题
        subInstance.setInsId(parentInstance.getInsId());
        subInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
        subInstance.setInsStatus(DhProcessInstance.STATUS_ACTIVE);
        subInstance.setProAppId(parentInstance.getProAppId());
        if (StringUtils.isNotBlank(processNode.getExternalId())) {
            // 子流程的proUid设为外链的流程的proUid
            subInstance.setProUid(processNode.getExternalId());
        } else {
            subInstance.setProUid(parentInstance.getProUid());
        }
        subInstance.setProVerUid(parentInstance.getProVerUid());
        subInstance.setInsInitDate(new Date());
        subInstance.setInsParent(parentInstance.getInsUid());
        subInstance.setInsInitUser(creatorId);
        subInstance.setCompanyNumber(parentInstance.getCompanyNumber());
        subInstance.setDepartNo(parentInstance.getDepartNo());
        subInstance.setInsBusinessKey(parentInstance.getInsBusinessKey());  // 流程关键字
        subInstance.setTokenId(tokenId);
        subInstance.setTokenActivityId(processNode.getActivityId());

        // 从父流程实例中获取自己表单中name相同的值作为子流程的数据
        JSONObject insData = assembleinsDataOfSubProcess(subInstance, parentInstance);
        subInstance.setInsData(insData.toJSONString());
        return subInstance;
    }

    /**
     * 为子流程生成insData
     * @param parentInstance
     * @param subInstance
     * @return
     */
    private JSONObject assembleinsDataOfSubProcess(DhProcessInstance subInstance, DhProcessInstance parentInstance) {
        JSONObject insData = new JSONObject();
        // processData部分
        JSONObject processData = new JSONObject();
        processData.put("insUid", subInstance.getInsUid());
        processData.put("departNo", subInstance.getDepartNo());
        processData.put("companyNumber", subInstance.getCompanyNumber());
        processData.put("insTitle", subInstance.getInsTitle());
        processData.put("insInitUser", subInstance.getInsInitUser());
        insData.put("processData", processData);

        // formData部分
        JSONObject formData = new JSONObject();
        BpmActivityMeta processNode = bpmActivityMetaMapper.queryByPrimaryKey(subInstance.getTokenActivityId());
        BpmActivityMeta firstTaskNode = bpmActivityMetaService.getFirstUserTaskMetaOfSubProcess(processNode);
        DhStep formStepOfTaskNode = dhStepService.getFormStepOfTaskNode(firstTaskNode, subInstance.getInsBusinessKey());
        if (formStepOfTaskNode != null) {
            // 根据表单字段获取值
            String formUid = formStepOfTaskNode.getStepObjectUid(); // 获得表单主键
            ServerResponse<List<BpmFormField>> response = bpmFormFieldService.queryFieldByFormUid(formUid);
            List<BpmFormField> allFields = response.getData();
            JSONObject parentInsData = JSON.parseObject(parentInstance.getInsData());
            JSONObject parentFormData = parentInsData.getJSONObject("formData");
            for (BpmFormField field : allFields) {
                String fldCodeName = field.getFldCodeName(); // 字段name属性
                if (StringUtils.isNotBlank(fldCodeName)) {
                    JSONObject obj = parentFormData.getJSONObject(fldCodeName);
                    if (obj != null) {
                        formData.put(fldCodeName, obj);
                    }
                }
            }

        }
        insData.put("formData", formData);
        return insData;
    }

}
