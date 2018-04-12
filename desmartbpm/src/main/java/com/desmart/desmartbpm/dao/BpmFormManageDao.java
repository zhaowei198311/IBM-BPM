package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

@Repository
public interface BpmFormManageDao {

	List<BpmForm> listForm(@Param("dhProcessList")List<DhProcessDefinition> dhProcessList, @Param("formTitle")String formTitle);

}
