package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.DateUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfOuttimeNotifyType;
import com.desmart.desmartbpm.service.AutoMailNotifyService;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysEmailUtilBean;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SendEmailService;
@Service
public class AutoMailNotifyServiceImpl implements AutoMailNotifyService {
	
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private DhActivityAssignMapper dhActivityAssignMapper;
	@Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    
    @Scheduled(cron = "0 0 0/1 * * ?")
	@Override
	public ServerResponse timeoutMailNotify() {
		//查询已经超时的任务
		DhTaskInstance selective = new DhTaskInstance();
		selective.setTimeoutNotifyCount(1);//判断小于1，即还未发送过超时通知
		selective.setTaskDueDate(DateUtil.format(new Date()));//判断到期日期小于当前日期即超时
		List<DhTaskInstance> timeoutTaskList = dhTaskInstanceMapper.getTimeoutTaskList(selective);
		String bpmformsHost = bpmGlobalConfigService.getFirstActConfig().getBpmformsHost();//获取服务器ip地址
		ServerResponse serverResponse = null;
		List<String> toUserNoList = new ArrayList<>();//收件人工号集合 
		for (DhTaskInstance dhTaskInstance : timeoutTaskList) {
			DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhTaskInstance.getInsUid());
			if(dhProcessInstance!=null) {
				BpmActivityMeta currBpmActivityMeta = 
						bpmActivityMetaService.getBpmActivityMeta(dhProcessInstance.getProAppId()
								, dhTaskInstance.getActivityBpdId(), dhProcessInstance.getProVerUid(), dhProcessInstance.getProUid());
				
				String actcAssignType = currBpmActivityMeta.getDhActivityConf().getActcOuttimeNotifyType();
				DhActivityConfOuttimeNotifyType assignTypeEnum = DhActivityConfOuttimeNotifyType.codeOf(actcAssignType);
				if(assignTypeEnum!=null) {
					switch (assignTypeEnum) {
						case USERS://选择的用户集合
							DhActivityAssign selective1 = new DhActivityAssign();
							selective1.setActivityId(currBpmActivityMeta.getActivityId());
							selective1.setActaType(DhActivityAssignType.OUTTIME_NOTIFY.getCode());
							List<DhActivityAssign> list = dhActivityAssignMapper.listByDhActivityAssignSelective(selective1);
							for (DhActivityAssign dhActivityAssign : list) {
								toUserNoList.add(dhActivityAssign.getActaAssignId());
							}
							break;
						case HANDLER_USER://当前处理人，有代理人就发给代理人
							if(dhTaskInstance.getTaskDelegateUser()!=null&&!"".equals(dhTaskInstance.getTaskDelegateUser())) {
								toUserNoList.add(dhTaskInstance.getTaskDelegateUser());
							}else if(dhTaskInstance.getUsrUid()!=null&&!"".equals(dhTaskInstance.getUsrUid())){
								toUserNoList.add(dhTaskInstance.getUsrUid());
							}
							break;
						case HANDLER_USER_SUPERIOR://如果任务被代理，则发送给代理人的上级
							if(dhTaskInstance.getTaskDelegateUser()!=null&&!"".equals(dhTaskInstance.getTaskDelegateUser())) {
								SysUser delegateUser = sysUserMapper.queryByPrimaryKey(dhTaskInstance.getTaskDelegateUser());
								toUserNoList.add(delegateUser.getManagernumber());
							}else if(dhTaskInstance.getUsrUid()!=null&&!"".equals(dhTaskInstance.getUsrUid())){
								SysUser usrUser = sysUserMapper.queryByPrimaryKey(dhTaskInstance.getUsrUid());
								toUserNoList.add(usrUser.getManagernumber());
							}
							break;
					}
				
					SysEmailUtilBean sysEmailUtilBean = null;
			        	if(currBpmActivityMeta.getDhActivityConf().getActcMailNotifyTemplate()!=null
			        			&&!"".equals(currBpmActivityMeta.getDhActivityConf().getActcMailNotifyTemplate())&&toUserNoList.size()>0) {
			        		//设置邮箱模板以及邮件收件人工号集合
			        		sysEmailUtilBean = new SysEmailUtilBean();
			        		sysEmailUtilBean.setNotifyTemplateUid(currBpmActivityMeta.getDhActivityConf().getActcMailNotifyTemplate());
			        		sysEmailUtilBean.setToUserNoList(toUserNoList);
			        	}
					//发送邮件通知
		        	if(sysEmailUtilBean!=null) {
		        		sysEmailUtilBean.setDhTaskInstance(dhTaskInstance);
		        		sysEmailUtilBean.setBpmformsHost(bpmformsHost);
		        		serverResponse = sendEmailService.dhSendEmail(sysEmailUtilBean);
		        		if(serverResponse.isSuccess()) {
		        			dhTaskInstance.setTimeoutNotifyCount(1);//邮件发送成功，修改超时通知计数
		        		}
		        	}
				}
			}
		}
		if(timeoutTaskList!=null&&timeoutTaskList.size()>0) {
			dhTaskInstanceMapper.updateTaskByBatch(timeoutTaskList);//批量修改
		}
		System.out.println("超时通知完成");
		return serverResponse;
	}

}

