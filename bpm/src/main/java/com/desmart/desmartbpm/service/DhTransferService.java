package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.entity.DhTransferData;

import java.util.List;
import java.util.Map;

/**
 * 流程迁移服务，迁移到新环境
 */
public interface DhTransferService {

	/**
	 * 导出一个流程定义相关的数据
	 * @param proAppId
	 * @param proUid
	 * @param proVerUid
	 * @return
	 */
	ServerResponse<DhTransferData> exportData(String proAppId, String proUid, String proVerUid);
}
