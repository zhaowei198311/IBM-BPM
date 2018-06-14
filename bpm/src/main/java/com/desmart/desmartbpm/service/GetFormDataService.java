package com.desmart.desmartbpm.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;

@Service
public interface GetFormDataService {
	
	ServerResponse<?> insertFormData(Map<String, Object> map);
}
