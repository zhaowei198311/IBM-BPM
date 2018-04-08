package com.desmart.desmartbpm.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.desmart.desmartbpm.exception.PlatformException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

//@Component
public class MqChannelManager {
	private static final Logger LOG = LoggerFactory.getLogger(MqChannelManager.class);
	private Connection connection;
	
	@PostConstruct
	public void init() {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    factory.setPort(5672);
	    // 设置vhost
	    factory.setVirtualHost("/myhost");
	    factory.setUsername("admin");
	    factory.setPassword("admin");
	    connection = null;
	     
		try {
		    connection = factory.newConnection();
		} catch (IOException | TimeoutException e) {
		    LOG.error("MQ启动异常", e);
		}
	}
	
	@PreDestroy
	public void destory() {
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				LOG.error("MQ关闭异常", e);
			}
		}
	}
	
	/**
	 * 获得一个信道
	 * @return
	 */
	public Channel getChannel() {
		Channel channel = null;
		try {
			channel = connection.createChannel();
		} catch (IOException e) {
			LOG.error("MQ获取连接失败，", e);
			throw new PlatformException("MQ获取连接失败");
		}
		return channel;
	} 
	
	
	
}
