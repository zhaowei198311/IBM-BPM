package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessMeta;

@Repository
public interface BpmFormManageMapper {

	List<BpmForm> listFormByProDefinition(@Param("formTitle")String formTitle, 
			@Param("proUid")String proUid, @Param("proVerUid")String proVerUid);

	List<BpmForm> listFormByProUidList(@Param("metaList")List<DhProcessMeta> metaList, 
			@Param("formTitle")String formTitle);

	int saveForm(BpmForm bpmForm);

	BpmForm queryFormByName(String dynTitle);
}
