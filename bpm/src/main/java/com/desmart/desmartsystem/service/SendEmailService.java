package com.desmart.desmartsystem.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhTaskInstance;

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
     * @param taskInstance
     * @param notifyTemplateUid
     * @return
     */
    public ServerResponse dhSendEmail(DhTaskInstance taskInstance,String notifyTemplateUid);
}
