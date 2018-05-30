/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.RouteStatus;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.common.util.FormDataUtil;
import com.desmart.common.util.RestUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.MenusService;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SysUserDepartmentService;
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
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	@Autowired
	private SysUserDepartmentService sysUserDepartmentService;
	@Autowired
	private DhStepService dhStepService;
	@Autowired
	private BpmFormManageService bpmFormManageService;
	@Autowired
	private MenusService menusService;
	@Autowired
	private DhDraftsMapper dhDraftsMapper;
	
	@Autowired
	private DhActivityConfMapper dhActivityConfMapper;
	@Autowired
	private DhRouteService dhRouteService;
	
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
    public ServerResponse startProcess(String data) {
        if (StringUtils.isBlank(data)) {
            return ServerResponse.createByErrorMessage("缺少必要参数");
        }
        JSONObject dataJson = JSONObject.parseObject(data);
        JSONObject formDataFromTask = (JSONObject)dataJson.get("formData");
        JSONArray routeData = dataJson.getJSONArray("routeData");
        JSONObject processData = (JSONObject)dataJson.get("processData");
        String companyNumber = processData.getString("companyNumber");;
        String departNo = processData.getString("departNo");;
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
        
        DhProcessDefinition startableDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
        
        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        SysUser currentUser = sysUserMapper.queryByPrimaryKey(currentUserUid);
        BpmActivityMeta firstHumanActivity = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, proVerUid).getData();
        
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
            
            List<BpmActivityMeta> nextActivities = dhRouteService.getNextActivities(firstHumanActivity, mergedFromData);
            dhRouteService.updateGatewayRouteResult(firstHumanActivity, insId, mergedFromData);
            
            // 封装下一环节的处理人
            ServerResponse<CommonBusinessObject> assembleResponse = dhRouteService.assembleCommonBusinessObject(pubBo, routeData);
            if (!assembleResponse.isSuccess()) {
                return assembleResponse;
            }
            
            // 完成第一个任务
            BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
            Map<String, HttpReturnStatus> commitTaskMap = bpmTaskUtil.commitTask(taskId, pubBo);
            // 如果完成任务成功
            if (!commitTaskMap.containsKey("errorMap") && commitTaskMap.get("commitTaskResult") != null && commitTaskMap.get("commitTaskResult").getCode() == 200) {
                // 更新草稿流程实例的状态
                DhProcessInstance instanceSelective = new DhProcessInstance();
                instanceSelective.setInsUid(dhProcessInstance.getInsUid());
                instanceSelective.setInsTitle("DEMO_TITLE");
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
                dhRoutingRecord.setRouteType(RouteStatus.ROUTE_STARTPROCESS);
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
	 * @param httpReturnStatus
	 */
	private int getProcessId(HttpReturnStatus httpReturnStatus) {
	    JSONObject jsonBody = JSONObject.parseObject(httpReturnStatus.getMsg());
        JSONObject jsonBody2 = JSONObject.parseObject(String.valueOf(jsonBody.get("data")));
        return new Integer(jsonBody2.getString("piid"));
	}
	
	
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
			String result = httpClientUtils.checkLoginIbm("http://10.0.4.201:9080/rest/bpm/wle/v1/visual/processModel/instances?instanceIds=["+insId+"]&showCurrentActivites=true&showExecutionPath=true&showNote=true&showColor=true&image=true");
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
        processInstance.setInsTitle("DEMO_TITLE");
        processInstance.setInsId(-1);
        processInstance.setInsStatusId(DhProcessInstance.STATUS_ID_DRAFT);
        processInstance.setInsStatus(DhProcessInstance.STATUS_DRAFT);
        processInstance.setProAppId(dhProcessDefinition.getProAppId());
        processInstance.setProUid(dhProcessDefinition.getProUid());
        processInstance.setProVerUid(dhProcessDefinition.getProVerUid());
        processInstance.setInsInitDate(new Date());
        processInstance.setInsInitUser((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
        //processInstance.setCompanyNumber(currentUser.getCompanynumber());
        JSONObject insData = new JSONObject();
        insData.put("formData", new JSONObject());
        JSONObject processData = new JSONObject();
        processData.put("insUid", processInstance.getInsUid());
        insData.put("processData", processData);
        
        processInstance.setInsData(insData.toJSONString());
        //processInstance.setInsStatus("run");
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
        
        String formData = "";
        if (StringUtils.isBlank(insUid)) {
            // 不是草稿箱来的
            processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(proAppId, proUid);
            if (processDefintion == null) {
                return ServerResponse.createByErrorMessage("当前流程没有可发起的版本");
            }
            processInstance = this.generateDraftDefinition(processDefintion);
        } else {
            // 是草稿箱来的
            processInstance = this.getByInsUid(insUid);
            if (processInstance == null) {
                return ServerResponse.createByErrorMessage("草稿中的流程实例不存在");
            }
            processDefintion = dhProcessDefinitionService.getStartAbleProcessDefinition(processInstance.getProAppId(), processInstance.getProUid());
            if (processDefintion == null || !processDefintion.getProVerUid().equals(processInstance.getProVerUid())) {
                return ServerResponse.createByErrorMessage("不能用此草稿版本发起流程");
            }
            proAppId = processInstance.getProAppId();
            proUid = processInstance.getProUid();
            DhDrafts dhDrafts = dhDraftsMapper.queryDraftsByInsUid(insUid);
            JSONObject jsonObj = JSONObject.parseObject(dhDrafts.getDfsData());
            formData = jsonObj.getString("formData");
        }
        
        ServerResponse<BpmActivityMeta> metaResponse = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, processDefintion.getProVerUid());
        if (!metaResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("找不到第一个人工环节");
        }
        BpmActivityMeta firstHumanMeta = metaResponse.getData();
        
        // 获得默认步骤的列表
        List<DhStep> steps = dhStepService.getStepsOfBpmActivityMetaByStepBusinessKey(firstHumanMeta, "default");
        DhStep formStep = getFirstFormStepOfStepList(steps);
        if (formStep == null) {
            return ServerResponse.createByErrorMessage("找不到表单步骤");
        }
        
        // 获得表单文件内容
        ServerResponse formResponse = bpmFormManageService.getFormFileByFormUid(formStep.getStepObjectUid());
        if (!formResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("获得表单数据失败");
        }
        
        if (StringUtils.isBlank(insUid)) {
            dhProcessInstanceMapper.insertProcess(processInstance);
        }
        
        resultMap.put("currentUser", currentUser);
        resultMap.put("processDefinition", processDefintion);
        resultMap.put("formData", formData);
        resultMap.put("bpmActivityMeta", firstHumanMeta);
        resultMap.put("dhActivityConf", firstHumanMeta.getDhActivityConf());
        resultMap.put("userDepartmentList", userDepartmentList);
        resultMap.put("dhStep", formStep);
        resultMap.put("formHtml", formResponse.getData());
        resultMap.put("processInstance", processInstance);
        return ServerResponse.createBySuccess(resultMap);
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
    @Transactional
	public ServerResponse rejectProcess(int insId,String activityId, String user) {
		if(insId!=0 || StringUtils.isBlank(activityId) || StringUtils.isBlank(user)) {
			return ServerResponse.createByErrorMessage("缺少必要参数");
		}
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.queryByInsId(insId);
		String startProcessHuman = dhProcessInstance.getInsInitUser(); // 流程发起人
		
		DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
		// 当前环节是否允许驳回 （TRUE,FALSE）
		String rejectboolean = dhActivityConf.getActcCanReject();
		// 当前环节驳回方式 (toProcessStart发起人,toPreActivity上个环节,toActivities选择环节)
		String rejectType =  dhActivityConf.getActcRejectType();
		// 可以驳回
		if(Const.Boolean.TRUE.equals(rejectboolean)) {
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
			switch (rejectType) {
				case "toProcessStart":
					// 发起人
					bpmProcessUtil.rejectProcess(insId, activityId, startProcessHuman);
					break;
				case "toPreActivity":
					// 上个环节
					break;
				case "toActivities":
					// 选择环节
					bpmProcessUtil.rejectProcess(insId, activityId, user);
					break;
			}	
		}
		return ServerResponse.createBySuccess();
	}
}
