/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.RestUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
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
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	
	
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
        
        
        
        String proAppId = processData.getString("proAppId");
        String proUid = processData.getString("proUid");
        String proVerUid = processData.getString("proVerUid");
        
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("缺少必要参数");
        }
        
        
        
        
        log.info("发起流程开始......");
        String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        SysUser currentUser = sysUserMapper.queryByPrimaryKey(currentUserUid);
        BpmActivityMeta firstHumanActivity = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, proVerUid).getData();
        
        
        
        
        HttpReturnStatus result = new HttpReturnStatus();
        
        // 掉用API 发起一个流程
        BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        CommonBusinessObject pubBo = new CommonBusinessObject();
        pubBo.setCreatorId(currentUserUid);
        pubBo.setNextOwners_0(Arrays.asList(new String[] {"00011178"}));
        
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
        result = bpmProcessUtil.startProcess(proAppId, proUid, proVerUid, pubBo);
        
        // 如果获取API成功 将返回过来的流程数据 保存到 平台
        if (!BpmClientUtils.isErrorResult(result)) {
            // 获得流流程编号,和第一个任务的编号
            int insId = getProcessId(result);
            int taskId = getFirstTaskId(result);
            
            BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
            HttpReturnStatus startTaskResult = bpmTaskUtil.startTask(taskId);
            // 如果完成任务成功
            if (200 == startTaskResult.getCode()) {
                // 数据库中插入流程， 数据库中插入任务
                DhProcessInstance processInstance = new DhProcessInstance();
                processInstance.setInsUid(EntityIdPrefix.DH_PROCESS_INSTANCE + UUID.randomUUID().toString());
                processInstance.setInsTitle("DEMO_TITLE");
                processInstance.setInsId(insId);
                processInstance.setInsStatusId(12);
                processInstance.setProAppId(proAppId);
                processInstance.setProUid(proUid);
                processInstance.setProVerUid(proVerUid);
                processInstance.setInsInitDate(new Date());
                processInstance.setInsInitUser(currentUserUid);
                processInstance.setCompanyNumber(currentUser.getCompanynumber());
                JSONObject insData = new JSONObject();
                insData.put("formData", formDataFromTask);
                processInstance.setInsData(insData.toJSONString());
                processInstance.setInsStatus("run");
                dhProcessInstanceMapper.insertProcess(processInstance);
                
                DhTaskInstance taskInstance = new DhTaskInstance();
                taskInstance.setTaskUid(EntityIdPrefix.DH_TASK_INSTANCE + UUID.randomUUID().toString());
                taskInstance.setUsrUid(currentUserUid);
                taskInstance.setActivityBpdId(firstHumanActivity.getActivityBpdId());
                taskInstance.setTaskData(data);
                taskInstance.setTaskId(taskId);
                taskInstance.setInsUid(processInstance.getInsUid());
                taskInstance.setTaskType(DhTaskInstance.TYPE_NORMAL);
                taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
                taskInstance.setTaskInitDate(new Date());
                taskInstance.setTaskFinishDate(new Date());
                dhTaskInstanceMapper.insertTask(taskInstance);
                
                // 任务完成后 保存到流转信息表里面
                DhRoutingRecord dhRoutingRecord = new DhRoutingRecord();
                dhRoutingRecord.setRouteUid(EntityIdPrefix.DH_ROUTING_RECORD + String.valueOf(UUID.randomUUID()));
                dhRoutingRecord.setInsUid(processInstance.getInsUid());
                dhRoutingRecord.setActivityName(firstHumanActivity.getActivityName());
                dhRoutingRecord.setRouteType(RouteStatus.ROUTE_STARTPROCESS);
                // 发起流程 第一个流转环节信息 的 用户id 是 自己
                dhRoutingRecord.setUserUid(currentUserUid);
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
}
