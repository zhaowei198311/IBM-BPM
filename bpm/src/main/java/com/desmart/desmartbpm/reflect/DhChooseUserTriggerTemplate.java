package com.desmart.desmartbpm.reflect;

import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

public interface DhChooseUserTriggerTemplate {
    
    List<String> execute(WebApplicationContext ac, String insUid, JSONObject jsonObject);
    
}
