package com.desmart.desmartbpm.mq.rabbit;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;

/**
 * rabbitMQ消费者，需要实现MessageListener（不同业务场景的消费者和监听器绑定的过程是在配置文件中实现的）
 * @author zbw
 *
 */
public class MQConsumer2 implements ChannelAwareMessageListener{
	
private static final Logger log = LoggerFactory.getLogger(MQConsumer2.class);
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
		String str = null;
		try {
			str = new String(message.getBody(), "UTF-8");
			System.out.println("Body:" + str);
		} catch (UnsupportedEncodingException e) {
			log.info("获取message中body内容出错...");
			e.printStackTrace();
		}finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
		/*JSONObject json = JSONObject.parseObject(message.toString());
		System.out.println("=====" + json.get("accountType"));*/
	}


}
