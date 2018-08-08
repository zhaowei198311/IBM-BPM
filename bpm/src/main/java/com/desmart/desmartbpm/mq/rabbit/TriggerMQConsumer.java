package com.desmart.desmartbpm.mq.rabbit;

import com.desmart.desmartbpm.service.DhTriggerStepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.rabbitmq.client.Channel;

/**
 * rabbitMQ消费者，需要实现MessageListener（不同业务场景的消费者和监听器绑定的过程是在配置文件中实现的）
 * @author zbw
 */
public class TriggerMQConsumer implements ChannelAwareMessageListener {

	private static final Logger log = LoggerFactory.getLogger(TriggerMQConsumer.class);

	@Autowired
	private DhTriggerStepService dhTriggerStepService;


	// 监听新消息
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		try {
			dhTriggerStepService.consumeTriggerMessageFirstTime(message);
		} catch (Exception e) {
			log.error("触发器队列消费者处理消息异常", e);
		} finally {
			// 手动确认消息的消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}



}
