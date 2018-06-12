package com.desmart.desmartbpm.mq.rabbit;

public interface MqProducerService {

	boolean sendMessage(String queueKey, Object object);
	
}
