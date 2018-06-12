package com.desmart.desmartbpm.mq.rabbit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;


@Service
public class MQProducer implements MqProducerService {

	private static final Logger log = LoggerFactory.getLogger(MQProducer.class);
	
	@Resource(name = "amqpTemplate")
	private AmqpTemplate amqpTemplate;

	/**
     * convertAndSend：将Java对象转换为消息发送到匹配Key的交换机中Exchange，由于配置了JSON转换，
     * 这里是将Java对象转换成JSON字符串的形式。
     * queueKey:消息队列和exchange绑定的key
     * message：发送的消息的内容
	 * @param message
	 */
	@Override
	public boolean sendMessage(String queueKey, Object message) {
		//System.out.println("rabbitmq发送数据：" + message.toString());
		if(null == message) {
			log.info("rabbitmq生产者发送消息内容为null...");
			return false;
		}
		try {
			amqpTemplate.convertAndSend("queueTestKey", "hello123");
			return true;
		} catch (AmqpException e) {
			log.info("rabbitmq生产者发送消息失败...");
			e.printStackTrace();
		}
		return false;
    }

}
