/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.util.http.HttpClientUtils;

/**
 * <p>
 * Title: ProcessInstanceController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年5月9日
 */
@Controller
@RequestMapping(value = "/processInstance")
public class DhProcessInstanceController {
	
	private Logger log = Logger.getLogger(DhProcessInstanceController.class);

	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;

	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	@RequestMapping(value = "/startProcess")
	@ResponseBody
	public ServerResponse startProcess(String data) {
		// 发起流程		
		try {
			return dhProcessInstanceService.startProcess(data);
        } catch (Exception e) {
            log.error("发起流程失败", e);
            return ServerResponse.createByErrorMessage("发起流程失败");
        }
	}
	
	@RequestMapping(value = "/queryProcessByUser")
	@ResponseBody
	public ServerResponse queryProcessByUser() {
		
		return null;	
	}
	
	@RequestMapping(value = "/viewProcess")
	@ResponseBody
	public String viewProcess(String insId) {
		// 查看流程图 需要流程实例  这一步目的是 去 掉用IBM 做一次 登陆验证
		HttpClientUtils httpUtils = new HttpClientUtils();
		String url = "http://10.0.4.201:9080/teamworks/executecf?modelID=1.36bdcc65-8d6a-4635-85cf-57cab68a7e45&branchID=2063.34a0ce6e-631b-465d-b0dc-414c39fb893f&tw.local.processInstanceId="+insId;
		httpUtils.checkApiLogin("get", url, null);
		return url;
	}

	/**
	 * 根据userId查询instuid,再根据instuid查processInstanceList
	 * 
	 * @param userId
	 *            用户id
	 * @param proUid
	 *            流程id
	 * @param proAppId
	 *            流程应用库id
	 * @param verUid
	 *            流程版本id
	 * @return
	 */
	@RequestMapping(value = "/getInfo")
	@ResponseBody
	public ServerResponse getInfo(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "proAppId") String proAppId, @RequestParam(value = "proUid") String proUid,
			@RequestParam(value = "verUid") String verUid,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		// userId = "00011178";
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance.setUsrUid(userId);
		return dhTaskInstanceService.selectTaskByUser(taskInstance, pageNum, pageSize);// 根据userId查询taskList
	}

	@RequestMapping(value = "/queryProcessByUserAndType")
	@ResponseBody
	public ServerResponse queryProcessByUserAndType(@RequestParam(value = "insTitle", required = false) String insTitle,
			@RequestParam(value = "insStatusId", required = false) String insStatusId,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("insTitle", insTitle);
		paramsMap.put("insStatusId", insStatusId);
		return dhProcessInstanceService.queryByStausOrTitle(paramsMap, pageNum, pageSize);
	}

	@RequestMapping(value = "/queryProcessByActive")
	@ResponseBody
	public ServerResponse queryProcessByActive(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		// 根据当前用户查询 他有哪些流程
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance
				.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		return dhTaskInstanceService.selectTaskByUser(taskInstance, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/rejectProcess")
	@ResponseBody
	public ServerResponse rejectProcess() {
		
		return null;
	}
}
