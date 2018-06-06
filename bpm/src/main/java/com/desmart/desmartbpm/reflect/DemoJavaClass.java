package com.desmart.desmartbpm.reflect;

import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;



public class DemoJavaClass implements DhJavaClassTriggerTemplate {

	@Override
	public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject) {
		System.out.println("dddd");
		System.out.println(jsonObject);
	}

}
