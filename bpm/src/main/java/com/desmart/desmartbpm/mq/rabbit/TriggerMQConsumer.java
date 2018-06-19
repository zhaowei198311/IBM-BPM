package com.desmart.desmartbpm.mq.rabbit;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTriggerException;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.service.DhTriggerExceptionService;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.rabbitmq.client.Channel;

/**
 * rabbitMQ消费者，需要实现MessageListener（不同业务场景的消费者和监听器绑定的过程是在配置文件中实现的）
 * @author zbw
 */
public class TriggerMQConsumer implements ChannelAwareMessageListener {

	private static final Logger log = LoggerFactory.getLogger(TriggerMQConsumer.class);

	@Autowired
	private DhStepService dhStepService;
	@Autowired
	DhTaskInstanceService dhTaskInstanceService;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	@Autowired
	private DhTriggerExceptionService dhTriggerExceptionService;
	@Autowired
	private DhRouteService dhRouteServiceImpl;
	@Autowired
	DhTriggerService dhTriggerService;
	
	private WebApplicationContext wac;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {

		String str = null;
		try {
			str = new String(message.getBody(), "UTF-8");
			JSONObject jsonObject = JSONObject.parseObject(str);
			execute(jsonObject, message);
		} catch (UnsupportedEncodingException e) {
			log.info("获取message中body内容出错...");
			e.printStackTrace();
		} finally {
			// 手动确认消息的消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}

	public void execute(JSONObject json, Message mqMessage) {

		// 解析数据，转为bean对象
		JSONObject taskInstanceJson = json.getJSONObject("currTaskInstance");
		JSONObject dhStepJson = json.getJSONObject("dhStep");
		JSONObject processInstanceJson = json.getJSONObject("currProcessInstance");
		JSONObject pubBoJson = json.getJSONObject("pubBo");// CommonBusinessObject
		JSONObject routingDataJson = json.getJSONObject("routingData");

		// 初始化
		DhTaskInstance dhTaskInstance = null;
		DhStep dhStep = null;
		DhProcessInstance dhProcessInstance = null;
		CommonBusinessObject pubBo = null;
		BpmRoutingData routingData = null;
		
		//jsonObject转javaBean对象
		try {
			if (null != taskInstanceJson) {
				dhTaskInstance = (DhTaskInstance) JSONObject.toJavaObject(taskInstanceJson, DhTaskInstance.class);
			}
			if (null != dhStepJson) {
				dhStep = (DhStep) JSONObject.toJavaObject(dhStepJson, DhStep.class);
			}
			if (null != processInstanceJson) {
				dhProcessInstance = (DhProcessInstance) JSONObject.toJavaObject(processInstanceJson,
						DhProcessInstance.class);
			}
			if (null != pubBoJson) {
				pubBo = (CommonBusinessObject) JSONObject.toJavaObject(pubBoJson, CommonBusinessObject.class);
			}
			if (null != routingDataJson) {
				routingData = (BpmRoutingData) JSONObject.toJavaObject(taskInstanceJson, BpmRoutingData.class);
			}
		} catch (Exception e) {
			log.info("TriggerMQConsumer消费者中解析jsonObject to bean 出错...");
			e.printStackTrace();
		}
		// 判断是否为触发器
		if (null != dhStep) {
			String stepObjectUid = dhStep.getStepObjectUid();
			if (null != stepObjectUid && "" != stepObjectUid) {
				String[] split = stepObjectUid.split(":");
				if (!"trigger".equals(split[0])) {
					return;
				}
			}
		}
		
		//调用触发器方法
		DhStep nextStep = dhStep;
		while (nextStep != null) {
			try {
				dhTriggerService.invokeTrigger(wac, String.valueOf(dhProcessInstance.getInsId()), dhStep.getStepObjectUid());
			} catch (Exception e) {
				DhTriggerException dhTriggerException = new DhTriggerException();
	        	dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + UUID.randomUUID());
	        	dhTriggerException.setCreateTime(new Date());
	        	dhTriggerException.setInsId(String.valueOf(dhProcessInstance.getInsId()));
	        	dhTriggerException.setMqMessage(String.valueOf(mqMessage));
	        	dhTriggerException.setStepId(dhStep.getStepUid());
	        	dhTriggerException.setTaskId(String.valueOf(dhTaskInstance.getTaskId()));
	        	dhTriggerException.setRequestParam(String.valueOf(mqMessage.getBody()));
	        	dhTriggerException.setErrorMessage(e.toString());
	        	int result = dhTriggerExceptionService.save(dhTriggerException);
	        	return;
			}
			nextStep = dhStepService.getNextStepOfCurrStep(nextStep);
		}
		
        //  调用api 完成任务
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
        Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTask(dhTaskInstance.getTaskId(), pubBo);
        Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
        if (errorMap.get("errorResult") == null) {
            ServerResponse<JSONObject> didMoveResponse = dhRouteServiceImpl.didTokenMove(dhProcessInstance.getInsId(), routingData);
            if (didMoveResponse.isSuccess()) {
                JSONObject processData = didMoveResponse.getData();
                if (processData != null) {
                    // 关闭需要结束的流程
                    dhProcessInstanceService.closeProcessInstanceByRoutingData(dhProcessInstance.getInsId(), routingData);
                    // 创建需要创建的子流程
                    dhProcessInstanceService.createSubProcessInstanceByRoutingData(dhProcessInstance, routingData, pubBo, processData);
                }
            } else {
            	log.error("判断TOKEN是否移动失败，流程实例编号：" + dhProcessInstance.getInsId() + " 任务主键：" + dhTaskInstance.getTaskId());
            }
        } else {
            throw new PlatformException("调用RESTful API完成任务失败");
        }
	}

}
