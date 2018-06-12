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
		mqProducerService.sendMessage("queueTestKey", sysUser);
	}

}
