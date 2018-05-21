package com.desmart.desmartportal.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartportal.entity.DhRoutingRecord;

public interface DhRoutingRecordService {

	/**
	 * 根据条件查询流转信息
	 * @param dhRoutingRecord
	 * @return
	 */
	List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord);

	/**
     * 包括关联映射处理人用户姓名--userName
     * 根据流程实例id和节点id列出所有状态是"on"的记录
     * @param insId 流程实例id
     * @param activityBpdId  元素id
     * @return
     */
    List<DhTaskHandler> getListByInsIdAndActivityBpdId(@Param("insId")Integer insId, @Param("activityBpdId")String activityBpdId);

}
