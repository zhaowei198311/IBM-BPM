package com.desmart.desmartbpm.mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.exception.MqAllNodesFailException;
import com.desmart.desmartbpm.exception.MqNodeFailException;
import com.desmart.desmartbpm.util.PropertiesUtil;
import com.desmart.desmartbpm.exception.MqException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 提供向队列发送消息的服务
 */
//@Service
public class MqSendUtil {
	private static final Logger LOG = LoggerFactory.getLogger(MqSendUtil.class);
	
	private Connection connection;
    private Channel channel;
    private List<Map<String, String>> nodeList;
    private int currentIndex = 0;
    private int lastIndex = 0;
    private final String EXCHANGE_NAME = "demo_task_exchange";
    private final String QUEUE_NAME = "demo_task_queue";
    private final String ROUTING_KEY = "demo_task";

    @PostConstruct
    public void init() {
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
        getHealthChannel();
    }

    @PreDestroy
    public void destroy() {
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
    
    
    /**
     * 初始化信道，创建队列和exchange
     * 当所有的节点不可用，抛出MqAllConnectionFailException
     */
    public synchronized  void getHealthChannel() {
        currentIndex = 0;

        while (currentIndex <= lastIndex) {
            try {
                getChannelByNodeParam(currentIndex);
            } catch (MqNodeFailException e) {
            	LOG.error("获取连接失败", e);
                currentIndex++;
                continue;
            }
            break;
        }

        if (currentIndex > lastIndex) {
            // 记录日志
        	LOG.error("所有节点连接失败");
            throw new MqAllNodesFailException("所有MQ节点不可用");
        } else {
            System.out.println("连接成功");
        }

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

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        } catch (Exception e) {
        	LOG.error("获取连接失败", e);
            throw new MqNodeFailException("获取连接失败：" + factory.getHost() + ":" + factory.getPort());
        }
    }


    /**
     * 发送消息，当所有节点不可用时，抛出MqException
     * @param message
     */
    public synchronized void sendMessage(String message) {

        try {
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("发送成功");
        } catch (IOException e) {
            try {
                getHealthChannel();
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            } catch (Exception e1) {
                throw new MqException("发送信息失败");
            }
        }
    }
    
    @Test
   public void rabbitTest() {
       init();
	   sendMessage("Hello");
	}
    
    public static void main(String[] args) {
    	MqSendUtil mqSendUtil = new MqSendUtil();
    	mqSendUtil.init();
    	mqSendUtil.sendMessage("Hello");
	}

}