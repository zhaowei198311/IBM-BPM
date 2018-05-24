/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.IBMApiUrl;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.util.JsonUtil;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.MenusService;
import com.desmart.desmartportal.service.SysDateService;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;
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
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysDateService sysDateService;

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
	public ServerResponse perform(String data) {
		log.info("完成任务开始......");
		try {
			
			log.info("完成任务结束......");
			return ServerResponse.createBySuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByError();
		}
	}

	public boolean isTaskExists(int taskId) {
		if (taskId == 0) {
			return false;
		}
		return dhTaskInstanceMapper.countByTaskId(taskId) > 0;
	}

	/**
	 * 任务代办 详细信息
	 */
	@Override
	public Map<String, Object> taskInfo(String taskUid) {
		log.info("代办任务详细信息查询 开始......");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			/**
			 * 首先要通过任务实例数据信息里的 流程实例id 去查询流程，流程图元素id(activityBpdId) form表单id 然后把这些数据存放map 返回给
			 * 前台 代办页面
			 */
			List<DhTaskInstance> taskList = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
			
			BpmActivityMeta activityMeta=new BpmActivityMeta();
			String activityId="";
			
			for (DhTaskInstance dhTaskInstance : taskList) {
				// 查询流程
				resultMap.put("taskId", dhTaskInstance.getTaskId());
				DhProcessInstance dhprocessInstance = dhProcessInstanceMapper
						.selectByPrimaryKey(dhTaskInstance.getInsUid());
				resultMap.put("proAppId", dhprocessInstance.getProAppId());
				resultMap.put("proUid", dhprocessInstance.getProUid());
				resultMap.put("proVerUid", dhprocessInstance.getProVerUid());
				resultMap.put("insUid", dhprocessInstance.getInsUid());
				resultMap.put("insId", dhprocessInstance.getInsId());
				resultMap.put("insData", dhprocessInstance.getInsData());
				// 查询流程图元素信息
				BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
				bpmActivityMeta.setBpdId(dhprocessInstance.getProUid());
				bpmActivityMeta.setProAppId(dhprocessInstance.getProAppId());
				bpmActivityMeta.setSnapshotId(dhprocessInstance.getProVerUid());
				List<BpmActivityMeta> bpmActivityList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
				
				
				
				//获取当前环节  找到下一环节
				bpmActivityMeta.setActivityBpdId(dhTaskInstance.getActivityBpdId());
				List<BpmActivityMeta> activityMetas=bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
				if(activityMetas!=null&&activityMetas.size()>0) {
					activityMeta=activityMetas.get(0);
					activityId=activityMeta.getActivityId();
				}
				resultMap.put("activityMetaList", menusService.backlogActivityHandler(activityMeta));
				resultMap.put("activityId", activityId);
				
				
				// 转json
				String listStr = JsonUtil.obj2String(bpmActivityList);
				resultMap.put("listStr", listStr);
				// 查找表单id
				Map<String, Object> formMap = dhProcessFormService.queryProcessForm(dhprocessInstance.getProAppId(),
						dhprocessInstance.getProUid(), dhprocessInstance.getProVerUid());
				resultMap.put("formId", formMap.get("formId"));
				return resultMap;
			}
		} catch (Exception e) {
			log.info("查询代办任务详细信息失败");
			e.printStackTrace();
			return null;
		}
		log.info("代办任务详细信息查询 结束......");
		return resultMap;
	}

	@Override
	public List<DhTaskInstance> selectByInsUidAndTaskTypeCondition(String insUid) {
		// TODO Auto-generated method stub
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
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectTaskAndProcessInfo(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize) {
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
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		// 创建时间
		Date createDate = new Date();
		// 时间个数
		Double timeAmount = null;
		// 时间单位
		String timeType = "";
		// 审批剩余时间
		int hour = 0;
		// 百分比
		int procent = 0;
		// 根据taskUid查询单个DH_TASK_INSTANCE对象
		List<DhTaskInstance> dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		for (DhTaskInstance dti : dhTaskInstance) {	
			createDate = dti.getTaskInitDate();
			// 根据proUid，proVerUid，proAppId，activityBpdId查询单个BPM_ACTIVITY_META对象
			String activityBpdId = dti.getActivityBpdId();
			BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
			bpmActivityMeta.setBpdId(proUid);
			bpmActivityMeta.setSnapshotId(proVerUid);
			bpmActivityMeta.setProAppId(proAppId);
			bpmActivityMeta.setActivityBpdId(activityBpdId);
			List<BpmActivityMeta> bpmActivityMeta_1 = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
			for (BpmActivityMeta bam : bpmActivityMeta_1) {
				// DH_ACTIVITY_CONF对象
				DhActivityConf dhActivityConf = bam.getDhActivityConf();
				timeAmount = dhActivityConf.getActcTime();
				timeType = dhActivityConf.getActcTimeunit();
			}
			
		}
		// 审批最后日期
		Date lastDate = sysDateService.lastTime(createDate, timeAmount, timeType);
		if (timeAmount == null) {
			timeAmount = 24.0;
		}else {
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
		}else {
			hour = (int) (lastDate.getTime() - new Date().getTime()) / (1000 * 60 * 60);
			// 百分比
			procent = (int) (((double)new Date().getTime() / lastDate.getTime()) * 100);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("precent", 100 - procent);
		map.put("hour", hour);
		return ServerResponse.createBySuccess(map);
	}

}
