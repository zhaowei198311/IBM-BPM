package com.desmart.desmartbpm.mq.rabbit;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desmart.desmartsystem.entity.SysUser;

@Controller
@RequestMapping(value="/test")
public class RabbitMQTestController {
	
	@Resource
	private MqProducerService mqProducerService;
	
	@RequestMapping(value="/mqtest")
	public void qmtest() {
		
		SysUser sysUser = new SysUser();
		sysUser.setAccountType("test123");
		for (int i = 0; i < 10; i++) {
			mqProducerService.sendMessage("queueTestKey", i);
			try {  
                //暂停一下，好让消息消费者去取消息打印出来  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            } 
		}
		
	}

}
