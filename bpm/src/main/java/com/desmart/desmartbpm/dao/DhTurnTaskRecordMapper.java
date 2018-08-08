package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhTurnTaskRecord;

@Repository
public interface DhTurnTaskRecordMapper {
	
	/**
	 * 保存任务移交记录
	 * @param dhTurnTaskRecord
	 * @return
	 */
	public Integer save(DhTurnTaskRecord dhTurnTaskRecord);
	/**
	 * 批量保存任务移交记录
	 * @param dhTurnTaskRecords
	 * @return
	 */
	public Integer batchSave(@Param("itemList")List<DhTurnTaskRecord> dhTurnTaskRecords);
}
