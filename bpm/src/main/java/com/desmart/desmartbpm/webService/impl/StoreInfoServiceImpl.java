package com.desmart.desmartbpm.webService.impl;

import javax.jws.WebService;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.service.DhNewStoreInfoService;
import com.desmart.desmartbpm.webService.StoreInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService(endpointInterface = "com.desmart.desmartbpm.webService.StoreInfoService")
@Component
public class StoreInfoServiceImpl implements StoreInfoService{

	@Autowired
	private DhNewStoreInfoService dhNewStoreInfoService;
	
	@Override
	public String queryStoreInfo() {
		
		JSONObject json = new JSONObject();
		json.put("DIANNAME", "上海市九亭镇");
		json.put("URL", "www.baidu.com");
		json.put("DIZHIURL", "www.baidu.com");
		return json.toJSONString();
	}

}
