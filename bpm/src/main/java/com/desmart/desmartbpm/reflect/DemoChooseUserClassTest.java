package com.desmart.desmartbpm.reflect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

public class DemoChooseUserClassTest implements DhChooseUserTriggerTemplate {

	@Override
	public List<String> execute(WebApplicationContext ac, String insUid, JSONObject jsonObject) {
		// TODO Auto-generated method stub
		List<String> userIds = new ArrayList<>();
		userIds.add("00056825");
		userIds.add("00014495");
		return userIds;
	}

}
