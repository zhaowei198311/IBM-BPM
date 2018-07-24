package com.desmart.desmartsystem.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.SendMail;
import com.desmart.desmartbpm.dao.DhNotifyTemplateMapper;
import com.desmart.desmartbpm.entity.DhNotifyTemplate;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SendEmailService;
import com.desmart.desmartsystem.service.SendingEmailService;
@Service
public class SendEmailServiceImpl implements SendEmailService {
	private static final Logger LOG = LoggerFactory.getLogger(SendingEmailService.class);
    private BpmGlobalConfig bpmGlobalConfig;
    private CloseableHttpClient httpClient;
    private HttpClientContext context;
    
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhNotifyTemplateMapper dhNotifyTemplateMapper;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    
    /**
     * 初始化方法，使用配置中的用户名密码
     */
    public void initialization() {
        bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        httpClient = HttpClients.custom().build();
        context = HttpClientContext.create();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(bpmGlobalConfig.getBpmAdminName(), bpmGlobalConfig.getBpmAdminPsw());
        credsProvider.setCredentials(AuthScope.ANY, credentials);
        context.setCredentialsProvider(credsProvider);
        
    }
	@Override
	public String sendingEmail(String to, String subject, String body) {
		initialization();
		String result = new String();
        HttpPost httpPost = new HttpPost("http://eip.laiyifen.com/oa/Produce/pdawebservice.nsf/agtSendToCWDA?OpenWebService");
        CloseableHttpResponse response = null;
        String msg = "";
        try {
        	StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
    		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
    		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    		sb.append(" <soap:Body><AGTSENDTOCWDAORDER xmlns=\"http://eip.laiyifen.com/oa/Produce/pdawebservice.nsf/agtSendToCWDA?OpenWebService/\">");
    		sb.append(" <GONGHAO>"+to+"</GONGHAO>");
    		sb.append(" <MAILSUBJECT> "+subject+" </MAILSUBJECT> ");
    		sb.append(" <CONTENT> "+body+" </CONTENT> ");
    		sb.append(" </AGTSENDTOCWDAORDER></soap:Body></soap:Envelope>");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            httpPost.setEntity(new StringEntity(sb.toString(), "UTF-8"));
            response = httpClient.execute(httpPost, context);
            msg = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            LOG.error("POST请求发生错误！", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

	}
	/** 
     * 关闭连接释放资源
     */
    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOG.error("连接关闭出错");
            }
        }
    }
	@Override
	public ServerResponse dhSendEmail(DhTaskInstance taskInstance, String notifyTemplateUid) {
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(taskInstance.getInsUid());
		JSONObject insData = JSONObject.parseObject(dhProcessInstance.getInsData());
		JSONObject formData = insData.getJSONObject("formData");
		List<String> toList = new ArrayList<>();
		if (taskInstance.getUsrUid() != null && !"".equals(taskInstance.getUsrUid())) {
			toList.add(taskInstance.getUsrUid());
		}
		if (taskInstance.getTaskDelegateUser() != null && !"".equals(taskInstance.getTaskDelegateUser())) {
			toList.add(taskInstance.getTaskDelegateUser());
		}
		DhNotifyTemplate dhNotifyTemplate = dhNotifyTemplateMapper.getByTemplateUid(notifyTemplateUid);
		String subject = dhNotifyTemplate.getTemplateSubject();
		String body = dhNotifyTemplate.getTemplateContent();
		body = body.replaceAll("\\n|\\r\\n","<br/>");
		body = body.replaceAll(" ","&nbsp;");
		Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			String t = matcher.group(1);
			switch (t) {
			case "name":
				body = body.replaceAll("\\{name\\}", "name");
				break;
			case "proName":
				body = body.replaceAll("\\{proName\\}", dhProcessInstance.getProName());
				break;
			case "proNo":
				body = body.replaceAll("\\{proNo\\}", "proNo");
				break;
			case "approvalUrl":
				String approvalUrl = "<a href= 'http://172.19.54.163:8090/bpm/user/menus/approval?taskUid=" + taskInstance.getTaskUid()+"'>点击前往</a>";
				body = body.replaceAll("\\{approvalUrl\\}", approvalUrl);
				break;
			default:
				JSONObject filedValue = formData.getJSONObject(t);
				if (filedValue != null) {
					body = body.replaceAll("\\{" + t + "\\}", filedValue.getString("value"));
				}
				break;
			}
		}
			SendMail sendMail = SendMail.getInstaance();
			List<String> toListTest = new ArrayList<>();
	    	toList.add("helloSSM@163.com");
	    	toList.add("1254431331@qq.com");
			if(sendMail.send(subject, body, toListTest)) {
				return ServerResponse.createBySuccess();
			}else {
				return ServerResponse.createByError();
			}

	}
}
