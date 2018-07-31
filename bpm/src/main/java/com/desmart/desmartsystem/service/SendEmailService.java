package com.desmart.desmartsystem.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.entity.SysEmailUtilBean;

public interface SendEmailService {
	/**
     * 调用接口发送邮件
     * @param to
     * @param subject
     * @param body
     * @return
     */
    public String sendingEmail(String to,String subject,String body);
    
    /**
     * 调用工具类发送邮件
     * @param sysEmailUtilBean
     * @return
     */
    public ServerResponse dhSendEmail(SysEmailUtilBean sysEmailUtilBean);
    
    /**
     * 分析环节配置用于发送邮件
     * @param insUid
     * @param dhStep
     * @return
     */
    public ServerResponse analyseBpmActivityMetaConfToSendMail(String insUid,DhStep dhStep);
}
