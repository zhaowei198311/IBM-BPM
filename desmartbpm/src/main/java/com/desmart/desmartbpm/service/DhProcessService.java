package com.desmart.desmartbpm.service;

import javax.servlet.http.HttpServletRequest;

import com.desmart.desmartbpm.common.ServerResponse;

public interface DhProcessService {
    
    ServerResponse getExposedProcess(HttpServletRequest request, Integer pageNum, Integer pageSize);

}
