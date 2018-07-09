package com.desmart.desmartbpm.mq.rabbit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.desmart.desmartbpm.mq.rabbit.MQProducer;



public class RabbitMQTest {

	
	private ApplicationContext context = null;
	
	@Before  
    public void setUp() throws Exception {  
        context = new ClassPathXmlApplicationContext("applicationContext.xml");  
    }
	
	@Test
	public void test1() {
		
		MQProducer mqProducer = (MQProducer) context.getBean("MQProducer");
		mqProducer.sendMessage("queueTestKey", "hello");
		/*MqSendUtil mqSendUtil = new MqSendUtil();
		mqSendUtil.sendMessage("helleo");*/
	}
	
}
