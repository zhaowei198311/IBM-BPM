package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import org.springframework.amqp.core.Message;

import java.util.ArrayList;
import java.util.List;

public interface DhTriggerStepService {

    /**
     * 第一次消费队列中的消息
     * @param mqMessage
     */
    void consumeTriggerMessageFirstTime(Message mqMessage);

    /**
     * 重试一个发生错误的步骤并提交任务
     * @param triggerExceptionId  记录在异常表中的数据主键
     * @return
     */
    ServerResponse retryErrorStepAndSubmitTask(String triggerExceptionId);



}