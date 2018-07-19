package com.desmart.desmartbpm.reflect;


import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;

import org.springframework.web.context.WebApplicationContext;



/**
 * Java类型的Trigger必须实现此方法
 *
 */
public interface DhJavaClassTriggerTemplate {

    /**
     * 执行触发器，3个参数在调用时由系统传入
     * @param ac  web容器对象
     * @param insUid   调用此触发器的流程实例主键
     * @param jsonObject  触发器参数转换得到的fastjson对象 JSONObject
     * @param dhStep  表单步骤
     */
    void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject,DhStep dhStep);
    
}

