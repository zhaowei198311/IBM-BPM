/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

/**  
* <p>Title: TaskInstanceController</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Controller
@RequestMapping(value = "/taskInstance")
public class DhTaskInstanceController {
	
	private Logger log = Logger.getLogger(DhTaskInstanceController.class);
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	
	/**
	 * 查询代办(任务状态接受)
	 * @param taskInstance
	 * @param pageNum 第几页
	 * @param pageSize 每页最大显示10
	 * @return
	 */
	@RequestMapping(value = "/queryTaskByReceived")
	@ResponseBody
	private ServerResponse queryTaskByReceived(DhTaskInstance taskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize,@DateTimeFormat(pattern ="yyyy-MM-dd")Date initTime) {
		taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		taskInstance.setTaskInitDate(initTime);
		taskInstance.setTaskStatus(DhTaskInstance.STATUS_RECEIVED);		
		return dhTaskInstanceService.selectTaskAndProcessInfo(taskInstance, pageNum, pageSize);
	}
	
	/**
	 * 查询已办(任务状态关闭)
	 * @param taskInstance
	 * @param pageNum 第几页
	 * @param pageSize 每页最大显示10
	 * @return
	 */
	@RequestMapping(value = "/queryTaskByClosed")
	@ResponseBody
	private ServerResponse queryTaskByClosed(DhTaskInstance taskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@DateTimeFormat(pattern ="yyyy-MM-dd")Date initTime,@DateTimeFormat(pattern ="yyyy-MM-dd")Date dueTime) {
		taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		taskInstance.setTaskInitDate(initTime);
		taskInstance.setTaskDueDate(dueTime);
		return dhTaskInstanceService.selectAllTask(taskInstance, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/finshedTask")
	@ResponseBody
	private ServerResponse finshedTask(@RequestParam(value="data") String data) {		
		//解析data
		return dhTaskInstanceService.perform(data);
	}
	
	/**
	 * |
	 * @Title: queryProgressBar  
	 * @Description: 查询审批进度  
	 * @param @param bpmActivityMeta
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	@RequestMapping(value = "/queryProgressBar")
	@ResponseBody
	public ServerResponse<?> queryProgressBar(String proUid, String proVerUid, String proAppId, String taskUid){
		return dhTaskInstanceService.queryProgressBar(proUid, proVerUid, proAppId, taskUid);
	}
	
	/**
	 * 
	 * @Title: addSure  
	 * @Description: 加签确定  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	@RequestMapping(value = "/addSure")
	@ResponseBody
	public ServerResponse<?> addSure(DhTaskInstance dhTaskInstance){
		return dhTaskInstanceService.addSure(dhTaskInstance);
	}
}
