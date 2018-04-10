package com.desmart.desmartbpm.service;

import javax.servlet.http.HttpServletRequest;

import com.desmart.desmartbpm.common.ServerResponse;

public interface DhProcessMetaService {
    
    /** 获得所有公开的流程 */ 
    ServerResponse getAllExposedProcess(Integer pageNum, Integer pageSize);
    
    /** 根据条件分页获得公开的流程 */
    ServerResponse getExposedProcess(Integer pageNum, Integer pageSize, String processAppName, 
            String processAppAcronym, String display); 

}
