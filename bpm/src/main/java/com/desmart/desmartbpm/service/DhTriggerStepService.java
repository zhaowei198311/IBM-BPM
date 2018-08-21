package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTaskException;
import org.springframework.amqp.core.Message;

public interface DhTriggerStepService {

    /**
     * 第一次消费队列中的消息
     * @param mqMessage
     */
    void consumeTriggerMessageFirstTime(Message mqMessage);

    /**
     * 重试一个发生错误的步骤并提交任务
     * @param dhTaskException  记录在异常表中的数据主键
     * @return 当失败时，data为新发生的DhTaskException
     */
    ServerResponse retryErrorStep(DhTaskException dhTaskException);



}