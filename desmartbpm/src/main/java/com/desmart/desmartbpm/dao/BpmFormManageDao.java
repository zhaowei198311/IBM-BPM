package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcess;

@Repository
public interface BpmFormManageDao {

	List<BpmForm> listForm(@Param("dhProcessList")List<DhProcess> dhProcessList, @Param("formTitle")String formTitle);

}
