/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.IBMApiUrl;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: TaskInstanceServiceImpl</p>  
* <p>Description: </p>  
* @author 张志颖  
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
	
	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectAllTask(DhTaskInstance taskInstance, Integer pageNum, Integer pageSize) {
		log.info("查询taskInstance开始......");
		try {
			// 查询用户
/*			// 根据用户id 查询用户信息
			SysUser sysUser = new SysUser();
			sysUser.setUserId(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
			SysUser sysUser2 = sysUserMapper.findById(sysUser);
			sysUser2.getUserName();*/
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
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectByPrimaryKey(String taskUid, Integer pageNum, Integer pageSize) {
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
	 * 根据用户id 查询 有多少代办任务
	 */
	@Override
	public int selectByusrUid(String usrUid) {
		return	dhTaskInstanceMapper.selectByusrUid(usrUid);
	}

	/* 
	 * 根据用户id  查询 他有哪些流程
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectTaskByUser(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize) {
		log.info("根据用户id查询有哪些流程.开始......");
		List <DhProcessInstance> resultList = new ArrayList<DhProcessInstance>();
		try {
			PageHelper.startPage(pageNum, pageSize);
			List <DhTaskInstance> taskInstanceList = dhTaskInstanceMapper.selectAllTask(taskInstance);//根据userId查询taskList
			if(taskInstanceList.size() > 0) {
				for(DhTaskInstance taskInstance1 : taskInstanceList) {
					DhProcessInstance processInstance = new DhProcessInstance();
					int id = Integer.parseInt(DhProcessInstance.STATUS_ACTIVE);
					processInstance.setInsUid(taskInstance1.getInsUid());//获取taskList里的insUid
					processInstance.setInsStatusId(id);
					List <DhProcessInstance> processInstanceList= dhProcessInstanceMapper.selectAllProcess(processInstance);//根据instUid查询processList
					for(DhProcessInstance p : processInstanceList) {
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
        return dhTaskInstanceMapper.getMaxTaskId();
    }
    
    public int insertBatch(List<DhTaskInstance> list) {
        return dhTaskInstanceMapper.insertBatch(list);
    }

	/**
	 * 寻找 并 设置流程变量 更具activityid 去 找 meta下的 LoopType 知道是简单循环还是多循环 (3种方式)要判断
	 * if loopType 为 none 单 实例 情况下 就不需要 activityCofg 表下的 会签变量数据(sign_Count_varname)
	 * else 就需要  会签变量数据 
	 * activityCofg  下的 分派变量名称 是 必须要的
	 * 
	 * @param activityId 环节关联id
	 * @param tkkid 任务实例id (引擎)
	 */
	@Override
	public void queryTaskSetVariable(String activityId, String tkkid) {
		
		log.info("寻找流程变量开始......");
		try {
			BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
			String LoopType = bpmActivityMeta.getLoopType();
			log.info("循环类型:"+LoopType);
			HttpReturnStatus result = new HttpReturnStatus();
			HttpClientUtils httpClientUtils = new HttpClientUtils();
			Map<String,Object> params = new HashMap<>();
			params.put("action", "setData");
			if("none".equals(LoopType)) {
				// 单实例循环
				DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(bpmActivityMeta.getActivityId());
				// 获取变量并赋值   {"pubBo":{"nextOwners_0":["XXXXXXXXX"]}}
				String variable = dhActivityConf.getActcAssignVariable();
				String jsonstr = "{\"pubBo\":{\""+variable+"\":[\"00011178\"],\"creatorId\":\"00011178\"}}";
			//	JSONObject jsonObj = JSONObject.   .parseObject(jsonstr);
				params.put("params", jsonstr);
				result = httpClientUtils.checkApiLogin("put", IBMApiUrl.IBM_API_TASK+tkkid, params);
				log.info("掉用API状态码:"+result.getCode());		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("寻找流程变量结束......");
	}

	/* 
	 *
	 */
	@Override
	public void perform(String tkkid) {
		log.info("完成任务开始......");
		try {
			HttpReturnStatus result = new HttpReturnStatus();
			HttpClientUtils httpClientUtils = new HttpClientUtils();
			Map<String,Object> params = new HashMap<>();
			params.put("action", "finish");
			params.put("parts", "all");
			result = httpClientUtils.checkApiLogin("put", IBMApiUrl.IBM_API_TASK+tkkid, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("完成任务结束......");
	}
}
