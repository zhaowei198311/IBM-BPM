package com.desmart.desmartsystem.service;

public interface SendEmailService {
	/**
     * 调用接口发送邮件
     * @param to
     * @param subject
     * @param body
     * @return
     */
    public String sendingEmail(String to,String subject,String body);
}
