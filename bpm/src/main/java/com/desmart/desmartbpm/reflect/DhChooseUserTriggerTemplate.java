package com.desmart.desmartbpm.reflect;

import java.util.List;

import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;

public interface DhChooseUserTriggerTemplate {
    
    List<String> execute(WebApplicationContext ac, String insUid, JSONObject jsonObject);
    
}
