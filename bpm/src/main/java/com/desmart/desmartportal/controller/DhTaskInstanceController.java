/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.Date;
import java.util.List;

import com.desmart.common.annotation.log.Log;
import net.sf.jsqlparser.schema.Server;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageInfo;


@Controller
@RequestMapping(value = "/taskInstance")
public class DhTaskInstanceController {
	private static final Logger LOG = LoggerFactory.getLogger(DhTaskInstanceController.class);
	
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
	public ServerResponse queryTaskByReceived(DhTaskInstance taskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize,@DateTimeFormat(pattern ="yyyy-MM-dd")Date initTime) {
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
	public ServerResponse queryTaskByClosed(DhTaskInstance taskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@DateTimeFormat(pattern ="yyyy-MM-dd")Date initTime,@DateTimeFormat(pattern ="yyyy-MM-dd")Date dueTime) {
		taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		taskInstance.setTaskStatus(DhTaskInstance.STATUS_CLOSED);
		taskInstance.setTaskInitDate(initTime);
		taskInstance.setTaskDueDate(dueTime);
		return dhTaskInstanceService.selectAllTask(taskInstance, pageNum, pageSize);
	}

	@RequestMapping(value = "/finshedTask")
	@ResponseBody
	@Log(description = "提交一个任务")
	public ServerResponse finshedTask(@RequestParam(value="data") String data) {
		try {
			return dhTaskInstanceService.perform(data);
		} catch (Exception e) {
			LOG.error("完成任务失败，提交数据：" + data, e);
			return ServerResponse.createByErrorMessage("完成任务失败");
		}

	}

	/**
	 * 驳回任务
	 * @param data
	 * @return
	 */
	@Log(description = "驳回一个任务")
	@RequestMapping(value = "/rejectTask")
	@ResponseBody
	public ServerResponse rejectTask(@RequestParam(value = "data") String data) {
		try {
			return dhTaskInstanceService.rejectTask(data);
		} catch (Exception e) {
			LOG.error("完成任务失败，提交数据：" + data, e);
			return ServerResponse.createByErrorMessage("驳回任务失败");
		}
	}

	/**
	 * 跳转到驳回的环节
	 * @param data
	 * @return
	 */
	@Log(description = "跳转到驳回的环节")
	@RequestMapping(value = "/skipFromReject")
	@ResponseBody
	public ServerResponse skipFromReject(@RequestParam(value = "data") String data) {
		try {
			return dhTaskInstanceService.skipFromReject(data);
		} catch (Exception e) {
			LOG.error("完成任务失败，提交数据：" + data, e);
			return ServerResponse.createByErrorMessage("驳回任务失败");
		}
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
	public ServerResponse<?> queryProgressBar(String activityId, String taskUid){
		return dhTaskInstanceService.queryProgressBar(activityId, taskUid);
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
	@Log(description = "进行加签操作")
	@RequestMapping(value = "/addSure")
	@ResponseBody
	public ServerResponse<?> addSure(DhTaskInstance dhTaskInstance){
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		return dhTaskInstanceService.addSure(dhTaskInstance, creator);
	}
	
	/**
	 * 
	 * @Title: finishAdd  
	 * @Description: 加签审批完成  
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	@Log(description = "提交一个任务")
	@RequestMapping(value = "/finishAdd")
	@ResponseBody
	public ServerResponse<?> finishAdd(String taskUid, String activityId, String approvalContent){
		return dhTaskInstanceService.finishAdd(taskUid, activityId, approvalContent);
	}
	
	/**
	 * 初始化PC端待办列表
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param insTitle
	 * @param createProcessUserName
	 * @param proName
	 * @param isAgent
	 * @return
	 */
	@RequestMapping("/loadBackLog")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadBackLog(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
			DhTaskInstance dhTaskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@RequestParam("insTitle")String insTitle,@RequestParam("createProcessUserName")String createProcessUserName
			,@RequestParam("proName")String proName,@RequestParam("isAgent")String isAgent) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        if((insTitle!=null && !"".equals(insTitle))||(proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setInsTitle(insTitle);
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
        if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
        	SysUser sysUser = new SysUser();
        	sysUser.setUserName(createProcessUserName);
        	dhTaskInstance.setSysUser(sysUser);
        }
		return dhTaskInstanceService.selectBackLogTaskInfoByCondition(startTime
				, endTime, dhTaskInstance, pageNum, pageSize, isAgent);
	}
	
	/**
	 * 初始化移动端待办列表
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param insTitle
	 * @param createProcessUserName
	 * @param proName
	 * @param isAgent
	 * @return
	 */
	@RequestMapping("/loadBackLogMove")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadBackLogMove(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
			DhTaskInstance dhTaskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@RequestParam("insTitle")String insTitle,@RequestParam("createProcessUserName")String createProcessUserName
			,@RequestParam("proName")String proName,@RequestParam("isAgent")String isAgent) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        if((insTitle!=null && !"".equals(insTitle))||(proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setInsTitle(insTitle);
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
        if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
        	SysUser sysUser = new SysUser();
        	sysUser.setUserName(createProcessUserName);
        	dhTaskInstance.setSysUser(sysUser);
        }
		return dhTaskInstanceService.selectBackLogTaskInfoByConditionMove(startTime
				, endTime, dhTaskInstance, pageNum, pageSize, isAgent);
	}
	
	/**
	 * 查询已办列表的数据
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param insTitle
	 * @param createProcessUserName
	 * @param proName
	 * @param isAgent
	 * @return
	 */
	@RequestMapping("/loadPageTaskByClosed")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosed(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
			DhTaskInstance dhTaskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@RequestParam("insTitle")String insTitle,@RequestParam("createProcessUserName")String createProcessUserName
			,@RequestParam("proName")String proName,@RequestParam("isAgent")String isAgent) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        if((insTitle!=null && !"".equals(insTitle))||(proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setInsTitle(insTitle);
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
        if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
        	SysUser sysUser = new SysUser();
        	sysUser.setUserName(createProcessUserName);
        	dhTaskInstance.setSysUser(sysUser);
        }
        try{
        	return dhTaskInstanceService.loadPageTaskByClosedByCondition(startTime
    				, endTime, dhTaskInstance, pageNum, pageSize, isAgent);
        }catch(Exception e){
        	LOG.error("渲染任务列表失败", e);
        	return ServerResponse.createByErrorMessage(e.getMessage());
        }
	}
	
	/**
	 * 查询移动端已办列表的数据
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param insTitle
	 * @param createProcessUserName
	 * @param proName
	 * @param isAgent
	 * @return
	 */
	@RequestMapping("/loadPageTaskByClosedMove")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedMove(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
			DhTaskInstance dhTaskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@RequestParam("insTitle")String insTitle,@RequestParam("createProcessUserName")String createProcessUserName
			,@RequestParam("proName")String proName,@RequestParam("isAgent")String isAgent) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        if((insTitle!=null && !"".equals(insTitle))||(proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setInsTitle(insTitle);
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
        if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
        	SysUser sysUser = new SysUser();
        	sysUser.setUserName(createProcessUserName);
        	dhTaskInstance.setSysUser(sysUser);
        }
        try{
        	return dhTaskInstanceService.loadPageTaskByClosedByConditionMove(startTime
    				, endTime, dhTaskInstance, pageNum, pageSize, isAgent);
        }catch(Exception e){
        	LOG.error("渲染任务列表失败", e);
        	return ServerResponse.createByErrorMessage(e.getMessage());
        }
	}
	
	/**
	 * 查询移动端办结列表的数据
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @param insTitle
	 * @param createProcessUserName
	 * @param proName
	 * @param isAgent
	 * @return
	 */
	@RequestMapping("/loadPageTaskByEndMove")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByEndMove(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime
			, @DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
			DhTaskInstance dhTaskInstance,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize
			,@RequestParam("insTitle")String insTitle,@RequestParam("createProcessUserName")String createProcessUserName
			,@RequestParam("proName")String proName,@RequestParam("isAgent")String isAgent) {
		String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhTaskInstance.setUsrUid(currentUserUid);
        if((insTitle!=null && !"".equals(insTitle))||(proName!=null&& !"".equals(proName))) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
        	dhProcessInstance.setInsTitle(insTitle);
        	dhProcessInstance.setProName(proName);
        	dhTaskInstance.setDhProcessInstance(dhProcessInstance);
        }
        if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
        	SysUser sysUser = new SysUser();
        	sysUser.setUserName(createProcessUserName);
        	dhTaskInstance.setSysUser(sysUser);
        }
        try{
        	return dhTaskInstanceService.loadPageTaskByEndByConditionMove(startTime
    				, endTime, dhTaskInstance, pageNum, pageSize, isAgent);
        }catch(Exception e){
        	LOG.error("渲染任务列表失败", e);
        	return ServerResponse.createByErrorMessage(e.getMessage());
        }
	}
	
	@RequestMapping("/alreadyClosedTask")
	@ResponseBody
	public Integer alreadyClosedTask(String userId) {
		if(userId==null) {
			userId  = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		}
		return dhTaskInstanceService.alreadyClosedTaskByusrUid(userId);
	}
	
	/**
	 * 查询抄送已读/未读任务
	 * @Title: queryTransfer  
	 * @Description: 查询抄送已读/未读任务  
	 * @param @param dhTaskInstance
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @return  
	 * @return ServerResponse<PageInfo<List<DhTaskInstance>>>  
	 * @throws
	 */
	@RequestMapping(value = "/queryTransfer")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> queryTransfer(@DateTimeFormat(pattern ="yyyy-MM-dd")Date startTime,
																		@DateTimeFormat(pattern ="yyyy-MM-dd")Date endTime,
																		@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
																		@RequestParam(value="pageSize", defaultValue="10")Integer pageSize,
																		@RequestParam("insTitle")String insTitle,
																		@RequestParam("proName")String proName,
																		@RequestParam("createProcessUserName")String createProcessUserName,
																		DhTaskInstance dhTaskInstance){
		String currentUserUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		dhTaskInstance.setUsrUid(currentUserUid);
        if(insTitle!=null && !"".equals(insTitle)) {
        	DhProcessInstance dhProcessInstance = new DhProcessInstance();
    		dhProcessInstance.setInsTitle(insTitle);
    		dhProcessInstance.setProName(proName);
    		dhTaskInstance.setDhProcessInstance(dhProcessInstance);
    	}
    	if(createProcessUserName!=null && !"".equals(createProcessUserName)) {
    		SysUser sysUser = new SysUser();
    		sysUser.setUserName(createProcessUserName);
    		dhTaskInstance.setSysUser(sysUser);
    	}
		return dhTaskInstanceService.queryTransfer(startTime, endTime, dhTaskInstance, pageNum, pageSize);
	}
	
	/**
	 * 
	 * @Title: queryTransferNumber  
	 * @Description: 查询抄送已读/未读任务数量
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return Integer  
	 * @throws
	 */
	@RequestMapping(value = "/queryTransferNumber")
	@ResponseBody
	public Integer queryTransferNumber(DhTaskInstance dhTaskInstance) {
		String usrUid = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		dhTaskInstance.setUsrUid(usrUid);
		return dhTaskInstanceService.queryTransferNumber(dhTaskInstance);
	}
	
	/**
	 * 
	 * @Title: updateTaskStatusOfTransfer  
	 * @Description: 点击抄送未读详情页面，更改任务状态  
	 * @param @param taskUid
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	@RequestMapping(value = "/updateTaskStatusOfTransfer")
	@ResponseBody
	public ServerResponse<?> updateTaskStatusOfTransfer(String taskUid){
		return dhTaskInstanceService.updateTaskStatusOfTransfer(taskUid);
	}
	
	/**
	 * 
	 * @Title: transferSure  
	 * @Description: 抄送确认  
	 * @param @param taskUid
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	@Log(description = "进行抄送")
	@RequestMapping(value = "/transferSure")
	@ResponseBody
	public ServerResponse<?> transferSure(String taskUid, String usrUid, String activityId){
		return dhTaskInstanceService.transferSure(taskUid, usrUid, activityId);
	}
	
	/**
	 * 
	 * @Title: loadPageTaskByStartProcess  
	 * @Description: 门店生命周期-任务实例页面查询  
	 * @param @param dhTaskInstance
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @param insTitle
	 * @param @param insInitUser
	 * @param @param insStatusId
	 * @param @param proUid
	 * @param @param proAppId
	 * @param @return  
	 * @return ServerResponse<PageInfo<List<DhTaskInstance>>>  
	 * @throws
	 */
	@RequestMapping("/loadPageTaskByStartProcess")
	@ResponseBody
	public ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByStartProcess(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
																					@RequestParam(value="pageSize", defaultValue="10")Integer pageSize,
																					@RequestParam("insUid")String insUid) {
		String usrUid = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		return dhTaskInstanceService.loadPageTaskByClosedByStartProcess(pageNum, pageSize, insUid, usrUid);
	}

	@Log(description = "取回任务")
	@RequestMapping("/revokeTask")
	@ResponseBody
	public ServerResponse revokeTask(String taskUid) {
		try {
			return dhTaskInstanceService.revokeTask(taskUid);
		} catch (Exception e) {
			LOG.error("取回任务失败", e);
			return ServerResponse.createByErrorMessage("取回任务失败");
		}
	}
}
