package com.desmart.desmartbpm.mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.exception.MqNodeFailException;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import com.desmart.desmartbpm.util.PropertiesUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Service
public class MqConsumer {
	private static final Logger LOG = LoggerFactory.getLogger(MqConsumer.class);
	
    private static final String EXCHANGE_NAME = "demo_task_exchange";
    private static final String QUEUE_NAME = "demo_task_queue";
    private static final String ROUTING_KEY = "demo_task";
    
    private Connection connection;
    private Channel channel;
    private Consumer consumer;
    private List<Map<String, String>> nodeList;
    private int currentIndex = 0;
    private int lastIndex = 0;
    
    
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    
    /**
     * 初始化节点信息并创建连接
     */
    private void init() {
        // 集群中节点信息
    	final String data = PropertiesUtil.getProperty("rabbit.cluster.info");
        String[] nodeStrs = data.split(";");
        lastIndex = nodeStrs.length - 1;
        nodeList = new ArrayList<>(nodeStrs.length);

        for (String nodeStr : nodeStrs) {
            String[] params = nodeStr.split(",");
            Map<String, String> map = new HashMap<>();
            map.put("host", params[0]);
            map.put("port", params[1]);
            map.put("vhost", params[2]);
            map.put("username", params[3]);
            map.put("password", params[4]);
            nodeList.add(map);
        }
        
        getChannelByNodeParam(0);
    }
    
    /**
     * 通过指定下标的参数来获得连接
     * @param currentIndex  节点配置对应的下标
     * @return
     */
    public void getChannelByNodeParam(int currentIndex) {

        ConnectionFactory factory = new ConnectionFactory();
        Map<String, String> currentNodeParam = nodeList.get(currentIndex);
        factory.setHost(currentNodeParam.get("host"));
        factory.setPort(new Integer(currentNodeParam.get("port")));
        factory.setVirtualHost(currentNodeParam.get("vhost"));
        factory.setUsername(currentNodeParam.get("username"));
        factory.setPassword(currentNodeParam.get("password"));

        System.out.println("正在获取连接：" + factory.getHost() + ":" + factory.getPort());

        connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        } catch (Exception e) {
            LOG.error("连接MQ节点异常", e);
            throw new MqNodeFailException("获取连接失败：" + factory.getHost() + ":" + factory.getPort());
        }
    }
    
    
    
    @PostConstruct
    public void doWork() {
    	init();
    	
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                	bpmGlobalConfigService.getFirstActConfig();
                	System.out.println("=======  处理了消息：" + message + "======");
                } catch (Exception e) {
                	LOG.error("消费者处理发生错误", e);
                } finally {
                	channel.basicAck(envelope.getDeliveryTag(), false);
                }
                
            }
        };

        try {
			channel.basicConsume(QUEUE_NAME, false, consumer);
		} catch (IOException e) {
			LOG.error("消费者发生异常：", e);
		}
    }
    
    @PreDestroy
    public void destory() {
    	try {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			LOG.error("MQ连接关闭异常", e);
		} 
    }
    
}
