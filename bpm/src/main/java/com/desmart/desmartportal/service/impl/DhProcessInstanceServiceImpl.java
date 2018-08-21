/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import com.desmart.desmartportal.dao.*;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartportal.service.*;
import com.desmart.desmartsystem.dao.*;
import com.desmart.desmartsystem.entity.*;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
	private TaskMongoDao taskMongoDao;
	@Autowired
	private InsDataDao insDataDao;
	@Autowired
	private DhFormNoService dhFormNoService;
	@Autowired
	private DhProcessMetaMapper dhprocessMetaMapper;
	@Autowired
    private SysDepartmentMapper sysDepartmentMapper;
	@Autowired
    private SysCompanyMapper sysCompanyMapper;
	@Autowired
	private SysHolidayService sysHolidayService;
	@Autowired
	private DhInstanceDocumentMapper dhInstanceDocumentMapper;


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


	private String getCurrentUserUid() {
		return String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
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
			taskInstance.setUsrUid(getCurrentUserUid());
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
		DatInsData datInsData = JSONObject.parseObject(insDataStr,  new TypeReference<DatInsData>(){});

		// 混合提交的表单内容和流程实例中的表单内容
		JSONObject formDataFromIns = datInsData.getFormData();
		JSONObject mergedFromData = FormDataUtil.formDataCombine(formDataFromTask, formDataFromIns);

		// 查看可供发起的流程定义版本
		DhProcessDefinition startableDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
		if (!mainProcessInstance.getProVerUid().equals(startableDefinition.getProVerUid())) {
			ServerResponse.createByErrorMessage("草稿版本不符合当前可发起版本，请重新起草");
		}

        String currentUserUid = getCurrentUserUid();
		// 查看当前用户有没有发起流程的权限
        if (!checkPermissionStart(startableDefinition)) {
            return ServerResponse.createByErrorMessage("您没有发起该流程的权限");
        }

        BpmActivityMeta startNodeOfMainProcess = bpmActivityMetaService.getStartNodeOfMainProcess(proAppId, proUid, proVerUid);
        // 获得主流程开始节点往后的下个环节信息（包含第一个任务节点）
        BpmRoutingData bpmRoutingDataAfterStartNode = dhRouteService.getBpmRoutingData(startNodeOfMainProcess, null);
        if (bpmRoutingDataAfterStartNode.getNormalNodes().size() == 0 || bpmRoutingDataAfterStartNode.getNormalNodes().size() > 1) {
            return ServerResponse.createByErrorMessage("获得主流程的第一个环节异常");
        }
        // 获得主流程第一个人员环节
        BpmActivityMeta firstUserTaskNode = bpmRoutingDataAfterStartNode.getNormalNodes().get(0);

        // 获得第一个人工环节后的路由信息
        BpmRoutingData bpmRoutingDataAfterFirstTask = dhRouteService.getBpmRoutingData(firstUserTaskNode, mergedFromData);
		// 生成表单号
        DhStep formStepOfTaskNode = dhStepService.getFormStepOfTaskNode(firstUserTaskNode, mainProcessInstance.getInsBusinessKey());
        BpmForm bpmForm = bpmFormManageService.getByFormUid(formStepOfTaskNode.getStepObjectUid());
        JSONArray formNoListJson = dhFormNoService.updateFormNoListJsonObject(bpmForm, datInsData.getFormNoList());

        // 检查用户传递的选人信息是否全面
        if (!dhRouteService.checkRouteData(firstUserTaskNode, routeDataFromTask, bpmRoutingDataAfterFirstTask)) {
            return ServerResponse.createByErrorMessage("缺少下个环节的用户信息");
        }

		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		// 传递第一个环节处理人信息
		CommonBusinessObject pubBo = new CommonBusinessObject();
        pubBo.setSmartformsHost(bpmGlobalConfig.getBpmformsHost() + bpmGlobalConfig.getBpmformsWebContext());
		String firstUserVarname = firstUserTaskNode.getDhActivityConf().getActcAssignVariable();
		List<String> creatorIdList = new ArrayList<>();
		creatorIdList.add(currentUserUid);
		CommonBusinessObjectUtils.setNextOwners(firstUserVarname, pubBo, creatorIdList);

		// 调用API 发起一个流程
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);

		// 如果获取API成功 将返回过来的流程数据
		if (!BpmClientUtils.isErrorResult(result)) {
			JSONObject startProcessDataJson = JSON.parseObject(result.getMsg());
			JSONObject dataJson = startProcessDataJson.getJSONObject("data");
            // 锁住这个任务防止拉取
            int taskId = Integer.parseInt(dataJson.getJSONArray("tasks").getJSONObject(0).getString("tkiid"));
            taskMongoDao.saveLockedTask(new LockedTask(taskId, new Date(), LockedTask.REASON_FIRST_TASK_OF_PROCESS));
            int insId = Integer.parseInt(dataJson.getString("piid"));
			// 在pubBo中设置实例编号，供网关环节决策使用
			pubBo.setInstanceId(String.valueOf(insId));
            // 更新网关决策服务中间表数据，为提交第一个任务做准备
			if (!CollectionUtils.isEmpty(bpmRoutingDataAfterFirstTask.getRouteResults())) {
				dhRouteService.updateGatewayRouteResult(insId, bpmRoutingDataAfterFirstTask);
			}
			// 如果有多实例循环，保存记录
			dhRouteService.saveTaskHandlerOfLoopTask(insId, bpmRoutingDataAfterFirstTask, pubBo);
            // 更新流程实例的信息
            mainProcessInstance.setInsUid(mainProcessInstance.getInsUid());
            mainProcessInstance.setInsTitle(insTitle);
            mainProcessInstance.setInsId(insId);
            mainProcessInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
            mainProcessInstance.setInsInitDate(new Date());
            mainProcessInstance.setCompanyNumber(companyNumber);
            mainProcessInstance.setDepartNo(departNo);
            DatProcessData datProcessData = datInsData.getProcessData();
            BeanUtils.copyProperties(mainProcessInstance, datProcessData); // 设置processData的值

            // 判断发起操作后是否有子流程
			boolean hasSubProcess = bpmRoutingDataAfterStartNode.getStartProcessNodes().size() > 0;
            // 设置主流程的insData数据
			if (hasSubProcess) { // 有子流程
				datInsData.setFormNoList(new JSONArray());
				datInsData.setFormData(new JSONObject());
            } else {
				// 如果不需要创建子流程， 就把表单数据保存到主流程实例中
				datInsData.setFormNoList(formNoListJson);
				datInsData.setFormData(mergedFromData);
            }
			mainProcessInstance.setInsData(JSON.toJSONString(datInsData));
            dhProcessInstanceMapper.updateByPrimaryKeySelective(mainProcessInstance); // 更新主流程数据
			DhProcessInstance processContainFirstTask = mainProcessInstance;
			if (hasSubProcess) {
				// 将表单信息设置到子流程
				datInsData.setFormNoList(formNoListJson);
				datInsData.setFormData(mergedFromData);
				processContainFirstTask = createSubProcessAfterStartProcess(bpmRoutingDataAfterStartNode,
						mainProcessInstance, startProcessDataJson, datInsData);
				// 将主流程上传的附件转移给子流程
				transferDocumentsToProcessContainsFirstTask(mainProcessInstance.getInsUid(), processContainFirstTask.getInsUid());
			}

            // 由于流程的第一个环节约定为起草，发起主流程不提交时，不会进入子流程
            // 从草稿箱删除这个流程的草稿
            dhDraftsMapper.deleteByInsUid(insUid);

            Map<String, Object> map = new HashMap<>();
            map.put("taskId", taskId);
            map.put("firstHumanActivity", firstUserTaskNode);
            map.put("dhProcessInstance", processContainFirstTask); // 包含第一个任务的流程实例
            map.put("routingData", bpmRoutingDataAfterFirstTask);
            map.put("pubBo", pubBo);
            map.put("dataJson", taskDataJson);
			return ServerResponse.createBySuccess(map);
		} else {
            logger.error("发起流程失败，调用RESTFul API失败：" + result.getMsg());
			return ServerResponse.createByErrorMessage("发起流程失败");
		}
	}


	private void transferDocumentsToProcessContainsFirstTask(String sourceInsUid, String targetInsUid) {
		dhInstanceDocumentMapper.updateInsUidToNewValue(sourceInsUid, targetInsUid);
	}

	/**
     * 发起流程动作，如果触发了子流程，则创建子流程的实例<br/>
	 * 此方法在提交一个任务前执行
	 * @return 最底层的流程实例
     */
	private DhProcessInstance createSubProcessAfterStartProcess(BpmRoutingData bpmRoutingData, DhProcessInstance mainProcessInstance,
                                                   JSONObject bpmProcessData, DatInsData datInsData) {
		DhProcessInstance result = null;
        List<BpmActivityMeta> startProcessNodes = bpmRoutingData.getStartProcessNodes();
        for (int i = 0, lastIndex = startProcessNodes.size() - 1; i < startProcessNodes.size(); i++) {
            BpmActivityMeta nodeIdentitySubProcess = startProcessNodes.get(i);
            // 获得子流程的tokenId
            String parentProcessFlowObjectId = null; // 当前子流程的上级流程的元素id
            if (!BpmActivityMeta.PARENT_ACTIVITY_ID_OF_MAIN_PROCESS.equals(nodeIdentitySubProcess.getParentActivityId())) {
                // 如果代表子流程的节点不在主流程上
                //parentProcessFlowObjectId = bpmActivityMetaMapper.queryByPrimaryKey();
                parentProcessFlowObjectId = bpmRoutingData.getNodeIdentitySubProcessByActivityId(nodeIdentitySubProcess.getParentActivityId()).getActivityBpdId();
            }
            TokenInfoUtil tokenInfoUtil = new TokenInfoUtil(nodeIdentitySubProcess.getActivityBpdId(), parentProcessFlowObjectId, bpmProcessData);
            String tokenId = tokenInfoUtil.getTokenId();
            if (tokenId == null) {
                throw new PlatformException("找不到子流程对应的tokenId");
            }
            // 找到子流程的上级流程（不一定是当前流程）
            DhProcessInstance parentProcessInstance = dhRouteService.getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(
                    mainProcessInstance, nodeIdentitySubProcess);
            // 子流程发起人就是主流程发起人
            DhProcessInstance subProcessInstacne = generateSubProcessInstanceByParentInstance(parentProcessInstance, mainProcessInstance, nodeIdentitySubProcess,
                    tokenId, mainProcessInstance.getInsInitUser());
            if (i == lastIndex) { // 最后一个即是最下层流程
                DatProcessData datProcessData = datInsData.getProcessData();
                datProcessData.setInsUid(subProcessInstacne.getInsUid());
                subProcessInstacne.setInsData(JSON.toJSONString(datInsData));
                subProcessInstacne.setInsTitle(mainProcessInstance.getInsTitle());
				result= subProcessInstacne;
            }
            dhProcessInstanceMapper.insertProcess(subProcessInstacne);
        }
		return result;
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
		processInstance.setInsInitUser(getCurrentUserUid());
		// 初始化insData数据
		DatInsData datInsData = new DatInsData();
		DatProcessData datProcessData = new DatProcessData();
		BeanUtils.copyProperties(processInstance, datProcessData);
		datInsData.setProcessData(datProcessData);
		datInsData.setFormData(new JSONObject());
		processInstance.setInsData(JSON.toJSONString(datInsData));
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
		String currentUserUid = getCurrentUserUid();
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
			if (checkPermissionStart(processDefintion)) {
				processInstance = this.generateDraftProcessInstance(processDefintion, insBusinessKey);
				dhProcessInstanceMapper.insertProcess(processInstance);
			} else {
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
			JSONObject dfsDataJson = JSONObject.parseObject(dhDrafts.getDfsData());
            // 如果草稿中已选过部门和组织带到前台
			JSONObject draftProcessData = dfsDataJson.getJSONObject("processData");
			resultMap.put("departNo", draftProcessData.getString("departNo"));
			resultMap.put("companyNumber", draftProcessData.getString("companyNumber"));
			// 将草稿中的insTitle带到页面上
			resultMap.put("draftTitle", dhDrafts.getDfsTitle()); // 草稿中填写的流程标题
		}

		ServerResponse<BpmActivityMeta> getFirstTaskNodeResponse = dhRouteService.getActualFirstUserTaskNodeOfMainProcess(proAppId,
				proUid, processDefintion.getProVerUid());
		if (!getFirstTaskNodeResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("找不到第一个人工环节");
		}
		// 获得主流程的第一个环节
		BpmActivityMeta firstUserTaskNode = getFirstTaskNodeResponse.getData();
        // 获得第一个环节的配置
		DhActivityConf dhActivityConf = dhActivityConfMapper
				.selectByPrimaryKey(firstUserTaskNode.getDhActivityConf().getActcUid());

		// 根据步骤关键字，得到这个环节的步骤
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

		ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFieldPermissionByStepUid(formStep);
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
		resultMap.put("bpmActivityMeta", firstUserTaskNode); // 当前环节
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
		String userUid = getCurrentUserUid();
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
			sysRoleUser.setUserUid(userUid);
			List<SysRoleUser> result = sysRoleUserMapper.selectAll(sysRoleUser);
			
			for (DhObjectPermission dhObjectPermission2 : list) {
				switch (dhObjectPermission2.getOpParticipateType()) {
				case "USER":
					if(userUid.equals(dhObjectPermission2.getOpParticipateUid())) {
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
		DhProcessDefinition startAbleDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
		if (startAbleDefinition == null) {
			return ServerResponse.createByErrorMessage("当前流程未启用可发起版本");
		}
        // 开发起版本设置的可发起关键字
        String proStartBusinessKey = startAbleDefinition.getProStartBusinessKey();
		if (StringUtils.isBlank(proStartBusinessKey)) {
            return ServerResponse.createByErrorMessage("当前流程未配置可发起的关键字");
        }
        List<String> startBusinessKeys = Arrays.asList(proStartBusinessKey.split(";"));

        // 校验用户有没有发起流程的权限
		if (!checkPermissionStart(startAbleDefinition)) {
			return ServerResponse.createByErrorMessage("无权限发起当前流程");
		}
        // 获得主流程发起相关的关键字
        Set<String> allBusinessKeySet = dhStepService.listStepBusinessKeyOfMainProcess(proAppId, proUid, startAbleDefinition.getProVerUid());
        // 从所有相关的关键字中过滤出可发起的关键字
        Set<String> stepBusinessKeys = new HashSet<>();
        for (String businessKey : allBusinessKeySet) {
            if (startBusinessKeys.contains(businessKey)) {
                stepBusinessKeys.add(businessKey);
            }
        }
        if (stepBusinessKeys.isEmpty()) {
            return ServerResponse.createByErrorMessage("当前流程未配置可发起的关键字");
        }
        if (stepBusinessKeys.size() == 1) {
            // 只有一个关键字
            Map<String, Object> result = new HashMap<>();
            result.put("flag", 1);
            result.put("stepBusinessKey", stepBusinessKeys.iterator().next());
            return ServerResponse.createBySuccess(result);
        } else {
            // 有多个关键字
            return ServerResponse.createBySuccess(stepBusinessKeys);
        }
	}

	@Override
    public ServerResponse closeProcessInstanceByRoutingData(int insId, BpmRoutingData routingData, JSONObject processDataJson) {
        List<BpmActivityMeta> endProcessNodes = routingData.getEndProcessNodes();
        List<BpmActivityMeta> mainEndNodes = routingData.getMainEndNodes();
		if (CollectionUtils.isEmpty(endProcessNodes) && CollectionUtils.isEmpty(mainEndNodes)) {
			return ServerResponse.createBySuccess();
		}
		if (processDataJson == null) {
			// 获得processData数据
			BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
			HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
			if (HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
				throw new PlatformException("通过RESTful API获得流程实例信息失败");
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
    public ServerResponse createSubProcessInstanceByRoutingData(DhProcessInstance currProcessInstance, BpmRoutingData bpmRoutingData,
																CommonBusinessObject pubBo, JSONObject processDataJson) {
        List<BpmActivityMeta> startProcessNodes = bpmRoutingData.getStartProcessNodes();
        if (startProcessNodes.isEmpty()) {
	        return ServerResponse.createBySuccess();
        }
        int insId = currProcessInstance.getInsId(); // 流程实例号

        if (processDataJson == null) { // 如果没有传入流程实例信息, 根据流程实例号主动获取信息
            BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
            BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(globalConfig);
            HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
            if (HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
                throw new PlatformException("通过RESTful API获得流程实例信息失败");
            }
            processDataJson = JSON.parseObject(processDataReturnStatus.getMsg());
        }

        // 所有需要创建的子流程节点
        List<BpmActivityMeta> startProcessNodeList = bpmRoutingData.getStartProcessNodes();
        for (BpmActivityMeta nodeIdentitySubProcess : startProcessNodeList) {
            // 获得子流程的tokenId
			String parentProcessFlowObjectId = null; // 当前子流程的上级流程的元素id
			if (!BpmActivityMeta.PARENT_ACTIVITY_ID_OF_MAIN_PROCESS.equals(nodeIdentitySubProcess.getParentActivityId())) {
				// 如果代表子流程的节点不在主流程上
                parentProcessFlowObjectId = bpmActivityMetaMapper.queryByPrimaryKey(nodeIdentitySubProcess.getParentActivityId()).getActivityBpdId();
            }
            TokenInfoUtil tokenInfoUtil = new TokenInfoUtil(nodeIdentitySubProcess.getActivityBpdId(), parentProcessFlowObjectId, processDataJson);
            String tokenId = tokenInfoUtil.getTokenId();
            if (tokenId == null) {
                throw new PlatformException("找不到子流程对应的tokenId");
            }
			// 找到子流程的上级流程（不一定是当前流程）
			DhProcessInstance parentProcessInstance = dhRouteService.getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(
					currProcessInstance, nodeIdentitySubProcess);
			
            // 从pubBo中获得流程发起人id
            // 子流程的第一个人工环节
            BpmActivityMeta firstUserTaskNode = nodeIdentitySubProcess.getFirstTaskNode();
            if (firstUserTaskNode == null) {
               firstUserTaskNode = getFirstTaskFromParentProcessNode(nodeIdentitySubProcess, bpmRoutingData.getActIdAndNodeIdentitySubProcessMap());
               if (firstUserTaskNode == null) {
                   throw new PlatformException("创建子流程时获取发起人失败");
               }
            }
            String assignVariable = firstUserTaskNode.getDhActivityConf().getActcAssignVariable();
            List<String> owners = CommonBusinessObjectUtils.getNextOwners(assignVariable, pubBo);
            String processCreator = owners.get(0);
            // 创建子流程实例, 流程发起人取分配人中的第一个用户
            DhProcessInstance subProcessInstacne = generateSubProcessInstanceByParentInstance(parentProcessInstance, currProcessInstance, nodeIdentitySubProcess,
                    tokenId, processCreator);
            dhProcessInstanceMapper.insertProcess(subProcessInstacne);
        }

	    return ServerResponse.createBySuccess();
    }

    /**
     * 从上级流程中查找流程发起后的第一个任务
     * @param nodeIdentitySubProcess
     * @param actIdAndNodeIdentitySubProcessMap
     * @return
     */
    private BpmActivityMeta getFirstTaskFromParentProcessNode(BpmActivityMeta nodeIdentitySubProcess, Map<String, BpmActivityMeta> actIdAndNodeIdentitySubProcessMap) {
        if (nodeIdentitySubProcess.getFirstTaskNode() != null) {
            return nodeIdentitySubProcess.getFirstTaskNode();
        } else {
            BpmActivityMeta parentProcessNode = actIdAndNodeIdentitySubProcessMap.get(nodeIdentitySubProcess.getParentActivityId());
            if (parentProcessNode != null) {
                return getFirstTaskFromParentProcessNode(parentProcessNode, actIdAndNodeIdentitySubProcessMap);
            } else {
                return null;
            }
        }
    }

    // 创建子流程实例的方法
    @Override
    public DhProcessInstance generateSubProcessInstanceByParentInstance(DhProcessInstance parentInstance,  DhProcessInstance currProcessInstance, BpmActivityMeta processNode,
                                                                        String tokenId, String creatorId) {
        DhProcessInstance subInstance = new DhProcessInstance();
        subInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
        DhProcessMeta processMeta = dhprocessMetaMapper.queryByProAppIdAndProUid(processNode.getProAppId(), processNode.getExternalId());
        // 新流程的title是 父流程的title
        subInstance.setInsTitle(parentInstance.getInsTitle() + "-" + processMeta.getProName()); // 设置子流程标题
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
		subInstance.setInsBusinessKey(InsDataUtil.getBusinessKeyOfNextProcess(currProcessInstance, parentInstance));  // 流程关键字

		DatInsData datInsData = new DatInsData();
		DatProcessData datProcessData = new DatProcessData();
		BeanUtils.copyProperties(subInstance, datProcessData);
		datInsData.setProcessData(datProcessData);
		datInsData.setFormData(new JSONObject());
		subInstance.setInsData(JSON.toJSONString(datInsData));
        return subInstance;
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


    /**
     * 根据条件发起一个流程，并停留在第一个节点上
     * @param dhProcessInstance
     * @return
     */
	public ServerResponse<DatLaunchProcessResult> launchProcess(DhProcessInstance dhProcessInstance) {
        String proAppId = dhProcessInstance.getProAppId();
        String proUid = dhProcessInstance.getProUid();
        String insBusinessKey = dhProcessInstance.getInsBusinessKey();
        String insInitUser = dhProcessInstance.getInsInitUser();
        String departNo = dhProcessInstance.getDepartNo();
        String companyNumber = dhProcessInstance.getCompanyNumber();
        String insTitle = dhProcessInstance.getInsTitle();

        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(insInitUser)
                || StringUtils.isBlank(departNo) || StringUtils.isBlank(companyNumber)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }

        SysUser creator = sysUserMapper.queryByPrimaryKey(insInitUser);
        if (creator == null) {
            return ServerResponse.createByErrorMessage("流程发起人不存在");
        } else if (sysDepartmentMapper.queryByPrimaryKey(departNo) == null) {
            return ServerResponse.createByErrorMessage("指定的部门不存在");
        } else if (sysCompanyMapper.queryByCompanyCode(companyNumber) == null) {
            return ServerResponse.createByErrorMessage("指定的公司编码不存在");
        }

		if (StringUtils.isBlank(insBusinessKey)) {
			dhProcessInstance.setInsBusinessKey("default");
		}

        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();

        DhProcessDefinition startAbleProcessDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
		if (startAbleProcessDefinition == null) {
			ServerResponse.createByErrorMessage("指定流程没有可发起版本");
		}
		String proVerUid = startAbleProcessDefinition.getProVerUid();
        BpmActivityMeta startNodeOfMainProcess = bpmActivityMetaService.getStartNodeOfMainProcess(proAppId, proUid, proVerUid);
        // 获得主流程开始节点往后的下个环节信息（包含第一个任务节点）
        BpmRoutingData bpmRoutingDataAfterStartNode = dhRouteService.getBpmRoutingData(startNodeOfMainProcess, null);
        if (bpmRoutingDataAfterStartNode.getNormalNodes().size() == 0 || bpmRoutingDataAfterStartNode.getNormalNodes().size() > 1) {
            return ServerResponse.createByErrorMessage("获得主流程的第一个环节异常");
        }
        // 获得主流程第一个人员环节
        BpmActivityMeta firstUserTaskNode = bpmRoutingDataAfterStartNode.getNormalNodes().get(0);
		// 设置流程发起人
        CommonBusinessObject pubBo = new CommonBusinessObject();
        pubBo.setSmartformsHost(bpmGlobalConfig.getBpmformsHost() + bpmGlobalConfig.getBpmformsWebContext());
        String firstUserVarname = firstUserTaskNode.getDhActivityConf().getActcAssignVariable();
        List<String> creatorIdList = new ArrayList<>();
        creatorIdList.add(insInitUser);
        CommonBusinessObjectUtils.setNextOwners(firstUserVarname, pubBo, creatorIdList);

        // 调用API 发起一个流程
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);

        // 如果获取API成功 将返回过来的流程数据
        if (BpmClientUtils.isErrorResult(result)) {
        	logger.error("调用RESTFul API发起流程失败，proAppId:" + proAppId + "proUid:" + proUid);
        	return ServerResponse.createByErrorMessage("调用RESTFul API发起流程失败");
		}
		JSONObject startProcessDataJson = JSON.parseObject(result.getMsg());
		JSONObject dataJson = startProcessDataJson.getJSONObject("data");
		// 锁住这个任务防止拉取
		int taskId = Integer.parseInt(dataJson.getJSONArray("tasks").getJSONObject(0).getString("tkiid"));
		taskMongoDao.saveLockedTask(new LockedTask(taskId, new Date(), LockedTask.REASON_FIRST_TASK_OF_PROCESS));
		int insId = Integer.parseInt(dataJson.getString("piid"));
		// 在pubBo中设置实例编号，供网关环节决策使用
		pubBo.setInstanceId(String.valueOf(insId));

		// 将主流程保存到数据库
		dhProcessInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + String.valueOf(UUID.randomUUID()));
		if (StringUtils.isBlank(insTitle)) {
			dhProcessInstance.setInsTitle(startAbleProcessDefinition.getProName());
		}
		dhProcessInstance.setInsId(insId);
		dhProcessInstance.setInsStatus("status");
		dhProcessInstance.setInsParent(DhProcessInstance.INS_PARENT_OF_MAIN_PROCESS);
		dhProcessInstance.setInsStatusId(DhProcessInstance.STATUS_ID_ACTIVE);
		dhProcessInstance.setInsInitDate(new Date());
		dhProcessInstance.setProVerUid(startAbleProcessDefinition.getProVerUid());
		// 装配insData
		DatInsData datInsData = new DatInsData();
		DatProcessData datProcessData = new DatProcessData();
		BeanUtils.copyProperties(dhProcessInstance, datProcessData); // 设置processData的值
		datInsData.setProcessData(datProcessData);
		datInsData.setFormData(new JSONObject());
		datInsData.setFormNoList(new JSONArray());
		dhProcessInstance.setInsData(JSON.toJSONString(datInsData));
		dhProcessInstanceMapper.insertProcess(dhProcessInstance);  // 保存到数据库
		// 判断发起操作后是否有子流程
		boolean hasSubProcess = bpmRoutingDataAfterStartNode.getStartProcessNodes().size() > 0;
		DhProcessInstance processContainsFirstTask = dhProcessInstance;
		if (hasSubProcess) {
			processContainsFirstTask = createSubProcessAfterStartProcess(bpmRoutingDataAfterStartNode,
					dhProcessInstance, startProcessDataJson, datInsData);
		}
		// 创建第一个任务
		DhTaskInstance firstTaskInstance = new DhTaskInstance();
		firstTaskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + String.valueOf(UUID.randomUUID()));
		firstTaskInstance.setUsrUid(insInitUser);
		firstTaskInstance.setActivityBpdId(firstUserTaskNode.getActivityBpdId());
		firstTaskInstance.setTaskData("{}");
		firstTaskInstance.setTaskId(taskId);
		firstTaskInstance.setTaskTitle(firstUserTaskNode.getActivityName());
		firstTaskInstance.setInsUid(processContainsFirstTask.getInsUid());
		firstTaskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
		firstTaskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
		firstTaskInstance.setTaskInitDate(new Date());
		firstTaskInstance.setTaskActivityId(firstUserTaskNode.getActivityId());
		DhActivityConf conf = firstUserTaskNode.getDhActivityConf();
		Double timeAmount = 1.0;
		String timeUnit = "day";
		if (conf.getActcTime() != null && conf.getActcTime().doubleValue() != 1.0) {
			timeAmount = conf.getActcTime();
		}
		if (StringUtils.isNotBlank(conf.getActcTimeunit())) {
			timeUnit = conf.getActcTimeunit();
		}
		Date dueDate = sysHolidayService.calculateDueDate(firstTaskInstance.getTaskInitDate(), timeAmount, timeUnit);
		firstTaskInstance.setTaskDueDate(dueDate);
		dhTaskInstanceMapper.insertTask(firstTaskInstance);
		DatLaunchProcessResult launchProcessResult = new DatLaunchProcessResult(dhProcessInstance,
				processContainsFirstTask, firstTaskInstance);
		return ServerResponse.createBySuccess(launchProcessResult);
    }

    
}
