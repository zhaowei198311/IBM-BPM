package com.desmart.desmartsystem.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhNotifyTemplateMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhNotifyTemplate;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhActivityConfService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysEmailUtilBean;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysUser;
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
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private DhProcessInstanceService dhProcessInstanceService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    @Autowired
    private DhActivityConfService dhActivityConfService;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
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
	public ServerResponse dhSendEmail(SysEmailUtilBean sysEmailUtilBean) {
		if(sysEmailUtilBean==null) {
			return ServerResponse.createByErrorMessage("发送邮件失败，缺少必要参数");
		}
		if((sysEmailUtilBean.getToUserNoList()==null&&sysEmailUtilBean.getToEmailList()==null)
				||(sysEmailUtilBean.getToUserNoList()==null&&sysEmailUtilBean.getToEmailList().size()==0)
				||(sysEmailUtilBean.getToUserNoList().size()==0&&sysEmailUtilBean.getToEmailList()==null)
				||(sysEmailUtilBean.getToUserNoList().size()==0&&sysEmailUtilBean.getToEmailList().size()==0)) {
			return ServerResponse.createByErrorMessage("发送邮件失败，收件人不能为空");
		}
		List<String> toList = new ArrayList<>();
		if(sysEmailUtilBean.getToEmailList()!=null&&sysEmailUtilBean.getToEmailList().size()>0) {
			toList.addAll(sysEmailUtilBean.getToEmailList());
		}
		//根据工号查询邮箱地址
		if(sysEmailUtilBean.getToUserNoList()!=null&&sysEmailUtilBean.getToUserNoList().size()>0) {
			List<SysUser> list = sysUserMapper.listByPrimaryKeyList(sysEmailUtilBean.getToUserNoList());
			if(list!=null&&list.size()>0) {
				for (SysUser sysUser : list) {
					toList.add(sysUser.getEmail());
				}
			}
		}
		
		
		DhNotifyTemplate dhNotifyTemplate = dhNotifyTemplateMapper.getByTemplateUid(sysEmailUtilBean.getNotifyTemplateUid());
		String subject = dhNotifyTemplate.getTemplateSubject();
		String body = dhNotifyTemplate.getTemplateContent();
		body = body.replaceAll("\\n|\\r\\n","<br/>");
		body = body.replaceAll(" ","&nbsp;");
		/*subject = subject.replaceAll("\\n|\\r\\n","<br/>");
		subject = subject.replaceAll(" ","&nbsp;");*/
		
		if(sysEmailUtilBean.getDhTaskInstance()!=null) {
			DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(
								sysEmailUtilBean.getDhTaskInstance().getInsUid());
			JSONObject insData = JSONObject.parseObject(dhProcessInstance.getInsData());
			JSONObject formData = insData.getJSONObject("formData");
			
			Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}");
			Matcher matcher = pattern.matcher(body);
			while (matcher.find()) {
				String t = matcher.group(1);
				switch (t) {
				case "proName":
					body = body.replaceAll("\\{proName\\}", dhProcessInstance.getProName());
					subject = subject.replaceAll("\\{proName\\}", dhProcessInstance.getProName());
					break;
				case "proNo":
					body = body.replaceAll("\\{proNo\\}", "proNo");
					subject = subject.replaceAll("\\{proNo\\}", "proNo");
					break;
				case "hideUrl":
					if(StringUtils.isNotBlank(sysEmailUtilBean.getDhTaskInstance().getTaskUid())) {
						String hideUrl = "<a href= '"+sysEmailUtilBean.getBpmformsHost()+"bpm/user/menus/approval?taskUid=" + sysEmailUtilBean.getDhTaskInstance().getTaskUid()+"'>请您单击这里进行查看</a>";
						body = body.replaceAll("\\{hideUrl\\}", hideUrl);
					}else {
						body = body.replaceAll("\\{hideUrl\\}", "");
					}
					break;
				case "showUrl":
					if(StringUtils.isNotBlank(sysEmailUtilBean.getDhTaskInstance().getTaskUid())) {
						String showUrl = "<a href= '"+sysEmailUtilBean.getBpmformsHost()+"bpm/user/menus/approval?taskUid=" + sysEmailUtilBean.getDhTaskInstance().getTaskUid()+"'>"
								+sysEmailUtilBean.getBpmformsHost()+"/bpm/user/menus/approval?taskUid=" + sysEmailUtilBean.getDhTaskInstance().getTaskUid()+"</a>";
						body = body.replaceAll("\\{showUrl\\}", showUrl);
					}else {
						body = body.replaceAll("\\{showUrl\\}", "");
					}
					break;
				default:
					JSONObject filedValue = formData.getJSONObject(t);
					if (filedValue != null) {
						body = body.replaceAll("\\{" + t + "\\}", filedValue.getString("value"));
						subject = subject.replaceAll("\\{" + t + "\\}", filedValue.getString("value"));
					}
					break;
				}
			}
		}
			SendMail sendMail = SendMail.getInstaance();
			List<String> toListTest = new ArrayList<>();
			toListTest.add("helloSSM@163.com");
			toListTest.add("1254431331@qq.com");
			if(toListTest!=null&&toListTest.size()>0) {
				if(sendMail.send(subject, body, toListTest)) {
					return ServerResponse.createBySuccess();
				}else {
					return ServerResponse.createByErrorMessage("邮件发送失败");
				}
			}else {
				return ServerResponse.createByErrorMessage("邮件通知人为空");
			}

	}
	@Override
	public ServerResponse analyseBpmActivityMetaConfToSendMail(String insUid,DhStep dhStep) {
		DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
		BpmActivityMeta currActivityMeta = bpmActivityMetaService.getBpmActivityMeta(dhProcessInstance.getProAppId()
					, dhStep.getActivityBpdId(), dhProcessInstance.getProVerUid(), dhProcessInstance.getProUid());
		String bpmformsHost = bpmGlobalConfigService.getFirstActConfig().getBpmformsHost();
		if(currActivityMeta!=null) {
			DhActivityConf dhActivityConf = 
					dhActivityConfMapper.selectByPrimaryKey(currActivityMeta.getDhActivityConf().getActcUid());
			  //加载通知指定人信息
			ServerResponse serverResponse = dhActivityConfService.loadInteriorNotifyOfActivity(dhActivityConf);
			if(!serverResponse.isSuccess()) {
				return serverResponse;
			}
			serverResponse = dhActivityConfService.loadExteriorNotifyOfActivity(dhActivityConf);
			if(!serverResponse.isSuccess()) {
				return serverResponse;
			}
			
			//因为内部通知和外部通知模板不同，所以需要分别发送通知
			List<String> interiorUserList = new ArrayList<>();
			if(StringUtils.isNotBlank(dhActivityConf.getInteriorNotifyRole())) {
				String[] roleNos = dhActivityConf.getInteriorNotifyRole().split(";");
				for (int i = 0; i < roleNos.length; i++) {
					List<SysRoleUser> sysRoleUsers = sysRoleUserMapper.selectByRoleUid(roleNos[i]);
					for (SysRoleUser sysRoleUser : sysRoleUsers) {
						interiorUserList.add(sysRoleUser.getUserUid());
					}
				}
			}
			if (StringUtils.isNotBlank(dhActivityConf.getInteriorNotifyUser())) {
				String[] userNos = dhActivityConf.getInteriorNotifyUser().split(";");
				for (int i = 0; i < userNos.length; i++) {
					interiorUserList.add(userNos[i]);
				}
			}
			if(interiorUserList.size()>0) {
				SysEmailUtilBean sysEmailUtilBean = new SysEmailUtilBean();
				DhTaskInstance dhTaskInstance = new DhTaskInstance();
				dhTaskInstance.setInsUid(dhProcessInstance.getInsUid());//用来发送邮件时查询流程实例数据
				sysEmailUtilBean.setDhTaskInstance(dhTaskInstance);
				sysEmailUtilBean.setToUserNoList(interiorUserList);
				sysEmailUtilBean.setBpmformsHost(bpmformsHost);
				sysEmailUtilBean.setNotifyTemplateUid(dhActivityConf.getActcInteriorNotifyTemplate());
				
				this.dhSendEmail(sysEmailUtilBean);
			}
			if(dhActivityConf.getExteriorNotifyMailList()!=null&&dhActivityConf.getExteriorNotifyMailList().size()>0) {
				SysEmailUtilBean sysEmailUtilBean = new SysEmailUtilBean();
				DhTaskInstance dhTaskInstance = new DhTaskInstance();
				dhTaskInstance.setInsUid(dhProcessInstance.getInsUid());//用来发送邮件时查询流程实例数据
				sysEmailUtilBean.setDhTaskInstance(dhTaskInstance);
				sysEmailUtilBean.setToEmailList(dhActivityConf.getExteriorNotifyMailList());
				sysEmailUtilBean.setBpmformsHost(bpmformsHost);
				sysEmailUtilBean.setNotifyTemplateUid(dhActivityConf.getActcExteriorNotifyTemplate());
				
				this.dhSendEmail(sysEmailUtilBean);
			}
		}else {
			return ServerResponse.createByErrorMessage("获取环节配置信息失败");
		}
		return null;
	}
}
