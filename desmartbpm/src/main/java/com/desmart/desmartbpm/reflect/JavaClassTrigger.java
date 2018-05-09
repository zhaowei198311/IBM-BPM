package com.desmart.desmartbpm.reflect;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

/**
 * Java类型的Trigger必须实现此方法
 *
 */
public interface JavaClassTrigger {

    void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject);
    
}

