package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmActivityMeta;

@Repository
public interface BpmActivityMetaDao {

	List<BpmActivityMeta> queryByBpmProcessSnapshotId(String bpmProcessSnapshotId);
	
	
}
