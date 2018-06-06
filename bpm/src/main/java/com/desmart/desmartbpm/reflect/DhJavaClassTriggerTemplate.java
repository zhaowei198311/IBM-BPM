package com.desmart.desmartbpm.reflect;

import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;



/**
 * Java类型的Trigger必须实现此方法
 *
 */
public interface DhJavaClassTriggerTemplate {

    void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject);
    
}

