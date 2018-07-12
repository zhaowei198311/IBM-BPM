package com.desmart.desmartbpm.mq.rabbit;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.TypeReference;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmTaskUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTriggerException;

import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartbpm.service.DhTriggerExceptionService;
import com.desmart.desmartbpm.service.DhTriggerService;
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
	private DhTriggerService dhTriggerService;
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;


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

	// 消费消息
	public void execute(JSONObject json, Message mqMessage) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		// 解析数据，转为bean对象
		JSONObject taskInstanceJson = json.getJSONObject("currTaskInstance");
		JSONObject dhStepJson = json.getJSONObject("dhStep");
		JSONObject processInstanceJson = json.getJSONObject("currProcessInstance");
		JSONObject pubBoJson = json.getJSONObject("pubBo");// CommonBusinessObject
		JSONObject routingDataJson = json.getJSONObject("routingData");
		JSONObject routingRecordJson = json.getJSONObject("routingRecord");


		// 初始化
		DhTaskInstance dhTaskInstance = null;
		DhStep dhStep = null;
		DhProcessInstance dhProcessInstance = null;
		CommonBusinessObject pubBo = null;
		BpmRoutingData routingData = null;
		DhRoutingRecord dhRoutingRecord = null;

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
				// 复杂对象特别处理
				routingData = JSONObject.parseObject(routingDataJson.toString(), new TypeReference<BpmRoutingData>(){});
			}
			if (null != routingDataJson) {
				dhRoutingRecord = (DhRoutingRecord) JSONObject.toJavaObject(routingRecordJson, DhRoutingRecord.class);
			}

		} catch (Exception e) {
			log.info("TriggerMQConsumer消费者中解析jsonObject to bean 出错...");
			e.printStackTrace();
		}
		
		//调用触发器方法
		DhStep step = dhStep;
		while (step != null) {
			if (DhStep.TYPE_TRIGGER.equals(step.getStepType()) && StringUtils.isNotBlank(step.getStepObjectUid())) {
				ServerResponse<Map<String, String>> response = dhTriggerService.invokeTrigger(wac, dhProcessInstance.getInsUid(), dhStep);
				Map<String,String> resultMap = response.getData();
				if(resultMap.get("status").equals("1")) {
					DhTriggerException dhTriggerException = new DhTriggerException();
			        dhTriggerException.setId(EntityIdPrefix.DH_TRIGGER_EXCEPTION + UUID.randomUUID());
			        dhTriggerException.setCreateTime(new Date());
			        dhTriggerException.setInsId(String.valueOf(dhProcessInstance.getInsId()));
			        dhTriggerException.setMqMessage(String.valueOf(mqMessage));
			        dhTriggerException.setStepId(dhStep.getStepUid());
			        dhTriggerException.setTaskId(String.valueOf(dhTaskInstance.getTaskId()));
			        dhTriggerException.setRequestParam(resultMap.get("param"));
			        dhTriggerException.setErrorMessage(resultMap.get("msg"));
			        dhTriggerException.setStatus("true");
			        int result = dhTriggerExceptionService.save(dhTriggerException);
			        return;
				}
			}
			step = dhStepService.getNextStepOfCurrStep(step);
		}
		
        //  调用api 完成任务
		int taskId = dhTaskInstance.getTaskId();
		int insId = dhProcessInstance.getInsId();

		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmTaskUtil bpmTaskUtil = new BpmTaskUtil(bpmGlobalConfig);
		Map<String, HttpReturnStatus> resultMap = bpmTaskUtil.commitTaskWithOutUserInSession(taskId, pubBo);
		Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(resultMap);
		if (errorMap.get("errorResult") == null) {
			// 判断实际TOKEN是否移动了
			ServerResponse<JSONObject> didTokenMoveResponse = dhRouteServiceImpl.didTokenMove(insId, routingData);
			if (didTokenMoveResponse.isSuccess()) {
				JSONObject processData = didTokenMoveResponse.getData();
				if (processData != null) {
					// 实际Token移动了
					dhRoutingRecordMapper.insert(dhRoutingRecord);
					// 关闭需要结束的流程
					dhProcessInstanceService.closeProcessInstanceByRoutingData(insId, routingData, processData);
					// 创建需要创建的子流程
					dhProcessInstanceService.createSubProcessInstanceByRoutingData(dhProcessInstance, routingData, pubBo, processData);
				} else {
					// 实际Token没有移动,可能是并行的任务没有被完成， 更新流转信息，去掉to的部分
					dhRoutingRecord.setActivityTo(null);
					dhRoutingRecordMapper.insert(dhRoutingRecord);
				}
			} else {
				log.error("判断TOKEN是否移动失败，流程实例编号：" + insId + " 任务主键：" + dhTaskInstance.getInsUid());
			}
		} else {
			throw new PlatformException("调用RESTful API完成任务失败");
		}
	}

}
