package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;

public interface DhRouteService {
	
	/**
	 * 查询获取环节处理人
	 * @param processInstance 流程实例对象
	 * @param pageNum 
	 * @param pageSize
	 * @return List<DhProcessInstance> 集合
	 */
	ServerResponse<List<BpmActivityMeta>> showRouteBar(String insUid, String activityId, String departNo, String companyNum, String formData);
	
	
}
