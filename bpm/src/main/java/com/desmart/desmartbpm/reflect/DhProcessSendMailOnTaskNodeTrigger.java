package com.desmart.desmartbpm.reflect;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartsystem.service.SendEmailService;

/**
 * 任务发送邮件给指定用户
 * @author lys
 *
 */
public class DhProcessSendMailOnTaskNodeTrigger extends DhOneTimeJavaClassTrigger{

	@Override
	public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		SendEmailService sendEmailService = ac.getBean(SendEmailService.class);
		ServerResponse serverResponse = sendEmailService.analyseBpmActivityMetaConfToSendMail(insUid, dhStep);
		if (!serverResponse.isSuccess()) {
			throw new PlatformException(serverResponse.getMsg());
		}
	}

}
