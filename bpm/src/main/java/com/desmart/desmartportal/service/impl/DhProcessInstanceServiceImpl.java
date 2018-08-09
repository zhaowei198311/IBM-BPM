/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.*;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.*;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionObjType;
import com.desmart.desmartbpm.mongo.InsDataDao;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartbpm.service.*;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartportal.service.DhFormNoService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.*;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.security.util.Resources_pt_BR;

import java.util.*;

@Service
public class DhProcessInstanceServiceImpl implements DhProcessInstanceService {
	private Logger logger = Logger.getLogger(DhProcessInstanceServiceImpl.class);

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
	@Autowired
	private TaskMongoDao taskMongoDao;
	@Autowired
	private InsDataDao insDataDao;
	@Autowired
	private DhFormNoService dhFormNoService;
	@Autowired
    private DhStepMapper dhStepMapper;
	@Autowired
	private DhProcessMetaMapper dhprocessMetaMapper;

	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance,
			Integer pageNum, Integer pageSize) {
		logger.info("查询所有process开始");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhProcessInstance> resultList = dhProcessInstanceMapper.queryBySelective(processInstance);
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("查询所有process结束");
		return null;
	}

	/**
	 * 根据流程实例主键 查询流程
	 */
	@Override
	public ServerResponse selectByPrimaryKey(String insUid) {
		logger.info("");
		DhProcessInstance proInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		if (null == proInstance) {
			throw new PlatformException("找不到目标流程实例");
		}
		logger.info("");
		return ServerResponse.createBySuccess(proInstance);
	}

	/**
	 * 根据流程实例主键 修改流程
	 */
	@Override
	public int updateByPrimaryKeySelective(DhProcessInstance dhProcessInstance) {
		return dhProcessInstanceMapper.updateByPrimaryKeySelective(dhProcessInstance);
	}

	/**
	 * 根据流程实例主键 删除流程
	 */
	@Override
	public int deleteByPrimaryKey(String insUid) {
		logger.info("");
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("");
		return 0;
	}

	/**
	 * 添加新的流程实例
	 */
	@Override
	public void insertProcess(DhProcessInstance processInstance) {
		logger.info("添加新的流程实例 Start...");
		try {
			if (processInstance != null) {
				dhProcessInstanceMapper.insertProcess(processInstance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("添加新的流程实例 End...");
	}

	/**
	 * 根据用户id 以及类型 查询用户所用有的流程 按条件 查询
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectProcessByUserAndType(
			DhProcessInstance processInstance, Integer pageNum, Integer pageSize) {
		logger.info("通过用户查询流程实例 Start...");
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
		logger.info("通过用户查询流程实例 End...");
		return null;
	}

	/*
	 * 模糊按条件查询
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,
			Integer pageNum, Integer pageSize) {
		logger.info("模糊查询流程实例 Start...");
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
		logger.info("模糊查询流程实例 End...");
		return null;
	}

	// 发起流程核心方法
	@Override
	@Transactional
	public ServerResponse<Map<String, Object>> startProcess(String data) {
		if (StringUtils.isBlank(data)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		JSONObject taskDataJson = JSONObject.parseObject(data);
		JSONObject formDataFromTask = (JSONObject) taskDataJson.get("formData");
		JSONArray routeDataFromTask = taskDataJson.getJSONArray("routeData");
		JSONObject processDataFromTask = (JSONObject) taskDataJson.get("processData");
		String insTitle = processDataFromTask.getString("insTitle");
		String companyNumber = processDataFromTask.getString("companyNumber");
		String departNo = processDataFromTask.getString("departNo");
		String insUid = processDataFromTask.getString("insUid");

		DhProcessInstance mainProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		if (mainProcessInstance == null || DhProcessInstance.STATUS_ID_DRAFT != mainProcessInstance.getInsStatusId().intValue()) {
			return ServerResponse.createByErrorMessage("流程实例状态异常");
		}
		String proAppId = mainProcessInstance.getProAppId();
		String proUid = mainProcessInstance.getProUid();
		String proVerUid = mainProcessInstance.getProVerUid();
		String insDataStr = mainProcessInstance.getInsData();

		// 混合提交的表单内容和流程实例中的表单内容
		JSONObject insDataJson = JSON.parseObject(insDataStr);
		JSONObject formDataFromIns = insDataJson.getJSONObject("formData");
		JSONObject mergedFromData = FormDataUtil.formDataCombine(formDataFromTask, formDataFromIns);

		// 查看可供发起的流程定义版本
		DhProcessDefinition startableDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
		if (!mainProcessInstance.getProVerUid().equals(startableDefinition.getProVerUid())) {
			ServerResponse.createByErrorMessage("草稿版本不符合当前可发起版本，请重新起草");
		}

        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		// 查看当前用户有没有发起流程的权限
        if (!checkPermissionStart(startableDefinition)) {
            return ServerResponse.createByErrorMessage("您没有发起该流程的权限");
        }

        // 获得主流程开始节点
        BpmActivityMeta startNodeOfMainProcess = bpmActivityMetaService.getStartNodeOfMainProcess(proAppId, proUid, proVerUid);
        // 获得开始节点往后的路由信息
        BpmRoutingData bpmRoutingDataFromMainStartNode = dhRouteService.getBpmRoutingData(startNodeOfMainProcess, mergedFromData);
        // 获得第一个人工节点
        BpmActivityMeta firstHumanActivity = bpmRoutingDataFromMainStartNode.getNormalNodes().iterator().next();

        // 获得第一个人工环节后的路由信息
        BpmRoutingData routingData = dhRouteService.getBpmRoutingData(firstHumanActivity, mergedFromData);
		// 生成表单号
        DhStep formStepOfTaskNode = dhStepService.getFormStepOfTaskNode(firstHumanActivity, mainProcessInstance.getInsBusinessKey());
        BpmForm bpmForm = bpmFormManageService.getByFormUid(formStepOfTaskNode.getStepObjectUid());
        JSONArray formNoListJson = dhFormNoService.updateFormNoListJsonObject(bpmForm, insDataJson.getJSONArray("formNoList"));

        // 检查用户传递的选人信息是否全面
        if (!dhRouteService.checkRouteData(firstHumanActivity, routeDataFromTask, routingData)) {
            return ServerResponse.createByErrorMessage("缺少下个环节的用户信息");
        }

		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		// 传递第一个环节处理人信息
		CommonBusinessObject pubBo = new CommonBusinessObject();
        pubBo.setSmartformsHost(bpmGlobalConfig.getBpmformsHost() + bpmGlobalConfig.getBpmformsWebContext());
		String firstUserVarname = firstHumanActivity.getDhActivityConf().getActcAssignVariable();
		List<String> creatorIdList = new ArrayList<>();
		creatorIdList.add(currentUserUid);
		CommonBusinessObjectUtils.setNextOwners(firstUserVarname, pubBo, creatorIdList);

		// 调用API 发起一个流程
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);

		// 如果获取API成功 将返回过来的流程数据
		if (!BpmClientUtils.isErrorResult(result)) {
			JSONObject startProcessDataJson = JSON.parseObject(result.getMsg());
            int insId = Integer.parseInt(startProcessDataJson.getJSONObject("data").getString("piid"));
            int taskId = Integer.parseInt(startProcessDataJson.getJSONObject("data").getJSONArray("tasks").getJSONObject(0).getString("tkiid"));
            // 锁住这个任务防止拉取
            taskMongoDao.saveLockedTask(new LockedTask(taskId, new Date(), LockedTask.REASON_FIRST_TASK_OF_PROCESS));

			// 在pubBo中设置实例编号，供网关环节决策使用
			pubBo.setInstanceId(String.valueOf(insId));

			// 更新网关决策服务中间表数据
            dhRouteService.updateGatewayRouteResult(insId, routingData);
			dhRouteService.saveTaskHandlerOfLoopTask(insId, routingData, pubBo);

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
            insDataJson.put("formData", mergedFromData);
            processDataFromTask.put("insInitUser", currentUserUid);
            insDataJson.put("processData", processDataFromTask);
            insDataJson.put("formNoList", formNoListJson);

            instanceSelective.setInsData(insDataJson.toJSONString());
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
            map.put("dataJson", taskDataJson);
			return ServerResponse.createBySuccess(map);
		} else {
            logger.error("发起流程失败，调用RESTFul API失败。" + result.getMsg());
			return ServerResponse.createByErrorMessage("发起流程失败");
		}
	}

	@Override
    @Transactional
	public ServerResponse commitFirstTask(int taskId, BpmActivityMeta firstHumanActivity, DhProcessInstance mainProcessInstance,
                                          BpmRoutingData routingData,
                                          CommonBusinessObject pubBo, JSONObject dataJson) {
        SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
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

		DhTaskInstance taskInstance = generateFirstTaskOfMainProcess(taskId, firstHumanActivity, dataJson.toJSONString(), mainProcessInstance);
		dhTaskInstanceMapper.insertTask(taskInstance);

        // 任务完成后 保存到流转信息表里面
        DhRoutingRecord dhRoutingRecord =
                dhRoutingRecordService.generateSubmitTaskRoutingRecordByTaskAndRoutingData(taskInstance, routingData, true);
        dhRoutingRecordMapper.insert(dhRoutingRecord);

        // 完成第一个任务
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(globalConfig);
        Map<String, HttpReturnStatus> commitTaskMap = bpmTaskUtil.commitTask(taskId, pubBo);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(commitTaskMap);
        if (errorMap.get("errorResult") != null) {
            throw new PlatformException("提交第一个任务失败，流程实例id：" + insId);
        }
		try { // 等待半秒给引擎处理处理时间
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        // 如果需要创建子流程，就创建
        createSubProcessInstanceByRoutingData(mainProcessInstance, routingData, pubBo, null);
		closeProcessInstanceByRoutingData(insId, routingData, null);
        return ServerResponse.createBySuccess();
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
		processInstance.setInsUpdateDate(new Date());
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

		DhDrafts dhDrafts = null;
		if (StringUtils.isBlank(insUid)) {
			// 不是草稿箱来的
			processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
			if (processDefintion == null) {
				return ServerResponse.createByErrorMessage("当前流程没有可发起的版本");
			}
			if(checkPermissionStart(processDefintion)) {
				processInstance = this.generateDraftProcessInstance(processDefintion, insBusinessKey);
				dhProcessInstanceMapper.insertProcess(processInstance);
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
			dhDrafts = dhDraftsMapper.queryDraftsByInsUid(insUid);
			// 将草稿中选择的部门和组织信息带到页面上
			JSONObject jsonObjDraft = JSONObject.parseObject(dhDrafts.getDfsData());
			JSONObject processDataDraft = jsonObjDraft.getJSONObject("processData");
			resultMap.put("departNo", processDataDraft.getString("departNo"));
			resultMap.put("companyNumber", processDataDraft.getString("companyNumber"));
			// 将草稿中的insTitle带到页面上
			resultMap.put("dhDrafts", dhDrafts);
		}

		ServerResponse<BpmActivityMeta> metaResponse = dhRouteService.getActualFirstUserTaskNodeOfMainProcess(proAppId,
				proUid, processDefintion.getProVerUid());
		if (!metaResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("找不到第一个人工环节");
		}
		BpmActivityMeta firstUserTaskNode = metaResponse.getData();

		DhActivityConf dhActivityConf = dhActivityConfMapper
				.selectByPrimaryKey(firstUserTaskNode.getDhActivityConf().getActcUid());

		// 根据步骤关键字，得到所有这个环节的步骤
		List<DhStep> steps = dhStepService.getStepsWithFormByBpmActivityMetaAndStepBusinessKey(firstUserTaskNode, processInstance.getInsBusinessKey());

		// 获得表单步骤
		DhStep formStep = getFirstFormStepOfStepList(steps);
		if (formStep == null) {
			return ServerResponse.createByErrorMessage("找不到表单步骤");
		}

		// 调用表单步骤前的触发器
		DhTaskInstance tempTask = new DhTaskInstance();
		tempTask.setInsUid(processInstance.getInsUid());
		ServerResponse executeStepResponse = dhStepService.executeStepBeforeFormStep(steps.get(0), tempTask);
		if (!executeStepResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage(executeStepResponse.getMsg());
		}
		// 触发器调用过后重新获取流程实例
		processInstance = getByInsUid(processInstance.getInsUid());
		// 混合草稿中fromData数据并更新
		if (dhDrafts != null) {
			processInstance = InsDataUtil.mergeDraftDataForStartProcess(dhDrafts, processInstance);
		}

		ServerResponse<String> fieldPermissionResponse = bpmFormFieldService
				.queryFieldPermissionByStepUid(formStep);
		if (!fieldPermissionResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("缺少表单权限信息");
		}
		String fieldPermissionInfo = fieldPermissionResponse.getData();
        // 获得表单
		BpmForm bpmForm = bpmFormManageService.getByFormUid(formStep.getStepObjectUid());

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
		resultMap.put("formData", FormDataUtil.getFormDataStringFromProcessInstance(processInstance));
		resultMap.put("bpmActivityMeta", firstUserTaskNode);
		resultMap.put("dhActivityConf", dhActivityConf);
		resultMap.put("userDepartmentList", userDepartmentList);
		resultMap.put("dhStep", formStep);
		resultMap.put("processInstance", processInstance);
		return ServerResponse.createBySuccess(resultMap);
	}

    /**
     * 查看当前用户有没有发起指定流程的权限
     * @param processDefintion 需要发起的流程定义
     * @return
     */
	public boolean checkPermissionStart(DhProcessDefinition processDefintion) {
		boolean flag = false;
		String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
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

	/**
	 * 从给定的步骤列表中，找到第一个表单步骤，步骤列表是已按序号排序的
	 * @param stepList
	 * @return
	 */
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
				// 发起人
				// 获得流程发起人
				String insInitUser = dhProcessInstance.getInsInitUser();
				SysUser sysUser = new SysUser();
				sysUser.setUserUid(insInitUser);
				SysUser sysUser2 = sysUserMapper.findById(sysUser);
				sysUser2.getUserName();
				// 获得流转数据中的发起流程的环节id
				Map<String, Object> toProcessStartMap = new HashMap<>();
				// 获得当前流程的第一个人工环节（此流程层面）
				BpmActivityMeta toBpmActivityMeta = new BpmActivityMeta();
				if("0".equals(currentbpmActivityMeta3.getParentActivityId())) {
					// 当前节点属于主流程，获得主流程第一个人工节点
					toBpmActivityMeta = bpmActivityMetaService.getFirstUserTaskNodeOfMainProcess(
							currentbpmActivityMeta3.getProAppId(), currentbpmActivityMeta3.getBpdId()
							, currentbpmActivityMeta3.getSnapshotId());
				}else {
					// 当前节点属于一个子流程
					BpmActivityMeta parentBpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(currentbpmActivityMeta3.getParentActivityId());
					toBpmActivityMeta = bpmActivityMetaService.getFirstUserTaskNodeOfSubProcess(parentBpmActivityMeta);
				}
				if (toBpmActivityMeta == null) {
					return ServerResponse.createByErrorMessage("此流程没有起草环节");
				}
				toProcessStartMap.put("insId", insId);
				toProcessStartMap.put("activityBpdId", toBpmActivityMeta.getActivityBpdId());
				toProcessStartMap.put("activityName", toBpmActivityMeta.getActivityName());
				// 任务处理人即流程发起人
				toProcessStartMap.put("userId", dhProcessInstance.getInsInitUser());
				toProcessStartMap.put("userName", dhProcessInstance.getInitUserFullname());
				activitiMapList.add(toProcessStartMap);
				return ServerResponse.createBySuccess(activitiMapList);
			case "toPreActivity":
				// 上个环节
				Map<String, Object> toPreActivityMap = new HashMap<>();
				ServerResponse<BpmActivityMeta> preActiivtyResponse = dhRouteService.getPreActivityByDiagram(dhProcessInstance,
						currentbpmActivityMeta3);
				if (!preActiivtyResponse.isSuccess()) {
					return ServerResponse.createByErrorMessage("获取可驳回环节失败");
				}
				BpmActivityMeta preMeta = preActiivtyResponse.getData();
				toPreActivityMap.put("userId", preMeta.getUserUid());
				toPreActivityMap.put("userName", preMeta.getUserName());
				toPreActivityMap.put("insId", insId);
				toPreActivityMap.put("activityBpdId", preMeta.getActivityBpdId());
				toPreActivityMap.put("activityName", preMeta.getActivityName());
				activitiMapList.add(toPreActivityMap);
				return ServerResponse.createBySuccess(activitiMapList);
			case "toActivities":
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
					if (DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK.equals(dhRoutingRecord3.getRouteType())
							|| DhRoutingRecord.ROUTE_TYPE_START_PROCESS.equals(dhRoutingRecord3.getRouteType())) {
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
	public ServerResponse selectBusinessKeyToStartProcess(String proAppId, String proUid) {
		if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid)) {
			return ServerResponse.createByErrorMessage("缺少必要的参数");
		}
		// 获得可发起的版本
		DhProcessDefinition enabledDefinition = dhProcessDefinitionService .getStartAbleProcessDefinition(proAppId, proUid);
		if (enabledDefinition == null) {
			return ServerResponse.createByErrorMessage("当前流程没有可发起的版本");
		}
		// 校验用户有没有发起流程的权限
		if (!checkPermissionStart(enabledDefinition)) {
			return ServerResponse.createByErrorMessage("无权限发起当前流程");
		}
		/*  1. 获得流程发起后的第一个节点
			2. 得到第一个节点所属的流程（主流程或子流程）
			3. 获得那个流程对应的所有关键字
		*/
        ServerResponse<BpmActivityMeta> getFirstNodeResponse = dhRouteService.getActualFirstUserTaskNodeOfMainProcess(proAppId,
                proUid, enabledDefinition.getProVerUid());
        if (!getFirstNodeResponse.isSuccess()) {
            return getFirstNodeResponse;
        }
        BpmActivityMeta firstUserTaskNode = getFirstNodeResponse.getData();
        List<DhStep> steps = null;
        if (BpmActivityMeta.PARENT_ACTIVITY_ID_OF_MAIN_PROCESS.equals(firstUserTaskNode.getParentActivityId())) {
            // 第一个任务是主流程的任务
            steps = dhStepMapper.listStepsOfProcessDefinition(proAppId, proUid, enabledDefinition.getProVerUid());
        } else {
            // 第一个任务是子流程内的任务
            BpmActivityMeta nodeIdentitySubProcess = bpmActivityMetaMapper.queryByPrimaryKey(firstUserTaskNode.getParentActivityId());
            String externalId = nodeIdentitySubProcess.getExternalId();
            steps = dhStepMapper.listStepsOfProcessDefinition(proAppId, externalId, enabledDefinition.getProVerUid());
        }
        if (steps.isEmpty()) {
            return ServerResponse.createByErrorMessage("流程未配置步骤");
        }
        Set<String> stepBusinessKeys = new HashSet<>();
        for (DhStep step : steps) {
            stepBusinessKeys.add(step.getStepBusinessKey());
        }
        if (stepBusinessKeys.size() == 1) {
            // 只有一个关键字
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            result.put("stepBusinessKey", steps.get(0).getStepBusinessKey());
            return ServerResponse.createBySuccess(result);
        } else {
            // 有多个关键字
            return ServerResponse.createBySuccess(stepBusinessKeys);
        }
	}

	@Override
    public ServerResponse closeProcessInstanceByRoutingData(int insId, BpmRoutingData routingData, JSONObject processDataJson) {
        Set<BpmActivityMeta> endProcessNodes = routingData.getEndProcessNodes();
		Set<BpmActivityMeta> mainEndNodes = routingData.getMainEndNodes();
		if (CollectionUtils.isEmpty(endProcessNodes) && CollectionUtils.isEmpty(mainEndNodes)) {
			return ServerResponse.createBySuccess();
		}
		if (processDataJson == null) {
			// 获得processData数据
			BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
			HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
			if (HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
				return ServerResponse.createByErrorMessage("通过RESTful API获得流程实例信息失败");
			}
			processDataJson = JSON.parseObject(processDataReturnStatus.getMsg());
		}
        for (Iterator<BpmActivityMeta> it = endProcessNodes.iterator(); it.hasNext();) {
            BpmActivityMeta item = it.next();
            DhProcessInstance subInstance = dhProcessInstanceMapper.getByInsIdAndTokenActivityId(insId, item.getActivityId());
            // 判断代表子流程的tokenId是否在执行树中存在
			if (ProcessDataUtil.containsTokenId(subInstance.getTokenId(), processDataJson)) {
				continue;
			}

            // 更新实例状态
            DhProcessInstance selective = new DhProcessInstance(subInstance.getInsUid());
            selective.setInsFinishDate(new Date());
            selective.setInsStatusId(DhProcessInstance.STATUS_ID_COMPLETED);
            dhProcessInstanceMapper.updateByPrimaryKeySelective(selective);
        }
        if (mainEndNodes.size() > 0 && ProcessDataUtil.isProcessFinished(processDataJson)) {
        	// 判断主流程是否结束
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
        // DhProcessInstance mainProcessInstance = dhProcessInstanceMapper.getMainProcessByInsId(insId);

        if (processDataJson == null) { // 如果没有传入流程实例信息, 主动获取
            BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
            BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
            HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
            if (HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
                return ServerResponse.createByErrorMessage("通过RESTful API获得流程实例信息失败");
            }
            processDataJson = JSON.parseObject(processDataReturnStatus.getMsg());
        }
		List<BpmActivityMeta> startProcessNodeList = routingData.getStartProcessNodesOnSameDeepLevel();
        startProcessNodeList.addAll(routingData.getStartProcessNodesOnOtherDeepLevel());

        for (BpmActivityMeta startProcessNode : startProcessNodeList) {
            // 子流程的第一个人工环节
            BpmActivityMeta firstUserTaskNode = startProcessNode.getFirstTaskNode();
            // 获得子流程的tokenId
            String tokenId = ProcessDataUtil.getTokenIdIdentifySubProcess(processDataJson, startProcessNode.getActivityBpdId(),
                    firstUserTaskNode.getActivityBpdId());

			// 找到子流程的上级流程（不一定是当前流程）
			DhProcessInstance parentProcessInstance = dhRouteService.getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(
					currProcessInstance, startProcessNode);
			
            // 从pubBo中获得流程发起人id
            String assignVariable = firstUserTaskNode.getDhActivityConf().getActcAssignVariable();
            List<String> owners = CommonBusinessObjectUtils.getNextOwners(assignVariable, pubBo);
            // 创建子流程实例
            DhProcessInstance subProcessInstacne = generateSubProcessInstanceByParentInstance(parentProcessInstance, currProcessInstance, startProcessNode,
                    tokenId, owners.get(0));

            dhProcessInstanceMapper.insertProcess(subProcessInstacne);
        }

	    return ServerResponse.createBySuccess();
    }

    // 创建子流程实例的方法
    @Override
    public DhProcessInstance generateSubProcessInstanceByParentInstance(DhProcessInstance parentInstance,  DhProcessInstance currProcessInstance, BpmActivityMeta processNode,
                                                                        String tokenId, String creatorId) {
        DhProcessInstance subInstance = new DhProcessInstance();
        subInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
        // 新流程的title是 父流程的title
        subInstance.setInsTitle(parentInstance.getInsTitle()); // 流程标题与父流程相同
        subInstance.setInsId(parentInstance.getInsId());
        subInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
        subInstance.setInsStatus(DhProcessInstance.STATUS_ACTIVE);
        subInstance.setProAppId(parentInstance.getProAppId());
        if (StringUtils.isNotBlank(processNode.getExternalId())) {
            // 如果是外链子流程，proUid设为外链的流程的proUid
            subInstance.setProUid(processNode.getExternalId());
        } else {
        	// 不然设为代表子流程的节点的bpd_id
            subInstance.setProUid(processNode.getBpdId());
        }
        subInstance.setProVerUid(parentInstance.getProVerUid());
        subInstance.setInsInitDate(new Date());
        subInstance.setInsParent(parentInstance.getInsUid());
        subInstance.setInsInitUser(creatorId);
        subInstance.setCompanyNumber(parentInstance.getCompanyNumber());
        subInstance.setDepartNo(parentInstance.getDepartNo());
        subInstance.setTokenId(tokenId);
		subInstance.setTokenActivityId(processNode.getActivityId());
		// 从当前流程中获得新实例的流程关键字
		subInstance.setInsBusinessKey(InsDataUtil.getBusinessKeyOfNextProcess(currProcessInstance));  // 流程关键字

		// 从父流程实例中获取自己表单中name相同的值作为子流程的数据
        JSONObject insData = assembleInsDataForSubProcess(subInstance);
        subInstance.setInsData(insData.toJSONString());
        return subInstance;
    }

    /**
     * 为子流程生成insData数据
     * @param subInstance
     * @return
     */
    private JSONObject assembleInsDataForSubProcess(DhProcessInstance subInstance) {
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
        insData.put("formData", formData);
        return insData;
    }

    private DhTaskInstance generateFirstTaskOfMainProcess(int taskId, BpmActivityMeta taskNode, String taskData, DhProcessInstance mainProcess) {
		// 创建第一个任务实例，第一个任务一定属于主流程
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
		taskInstance.setUsrUid((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		taskInstance.setActivityBpdId(taskNode.getActivityBpdId());
		taskInstance.setTaskData(taskData);
		taskInstance.setTaskId(taskId);
		taskInstance.setTaskTitle(taskNode.getActivityName());
		taskInstance.setInsUid(mainProcess.getInsUid());
		taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
		taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		taskInstance.setTaskInitDate(new Date());
		taskInstance.setTaskFinishDate(new Date());
		taskInstance.setTaskActivityId(taskNode.getActivityId());
		return taskInstance;
	}

	@Override
	public ServerResponse<Object> queryProcessInstanceByIds(String status, String processName, Date startTime, Date endTime,
															Integer pageNum, Integer pageSize,
															String usrUid, String proUid, String proAppId,String retrieveData) {
		
		JSONArray jsonArray = JSONArray.parseArray(retrieveData);
		List<JSONObject> processInstanceList = 
				insDataDao.queryInsData(status, processName, startTime, endTime, pageNum, pageSize
										, usrUid, proUid, proAppId,jsonArray);
		DhProcessMeta dhProcessMeta = dhprocessMetaMapper.queryByProAppIdAndProUid(proAppId, proUid);
		if(dhProcessMeta==null) {
			return ServerResponse.createByErrorMessage("流程元数据不存在");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("proName", dhProcessMeta.getProName());
		map.put("insDataList", processInstanceList);
		return ServerResponse.createBySuccess(map);
	}

    
}
