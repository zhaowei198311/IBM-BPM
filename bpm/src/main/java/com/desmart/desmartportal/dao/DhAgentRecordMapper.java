package com.desmart.desmartportal.dao;

import java.util.List;

import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhAgentRecord;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DhAgentRecordMapper {
    int deleteByPrimaryKey(String agentDetailId);

    int insert(DhAgentRecord record);

    int insertSelective(DhAgentRecord record);

    DhAgentRecord selectByPrimaryKey(String agentDetailId);

    int updateByPrimaryKeySelective(DhAgentRecord record);

    int updateByPrimaryKey(DhAgentRecord record);
    
    int insertBatch(List<DhAgentRecord> list);

	/**
	 * 根据代理id删除代理记录
	 * @param agentId
	 * @return
	 */
	int deleteByAgentId(String agentId);

	/**
	 * 根据代理信息查询对应的代办代理任务id集合
	 * @param dhAgentList
	 * @return
	 */
	List<String> queryAgentRecordListByAgentList(@Param("list")List<DhAgent> dhAgentList);
}