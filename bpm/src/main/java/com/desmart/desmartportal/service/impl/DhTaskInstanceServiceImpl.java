/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.util.*;
import com.desmart.desmartbpm.dao.DhSynTaskRetryMapper;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.mongo.TaskMongoDao;
import com.desmart.desmartbpm.mq.rabbit.MqProducerService;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartportal.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.IBMApiUrl;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
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
	private DhRouteService dhRouteServiceImpl;
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
	private DhProcessDefinitionService dhProcessDefinitionService;
	@Autowired
	private TaskMongoDao taskMongoDao;
	@Autowired
	private DhSynTaskRetryMapper dhSynTaskRetryMapper;

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
		JSONObject taskData = JSONObject.parseObject(String.valueOf(dataJson.get("taskData")));
		JSONObject processDataIn = JSONObject.parseObject(String.valueOf(dataJson.get("processData")));
		JSONObject formDataIn = JSONObject.parseObject(String.valueOf(dataJson.get("formData")));
        JSONArray routeData = JSONObject.parseArray(String.valueOf(dataJson.get("routeData")));

		Integer taskId = Integer.parseInt(taskData.getString("taskId"));
		String taskUid = taskData.getString("taskUid");
		// 根据任务标识和用户 去查询流程 实例
		DhTaskInstance currTask = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (currTask == null) {
			return ServerResponse.createByErrorMessage("当前任务不存在!");
		}
		// 校验任务状态
		if (!"12".equals(currTask.getTaskStatus())) {// 检查任务是否重复提交
			return ServerResponse.createByErrorMessage("任务已经被提交或暂停");
		}
		// 校验任务类型
        String taskType = currTask.getTaskType();
		if (!(DhTaskInstance.TYPE_SIMPLE_LOOP.equals(taskType) || DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP.equalsIgnoreCase(taskType)
                || DhTaskInstance.TYPE_NORMAL.equals(taskType))) {
            return ServerResponse.createByErrorMessage("任务类型异常");
        }


        if (!checkTaskUser(currTask, currentUserUid)) {// 检查任务是否有权限提交
            return ServerResponse.createByErrorMessage("提交失败，您没有完成任务的权限");
        }

        // 获得流程实例
        DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
        if (currProcessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }

        String insUid = currProcessInstance.getInsUid();
        Integer insId = currProcessInstance.getInsId();
        BpmActivityMeta currTaskNode = bpmActivityMetaService.queryByPrimaryKey(currTask.getTaskActivityId());
        DhActivityConf dhActivityConf = currTaskNode.getDhActivityConf();

        // 整合formdata
        JSONObject mergedFormData = new JSONObject();
        JSONObject insJson = JSONObject.parseObject(currProcessInstance.getInsData());
        JSONObject processDataJson = insJson.getJSONObject("processData");

        if (StringUtils.isNotBlank(formDataIn.toJSONString())) {
            mergedFormData = FormDataUtil.formDataCombine(formDataIn, insJson.getJSONObject("formData"));
        }

		// 检查是否需要更新insTitle
		if (canEditInsTitle(currTask, currProcessInstance)) {
			String insTitle = processDataIn.getString("insTitle");
			if (StringUtils.isBlank(insTitle) || insTitle.trim().length()>30) {
				return ServerResponse.createByErrorMessage("流程标题异常");
			}
			currProcessInstance.setInsTitle(insTitle);
			processDataJson.put("insTitle", insTitle);
			insJson.put("processData", processDataJson);
		}

        // 根据配置保存审批意见
        ServerResponse response = dhapprovalOpinionService.saveDhApprovalOpiionWhenSubmitTask(currTask, dhActivityConf, dataJson);
        if (!response.isSuccess()) {
            throw new PlatformException(response.getMsg());
        }

        // 修改当前任务实例状态为已完成
		updateDhTaskInstanceWhenFinishTask(currTask, data);

        //完成任务 删除任务的草稿数据
        dhDraftsMapper.deleteByTaskUid(taskUid);

        //如果任务为类型为normal，则将其它相同任务id的任务废弃
        if(DhTaskInstance.TYPE_NORMAL.equals(currTask.getTaskType())) {
            dhTaskInstanceMapper.abandonOtherUnfinishedTaskByTaskId(taskUid, taskId);
        }

        // 修改流程实例信息
        insJson.put("formData", mergedFormData);
        currProcessInstance.setInsUpdateDate(DateUtil.format(new Date()));
        currProcessInstance.setInsData(insJson.toJSONString());
        dhProcessInstanceMapper.updateByPrimaryKeySelective(currProcessInstance);

        // 获得下个节点的路由信息
        BpmRoutingData routingData = dhRouteServiceImpl.getRoutingDataOfNextActivityTo(currTaskNode, mergedFormData);

        // 判断Token是否移动
        boolean willTokenMove = dhRouteServiceImpl.willFinishTaskMoveToken(currTask);

        // 任务完成后 保存到流转信息表里面
        ServerResponse<DhRoutingRecord> generateRoutingRecordResponse =
                dhRoutingRecordService.generateSubmitTaskRoutingRecordByTaskAndRoutingData(currTask, routingData, willTokenMove);
        DhRoutingRecord routingRecord = generateRoutingRecordResponse.getData();

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

        // 检查用户传递的选人信息是否全面
        if (!dhRouteServiceImpl.checkRouteData(currTaskNode, routeData, routingData)) {
            throw new PlatformException("缺少下个环节的用户信息");
        }

        // 装配处理人信息
        CommonBusinessObject pubBo = new CommonBusinessObject(insId);
        ServerResponse<CommonBusinessObject> serverResponse = dhRouteServiceImpl.assembleCommonBusinessObject(pubBo, routeData);
        if (!serverResponse.isSuccess()) {
            throw new PlatformException("装配处理人信息失败，缺少下个环节处理人信息");
        }
        // 如果下个环节有循环任务（保存/更新）处理人信息
        dhRouteServiceImpl.saveTaskHandlerOfLoopTask(insId, routeData);

        // 装配无法选择处理人的子流程发起人信息
		dhRouteServiceImpl.assembleInitUserOfSubProcess(currProcessInstance, pubBo, routingData);

        // 更新网关决策条件
        if (routingData.getGatewayNodes().size() > 0) {
            ExecutorService executorService = threadPoolProvideService.getThreadPoolToUpdateRouteResult();
            Future<Boolean> future = executorService.submit(new SaveRouteResultCallable(dhRouteServiceImpl, insId, routingData));
            Boolean updateSucess = true;
            try {
                updateSucess = future.get(2, TimeUnit.SECONDS); // 最多等待2秒
            } catch (Exception e) {
                updateSucess = false;
            }
            if (!updateSucess) {
                throw new PlatformException("更新路中间表失败");
            }
        }

        // 判断后续有没有触发器需要调用
        // 获得当前步骤
        DhStep formStepOfTaskNode = dhStepService.getFormStepOfTaskNode(currTaskNode, currProcessInstance.getInsBusinessKey());
        DhStep nextStep = dhStepService.getNextStepOfCurrStep(formStepOfTaskNode);
        if (nextStep == null) {
            // 没有后续步骤
            //  调用api 完成任务
            BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
            Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(taskId, pubBo);
            Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
            if (errorMap.get("errorResult") == null) {
                // 判断实际TOKEN是否移动了
                ServerResponse<JSONObject> didTokenMoveResponse = dhRouteServiceImpl.didTokenMove(insId, routingData);
                if (didTokenMoveResponse.isSuccess()) {
                    JSONObject processData = didTokenMoveResponse.getData();
                    if (processData != null) {
                        // 实际Token移动了
                        // 关闭需要结束的流程
                        dhRoutingRecordMapper.insert(routingRecord);
                        dhProcessInstanceService.closeProcessInstanceByRoutingData(insId, routingData);
                        // 创建需要创建的子流程
                        dhProcessInstanceService.createSubProcessInstanceByRoutingData(currProcessInstance, routingData, pubBo, processData);
                    } else {
                        // 实际Token没有移动, 更新流转信息
                        routingRecord.setActivityTo(null);
                        dhRoutingRecordMapper.insert(routingRecord);                    }
                } else {
                    log.error("判断TOKEN是否移动失败，流程实例编号：" + insId + " 任务主键：" + taskUid);
                }
                return ServerResponse.createBySuccess();
            } else {
                throw new PlatformException("调用RESTful API完成任务失败");
            }
        } else {
            // 有后续步骤, MQ交互
            // 向队列发出消息
            Map<String, Object> map = new HashMap<>();
			map.put("currProcessInstance", currProcessInstance);
			map.put("currTaskInstance", currTask);
			map.put("dhStep", nextStep);
			map.put("pubBo", pubBo);
			map.put("routingData", routingData); // 预判的下个环节信息
			map.put("routingRecord", routingRecord); // 流转记录
            String paramStr = JSON.toJSONString(map);
            boolean result = mqProducerService.sendMessage("stepQueueKey", paramStr);
            return ServerResponse.createBySuccess();
        }
    }

	/**
	 *	设置流转到的节点activityId  逗号分隔
	 * @param nextBpmActivityMetas
	 * @param dhRoutingRecord
	 */
	private void setActivityToValues(Set<BpmActivityMeta> nextBpmActivityMetas, DhRoutingRecord dhRoutingRecord) {
		StringBuffer stringBuffer = new StringBuffer();
        Iterator<BpmActivityMeta> it = nextBpmActivityMetas.iterator();
        while (it.hasNext()) {
            BpmActivityMeta meta = it.next();
            stringBuffer.append(meta.getActivityId()).append(",");
        }
        String str = stringBuffer.toString();
        str = str.substring(0, str.length() - 1);
        dhRoutingRecord.setActivityTo(str);
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
	    
	    // 获得当前环节
	    BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(dhTaskInstance.getTaskActivityId());
	    if (currTaskNode == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    DhActivityConf dhActivityConf = dhActivityConfMapper.selectByPrimaryKey(currTaskNode.getDhActivityConf().getActcUid());
	    
	    // 查看能否编辑insTitle
        boolean canEditInsTitle = canEditInsTitle(dhTaskInstance, dhprocessInstance);

        List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(currTaskNode, dhprocessInstance.getInsBusinessKey());
        if (steps.isEmpty()) {
            return ServerResponse.createByErrorMessage("找不到表单步骤");
        }

        // todo 调用表单前的触发器

	    DhStep formStep = dhStepService.getFormStepOfStepList(steps);

        ServerResponse getFormResponse = bpmFormManageService.queryFormByFormUid(formStep.getStepObjectUid());
        if (!getFormResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单");
        }
        BpmForm bpmForm = (BpmForm)getFormResponse.getData();
        
        ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFieldPermissionByStepUid(formStep.getStepUid());
        if (!fieldPermissionResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单权限信息");
        }
        String fieldPermissionInfo = fieldPermissionResponse.getData();
        
        // 是否显示环节权责控制
        if (StringUtils.isBlank(dhActivityConf.getActcResponsibility()) || "<br>".equals(dhActivityConf.getActcResponsibility())) {
            resultMap.put("showResponsibility", "FALSE");
        } else {
            resultMap.put("showResponsibility", "TRUE");
        }
        
        
        String insDataStr = dhprocessInstance.getInsData();
        JSONObject insData = JSON.parseObject(insDataStr);
        JSONObject formData = insData.getJSONObject("formData");
        //获取草稿数据
        DhDrafts dhDrafts = dhDraftsMapper.queryDraftsByTaskUid(taskUid);
        JSONObject approvalData = null;
        if(dhDrafts!=null) {
        	if(dhDrafts.getDfsData()!=null) {
        		JSONObject dfsData = JSON.parseObject(dhDrafts.getDfsData());
        		JSONObject dfsFormData = dfsData.getJSONObject("formData");
        		formData = FormDataUtil.formDataCombine(dfsFormData,
        				formData);
        		approvalData = dfsData.getJSONObject("approvalData");
        	}
        }
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
	public ServerResponse<?> queryProgressBar(String activityId, String taskUid) {
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
			SysUser sysUser = new SysUser();
			for (String string : usrUids) {
				sysUser.setUserName(string);
				List<SysUser> sysUserList = sysUserMapper.selectAll(sysUser);
				dhTaskInstance.setUsrUid(sysUserList.get(0).getUserId());
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
					completedSigning += string+",";
				}	
			}
			// 如果会签人全部都已经会签过，则直接返回
			String[] check = completedSigning.split(",");
			if (Arrays.equals(usrUids, check)) {
				return ServerResponse.createByErrorMessage(completedSigning.substring(0, completedSigning.length()-1)+"已经会签过!");
			}

			// 路由表记录
			DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
			dhRoutingRecord.setRouteUid("routing_record:"+UUID.randomUUID());
			dhRoutingRecord.setInsUid(dhTaskInstance.getInsUid());
			dhRoutingRecord.setActivityName(dti.getTaskTitle());
			dhRoutingRecord.setRouteType("addTask");
			dhRoutingRecord.setUserUid(dti.getUsrUid());
			dhRoutingRecord.setActivityId(activityId);
//			dhRoutingRecord.setActivityTo(activityId);
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
	    if (dhTaskInstance == null || !DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
	      if(!DhTaskInstance.STATUS_WAIT_ALL_ADD_FINISH.equals(dhTaskInstance.getTaskStatus())) {
	    	  return ServerResponse.createByErrorMessage("任务不存在或任务状态异常");  
	      }
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
	    
	    DhStep formStep = dhStepService.getFormStepOfTaskNode(currTaskNode, dhprocessInstance.getInsBusinessKey());

        ServerResponse getFormResponse = bpmFormManageService.queryFormByFormUid(formStep.getStepObjectUid());
        if (!getFormResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单");
        }
        BpmForm bpmForm = (BpmForm)getFormResponse.getData();
        
        ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFinshedFieldPerMissionByStepUid(formStep.getStepUid());
        if (!fieldPermissionResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单权限信息");
        }
        String fieldPermissionInfo = fieldPermissionResponse.getData();
        
        resultMap.put("bpmForm", bpmForm);
        resultMap.put("activityMeta", currTaskNode);
        resultMap.put("activityConf", currTaskNode.getDhActivityConf());
        resultMap.put("dhStep", formStep);
        resultMap.put("processInstance", dhprocessInstance);
	    resultMap.put("taskInstance", dhTaskInstance);
	    resultMap.put("fieldPermissionInfo",fieldPermissionInfo);
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
        ServerResponse getFormResponse = bpmFormManageService.queryFormByFormUid(formStep.getStepObjectUid());
        if (!getFormResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单");
        }
        BpmForm bpmForm = (BpmForm)getFormResponse.getData();
        
        ServerResponse<String> fieldPermissionResponse = bpmFormFieldService.queryFinshedFieldPerMissionByStepUid(formStep.getStepUid());
        if (!fieldPermissionResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("缺少表单权限信息");
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
			// 判断taskType类型
			String type = dhTaskInstance.getTaskType();
			// 插入审批意见
			DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
			dhApprovalOpinion.setAprOpiId("apr_idea:"+UUID.randomUUID());
			dhApprovalOpinion.setInsUid(dhTaskInstance.getInsUid());
			dhApprovalOpinion.setTaskUid(taskUid);
			dhApprovalOpinion.setAprOpiIndex(0);
			dhApprovalOpinion.setAprUserId(dhTaskInstance.getUsrUid());
			dhApprovalOpinion.setAprTimeNumber(0);
			dhApprovalOpinion.setAprOpiComment(approvalContent);
			dhApprovalOpinion.setAprStatus("ok");
			dhApprovalOpinion.setAprDate(new Timestamp(System.currentTimeMillis()));
			dhApprovalOpinion.setActivityId(activityId);
			dhapprovalOpinionService.insert(dhApprovalOpinion);
			
			// 路由表记录
			DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
			dhRoutingRecord.setRouteUid("routing_record:"+UUID.randomUUID());
			dhRoutingRecord.setInsUid(dhTaskInstance.getInsUid());
			dhRoutingRecord.setActivityName(dhTaskInstance.getTaskTitle());
			dhRoutingRecord.setRouteType("finishAddTask");
			dhRoutingRecord.setUserUid(dhTaskInstance.getUsrUid());
			dhRoutingRecord.setActivityId(activityId);
//			dhRoutingRecord.setActivityTo(activityId);
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
				for (DhTaskInstance dti : dhTaskInstanceList) {
					if (dhTaskInstance.getTaskUid().equals(dti.getTaskUid())) {
						dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
						dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
					}else {
						dti.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
						dhTaskInstanceMapper.updateByPrimaryKeySelective(dti);
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
					dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
				}else {
					// 将主任务状态回归到正常状态
					DhTaskInstance task = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getFromTaskUid());
					// 重新计算剩余时间
					long dueDate = task.getTaskDueDate().getTime() + (long) (task.getRemainHours() * 60 * 60 * 1000);
					task.setTaskDueDate(new Date(dueDate));
					task.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKeySelective(task);
					// 将当前任务关闭
					dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
					dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
				}
			}
			// 说明：如果taskType为multiInstanceLoopAdd,则需要主任务所有会签任务都审批完，主任务方可回归正常状态
			if (DhTaskInstance.TYPE_MULTI_INSTANCE_LOOPADD.equals(type)) {
				// 将当前任务关闭
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
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_INIT_DATE DESC");
		dhTaskInstance.setTaskStatus("32");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectBackLogTaskInfoByCondition(startTime, endTime, dhTaskInstance);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public Integer selectBackLogByusrUid(String usrUid) {
		// TODO Auto-generated method stub
		return dhTaskInstanceMapper.selectBackLogByusrUid(usrUid);
	}

	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByCondition(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_FINISH_DATE DESC");
		dhTaskInstance.setTaskStatus("32");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByClosedByCondition(startTime, endTime, dhTaskInstance);
		PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public Integer alreadyClosedTaskByusrUid(String userId) {
		// TODO Auto-generated method stub
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
		SysUser sysUser = new SysUser();
		for (String string : usrUidArray) {
			sysUser.setUserName(string);
			List<SysUser> sysUserList = sysUserMapper.selectAll(sysUser);
			dhTaskInstance.setUsrUid(sysUserList.get(0).getUserId());
			DhTaskInstance dti = dhTaskInstanceMapper.getBytaskTypeAndUsrUid(dhTaskInstance);
			if (dti != null) {
				errorInformation += string + ",";
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
		// 路由表记录
		DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
		dhRoutingRecord.setRouteUid("routing_record:"+UUID.randomUUID());
		dhRoutingRecord.setInsUid(dhTaskInstance.getInsUid());
		dhRoutingRecord.setActivityName(dhTaskInstance.getTaskTitle());
		dhRoutingRecord.setRouteType("addTask");
		dhRoutingRecord.setUserUid(dhTaskInstance.getUsrUid());
		dhRoutingRecord.setActivityId(activityId);
//		dhRoutingRecord.setActivityTo(activityId);
		dhRoutingRecordMapper.insert(dhRoutingRecord);
		if (!errorInformation.isEmpty()) {
			return ServerResponse.createByErrorMessage(errorInformation.substring(0,errorInformation.length()-1) + "已经抄送过,其他人员操作成功!");
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByStartProcess(
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize,String insTitle,
			String insInitUser,Integer insStatusId,String proAppId,String proUid) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        DhProcessInstance dhProcessInstance = new DhProcessInstance();
        DhProcessDefinition processDefintion = dhProcessDefinitionService
				.getStartAbleProcessDefinition(proAppId,
						proUid);
        if(processDefintion==null) {
        	return ServerResponse.createByErrorMessage("该流程版本有异常");
        			}
        dhProcessInstance.setProAppId(proAppId);
        dhProcessInstance.setProUid(proUid);
        dhProcessInstance.setProVerUid(processDefintion.getProVerUid());
        dhProcessInstance.setInsStatusId(insStatusId);
        if(insTitle!=null && !"".equals(insTitle)) {
        		dhProcessInstance.setInsTitle(insTitle);
        	}
        if(insInitUser!=null && "current".equals(insInitUser)) {
    		dhProcessInstance.setInsInitUser(currentUserUid);
    	}
        dhTaskInstance.setDhProcessInstance(dhProcessInstance);
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy("TASK_FINISH_DATE DESC,TASK_FINISH_DATE DESC");
		dhTaskInstance.setTaskStatus("12,-2,32");
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.selectPageTaskByClosedByCondition(null, null, dhTaskInstance);
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
            startNode = bpmActivityMetaService.getStartMetaOfMainProcess(processInstance.getProAppId(),
                    processInstance.getProUid(), processInstance.getProVerUid());
        } else {
            // 流程是子流程
            BpmActivityMeta subProcessNode = bpmActivityMetaService.queryByPrimaryKey(processInstance.getTokenActivityId());
            if (subProcessNode == null) return false;
            startNode = bpmActivityMetaService.getStartMetaOfSubProcess(subProcessNode);
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
		JSONObject routeData = JSONObject.parseObject(String.valueOf(dataJson.get("routeData")));
		JSONObject approvalData = JSONObject.parseObject(String.valueOf(dataJson.get("approvalData")));// 获取审批信息
		JSONObject taskData = JSONObject.parseObject(String.valueOf(dataJson.get("taskData")));
		// 目标环节
		String targetActivityBpdId = routeData.getString("activityBpdId");
		String taskUid = taskData.getString("taskUid");


		// 判断驳回的任务是否存在
		if (StringUtils.isBlank(taskUid)) {
			return ServerResponse.createByErrorMessage("缺少任务信息");
		}
		DhTaskInstance currTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (currTaskInstance == null || !"12".equals(currTaskInstance.getTaskStatus())) {
			return ServerResponse.createByErrorMessage("任务不存在，或状态异常");
		}

		String currActivityId = currTaskInstance.getTaskActivityId();
		BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(currActivityId);

		// 判断当前节点能否驳回, 如果不是普通节点不允许驳回
		if (!"TRUE".equals(currTaskNode.getDhActivityConf().getActcCanReject())
				|| !"none".equals(currTaskNode.getLoopType())) {
			return ServerResponse.createByErrorMessage("当前节点不允许驳回");
		}

		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTaskInstance.getInsUid());
		if (dhProcessInstance == null) return ServerResponse.createByErrorMessage("流程实例不存在");

		int taskId = currTaskInstance.getTaskId();
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
		if (StringUtils.isBlank(tokenId)) {
			return ServerResponse.createByErrorMessage("驳回失败，找不到tokenID");
		}

		// 获得目标节点
		BpmActivityMeta targetNode = bpmActivityMetaService
				.getByActBpdIdAndParentActIdAndProVerUid(targetActivityBpdId, currTaskNode.getParentActivityId()
						, currTaskNode.getSnapshotId());
		String actcAssignVariable = targetNode.getDhActivityConf().getActcAssignVariable();

		// 完成现在的任务
		updateDhTaskInstanceWhenFinishTask(currTaskInstance, data);

		// 关闭关联的任务（同一个节点上的任务）
		abandonRelationTaskOnTaskNode(currTaskInstance);

		// 保存审批意见
		dhapprovalOpinionService.saveDhApprovalOpiionWhenRejectTask(currTaskInstance, dataJson);

		// 保存流转记录
		DhRoutingRecord routingRecord = dhRoutingRecordService.generateRejectTaskRoutingRecordByTaskAndRoutingData(currTaskInstance, targetNode);
		dhRoutingRecordMapper.insert(routingRecord);

		HttpReturnStatus httpReturnStatus = bpmProcessUtil.moveToken(insId, targetActivityBpdId, tokenId);
		if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
			throw new PlatformException("调用API 驳回失败");
		}

		// API 调用成功的情况
		JSONObject processData = JSON.parseObject(httpReturnStatus.getMsg());
		// 驳回节点上产生的任务
		List<Integer> taskIdList = ProcessDataUtil.getActiveTaskIdByFlowObjectId(targetActivityBpdId, processData);
		// 阻止拉取任务
		lockTasksForRejectTaskByTaskIdList(taskIdList);

		// todo 重新分配任务
		// 获得任务处理人
		String taskOwner = routeData.getString("userUid");
		BpmTaskUtil taskUtil = new BpmTaskUtil(bpmGlobalConfig);
        HttpReturnStatus applyTastReturnStatus = taskUtil.applyTask(taskIdList.get(0), taskOwner);
        if (HttpReturnStatusUtil.isErrorResult(applyTastReturnStatus)) {
            log.error("重新分配任务失败，任务id:" + taskIdList.get(0));
        }

		saveTaskToRetryTable(taskIdList.get(0));
		unlockTask(taskIdList.get(0));

        return ServerResponse.createBySuccess();
	}

	/**
	 * 锁住驳回后还没有重新分配的任务
	 * @param taskIdList
	 */
	private void lockTasksForRejectTaskByTaskIdList(List<Integer> taskIdList) {
		if (taskIdList == null || taskIdList.isEmpty()) {
			return;
		}
		List<LockedTask> lockedTasks = new ArrayList<>();
		for (Integer taskId : taskIdList) {
			lockedTasks.add(new LockedTask(taskId, new Date(), LockedTask.REASON_REJECT_TASK));
		}
		taskMongoDao.batchSaveLockedTasks(lockedTasks);
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
	private int abandonRelationTaskOnTaskNode(DhTaskInstance taskInstance) {
		return dhTaskInstanceMapper.abandonOtherUnfinishedTasksOnTaskActivityId(taskInstance.getTaskUid(), taskInstance.getTaskActivityId(),
				taskInstance.getInsUid());
	}

	/**
	 * 完成任务时，更新任务实例状态和提交内容
	 * @param dhTaskInstance
	 * @param taskData
	 * @return
	 */
	private int updateDhTaskInstanceWhenFinishTask(DhTaskInstance dhTaskInstance, String taskData) {
		// 修改当前任务实例状态为已完成
		DhTaskInstance taskInstanceSelective = new DhTaskInstance();
		taskInstanceSelective.setTaskUid(dhTaskInstance.getTaskUid());
		taskInstanceSelective.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		taskInstanceSelective.setTaskFinishDate(new Date());
		taskInstanceSelective.setTaskData(taskData);
		return dhTaskInstanceMapper.updateByPrimaryKeySelective(taskInstanceSelective);
	}

	private int saveTaskToRetryTable(int taskId) {
		DhSynTaskRetry dhSynTaskRetry = new DhSynTaskRetry();
		dhSynTaskRetry.setId(EntityIdPrefix.DH_SYN_TASK_RETRY + UUID.randomUUID().toString());
		dhSynTaskRetry.setTaskId(taskId);
		dhSynTaskRetry.setRetryCount(0);
		dhSynTaskRetry.setStatus(0);
		return dhSynTaskRetryMapper.insert(dhSynTaskRetry);
	}

}
