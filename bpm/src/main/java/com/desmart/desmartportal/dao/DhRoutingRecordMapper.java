package com.desmart.desmartportal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhRoutingRecord;
@Repository
public interface DhRoutingRecordMapper {
	
	/**
	 * 根据条件查询流转信息
	 * @param dhRoutingRecord
	 * @return
	 */
	List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord);
	
	/**
	 * 添加流转信息
	 * @param dhRoutingRecord
	 */
	int insert(DhRoutingRecord dhRoutingRecord);
	
	/**
	 * 按时间倒序排列 toActivityId为指定节点的流转记录
	 * @param insUid
	 * @param currActivityId
	 * @return
	 */
	List<DhRoutingRecord> listPreRoutingRecord(@Param("insUid")String insUid, @Param("currActivityId")String currActivityId);


	List<DhRoutingRecord> listBySelective(DhRoutingRecord dhRoutingRecord);


}
