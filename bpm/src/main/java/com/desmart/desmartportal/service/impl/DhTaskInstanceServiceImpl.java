/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.desmart.desmartbpm.exception.PlatformException;
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
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.IBMApiUrl;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.FormDataUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.BpmFormManageService;
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
	private DhApprovalOpinionService dhapprovalOpinionServiceImpl;

	@Autowired
	private DhRouteService dhRouteServiceImpl;
	
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	
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
	 *
	 */
	@Override
	@Transactional
	public ServerResponse perform(String data) {
		if (StringUtils.isBlank(data)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		JSONObject dataJson = JSONObject.parseObject(data);
		JSONObject taskData = JSONObject.parseObject(String.valueOf(dataJson.get("taskData")));
		JSONObject formData = JSONObject.parseObject(String.valueOf(dataJson.get("formData")));
		JSONArray routeData = JSONObject.parseArray(String.valueOf(dataJson.get("routeData")));
        String currUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);

		Integer taskId = Integer.parseInt(taskData.getString("taskId"));
		String taskUid = taskData.getString("taskUid");
		// 根据任务标识和用户 去查询流程 实例
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (dhTaskInstance == null) {
			return ServerResponse.createByErrorMessage("当前任务不存在!");
		}
		// 校验任务状态
		if (!"12".equals(dhTaskInstance.getTaskStatus())) {// 检查任务是否重复提交
			return ServerResponse.createByErrorMessage("任务已经被提交或暂停");
		}
		// 校验任务类型
        String taskType = dhTaskInstance.getTaskType();
		if (!(DhTaskInstance.TYPE_SIMPLE_LOOP.equals(taskType) || DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP.equals(taskType)
                || DhTaskInstance.TYPE_NORMAL.equals(taskType))) {
            return ServerResponse.createByErrorMessage("任务类型异常");
        }
        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        if (!checkTaskUser(dhTaskInstance, currentUserUid)) {// 检查任务是否有权限提交
            return ServerResponse.createByErrorMessage("提交失败，您没有完成任务的权限");
        }

        // 获得流程实例
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
        if (dhProcessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }

        String insUid = dhProcessInstance.getInsUid();
        Integer insId = dhProcessInstance.getInsId();
        BpmActivityMeta currTaskNode = bpmActivityMetaServiceImpl.queryByPrimaryKey(dhTaskInstance.getTaskActivityId());
        DhActivityConf dhActivityConf = currTaskNode.getDhActivityConf();

        // 整合formdata
        JSONObject mergedFormData = new JSONObject();
        JSONObject insJson = JSONObject.parseObject(dhProcessInstance.getInsData());
        if (StringUtils.isNotBlank(formData.toJSONString())) {
            mergedFormData = FormDataUtil.formDataCombine(formData, insJson.getJSONObject("formData"));
        }

        // 装配处理人信息
        CommonBusinessObject pubBo = new CommonBusinessObject();
        ServerResponse<CommonBusinessObject> serverResponse = dhRouteServiceImpl
                .assembleCommonBusinessObject(pubBo, routeData);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        // 查看是否需要审批记录
        if ("TRUE".equals(dhActivityConf.getActcCanApprove())){
            JSONObject approvalData = dataJson.getJSONObject("approvalData");// 获取审批信息
            if (approvalData == null) {
                return ServerResponse.createByErrorMessage("缺少审批意见");
            }
            String aprOpiComment = approvalData.getString("aprOpiComment");
            if (StringUtils.isBlank(aprOpiComment)) {
                return ServerResponse.createByErrorMessage("缺少审批意见");
            }
            String aprStatus = "通过";
            DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
            dhApprovalOpinion.setInsUid(insUid);
            dhApprovalOpinion.setTaskUid(taskUid);
            dhApprovalOpinion.setActivityId(currTaskNode.getActivityId());
            dhApprovalOpinion.setAprOpiComment(aprOpiComment);
            dhApprovalOpinion.setAprStatus(aprStatus);
            ServerResponse serverResponse2 = dhapprovalOpinionServiceImpl.insertDhApprovalOpinion(dhApprovalOpinion);
            if (!serverResponse2.isSuccess()) {
                return serverResponse2;
            }
        }

        insJson.put("formData", mergedFormData);
        dhProcessInstance.setInsUpdateDate(DateUtil.format(new Date()));
        dhProcessInstance.setInsData(insJson.toJSONString());

        //判断流程是否结束
        BpmRoutingData routingData = dhRouteServiceImpl.getRoutingDataOfNextActivityTo(currTaskNode, formData);

        // 检查用户传递的选人信息是否全面
        if (!dhRouteServiceImpl.checkRouteData(currTaskNode, routeData, routingData)) {
            return ServerResponse.createByErrorMessage("缺少下个环节的用户信息");
        }

        Set<BpmActivityMeta> nextBpmActivityMetas = routingData.getNormalNodes();

        // 更新网关环节的信息
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
                return ServerResponse.createByErrorMessage("更新路中间表失败");
            }
        }


        if(nextBpmActivityMetas == null || nextBpmActivityMetas.size() == 0) {
            dhProcessInstance.setInsStatus(DhProcessInstance.STATUS_COMPLETED);
            dhProcessInstance.setInsStatusId(DhProcessInstance.STATUS_ID_COMPLETED);
        }


        //修改流程实例信息
        dhProcessInstanceMapper.updateByPrimaryKeySelective(dhProcessInstance);

        // 任务完成后 保存到流转信息表里面
        dhRoutingRecordService.saveSubmitTaskRoutingRecordByTaskAndRoutingData(dhTaskInstance, routingData);


        // 修改当前任务实例状态为已完成
        DhTaskInstance taskInstanceSelective = new DhTaskInstance();
        taskInstanceSelective.setTaskUid(taskUid);
        taskInstanceSelective.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
        taskInstanceSelective.setTaskFinishDate(DateUtil.format(new Date()));
        taskInstanceSelective.setTaskData(taskData.toJSONString());
        if(dhTaskInstanceMapper.updateByPrimaryKey(taskInstanceSelective) > 0) {
            //完成任务 删除任务的草稿数据
            dhDraftsMapper.deleteByInsUid(insUid);

            //如果任务为类型为normal，则将其它相同任务id的任务废弃
            if(DhTaskInstance.TYPE_NORMAL.equals(dhTaskInstance.getTaskType())) {
                dhTaskInstanceMapper.updateOtherTaskStatusByTaskId(taskUid,taskId, DhTaskInstance.STATUS_DISCARD);
            }
        }
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);

        // 调用方法完成任务
        Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(taskId, pubBo);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);

        if (errorMap.get("errorResult") == null) {// 任务完成，修改数据信息
            log.info("完成任务结束......");








            return ServerResponse.createBySuccess();
        }else {
            log.info("任务完成失败！");
            throw new PlatformException("任务完成失败");
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
	    BpmActivityMeta currMeta = bpmActivityMetaMapper.queryByFourElement(dhprocessInstance.getProAppId(), dhprocessInstance.getProUid(), 
	            dhprocessInstance.getProVerUid(), dhTaskInstance.getActivityBpdId());
	    if (currMeta == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    DhActivityConf dhActivityConf = dhActivityConfMapper.selectByPrimaryKey(currMeta.getDhActivityConf().getActcUid());
	    
	    
	    List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(currMeta, "default");
	    DhStep formStep = getFirstFormStepOfStepList(steps);
	    if (formStep == null) {
            return ServerResponse.createByErrorMessage("找不到表单步骤");
        }
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
        resultMap.put("activityMeta", currMeta);
        resultMap.put("activityConf", dhActivityConf);
        resultMap.put("dhStep", formStep);
        resultMap.put("processInstance", dhprocessInstance);
	    resultMap.put("taskInstance", dhTaskInstance);
	    resultMap.put("fieldPermissionInfo",fieldPermissionInfo);
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
			// 说明 dhTaskInstance.getActivityBpdId() 实际值为 activityId
			String activityId = dhTaskInstance.getActivityBpdId();
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
			// 上一个加签任务taskUid
			String taskUid = "";
			SysUser sysUser = new SysUser();
			for (String string : usrUids) {
				sysUser.setUserName(string);
				List<SysUser> sysUserList = sysUserMapper.selectAll(sysUser);
				dhTaskInstance.setUsrUid(sysUserList.get(0).getUserId());
				// 验证当前人员是否已经加签
				DhTaskInstance checkDhTaskInstance = dhTaskInstanceMapper.getByUserAndFromTaskUid(dhTaskInstance);
				if (checkDhTaskInstance == null) {
					// normalAdd:随机加签; simpleLoopAdd：顺序加签; multiInstanceLoopAdd:并行加签
					if (DhTaskInstance.TYPE_SIMPLE_LOOPADD.equals(dhTaskInstance.getTaskType())) {
						if (num > 1) {
							dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_WAIT_ADD);
							dhTaskInstance.setToTaskUid(taskUid);
						}else {
							dhTaskInstance.setToTaskUid(dhTaskInstance.getTaskUid());
						}
					}
					dhTaskInstance.setTaskUid("task_instance:"+UUID.randomUUID());
					taskUid = dhTaskInstance.getTaskUid();
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
			currentDhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_WAIT_ADD);
			dhTaskInstanceMapper.updateByPrimaryKey(currentDhTaskInstance);
			if (!completedSigning.isEmpty()) {
				return ServerResponse.createByErrorMessage(completedSigning.substring(0, completedSigning.length()-1)+"已经会签过,其他人员操作成功!");
			}
			return ServerResponse.createBySuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage(e.getMessage());
		}	
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
	public ServerResponse<Map<String, Object>> toFinshedTaskDetail(String taskUid) {
		Map<String, Object> resultMap = new HashMap<>();
	    
	    if (StringUtils.isBlank(taskUid)) {
	        return ServerResponse.createByErrorMessage("缺少必要的参数");
	    }
	    DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
	    if (dhTaskInstance == null || !DhTaskInstance.STATUS_CLOSED.equals(dhTaskInstance.getTaskStatus())) {
	      if(!DhTaskInstance.STATUS_WAIT_ADD.equals(dhTaskInstance.getTaskStatus())) {
	    	  return ServerResponse.createByErrorMessage("任务不存在或任务状态异常");  
	      }
	    }
	    DhProcessInstance dhprocessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
	    if (dhprocessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }
	    
	    // 获得当前环节
	    BpmActivityMeta currMeta = bpmActivityMetaMapper.queryByFourElement(dhprocessInstance.getProAppId(), dhprocessInstance.getProUid(), 
	            dhprocessInstance.getProVerUid(), dhTaskInstance.getActivityBpdId());
	    if (currMeta == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    
	    
	    List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(currMeta, "default");
	    DhStep formStep = getFirstFormStepOfStepList(steps);
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
	    BpmActivityMeta currMeta = bpmActivityMetaMapper.queryByFourElement(dhprocessInstance.getProAppId(), dhprocessInstance.getProUid(), 
	            dhprocessInstance.getProVerUid(), dhTaskInstance.getActivityBpdId());
	    if (currMeta == null) {
	        return ServerResponse.createByErrorMessage("找不到任务相关环节");
	    }
	    
	    
	    List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(currMeta, "default");
	    DhStep formStep = getFirstFormStepOfStepList(steps);
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
			dhapprovalOpinionServiceImpl.insert(dhApprovalOpinion);
			
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
				dhTaskInstanceMapper.updateByPrimaryKey(task);
				// 将当前任务关闭，其他会签人任务作废
				List<DhTaskInstance> dhTaskInstanceList = dhTaskInstanceMapper.getByFromTaskUid(dhTaskInstance.getFromTaskUid());
				for (DhTaskInstance dti : dhTaskInstanceList) {
					if (dhTaskInstance.getTaskUid().equals(dti.getTaskUid())) {
						dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
						dhTaskInstanceMapper.updateByPrimaryKey(dhTaskInstance);
					}else {
						dti.setTaskStatus(DhTaskInstance.STATUS_DISCARD);
						dhTaskInstanceMapper.updateByPrimaryKey(dti);
					}
				}
			}
			// 说明： 如果taskType为simpleLoopAdd，则按照加签人审批顺序进行
			if (DhTaskInstance.TYPE_SIMPLE_LOOPADD.equals(type)) {
				// 将下一个会签审批任务回归到正常状态
				DhTaskInstance nextDhTaskInstance = dhTaskInstanceMapper.getByToTaskUid(taskUid);
				if (nextDhTaskInstance != null) {
					nextDhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKey(nextDhTaskInstance);
					// 将当前任务关闭
					dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
					dhTaskInstanceMapper.updateByPrimaryKey(dhTaskInstance);
				}else {
					// 将主任务状态回归到正常状态
					DhTaskInstance task = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getFromTaskUid());
					// 重新计算剩余时间
					long dueDate = task.getTaskDueDate().getTime() + (long) (task.getRemainHours() * 60 * 60 * 1000);
					task.setTaskDueDate(new Date(dueDate));
					task.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKey(task);
					// 将当前任务关闭
					dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
					dhTaskInstanceMapper.updateByPrimaryKey(dhTaskInstance);
				}
			}
			// 说明：如果taskType为multiInstanceLoopAdd,则需要主任务所有会签任务都审批完，主任务方可回归正常状态
			if (DhTaskInstance.TYPE_MULTI_INSTANCE_LOOPADD.equals(type)) {
				// 将当前任务关闭
				dhTaskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
				dhTaskInstanceMapper.updateByPrimaryKey(dhTaskInstance);
				// 查询主任务的所有会签任务是否都已审批完成
				List<DhTaskInstance> dhTaskInstanceList = dhTaskInstanceMapper.getByFromTaskUid(dhTaskInstance.getFromTaskUid());
				if (dhTaskInstanceList.isEmpty()) {
					// 将主任务状态回归到正常状态
					DhTaskInstance task = dhTaskInstanceMapper.selectByPrimaryKey(dhTaskInstance.getFromTaskUid());
					// 重新计算剩余时间
					long dueDate = task.getTaskDueDate().getTime() + (long) (task.getRemainHours() * 60 * 60 * 1000);
					task.setTaskDueDate(new Date(dueDate));
					task.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);
					dhTaskInstanceMapper.updateByPrimaryKey(task);
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
		List<DhTaskInstance> resultList = dhTaskInstanceMapper.loadPageTaskByClosedByCondition(startTime, endTime, dhTaskInstance);
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
		int count = dhTaskInstanceMapper.updateByPrimaryKey(dhTaskInstance);
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

}
