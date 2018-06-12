package com.desmart.desmartbpm.mq.rabbit;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * rabbitMQ消费者，需要实现MessageListener（不同业务场景的消费者和监听器绑定的过程是在配置文件中实现的）
 * @author zbw
 *
 */
public class MQConsumer implements MessageListener{
	
	private static final Logger log = LoggerFactory.getLogger(MQConsumer.class);

	@Override
	public void onMessage(Message message){
		
		System.out.println("rabbitmq接受到消息：" + message);
		String str = null;
		try {
			str = new String(message.getBody(), "UTF-8");
			System.out.println("Body:" + str);
		} catch (UnsupportedEncodingException e) {
			log.info("获取message中body内容出错...");
			e.printStackTrace();
		}
		JSONObject json = JSONObject.parseObject(message.toString());
		System.out.println("=====" + json.get("accountType"));
	}

}
