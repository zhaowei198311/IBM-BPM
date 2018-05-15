/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartportal.common.BpmStatus;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.TaskInstance;
import com.desmart.desmartportal.service.TaskInstanceService;

/**  
* <p>Title: TaskInstanceController</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Controller
@RequestMapping(value = "/taskInstance")
public class TaskInstanceController {
	
	private Logger log = Logger.getLogger(TaskInstanceController.class);
	
	@Autowired
	private TaskInstanceService taskInstanceService;
	
	
	@RequestMapping(value = "/queryTask")
	@ResponseBody
	private ServerResponse queryTaskByList(TaskInstance taskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		taskInstance.setTaskStatus(BpmStatus.TASK_STATUS_RECEIVED);
		return taskInstanceService.selectAllTask(taskInstance, pageNum, pageSize);
	}
}
