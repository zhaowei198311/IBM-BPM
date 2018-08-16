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
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhSynTaskRetryMapper;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartbpm.mq.rabbit.MqProducerService;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartportal.dao.DhAgentMapper;
import com.desmart.desmartportal.dao.DhAgentRecordMapper;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartportal.service.*;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Autowired
	private SysHolidayService sysHolidayService;
	@Autowired
	private BpmFormManageService bpmFormManageService;
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	@Autowired
	private DhApprovalOpinionService dhapprovalOpinionService;
	@Autowired
	private DhRouteService dhRouteService;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private DhStepService dhStepService;
	@Autowired
	private BpmFormFieldService bpmFormFieldService;
	@Autowired
	private DhDraftsMapper dhDraftsMapper;
    @Autowired
    private ThreadPoolProvideService threadPoolProvideService;
    @Autowired
    private DhRoutingRecordService dhRoutingRecordService;
    @Autowired
    private DhProcessInstanceService dhProcessInstanceService;
    @Autowired
    private MqProducerService mqProducerService;
	@Autowired
	private TaskMongoDao taskMongoDao;
	@Autowired
	private DhSynTaskRetryMapper dhSynTaskRetryMapper;
	@Autowired
	private DhFormNoService dhFormNoService;
	@Autowired
	private DhAgentMapper dhAgentMapper;
	@Autowired
	private DhAgentRecordMapper dhAgentRecordMapper;


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
			PageHelper.startPage(pageNum, pageSize,"task_init_date desc");
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
	 * 根据用户id 查询 有多少代办任务 js前台定时 任务 每隔多少秒 去 查询一次用户待办
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
							.queryBySelective(processInstance);// 根据instUid查询processList
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


    @Override
    @Transactional
    public ServerResponse commitFirstTask(int taskId, BpmActivityMeta firstHumanActivity, DhProcessInstance processContainFirstTask,
                                          BpmRoutingData routingData,
                                          CommonBusinessObject pubBo, JSONObject dataJson) {
        // 获得流流程编号,和第一个任务的编号
        int insId = processContainFirstTask.getInsId();
        pubBo.setInstanceId(String.valueOf(insId));
        JSONArray routeDataArr = dataJson.getJSONArray("routeData");
        // 设置后续任务处理人
        ServerResponse<CommonBusinessObject> assembleResponse = dhRouteService.assembleCommonBusinessObject(pubBo,
                routeDataArr);
        if (!assembleResponse.isSuccess()) {
            return assembleResponse;
        }
        // 创建第一个任务
        DhTaskInstance taskInstance = generateFirstTaskOfMainProcess(taskId, firstHumanActivity, dataJson.toJSONString(), processContainFirstTask);
        dhTaskInstanceMapper.insertTask(taskInstance); // 插入第一个任务

        // 创建第一个任务的流转信息
        DhRoutingRecord dhRoutingRecord =
                dhRoutingRecordService.generateSubmitTaskRoutingRecordByTaskAndRoutingData(taskInstance, routingData, true);

        // 完成第一个任务
        BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
        DhStep formStepOfTaskNode = dhStepService.getFormStepOfTaskNode(firstHumanActivity, processContainFirstTask.getInsBusinessKey());
        DhStep nextStep = dhStepService.getNextStepOfCurrStep(formStepOfTaskNode);

        DataForSubmitTask dataForSubmitTask = new DataForSubmitTask();
        dataForSubmitTask.setBpmGlobalConfig(globalConfig);
        dataForSubmitTask.setCurrTaskInstance(taskInstance);
        dataForSubmitTask.setCurrentProcessInstance(processContainFirstTask);
        dataForSubmitTask.setPubBo(pubBo);
        dataForSubmitTask.setBpmRoutingData(routingData);
        dataForSubmitTask.setDhRoutingRecord(dhRoutingRecord);
        dataForSubmitTask.setNextStep(nextStep);
        dataForSubmitTask.setApplyUser(getCurrentUserUid());
        return finishTaskOrSendToMq(dataForSubmitTask);
    }

    /**
     * 生成主流程第一个环节的任务
     * @param taskId  任务id
     * @param taskNode  任务节点
     * @param taskData  提交上来的数据
     * @param processContainsFirstTask  含有第一个任务的流程，不一定是主流程
     * @return
     */
    private DhTaskInstance generateFirstTaskOfMainProcess(int taskId, BpmActivityMeta taskNode, String taskData, DhProcessInstance processContainsFirstTask) {
        // 创建第一个任务实例，第一个任务一定属于主流程
        DhTaskInstance taskInstance = new DhTaskInstance();
        taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
        taskInstance.setUsrUid(getCurrentUserUid());
        taskInstance.setActivityBpdId(taskNode.getActivityBpdId());
        taskInstance.setTaskData(taskData);
        taskInstance.setTaskId(taskId);
        taskInstance.setTaskTitle(taskNode.getActivityName());
        taskInstance.setInsUid(processContainsFirstTask.getInsUid());
        taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
        taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
        taskInstance.setTaskInitDate(new Date());
        taskInstance.setTaskFinishDate(new Date());
        taskInstance.setTaskActivityId(taskNode.getActivityId());
        return taskInstance;
    }

    private String getCurrentUserUid() {
	    return String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
    }


	/* 
	 * 完成任务
	 */
	@Override
	@Transactional
	public ServerResponse perform(String data) {
		if (StringUtils.isBlank(data)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);

		JSONObject dataJson = JSONObject.parseObject(data);
		JSONObject taskData = JSONObject.parseObject(String.valueOf(dataJson.get("taskData"))); // 提交的任务总信息
		JSONObject processDataIn = JSONObject.parseObject(String.valueOf(dataJson.get("processData")));
		JSONObject formDataIn = JSONObject.parseObject(String.valueOf(dataJson.get("formData"))); // 表单数据
        JSONArray routeData = JSONObject.parseArray(String.valueOf(dataJson.get("routeData"))); // 选人信息

		String taskUid = taskData.getString("taskUid");
		if (StringUtils.isBlank(taskUid)) {
			return ServerResponse.createByErrorMessage("缺少任务标识参数");
		}
		// 根据任务标识和用户 去查询流程 实例
		DhTaskInstance currTask = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (currTask == null) {
			return ServerResponse.createByErrorMessage("当前任务不存在!");
		}
		int taskId = currTask.getTaskId();
		// 校验任务状态
		if (!DhTaskInstance.STATUS_RECEIVED.equals(currTask.getTaskStatus())) {// 检查任务是否重复提交
			return ServerResponse.createByErrorMessage("任务已经被提交或暂停");
		}
		// 校验任务类型
        String taskType = currTask.getTaskType();
		// 查看任务是否是被引擎认可的基本任务
		if (!(DhTaskInstance.TYPE_SIMPLE_LOOP.equals(taskType) || DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP.equalsIgnoreCase(taskType)
                || DhTaskInstance.TYPE_NORMAL.equals(taskType))) {
            return ServerResponse.createByErrorMessage("任务类型异常");
        }
        if (!canUserSumbitTask(currTask, currentUserUid)) {// 检查任务是否有权限提交
            return ServerResponse.createByErrorMessage("您没有完成任务的权限");
        }
        // 获得流程实例
        DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
        if (currProcessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }
        int insId = currProcessInstance.getInsId();
		JSONObject insDataJson = JSONObject.parseObject(currProcessInstance.getInsData());
		JSONObject processDataJson = insDataJson.getJSONObject("processData");
		// 检查是否需要更新insTitle
		if (canEditInsTitle(currTask, currProcessInstance)) {
			String insTitle = processDataIn.getString("insTitle");
			if (StringUtils.isBlank(insTitle) || insTitle.trim().length() > 30) {
				return ServerResponse.createByErrorMessage("流程标题未填写或过长异常");
			}
			currProcessInstance.setInsTitle(insTitle);
			processDataJson.put("insTitle", insTitle);
			insDataJson.put("processData", processDataJson);
		}
		// 整合formdata
		JSONObject mergedFormData = insDataJson.getJSONObject("formData"); // 实例中的表单数据
        if (StringUtils.isNotBlank(formDataIn.toJSONString())) {
            mergedFormData = FormDataUtil.formDataCombine(formDataIn, mergedFormData);
        }

		// 获得任务环节、配置
		BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
        if (currTaskNode == null) {
        	return ServerResponse.createByErrorMessage("获得环节信息失败");
		}
		DhActivityConf dhActivityConf = currTaskNode.getDhActivityConf();
		// 获得下个节点的路由信息
		BpmRoutingData bpmRoutingData = dhRouteService.getBpmRoutingData(currTaskNode, mergedFormData);
		// 检查用户传递的选人信息是否全面
		if (!dhRouteService.checkRouteData(currTaskNode, routeData, bpmRoutingData)) {
			return ServerResponse.createByErrorMessage("缺少下个环节的处理人信息");
		}

        // 根据配置保存审批意见
		if (needApprovalOpinion(currTaskNode, currProcessInstance)) {
			ServerResponse saveApprovalOpinionResponse = dhapprovalOpinionService.saveDhApprovalOpinionWhenSubmitTask(currTask, dhActivityConf, dataJson);
			if (!saveApprovalOpinionResponse.isSuccess()) { // 如果更新审批意见失败
				return saveApprovalOpinionResponse;
			}
		}
        // 修改当前任务实例状态为已完成
		this.updateDhTaskInstanceWhenFinishTask(currTask, data);
		//如果任务为类型为normal，则将其它相同任务id的任务废弃
		if(DhTaskInstance.TYPE_NORMAL.equals(currTask.getTaskType())) {
			dhTaskInstanceMapper.abandonOtherUnfinishedTaskByTaskId(taskUid, taskId);
		}
        //完成任务 删除任务的草稿数据
        dhDraftsMapper.deleteByTaskUid(taskUid);

		// 获得当前步骤
		DhStep formStepOfTaskNode = dhStepService.getFormStepOfTaskNode(currTaskNode, currProcessInstance.getInsBusinessKey());
		BpmForm bpmForm = bpmFormManageService.getByFormUid(formStepOfTaskNode.getStepObjectUid());
		// 更新表单号
        JSONArray formNoListJsonObject = dhFormNoService.updateFormNoListJsonObject(bpmForm, insDataJson.getJSONArray("formNoList"));

        // 更新流程实例信息
        insDataJson.put("formData", mergedFormData);
        insDataJson.put("formNoList", formNoListJsonObject);
        currProcessInstance.setInsUpdateDate(DateUtil.format(new Date()));
        currProcessInstance.setInsData(insDataJson.toJSONString());
        dhProcessInstanceMapper.updateByPrimaryKeySelective(currProcessInstance);

        // 判断Token是否移动
        boolean willTokenMove = dhRouteService.willFinishTaskMoveToken(currTask, insId);

        // 生成流转信息
		DhRoutingRecord routingRecord = dhRoutingRecordService.generateSubmitTaskRoutingRecordByTaskAndRoutingData(currTask, bpmRoutingData, willTokenMove);

        // ========================== 根据预判token是否移动，区别处理
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        if (!willTokenMove) {
            // 保存流转信息
            dhRoutingRecordMapper.insert(routingRecord);
            // 如果token不移动，调用完成任务的RESTful API
            BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
            HttpReturnStatus completeTaskResult = bpmTaskUtil.completeTask(taskId);
            if (!HttpReturnStatusUtil.isErrorResult(completeTaskResult)) {
                return ServerResponse.createBySuccess();
            } else {
                throw new PlatformException("调用RESTful API完成任务失败");
            }

        }

        // 装配处理人信息
        CommonBusinessObject pubBo = new CommonBusinessObject(insId);
        ServerResponse<CommonBusinessObject> serverResponse = dhRouteService.assembleCommonBusinessObject(pubBo, routeData);
        if (!serverResponse.isSuccess()) {
            throw new PlatformException("装配处理人信息失败，缺少下个环节处理人信息");
        }

        // 装配用户无法选择的处理人信息
		dhRouteService.assembleTaskOwnerForNodesCannotChoose(currTask, currProcessInstance, pubBo, bpmRoutingData);

		// 如果下个环节有循环任务（保存/更新）处理人信息
		dhRouteService.saveTaskHandlerOfLoopTask(insId, bpmRoutingData, pubBo);

        // 更新网关决策条件
        if (bpmRoutingData.getGatewayNodes().size() > 0) {
			ServerResponse updateResponse = threadPoolProvideService.updateRouteResult(insId, bpmRoutingData);
            if (!updateResponse.isSuccess()) {
                throw new PlatformException("更新网关决策中间表失败");
            }
        }

        // 判断后续有没有触发器需要调用
        DhStep nextStep = dhStepService.getNextStepOfCurrStep(formStepOfTaskNode);
        // 完成任务
        DataForSubmitTask dataForSubmitTask = new DataForSubmitTask();
        dataForSubmitTask.setBpmGlobalConfig(bpmGlobalConfig);
        dataForSubmitTask.setCurrTaskInstance(currTask);
        dataForSubmitTask.setCurrentProcessInstance(currProcessInstance);
        dataForSubmitTask.setPubBo(pubBo);
        dataForSubmitTask.setBpmRoutingData(bpmRoutingData);
        dataForSubmitTask.setDhRoutingRecord(routingRecord);
        dataForSubmitTask.setNextStep(nextStep);
        return finishTaskOrSendToMq(dataForSubmitTask);
    }

    @Override
	public ServerResponse finishTaskOrSendToMq(DataForSubmitTask dataForSubmitTask) {
        DhProcessInstance currProcessInstance = dataForSubmitTask.getCurrentProcessInstance();
        DhTaskInstance currTaskInstance = dataForSubmitTask.getCurrTaskInstance();
        int insId = currProcessInstance.getInsId();
        int taskId = currTaskInstance.getTaskId();
        String taskUid = currTaskInstance.getTaskUid();
        DhRoutingRecord dhRoutingRecord = dataForSubmitTask.getDhRoutingRecord();
        BpmRoutingData bpmRoutingData = dataForSubmitTask.getBpmRoutingData();
        CommonBusinessObject pubBo = dataForSubmitTask.getPubBo();

        if (dataForSubmitTask.getNextStep() == null) {
			BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(dataForSubmitTask.getBpmGlobalConfig());
			Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(taskId, pubBo, dataForSubmitTask.getApplyUser());
            Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
            if (errorMap.get("errorResult") == null) {
                ServerResponse<JSONObject> didTokenMoveResponse = dhRouteService.didTokenMove(insId, bpmRoutingData);
                if (didTokenMoveResponse.isSuccess()) {
                    JSONObject processData = didTokenMoveResponse.getData();
                    if (processData != null) {
                        // 确认Token移动了, 插入流转记录
                        dhRoutingRecordMapper.insert(dhRoutingRecord);
                        // 关闭需要结束的流程
                        dhProcessInstanceService.closeProcessInstanceByRoutingData(insId, bpmRoutingData, processData);
                        // 创建需要创建的子流程
                        dhProcessInstanceService.createSubProcessInstanceByRoutingData(currProcessInstance, bpmRoutingData, pubBo, processData);
                    } else {
                        // 确认Token没有移动, 更新流转信息
                        dhRoutingRecord.setActivityTo(null);
                        dhRoutingRecordMapper.insert(dhRoutingRecord);
                    }
                    return ServerResponse.createBySuccess();
                } else {
                    log.error("判断TOKEN是否移动失败，流程实例编号：" + insId + " 任务主键：" + taskUid);
                    return ServerResponse.createByErrorMessage("判断TOKEN是否移动失败");
                }
            } else {
                throw new PlatformException("调用RESTful API完成任务失败");
            }
		} else {
            // 有后续步骤, MQ交互
            // 向队列发出消息
            Map<String, Object> map = new HashMap<>();
            map.put("currProcessInstance", currProcessInstance);
            map.put("currTaskInstance", currTaskInstance);
            map.put("dhStep", dataForSubmitTask.getNextStep());
            map.put("pubBo", pubBo);
            map.put("routingData", bpmRoutingData); // 预判的下个环节信息
            map.put("routingRecord", dhRoutingRecord); // 流转记录
			map.put("applyUser", dataForSubmitTask.getApplyUser());
            mqProducerService.sendMessage(PropertiesUtil.getProperty("rabbitmq.routingKey.triggerStep", "triggerStepKey"), map);
            return ServerResponse.createBySuccess();
		}
	}


	/**
	 * 判断用户是否有权限向引擎提交任务
	 * 
	 * @param currDhTaskInstance
	 * @param currUserUid
	 * @return
	 */
	private boolean canUserSumbitTask(DhTaskInstance currDhTaskInstance, String currUserUid) {
		if (StringUtils.isNotBlank(currDhTaskInstance.getTaskDelegateUser())) {
			// 任务被代理了
			return currDhTaskInstance.getTaskDelegateUser().equals(currUserUid);
		} else {
			// 任务没有被代理
			return currDhTaskInstance.getUsrUid().equals(currUserUid);
		}
	}

	public boolean isTaskExists(int taskId) {
		if (taskId == 0) {
			return false;
		}
		return dhTaskInstanceMapper.countByTaskId(taskId) > 0;
	}

	@Transactional
	public ServerResponse<Map<String, Object>> toDealTask(String taskUid) {
	    Map<String, Object> resultMap = new HashMap<>();
	    if (StringUtils.isBlank(taskUid)) {
	        return ServerResponse.createByErrorMessage("缺少必要的参数");
	    }
	    DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
	    if (dhTaskInstance == null || !DhTaskInstance.STATUS_RECEIVED.equals(dhTaskInstance.getTaskStatus())) {
	        return ServerResponse.createByErrorMessage("任务不存在或任务状态异常");
	    }
		String currUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
	    SysUser currentUser = sysUserMapper.queryByPrimaryKey(currUserUid);
		if (!canUserSumbitTask(dhTaskInstance, currUserUid)) {
			return ServerResponse.createByErrorMessage("您没有提交此任务的权限");
		}
	    DhProcessInstance dhprocessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
	    if (dhprocessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }
	    // 获得当前环节
	    BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(dhTaskInstance.getTaskActivityId());
	    if (currTaskNode == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    DhActivityConf dhActivityConf = dhActivityConfMapper.selectByPrimaryKey(currTaskNode.getDhActivityConf().getActcUid());
		// 获得表单步骤
		List<DhStep> steps = dhStepService.getStepsWithFormByBpmActivityMetaAndStepBusinessKey(currTaskNode, dhprocessInstance.getInsBusinessKey());
		if (steps.isEmpty()) {
			return ServerResponse.createByErrorMessage("找不到表单步骤");
		}
		DhStep formStep = dhStepService.getFormStepOfStepList(steps); // 从步骤中条选出表单步骤
		// 获得表单信息
		BpmForm bpmForm = bpmFormManageService.getByFormUid(formStep.getStepObjectUid());
		if (bpmForm == null) {
			return ServerResponse.createByErrorMessage("找不到表单");
		}
		// 获得表单字段权限信息
		ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFieldPermissionByStepUid(formStep);
		if (!fieldPermissionResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("缺少表单权限信息");
		}
		String fieldPermissionInfo = fieldPermissionResponse.getData();
		// 调用表单前的触发器
		ServerResponse executeStepResponse = dhStepService.executeStepBeforeFormStep(steps.get(0), dhTaskInstance);
		if (!executeStepResponse.isSuccess()) {
			// 调用失败
			resultMap.put("didTriggerBeforeFormError", true);
		}
		// 触发器调用过后重新获取流程实例
		dhprocessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
		// 结合草稿中的数据
		DhDrafts dhDraft = dhDraftsMapper.queryDraftsByTaskUid(taskUid);
		JSONObject approvalData = null;
		String insDataStr = dhprocessInstance.getInsData();
		JSONObject insDataJson = JSON.parseObject(insDataStr);
		JSONObject formData = insDataJson.getJSONObject("formData"); // 做页面展示
		if(dhDraft != null &&  StringUtils.isNotBlank(dhDraft.getDfsData())) {
			JSONObject dfsDataJson = JSON.parseObject(dhDraft.getDfsData());
			// 获得草稿中的表单信息
			JSONObject dfsFormData = dfsDataJson.getJSONObject("formData");
			// 将流程实例中的表单信息集合草稿中的信息，但不需要更新到流程实例对象中
			insDataJson.put("formData", FormDataUtil.formDataCombine(dfsFormData, formData));
			dhprocessInstance.setInsData(insDataJson.toJSONString());
			approvalData = dfsDataJson.getJSONObject("approvalData");
		}
	    // 查看此环节特有的操作
	    // 查看能否编辑insTitle
        boolean canEditInsTitle = canEditInsTitle(dhTaskInstance, dhprocessInstance);
        // 查看是否能提交到驳回的环节
        DataForSkipFromReject dataForSkipFromReject = getDataForSkipFromReject(dhTaskInstance, dhprocessInstance);
		// 是否显示环节权责控制
		if (StringUtils.isBlank(dhActivityConf.getActcResponsibility())
				|| "<br>".equals(dhActivityConf.getActcResponsibility().trim())) {
			resultMap.put("showResponsibility", "FALSE");
		} else {
			resultMap.put("showResponsibility", "TRUE");
		}

		// 获得表单编号
		String formNo = dhFormNoService.findFormNoByFormUid(bpmForm.getDynUid(), insDataJson.getJSONArray("formNoList"));
		bpmForm.setFormNo(formNo);
        // 记录任务被打开
        taskMongoDao.saveOpenedTask(new OpenedTask(dhTaskInstance.getTaskUid(), dhTaskInstance.getTaskId(), new Date()));

        resultMap.put("approvalData", approvalData);
        resultMap.put("formData", formData);
        resultMap.put("bpmForm", bpmForm);
        resultMap.put("activityMeta", currTaskNode);
        resultMap.put("activityConf", dhActivityConf);
        resultMap.put("dhStep", formStep);
        resultMap.put("processInstance", dhprocessInstance);
	    resultMap.put("taskInstance", dhTaskInstance);
	    resultMap.put("fieldPermissionInfo",fieldPermissionInfo);
	    resultMap.put("canEditInsTitle", canEditInsTitle);
	    resultMap.put("dataForSkipFromReject", dataForSkipFromReject);
	    resultMap.put("needApprovalOpinion", needApprovalOpinion(currTaskNode, dhprocessInstance));
	    resultMap.put("currentUser", currentUser);
	    return ServerResponse.createBySuccess(resultMap);
	}


	@Override
	public List<DhTaskInstance> selectByCondition(DhTaskInstance dhTaskInstance) {
		return dhTaskInstanceMapper.selectByCondition(dhTaskInstance);
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
	 * 根据任务实例查询待办 任务数据和流程数据
	 */
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectTaskAndProcessInfo(DhTaskInstance taskInstance,
			Integer pageNum, Integer pageSize) {
		log.info("根据任务实例查询任务数据和流程数据 Start......");
		try {
			PageHelper.startPage(pageNum, pageSize);
			// 这里查询的是待办
			String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
			taskInstance.setUsrUid(creator);
			taskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
			List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectTaskAndProcessInfo(taskInstance);
			PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据任务实例查询任务数据和流程数据 END......");
		return null;
	}
	
	@Override
	public ServerResponse<Map<String,Object>> queryProgressBar(String activityId, String taskUid) {
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

		// DH_ACTIVITY_CONF对象
		DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
		// 审批最后日期
		Date lastDate = null;
		if (dhActivityConf != null) {
			timeAmount = dhActivityConf.getActcTime();
			timeType = dhActivityConf.getActcTimeunit();
			lastDate = dti.getTaskDueDate();
		}else {
			lastDate =  sysHolidayService.calculateDueDate(createDate, timeAmount, timeType);
		}
		
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
		long remainingTime = lastDate.getTime() - new Date().getTime();
		hour = (int) (remainingTime / (1000 * 60 * 60));
		// 如果最大剩余时间大于配置时间，则百分比为0，剩余时间为配置时间
		if (hour > timeAmount) {
			hour = timeAmount.intValue();
		} else {
//			hour = (int) (lastDate.getTime() - new Date().getTime()) / (1000 * 60 * 60);
			// 剩余时间百分比
//			long reTime = new Date().getTime() - createDate.getTime();
			percent = (int) ((1 - hour / timeAmount) * 100);
		}
		Map<String, Object> map = new HashMap<>();
		if (hour < 0 || percent >= 100) {
			hour = -1;
			percent = 100;
		}
		map.put("percent", percent);
		map.put("hour", hour);	
		return ServerResponse.createBySuccess(map);
	}

	@Override
	@Transactional
	public ServerResponse<?> addSure(DhTaskInstance dhTaskInstance, String creator) {
		try {
			// 当前任务的taskUid
			String currentTaskUid = dhTaskInstance.getTaskUid();
			DhTaskInstance dti = dhTaskInstanceMapper.selectByPrimaryKey(currentTaskUid);
			// 查询该任务是否还有会签任务未审批完成
			List<DhTaskInstance> checkList = dhTaskInstanceMapper.getByFromTaskUid(dhTaskInstance.getTaskUid());
			if (!checkList.isEmpty()) {
				return ServerResponse.createByErrorMessage("当前任务已添加会签人，请等会签人审批结束!");
			}
			// 说明 dhTaskInstance.getTaskActivityId() 实际值为 activityId
			String activityId = dhTaskInstance.getTaskActivityId();
			BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
			// 加新任务			
			dhTaskInstance.setActivityBpdId(bpmActivityMeta.getActivityBpdId());
			dhTaskInstance.setTaskTitle(dti.getTaskTitle());
			dhTaskInstance.setFromTaskUid(dhTaskInstance.getTaskUid());
			dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
			dhTaskInstance.setTaskInitDate(new Date());
			// usrUid集合
			String[] usrUids = dhTaskInstance.getUsrUid().split(";");
			// 已经加签人员姓名
			String completedSigning = "";
			// 会签人审批顺序，说明：如果会签方式是顺序会签，则第一人taskType为12，其他人员taskType为暂停状态
			int num = 1;
			// 下一个加签任务taskUid
			String toTaskUid = "";
			
			for (String userUid : usrUids) {
				SysUser sysUser = sysUserMapper.queryByPrimaryKey(userUid);
				dhTaskInstance.setUsrUid(sysUser.getUserId());
				// 验证当前人员是否已经加签
				DhTaskInstance checkDhTaskInstance = dhTaskInstanceMapper.getByUserAndFromTaskUid(dhTaskInstance);
				if (checkDhTaskInstance == null) {
					if(num==1) {
						toTaskUid = "task_instance:"+UUID.randomUUID();
						dhTaskInstance.setTaskUid("task_instance:"+UUID.randomUUID());
					}else {
						dhTaskInstance.setTaskUid(toTaskUid);
						toTaskUid = "task_instance:"+UUID.randomUUID();
					}
					// normalAdd:随机加签; simpleLoopAdd：顺序加签; multiInstanceLoopAdd:并行加签
					if (DhTaskInstance.TYPE_SIMPLE_LOOPADD.equals(dhTaskInstance.getTaskType())) {
						if(num==usrUids.length) {
							dhTaskInstance.setToTaskUid("");
						}else {
							dhTaskInstance.setToTaskUid(toTaskUid);
						}
						if (num > 1) {
							dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_WAIT_ALL_ADD_FINISH);
						}/*else {
							dhTaskInstance.setToTaskUid(taskUid);
						}*/
					}
					dhTaskInstanceMapper.insertTask(dhTaskInstance);
					num++;
				}else {
					completedSigning += sysUser.getUserUid()+",";
				}	
			}
			// 如果会签人全部都已经会签过，则直接返回
			String[] check = completedSigning.split(",");
			if (Arrays.equals(usrUids, check)) {
				return ServerResponse.createByErrorMessage(completedSigning.substring(0, completedSigning.length()-1)+"已经会签过!");
			}

			// 路由表记录
			DhRoutingRecord dhRoutingRecord = dhRoutingRecordService.generateAddTaskRoutingRecord(dhTaskInstance);
			dhRoutingRecordMapper.insert(dhRoutingRecord);

			// 会签开始时，保存主任务的审批剩余时间
			sysHolidayService.remainHour(currentTaskUid, activityId);
			
			// 将当前任务暂挂
			DhTaskInstance currentDhTaskInstance = new DhTaskInstance();
			currentDhTaskInstance.setTaskUid(currentTaskUid);
			currentDhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_WAIT_ALL_ADD_FINISH);
			dhTaskInstanceMapper.updateByPrimaryKeySelective(currentDhTaskInstance);
			if (!completedSigning.isEmpty()) {
				return ServerResponse.createByErrorMessage(completedSigning.substring(0, completedSigning.length()-1)+"已经会签过,其他人员操作成功!");
			}
			return ServerResponse.createBySuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage(e.getMessage());
		}	
	}
	


	@Override
	public ServerResponse<Map<String, Object>> toFinshedTaskDetail(String taskUid) {
		Map<String, Object> resultMap = new HashMap<>();
	    
	    if (StringUtils.isBlank(taskUid)) {
	        return ServerResponse.createByErrorMessage("缺少必要的参数");
	    }
	    DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
	    String currUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
	    if (dhTaskInstance == null || !DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
	      if(!DhTaskInstance.STATUS_WAIT_ALL_ADD_FINISH.equals(dhTaskInstance.getTaskStatus())) {
	    	  if(dhTaskInstance.getTaskDelegateUser()!=null 
					&& !"".equals(dhTaskInstance.getTaskDelegateUser())
					&& !currUserUid.equals(dhTaskInstance.getTaskDelegateUser())
				) {
	    		  dhTaskInstance.setTaskStatus("-2");
	    	  }else {
	    		  return ServerResponse.createByErrorMessage("任务不存在或任务状态异常"); 
	    	  }
	      }
	    }
	    DhProcessInstance dhprocessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
	    if (dhprocessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }
	    JSONObject insDataJson = JSONObject.parseObject(dhprocessInstance.getInsData());
	    
	    // 获得当前环节
	    BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(dhTaskInstance.getTaskActivityId());
	    if (currTaskNode == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    
	    DhStep formStep = dhStepService.getFormStepOfTaskNode(currTaskNode, dhprocessInstance.getInsBusinessKey());

		BpmForm bpmForm = bpmFormManageService.getByFormUid(formStep.getStepObjectUid());
        if (bpmForm == null) {
            return ServerResponse.createByErrorMessage("缺少表单");
        }

        ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFinshedFieldPerMissionByStepUid(formStep);
        if (!fieldPermissionResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单权限信息");
        }
        String fieldPermissionInfo = fieldPermissionResponse.getData();

        // 判断任务是否能被取回
        DataForRevoke dataForRevoke = getDataForRevoke(dhTaskInstance, currTaskNode, dhprocessInstance);
     	// 获得表单编号
     	String formNo = dhFormNoService.findFormNoByFormUid(bpmForm.getDynUid(), insDataJson.getJSONArray("formNoList"));
     	bpmForm.setFormNo(formNo);

        resultMap.put("bpmForm", bpmForm);
        resultMap.put("activityMeta", currTaskNode);
        resultMap.put("activityConf", currTaskNode.getDhActivityConf());
        resultMap.put("dhStep", formStep);
        resultMap.put("processInstance", dhprocessInstance);
	    resultMap.put("taskInstance", dhTaskInstance);
	    resultMap.put("fieldPermissionInfo",fieldPermissionInfo);
	    resultMap.put("canBeRevoke", dataForRevoke != null ? "TRUE" : "FALSE");
	    return ServerResponse.createBySuccess(resultMap);
	}

	@Override
	public ServerResponse<Map<String, Object>> toAddSign(String taskUid) {
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
	    
	    // 获得当前环节
	    BpmActivityMeta currMeta = bpmActivityMetaMapper.queryByPrimaryKey(dhTaskInstance.getTaskActivityId());
	    if (currMeta == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    
	    DhStep formStep = dhStepService.getFormStepOfTaskNode(currMeta, dhprocessInstance.getInsBusinessKey());
	    if (formStep == null) {
            return ServerResponse.createByErrorMessage("找不到表单步骤");
        }
		BpmForm bpmForm = bpmFormManageService.getByFormUid(formStep.getStepObjectUid());
        if (bpmForm == null) {
            return ServerResponse.createByErrorMessage("缺少表单");
        }

        ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFinshedFieldPerMissionByStepUid(formStep);
        if (!fieldPermissionResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单权限信息");
        }
        // 是否显示环节权责控制
        if (StringUtils.isBlank(currMeta.getDhActivityConf().getActcResponsibility()) 
        		|| "<br>".equals(currMeta.getDhActivityConf().getActcResponsibility())) {
            resultMap.put("showResponsibility", "FALSE");
        } else {
            resultMap.put("showResponsibility", "TRUE");
        }
        String fieldPermissionInfo = fieldPermissionResponse.getData();
        
        resultMap.put("bpmForm", bpmForm);
        resultMap.put("activityMeta", currMeta);
        resultMap.put("activityConf", currMeta.getDhActivityConf());
        resultMap.put("dhStep", formStep);
        resultMap.put("processInstance", dhprocessInstance);
	    resultMap.put("taskInstance", dhTaskInstance);
	    resultMap.put("fieldPermissionInfo",fieldPermissionInfo);
	    return ServerResponse.createBySuccess(resultMap);
	}
	
	@Transactional
	@Override
	public ServerResponse<?> finishAdd(String taskUid, String activityId, String approvalContent) {
		try {
			// 当前任务
			DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
			if (dhTaskInstance == null) return ServerResponse.createByErrorMessage("任务不存在");

			// 判断taskType类型
			String type = dhTaskInstance.getTaskType();

			// 插入审批意见
            ServerResponse saveApprovalResponse = dhapprovalOpinionService.saveDhApprovalOpinionWhenFinishAdd(dhTaskInstance, approvalContent);
            if (!saveApprovalResponse.isSuccess()) {
                return saveApprovalResponse;
            }

            // 保存流转记录
            DhRoutingRecord dhRoutingRecord = dhRoutingRecordService.generateFinishAddTaskRoutingRecord(dhTaskInstance);
			dhRoutingRecordMapper.insert(dhRoutingRecord);

			// 说明：如果taskType为 normalAdd，则一人完成加签即可
			if (DhTaskInstance.TYPE_NORMAL_ADD.equals(type)) {
				// 将主任务状态回归到正常状态
				DhTaskInstance task = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getFromTaskUid());
				// 重新计算剩余时间
				long dueDate = task.getTaskDueDate().getTime() + (long) (task.getRemainHours() * 60 * 60 * 1000);
				task.setTaskDueDate(new Date(dueDate));
				task.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
				dhTaskInstanceMapper.updateByPrimaryKeySelective(task);
				// 将当前任务关闭，其他会签人任务作废
				List<DhTaskInstance> dhTaskInstanceList = dhTaskInstanceMapper.getByFromTaskUid(dhTaskInstance.getFromTaskUid());
				for (DhTaskInstance taskInstance : dhTaskInstanceList) {
					if (dhTaskInstance.getTaskUid().equals(taskInstance.getTaskUid())) {
						dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
						dhTaskInstance.setTaskFinishDate(new Date());
						dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
					}else {
						taskInstance.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
						dhTaskInstanceMapper.updateByPrimaryKeySelective(taskInstance);
					}
				}
			}
			// 说明： 如果taskType为simpleLoopAdd，则按照加签人审批顺序进行
			if (DhTaskInstance.TYPE_SIMPLE_LOOPADD.equals(type)) {
				// 将下一个会签审批任务回归到正常状态
				//DhTaskInstance nextDhTaskInstance = dhTaskInstanceMapper.getByToTaskUid(taskUid);
				DhTaskInstance nextDhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getToTaskUid());
				if (nextDhTaskInstance != null) {
					nextDhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKeySelective(nextDhTaskInstance);
					// 将当前任务关闭
					dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
					dhTaskInstance.setTaskFinishDate(new Date());
					dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
				}else {
					// 将主任务状态回归到正常状态
					DhTaskInstance task = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getFromTaskUid());
					// 重新计算剩余时间
					long dueDate = task.getTaskDueDate().getTime() + (long) (task.getRemainHours() * 60 * 60 * 1000);
					task.setTaskDueDate(new Date(dueDate));
					task.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKeySelective(task); // 更新源任务
					// 将当前任务关闭
					dhTaskInstance.setTaskFinishDate(new Date());
					dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
					dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance); //更新加签任务
				}
			}
			// 说明：如果taskType为multiInstanceLoopAdd,则需要主任务所有会签任务都审批完，主任务方可回归正常状态
			if (DhTaskInstance.TYPE_MULTI_INSTANCE_LOOPADD.equals(type)) {
				// 将当前任务关闭
				dhTaskInstance.setTaskFinishDate(new Date());
				dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
				dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
				// 查询主任务的所有会签任务是否都已审批完成
				List<DhTaskInstance> dhTaskInstanceList = dhTaskInstanceMapper.getByFromTaskUid(dhTaskInstance.getFromTaskUid());
				if (dhTaskInstanceList.isEmpty()) {
					// 将主任务状态回归到正常状态
					DhTaskInstance task = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getFromTaskUid());
					// 重新计算剩余时间
					long dueDate = task.getTaskDueDate().getTime() + (long) (task.getRemainHours() * 60 * 60 * 1000);
					task.setTaskDueDate(new Date(dueDate));
					task.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKeySelective(task);
				}
			}
			// 会签结束时，查询主任务剩余时间，重新计算剩余时间
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
		return ServerResponse.createBySuccess();
	}
	
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByCondition(Date startTime, Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize,String isAgent) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_INIT_DATE DESC");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectBackLogTaskInfoByCondition(startTime, endTime, dhTaskInstance, isAgent);
		for(DhTaskInstance resultTaskInstance:resultList) {
			String taskHandler = sysUserMapper.queryByPrimaryKey(resultTaskInstance.getUsrUid()).getUserName();
			resultTaskInstance.setTaskHandler(taskHandler);
			//判断代理人是否为空
			if(null!=resultTaskInstance.getTaskDelegateUser() && !"".equals(resultTaskInstance.getTaskDelegateUser())) {
				String taskAgentUserName = sysUserMapper.queryByPrimaryKey(resultTaskInstance.getTaskDelegateUser()).getUserName();
				resultTaskInstance.setTaskAgentUserName(taskAgentUserName);
			}
		}
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByConditionMove(Date startTime, Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize,String isAgent) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_INIT_DATE DESC");
		//分页查询待办列表
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectBackLogTaskInfoByCondition(startTime, endTime, dhTaskInstance, isAgent);
		for(DhTaskInstance resultTaskInstance:resultList) {
			if(resultTaskInstance.getTaskType().equals("normal")) {
				Map<String,Object> progressMap = queryProgressBar(resultTaskInstance.getTaskActivityId()
						,resultTaskInstance.getTaskUid()).getData();
				resultTaskInstance.setProgerssParamMap(progressMap);
			}
		}
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByCondition(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize, String isAgent) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_FINISH_DATE DESC");
		dhTaskInstance.setTaskStatus("'32'");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByClosedByCondition(startTime, endTime, dhTaskInstance, isAgent);
		for(DhTaskInstance resultTaskInstance:resultList) {
			String taskHandler = sysUserMapper.queryByPrimaryKey(resultTaskInstance.getUsrUid()).getUserName();
			resultTaskInstance.setTaskHandler(taskHandler);
			//判断代理人是否为空
			if(null!=resultTaskInstance.getTaskDelegateUser() && !"".equals(resultTaskInstance.getTaskDelegateUser())) {
				String taskAgentUserName = sysUserMapper.queryByPrimaryKey(resultTaskInstance.getTaskDelegateUser()).getUserName();
				resultTaskInstance.setTaskAgentUserName(taskAgentUserName);
			}
		}
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByConditionMove(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize, String isAgent) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_FINISH_DATE DESC");
		dhTaskInstance.setTaskStatus("'32'");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByClosedByConditionMove(startTime, endTime, dhTaskInstance, isAgent);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByEndByConditionMove(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize, String isAgent) {
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_FINISH_DATE DESC");
		dhTaskInstance.setTaskStatus("'32'");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByEndByConditionMove(startTime, endTime, dhTaskInstance, isAgent);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	/**
	 * 取回代理任务
	 */
	@Transactional
	@Override
	public ServerResponse revokeAgentOutTask() {
		//获得当前用户id
		String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//根据当前用户id获得当前用户的代理信息(代理和被代理)
		List<DhAgent> dhAgentList = dhAgentMapper.queryAgentOutByUserUid(currentUserUid);
		//根据代理信息id获得要取回的代办任务id集合
		List<String> revokeTaskUidList = new ArrayList<>();
		if(!dhAgentList.isEmpty()) {
			revokeTaskUidList = dhAgentRecordMapper.queryAgentRecordListByAgentList(dhAgentList);
		}
		if(!revokeTaskUidList.isEmpty()) {
			int renokeCount = dhTaskInstanceMapper.updateDelegateUserBatch(revokeTaskUidList);
			if(renokeCount!=revokeTaskUidList.size()) {
				throw new PlatformException("取回过期代理任务失败");
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public Integer alreadyClosedTaskByusrUid(String userId) {
		return dhTaskInstanceMapper.alreadyClosedTaskByusrUid(userId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> queryTransfer(Date startTime, Date endTime,
											DhTaskInstance dhTaskInstance,Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DhTaskInstance> dhTaskInstanceList = dhTaskInstanceMapper.queryTransferByTypeAndStatus(startTime, endTime, dhTaskInstance);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(dhTaskInstanceList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public Integer queryTransferNumber(DhTaskInstance dhTaskInstance) {
		return dhTaskInstanceMapper.queryTransferNumberByusrUid(dhTaskInstance);
	}

	@Override
	public ServerResponse<?> updateTaskStatusOfTransfer(String taskUid) {
		DhTaskInstance dhTaskInstance = new DhTaskInstance();
		dhTaskInstance.setTaskUid(taskUid);
		dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		dhTaskInstance.setTaskDueDate(new Date());
		int count = dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
		if (count > 0) {
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}

	@Override
	public ServerResponse<?> transferSure(String taskUid, String usrUid, String activityId) {
		// 当前任务实例
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		dhTaskInstance.setTaskType(DhTaskInstance.TYPE_TRANSFER);
		dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
		dhTaskInstance.setFromTaskUid(dhTaskInstance.getTaskUid());
		dhTaskInstance.setTaskInitDate(new Date());
		dhTaskInstance.setTaskDueDate(null);
		// 抄送人员
		String[] usrUidArray = usrUid.split(";");
		// 错误提示
		String errorInformation = "";
		//SysUser sysUser = new SysUser();
		for (String userUid : usrUidArray) {
			//sysUser.setUserName(string);
			SysUser sysUser = sysUserMapper.queryByPrimaryKey(userUid);
			dhTaskInstance.setUsrUid(sysUser.getUserId());
			DhTaskInstance dti = dhTaskInstanceMapper.getBytaskTypeAndUsrUid(dhTaskInstance);
			if (dti != null) {
				errorInformation += sysUser.getUserUid() + ",";
			}else {
				dhTaskInstance.setTaskUid("task_instance:" + UUID.randomUUID());
				dhTaskInstanceMapper.insertTask(dhTaskInstance);
			}
		}
		// 如果会签人全部都已经会签过，则直接返回
		String[] check = errorInformation.split(",");
		if (Arrays.equals(usrUidArray, check)) {
			return ServerResponse.createByErrorMessage(errorInformation.substring(0, errorInformation.length()-1)+"已经抄送过!");
		}

		if (!errorInformation.isEmpty()) {
			return ServerResponse.createByErrorMessage(errorInformation.substring(0, errorInformation.length()-1) + "已经抄送过,其他人员操作成功!");
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByStartProcess(
												Integer pageNum, Integer pageSize, String insUid, String usrUid) {
//		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        DhTaskInstance dhTaskInstance = new DhTaskInstance();
        dhTaskInstance.setInsUid(insUid);
		dhTaskInstance.setUsrUid(usrUid);
//        DhProcessInstance dhProcessInstance = new DhProcessInstance();
//        DhProcessDefinition processDefintion = dhProcessDefinitionService
//				.getStartAbleProcessDefinition(proAppId,
//						proUid);
//        if(processDefintion==null) {
//        	return ServerResponse.createByErrorMessage("该流程版本有异常");
//        			}
//        dhProcessInstance.setProAppId(proAppId);
//        dhProcessInstance.setProUid(proUid);
//        dhProcessInstance.setProVerUid(processDefintion.getProVerUid());
//        dhProcessInstance.setInsStatusId(insStatusId);
//        if(insTitle!=null && !"".equals(insTitle)) {
//        		dhProcessInstance.setInsTitle(insTitle);
//        	}
//        if(insInitUser!=null && "current".equals(insInitUser)) {
//    		dhProcessInstance.setInsInitUser(currentUserUid);
//    	}
//        dhTaskInstance.setDhProcessInstance(dhProcessInstance);
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_FINISH_DATE DESC,TASK_FINISH_DATE DESC");
		dhTaskInstance.setTaskStatus("'12','-2','32'");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByClosedByCondition(null, null, dhTaskInstance, null);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

    @Override
	public boolean canEditInsTitle(DhTaskInstance taskInstance, DhProcessInstance processInstance) {
        // 如果任务处于子流程或主流程的第一个人工环节，并且不是被驳回的，可以修改流程标题
        BpmActivityMeta taskNode = bpmActivityMetaMapper.queryByPrimaryKey(taskInstance.getTaskActivityId());
        BpmActivityMeta startNode = null;
        if (processInstance.getTokenActivityId() == null) {
            // 流程是主流程
            startNode = bpmActivityMetaService.getStartNodeOfMainProcess(processInstance.getProAppId(),
                    processInstance.getProUid(), processInstance.getProVerUid());
        } else {
            // 流程是子流程
            BpmActivityMeta subProcessNode = bpmActivityMetaService.queryByPrimaryKey(processInstance.getTokenActivityId());
            if (subProcessNode == null) return false;
            startNode = bpmActivityMetaService.getStartNodeOfSubProcess(subProcessNode);
        }

        String activityTo = startNode.getActivityTo();
        if (StringUtils.isBlank(activityTo) || !activityTo.contains(taskNode.getActivityBpdId())) return false;
        // 判断是否是初次提交，即这个节点的任务有没有被完成过
        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setTaskActivityId(taskInstance.getTaskActivityId());
        taskSelective.setInsUid(taskInstance.getInsUid());
        taskSelective.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
        int count = dhTaskInstanceMapper.selectAllTask(taskSelective).size();
        return count == 0 ? true : false;
    }

	@Override
	@Transactional
	public ServerResponse rejectTask(String data) {
		JSONObject dataJson = JSONObject.parseObject(data);
		JSONObject routeData = JSONObject.parseObject(String.valueOf(dataJson.get("routeData")));  // 驳回至环节信息
		JSONObject.parseObject(String.valueOf(dataJson.get("approvalData"))); // 驳回意见
		JSONObject taskData = JSONObject.parseObject(String.valueOf(dataJson.get("taskData"))); // 任务信息
		String taskOwner = routeData.getString("userUid");  // 驳回给谁
		if (StringUtils.isBlank(taskOwner) || sysUserMapper.queryByPrimaryKey(taskOwner.trim()) == null) {
			return ServerResponse.createByErrorMessage("缺少驳回后处理人信息，或查找驳回后处理人不存在于系统");
		}

		// 目标环节
		String targetActivityBpdId = routeData.getString("activityBpdId");
		String taskUid = taskData.getString("taskUid");

		// 判断驳回的任务是否存在
		if (StringUtils.isBlank(taskUid)) {
			return ServerResponse.createByErrorMessage("缺少任务标识信息");
		}
		DhTaskInstance currTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (currTaskInstance == null || !"12".equals(currTaskInstance.getTaskStatus())) {
			return ServerResponse.createByErrorMessage("任务不存在或状态异常，不是待处理状态");
		}

		String currActivityId = currTaskInstance.getTaskActivityId();
		BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(currActivityId);

		// 判断当前节点能否驳回, 如果不是普通节点不允许驳回
		if (!"TRUE".equals(currTaskNode.getDhActivityConf().getActcCanReject())
				|| !"none".equals(currTaskNode.getLoopType())) {
			return ServerResponse.createByErrorMessage("当前节点不允许驳回");
		}

		DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTaskInstance.getInsUid());
		if (currProcessInstance == null) {
			return ServerResponse.createByErrorMessage("流程实例不存在");
		}

		int taskId = currTaskInstance.getTaskId();
		int insId = currProcessInstance.getInsId();

		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		// 获取树流程信息
		HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
		if(HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
			return ServerResponse.createByErrorMessage("查询树流程信息出错");
		}

		// 获得任务的token
		JSONObject jsonObject = JSONObject.parseObject(processDataReturnStatus.getMsg());
		String tokenId = ProcessDataUtil.getTokenIdOfTask(taskId, jsonObject);
		if (StringUtils.isBlank(tokenId)) {
			return ServerResponse.createByErrorMessage("驳回失败，找不到tokenID");
		}

		// 获得目标节点
		BpmActivityMeta targetNode = bpmActivityMetaService
				.getByActBpdIdAndParentActIdAndProVerUid(targetActivityBpdId, currTaskNode.getParentActivityId()
						, currTaskNode.getSnapshotId());
		if (targetNode == null) {
			return ServerResponse.createByErrorMessage("找不到目标环节");
		}
		if (!"none".equals(targetNode.getLoopType())) {
			return ServerResponse.createByErrorMessage("目标环节不支持被驳回，任务类型异常");
		}

		// 完成现在的任务, 驳回也算一种完成
		updateDhTaskInstanceWhenFinishTask(currTaskInstance, data);

		// 关闭关联的任务（同一个节点上的任务）
		abandonRelationTaskOnTaskNode(currTaskInstance);

		// 保存审批意见
		dhapprovalOpinionService.saveDhApprovalOpinionWhenRejectTask(currTaskInstance, dataJson);

		// 保存流转记录
		DhRoutingRecord dhRoutingRecord = dhRoutingRecordService.generateRejectTaskRoutingRecordByTaskAndRoutingData(currTaskInstance, targetNode);
		dhRoutingRecordMapper.insert(dhRoutingRecord);

		// 移动token
		HttpReturnStatus httpReturnStatus = bpmProcessUtil.moveToken(insId, targetActivityBpdId, tokenId);
		if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
			throw new PlatformException("调用API 驳回失败");
		}

		// API 调用成功，重新获得流程实例信息
		JSONObject processData = JSON.parseObject(httpReturnStatus.getMsg());
		// 驳回节点上产生的任务
		List<Integer> taskIdList = ProcessDataUtil.getActiveTaskIdByFlowObjectId(targetActivityBpdId, processData);
		// 锁任务，阻止拉取
		taskMongoDao.batchlockTasks(taskIdList, LockedTask.REASON_REJECT_TASK);

		// 重新分配任务
		// 获得任务处理人
		BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
		ServerResponse serverResponse = taskUtil.changeOwnerOfLaswTask(taskIdList.get(0), taskOwner);
		if (!serverResponse.isSuccess()) {
			log.error("重新分配失败");
			return ServerResponse.createByErrorMessage("驳回失败");
		}
		// 记录到重拉列表，等待再次拉取
		saveTaskToRetryTable(taskIdList.get(0));
		// 解锁任务
		unlockTask(taskIdList.get(0));

        return ServerResponse.createBySuccess();
	}

	/**
	 * 将任务从锁住的任务中去除
	 * @param taskId
	 * @return
	 */
	private int unlockTask(int taskId) {
		return taskMongoDao.removeLockedTask(taskId);
	}

	/**
	 * 作废与当前任务相关的关联任务（同一个节点上）
	 * @param taskInstance
	 */
	@Override
	public Integer abandonRelationTaskOnTaskNode(DhTaskInstance taskInstance) {
		return dhTaskInstanceMapper.abandonOtherUnfinishedTasksOnTaskActivityId(taskInstance.getTaskUid(), taskInstance.getTaskActivityId(),
				taskInstance.getInsUid());
	}

	/**
	 * 完成任务时，
	 * 1.更新任务实例状态为关闭
	 * 2.更新提交内容
	 * 3.更新任务完成时间
	 * @param dhTaskInstance 任务实例
	 * @param taskData 提交上来的所有数据
	 * @return
	 */
	@Override
	public int updateDhTaskInstanceWhenFinishTask(DhTaskInstance dhTaskInstance, String taskData) {
		// 修改当前任务实例状态为已完成
		DhTaskInstance taskInstanceSelective = new DhTaskInstance();
		taskInstanceSelective.setTaskUid(dhTaskInstance.getTaskUid());
		taskInstanceSelective.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		taskInstanceSelective.setTaskFinishDate(new Date());
		taskInstanceSelective.setTaskData(taskData);
		return dhTaskInstanceMapper.updateByPrimaryKeySelective(taskInstanceSelective);
	}

	@Override
	public int updateDhTaskInstanceWhenAutoCommit(DhTaskInstance dhTaskInstance, String originalUser) {
		// 修改当前任务实例状态为已完成
		DhTaskInstance taskInstanceSelective = new DhTaskInstance();
		taskInstanceSelective.setTaskUid(dhTaskInstance.getTaskUid());
		taskInstanceSelective.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		taskInstanceSelective.setTaskFinishDate(new Date());
		taskInstanceSelective.setTaskData("{}");
		taskInstanceSelective.setUsrUid(originalUser);  // 任务还给初始人
		return dhTaskInstanceMapper.updateByPrimaryKeySelective(taskInstanceSelective);
	}

	/**
	 * 在重试拉取任务列表中插入一条数据
	 * @param taskId  任务编号
	 * @return
	 */
	@Override
	public int saveTaskToRetryTable(int taskId) {
		DhSynTaskRetry dhSynTaskRetry = new DhSynTaskRetry();
		dhSynTaskRetry.setId(EntityIdPrefix.DH_SYN_TASK_RETRY + UUID.randomUUID().toString());
		dhSynTaskRetry.setTaskId(taskId);
		dhSynTaskRetry.setRetryCount(0);
		dhSynTaskRetry.setStatus(0);
		return dhSynTaskRetryMapper.insert(dhSynTaskRetry);
	}

	@Override
	@Transactional
	public ServerResponse revokeTask(String taskUid) {
		if (StringUtils.isBlank(taskUid)) {
			return ServerResponse.createByErrorMessage("参数异常");
		}
        String currUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);

		DhTaskInstance finishedTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (finishedTaskInstance == null || !DhTaskInstance.STATUS_CLOSED.equals(finishedTaskInstance.getTaskStatus())) {
			return ServerResponse.createByErrorMessage("任务不存在，或状态异常");
		}

        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(finishedTaskInstance.getInsUid());
        if (dhProcessInstance == null) return ServerResponse.createByErrorMessage("流程实例不存在");

		String finishedActivityId = finishedTaskInstance.getTaskActivityId();
		BpmActivityMeta finishedTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(finishedActivityId);

        DataForRevoke dataForRevoke = getDataForRevoke(finishedTaskInstance, finishedTaskNode, dhProcessInstance);
        if (dataForRevoke == null) {
            return ServerResponse.createByErrorMessage("任务不可取回");
        }

        // 作废现在的任务
        abandonDhTaskInstance(dataForRevoke.getCurrTaskInstances());

        // 增加一条流转记录
        DhRoutingRecord routingRecord = dhRoutingRecordService.generateRevokeTaskRoutingRecord(finishedTaskInstance);
        dhRoutingRecordMapper.insert(routingRecord);

        // 调用RESTful API 取回
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus httpReturnStatus = bpmProcessUtil.moveToken(dataForRevoke.getInsId(), dataForRevoke.getTargetActivityBpdId(),
                dataForRevoke.getTokenId());
        if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
            throw new PlatformException("调用API 取回失败");
        }
        // API 调用成功的情况，获取新任务
        JSONObject processData = JSON.parseObject(httpReturnStatus.getMsg());
        List<Integer> taskIdList = ProcessDataUtil.getActiveTaskIdByFlowObjectId(dataForRevoke.getTargetActivityBpdId(), processData);
        // 阻止拉取任务
        taskMongoDao.batchlockTasks(taskIdList, LockedTask.REASON_REVOKE_TASK);
        // 重新分配
        BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
        ServerResponse serverResponse = taskUtil.changeOwnerOfLaswTask(taskIdList.get(0), currUserUid);
        if (!serverResponse.isSuccess()) {
            log.error("重新分配失败");
        }

        saveTaskToRetryTable(taskIdList.get(0));
        unlockTask(taskIdList.get(0));

		return ServerResponse.createBySuccess();
	}

	public ServerResponse skipFromReject(String data) {
		if (StringUtils.isBlank(data)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);

		JSONObject dataJson = JSONObject.parseObject(data);
		JSONObject taskData = JSONObject.parseObject(String.valueOf(dataJson.get("taskData")));
		JSONObject formDataIn = JSONObject.parseObject(String.valueOf(dataJson.get("formData")));

		String taskUid = taskData.getString("taskUid");
		// 根据任务标识和用户 去查询流程 实例
		DhTaskInstance currTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (currTaskInstance == null) {
			return ServerResponse.createByErrorMessage("当前任务不存在!");
		}
		// 校验任务状态
		if (!"12".equals(currTaskInstance.getTaskStatus())) {// 检查任务是否重复提交
			return ServerResponse.createByErrorMessage("任务已经被提交或暂停");
		}
		// 校验任务类型
		String taskType = currTaskInstance.getTaskType();
		if (!(DhTaskInstance.TYPE_SIMPLE_LOOP.equals(taskType) || DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP.equalsIgnoreCase(taskType)
				|| DhTaskInstance.TYPE_NORMAL.equals(taskType))) {
			return ServerResponse.createByErrorMessage("任务类型异常");
		}
		if (!canUserSumbitTask(currTaskInstance, currentUserUid)) {// 检查任务是否有权限提交
			return ServerResponse.createByErrorMessage("提交失败，您没有完成任务的权限");
		}

		BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(currTaskInstance.getTaskActivityId());

		// 获得流程实例
		DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTaskInstance.getInsUid());
		if (currProcessInstance == null) {
			return ServerResponse.createByErrorMessage("流程实例不存在");
		}

		// 获取跳转需要的信息
        DataForSkipFromReject dataForSkipFromReject = getDataForSkipFromReject(currTaskInstance, currProcessInstance);
		if (dataForSkipFromReject == null) {
		    return ServerResponse.createByErrorMessage("提交到驳回环节失败");
        }

        // 更新流程实例信息
        // 整合formdata
        JSONObject insJson = JSONObject.parseObject(currProcessInstance.getInsData());
        JSONObject mergedFormData = insJson.getJSONObject("formData");
        if (StringUtils.isNotBlank(formDataIn.toJSONString())) {
            mergedFormData = FormDataUtil.formDataCombine(formDataIn, insJson.getJSONObject("formData"));
        }
        insJson.put("formData", mergedFormData);
        currProcessInstance.setInsUpdateDate(new Date());
        currProcessInstance.setInsData(insJson.toJSONString());
        dhProcessInstanceMapper.updateByPrimaryKeySelective(currProcessInstance);

        // 保存审批意见
		if (needApprovalOpinion(currTaskNode, currProcessInstance)) {
			ServerResponse saveApprovalOpinionResponse = dhapprovalOpinionService.saveDhApprovalOpinionWhenSubmitTask(currTaskInstance,
					currTaskNode.getDhActivityConf(), dataJson);
			if (!saveApprovalOpinionResponse.isSuccess()) {
				return saveApprovalOpinionResponse;
			}
		}

        // 保存流转记录
        DhRoutingRecord routingRecord = dhRoutingRecordService.generateSkipFromRejectRoutingRecord(dataForSkipFromReject);
		dhRoutingRecordMapper.insert(routingRecord);

        // 完成任务，设置数据
        updateDhTaskInstanceWhenFinishTask(currTaskInstance, data);

		// 移动token
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus httpReturnStatus = bpmProcessUtil.moveToken(dataForSkipFromReject.getInsId(),
                dataForSkipFromReject.getTargetNode().getActivityBpdId(), dataForSkipFromReject.getTokenId());
        if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
            throw new PlatformException("调用API 取回失败");
        }
        // API 调用成功的情况，获取新任务
        JSONObject processData = JSON.parseObject(httpReturnStatus.getMsg());
        List<Integer> taskIdList = ProcessDataUtil.getActiveTaskIdByFlowObjectId(dataForSkipFromReject.getTargetNode().getActivityBpdId(), processData);
        // 阻止拉取任务
        taskMongoDao.batchlockTasks(taskIdList, LockedTask.REASON_SKIP_FROM_REJECT);
        // 重新分配
        BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
        ServerResponse serverResponse = taskUtil.changeOwnerOfLaswTask(taskIdList.get(0), currentUserUid);
        if (!serverResponse.isSuccess()) {
            log.error("重新分配失败");
        }

        saveTaskToRetryTable(taskIdList.get(0));
        unlockTask(taskIdList.get(0));

        return ServerResponse.createBySuccess();
	}

    /**
     * 判断指定任务环节是否需要填审批意见
     * @param taskNode  任务所在环节
	 * @param dhProcessInstance  任务所在流程实例
     * @return
     */
    private boolean needApprovalOpinion(BpmActivityMeta taskNode, DhProcessInstance dhProcessInstance) {
        BpmActivityMeta firstNode = null;
        // 如果是流程的第一个任务，不用填审批意见
        if ("0".equals(dhProcessInstance.getInsParent())) {
            // 任务属于主流程
            firstNode = bpmActivityMetaService.getFirstUserTaskNodeOfMainProcess(dhProcessInstance.getProAppId(),
                    dhProcessInstance.getProUid(), dhProcessInstance.getProVerUid());
        } else {
        	// 任务属于子流程
            firstNode = bpmActivityMetaService.getFirstUserTaskNodeOfSubProcess(
                    bpmActivityMetaMapper.queryByPrimaryKey(dhProcessInstance.getTokenActivityId()));
        }
        if (firstNode == null) { // 如果此
        	return true;
		}
        if (firstNode.getActivityId().equals(taskNode.getActivityId())) {
            return false;
        }
        // 不是起草环节的情况，根据配置看是否需要审批
        DhActivityConf dhActivityConf = taskNode.getDhActivityConf();
        return dhActivityConf.getActcCanApprove().equals("TRUE") ? true : false;
    }


    /**
     * 作废指定任务
     * @param taskList
     * @return
     */
    private void abandonDhTaskInstance(List<DhTaskInstance> taskList) {
        DhTaskInstance taskSelective = new DhTaskInstance();
        for (DhTaskInstance task : taskList) {
            taskSelective.setTaskUid(task.getTaskUid());
            taskSelective.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
            dhTaskInstanceMapper.updateByPrimaryKeySelective(taskSelective);
        }
    }

    /**
	 * 获得取回任务需要的信息，如果不能取回任务返回null
	 * @param finishedTaskInstance  已办任务实例
	 * @param finishedTaskNode  已办任务的节点
     * @param dhProcessInstance  任务所属实例
	 * @return
	 */
    private DataForRevoke getDataForRevoke(DhTaskInstance finishedTaskInstance, BpmActivityMeta finishedTaskNode, DhProcessInstance dhProcessInstance) {
        if (!DhTaskInstance.STATUS_CLOSED.equals(finishedTaskInstance.getTaskStatus())) {
            return null;
        }

        // 如果已完成的任务不是普通节点任务，不允许取回
        if (!BpmActivityMeta.LOOP_TYPE_NONE.equals(finishedTaskNode.getLoopType())
                || !finishedTaskInstance.getTaskType().equals(DhTaskInstance.TYPE_NORMAL)) {
            return null;
        }
        DhActivityConf finishedTaskNodeConf = finishedTaskNode.getDhActivityConf();
        if (!"TRUE".equals(finishedTaskNodeConf.getActcCanRevoke())) {
            return null;
        }

        String activityToStr = finishedTaskNode.getActivityTo();
        if (StringUtils.isBlank(activityToStr) || activityToStr.endsWith(",")) {
            return null;
        }

        // 检查下个直接连接点的类型
        BpmActivityMeta nodeAfterTaskNode = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityToStr,
                finishedTaskNode.getParentActivityId(), finishedTaskNode.getSnapshotId());

        // 只有下个节点非循环人工节点才能撤回
        if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(nodeAfterTaskNode.getBpmTaskType())
                || !nodeAfterTaskNode.getLoopType().equals(BpmActivityMeta.LOOP_TYPE_NONE)) {
            return null;
        }

        // 判断任务是不是当前用户完成的
        String currUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        if (finishedTaskInstance.getTaskDelegateUser() == null) {
            // 没有被代理，查看当前用户是不是任务处理人
            if (!finishedTaskInstance.getUsrUid().equals(currUserUid)) {
                return null;
            }
        } else {
            // 代理的任务，查看当前用户是不是代理人
            if (!finishedTaskInstance.getTaskDelegateUser().equals(currUserUid)) {
                return null;
            }
        }

        // 找到完成任务时的流转记录
        DhRoutingRecord routingRecordOfTask = dhRoutingRecordService.getSubmitRoutingRecordOfTask(finishedTaskInstance.getTaskUid());

        // 如果路由信息不包含to不能取回, 如果最后的操作不是提交不能取回
        if (routingRecordOfTask == null || routingRecordOfTask.getActivityTo() == null
                || !routingRecordOfTask.getRouteType().equals(DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK)) {
            return null;
        }

        // 得到下个环节任务信息
        List<DhTaskInstance> tasksOnAfterNode = listTasksOnBpmActivityMeta(finishedTaskInstance.getInsUid(), routingRecordOfTask.getActivityTo());
        if (tasksOnAfterNode == null || tasksOnAfterNode.isEmpty()) {
            return null;
        }
        Iterator<DhTaskInstance> iterator = tasksOnAfterNode.iterator();
        // 去除此任务提交前就产生的任务
        while (iterator.hasNext()) {
            DhTaskInstance task = iterator.next();
            if (task.getTaskInitDate().getTime() < finishedTaskInstance.getTaskFinishDate().getTime()) {
                iterator.remove();
            } else {
                if (!DhTaskInstance.STATUS_RECEIVED.equals(task.getTaskStatus())) {
                    // 如果状态不是待处理，就不允许取回
                    return null;
                }
            }
        }
        // 此时tasksOnAfterNode集合中留下的是尚未被处理的任务由普通任务派生
        if (tasksOnAfterNode.isEmpty()) {
            return null;
        }

        // 获得任务的token
        DhTaskInstance currTaskInstance = tasksOnAfterNode.get(0);
        int currTaskId = currTaskInstance.getTaskId();

        // 查询现在的任务有没有被打开过
        boolean hasOpened = taskMongoDao.hasTaskBeenOpened(currTaskId);
        if (hasOpened) return null;

        ServerResponse<String> processDataResponse = getProcessData(dhProcessInstance.getInsId());
        if (!processDataResponse.isSuccess()) {
            return null;
        }
        String tokenId = ProcessDataUtil.getTokenIdOfTask(currTaskId, JSON.parseObject(processDataResponse.getData()));

        DataForRevoke dataForRevoke = new DataForRevoke();
        dataForRevoke.setInsId(dhProcessInstance.getInsId());
        dataForRevoke.setTokenId(tokenId);
        dataForRevoke.setCurrTaskInstances(tasksOnAfterNode);
        dataForRevoke.setRoutingRecordToRemove(routingRecordOfTask);
        dataForRevoke.setTargetActivityBpdId(finishedTaskNode.getActivityBpdId());
        dataForRevoke.setFinishedTask(finishedTaskInstance);
        return dataForRevoke;
    }

	/**
	 * 获得跳转到驳回关节需要的信息，如果不能跳转到驳回环节返null
	 * @param currTaskInstance  当前任务
	 * @param dhProcessInstance 当前流程实例
	 * @return
	 */
    private DataForSkipFromReject getDataForSkipFromReject(DhTaskInstance currTaskInstance, DhProcessInstance dhProcessInstance) {
		// 最近路由到这个节点的路由记录是不是驳回
        List<DhRoutingRecord> routingRecords = dhRoutingRecordService.getAllRoutingRecordOfProcessInstance(dhProcessInstance.getInsUid());
        if (routingRecords.isEmpty()) return null;

        // 获得流转到当前任务节点的最近一条流转记录
        DhRoutingRecord routingRecordToCurrTaskNode = null;
        for (DhRoutingRecord routingRecord : routingRecords) {
            if (currTaskInstance.getTaskActivityId().equals(routingRecord.getActivityTo())) {
                routingRecordToCurrTaskNode = routingRecord;
                break;
            }
        }
        if (routingRecordToCurrTaskNode == null
                || !DhRoutingRecord.ROUTE_TYPE_REJECT_TASK.equals(routingRecordToCurrTaskNode.getRouteType())) {
            return null;
        }
        // 获得驳回节点信息
        BpmActivityMeta targetNode = bpmActivityMetaMapper.queryByPrimaryKey(routingRecordToCurrTaskNode.getActivityId());
        if (targetNode == null) return null;

        // 查看配置是否允许直接回到改环节
        if (!"TRUE".equals(targetNode.getDhActivityConf().getActcCanSkipFromReject())) {
            return null;
        }

        // 找到触发这个驳回的任务
        DhTaskInstance rejectTask = dhTaskInstanceMapper.selectByPrimaryKey(routingRecordToCurrTaskNode.getTaskUid());
        SysUser rejectUser = sysUserMapper.queryByPrimaryKey(rejectTask.getUsrUid());

        // 获得当前任务的tokenId
        ServerResponse<String> processDataResponse = getProcessData(dhProcessInstance.getInsId());
        if (!processDataResponse.isSuccess()) {
            return null;
        }
        String tokenId = ProcessDataUtil.getTokenIdOfTask(currTaskInstance.getTaskId(), JSON.parseObject(processDataResponse.getData()));
        if (StringUtils.isBlank(tokenId)) {
            return null;
        }

        DataForSkipFromReject dataForSkipFromReject = new DataForSkipFromReject();
        dataForSkipFromReject.setInsId(dhProcessInstance.getInsId());
        dataForSkipFromReject.setTokenId(tokenId);
        dataForSkipFromReject.setCurrTask(currTaskInstance);
        dataForSkipFromReject.setTargetNode(targetNode);
        dataForSkipFromReject.setNewTaskOwner(rejectTask.getUsrUid());
        dataForSkipFromReject.setNewTaskOwnerName(rejectUser.getUserName() + "(" + rejectTask.getUsrUid() + ");");
        return dataForSkipFromReject;
	}

    /**
     * 获得流程实例在指定节点上的任务， 按完成时间倒序排列
     * @param insUid
     * @param activityId
     * @return
     */
    public List<DhTaskInstance> listTasksOnBpmActivityMeta(String insUid, String activityId) {
        DhTaskInstance taskSelective = new DhTaskInstance();
        taskSelective.setInsUid(insUid);
        taskSelective.setTaskActivityId(activityId);
        return dhTaskInstanceMapper.selectAllTask(taskSelective);
    }


    /**
     * 获得流程实例当前信息
     * @param insId 流程实例编号
     * @return data中包含httpReturnStatus 的 msg
     */
    private ServerResponse<String> getProcessData(int insId) {
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        HttpReturnStatus returnStatus = bpmProcessUtil.getProcessData(insId);
        if (HttpReturnStatusUtil.isErrorResult(returnStatus)) {
            return ServerResponse.createByErrorMessage("获得流程信息出错");
        }
        return ServerResponse.createBySuccess(returnStatus.getMsg());

    }


	@Transactional
	@Override
	public ServerResponse submitSystemTask(DhTaskInstance systemTaskInstance, BpmGlobalConfig bpmGlobalConfig) {
		BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(systemTaskInstance.getTaskActivityId());
		if (currTaskNode == null) {
			throw new PlatformException("系统任务处理失败，找不到任务节点，taskUid：" + systemTaskInstance.getTaskUid());
		}
		DhActivityConf currTaskConf = currTaskNode.getDhActivityConf();
		if (!"TRUE".equals(currTaskConf.getActcIsSystemTask())) {
			throw new PlatformException("系统任务处理失败，任务不是系统任务，taskUid：" + systemTaskInstance.getTaskUid());
		}
		if (!BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(currTaskNode.getBpmTaskType())
				|| !BpmActivityMeta.LOOP_TYPE_NONE.equals(currTaskNode.getLoopType())) {
			throw new PlatformException("系统任务任务类型异常，不能处理循环任务，taskUid：" + systemTaskInstance.getTaskUid());
		}
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(systemTaskInstance.getInsUid());
		int insId = dhProcessInstance.getInsId();
		if (dhProcessInstance == null) {
			throw new PlatformException("系统任务处理失败，流程实例不存在");
		}
		JSONObject formDataJson = FormDataUtil.getFormDataJsonFromProcessInstance(dhProcessInstance);
		// 获得下个环节的信息
		BpmRoutingData bpmRoutingData = dhRouteService.getBpmRoutingData(currTaskNode, formDataJson);
		CommonBusinessObject pubBo = new CommonBusinessObject(insId);
		// 装配默认处理人
		dhRouteService.assembleTaskOwnerForSystemTask(systemTaskInstance, dhProcessInstance, pubBo, bpmRoutingData);
		// 如果下个环节有循环任务（保存/更新）处理人信息
		dhRouteService.saveTaskHandlerOfLoopTask(insId, bpmRoutingData, pubBo);

		// 更新网关决策条件
		if (bpmRoutingData.getGatewayNodes().size() > 0) {
			ServerResponse updateResponse = threadPoolProvideService.updateRouteResult(insId, bpmRoutingData);
			if (!updateResponse.isSuccess()) {
				throw new PlatformException("系统任务处理失败，更新网关决策表失败，taskUid：" + systemTaskInstance.getTaskUid());
			}
		}

		// 生成流转记录
		DhRoutingRecord routingRecord = null;

		// 判断是否是子流程的第一个节点，如果是第一个节点，就把任务还给流程发起人
		if (dhRouteService.isFirstTaskOfSubProcess(currTaskNode, dhProcessInstance)) {
			DhTaskInstance taskSelective = new DhTaskInstance();
			taskSelective.setTaskUid(systemTaskInstance.getTaskUid());
			taskSelective.setUsrUid(dhProcessInstance.getInsInitUser()); // 将任务给流程发起人
			taskSelective.setTaskFinishDate(new Date());
			taskSelective.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
			dhTaskInstanceMapper.updateByPrimaryKeySelective(taskSelective);
			routingRecord = dhRoutingRecordService.generateFirstTaskNodeOfSubProcessRoutingData(systemTaskInstance, bpmRoutingData, dhProcessInstance.getInsInitUser());
		} else {
			routingRecord = dhRoutingRecordService.generateSystemTaskRoutingRecord(currTaskNode,
					systemTaskInstance, bpmGlobalConfig.getBpmAdminName(), bpmRoutingData);
			// 如果只是普通的系统个任务，修改任务状
            updateDhTaskInstanceWhenFinishTask(systemTaskInstance, "{}");
		}

		// 获得步骤
		List<DhStep> steps = dhStepService.getStepsByBpmActivityMetaAndStepBusinessKey(currTaskNode, dhProcessInstance.getInsBusinessKey());
		DhStep nextStep = steps.isEmpty() ? null : steps.get(0);

		DataForSubmitTask dataForSubmitTask = new DataForSubmitTask();
		dataForSubmitTask.setBpmGlobalConfig(bpmGlobalConfig);
		dataForSubmitTask.setCurrTaskInstance(systemTaskInstance);
		dataForSubmitTask.setCurrentProcessInstance(dhProcessInstance);
		dataForSubmitTask.setPubBo(pubBo);
		dataForSubmitTask.setBpmRoutingData(bpmRoutingData);
		dataForSubmitTask.setDhRoutingRecord(routingRecord);
		dataForSubmitTask.setNextStep(nextStep);
		dataForSubmitTask.setApplyUser(null); // 系统任务不需要被认领
		return finishTaskOrSendToMq(dataForSubmitTask);
	}

}
