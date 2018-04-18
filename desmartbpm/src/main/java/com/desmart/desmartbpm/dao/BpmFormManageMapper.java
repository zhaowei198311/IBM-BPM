package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

@Repository
public interface BpmFormManageMapper {

	List<BpmForm> listFormByProcessCategory(@Param("dhProcessList")List<DhProcessDefinition> dhProcessList,
			@Param("formTitle")String formTitle);

	List<BpmForm> listFormByProDefinition(@Param("formTitle")String formTitle, 
			@Param("proUid")String proUid, @Param("proVerUid")String proVerUid);

}
