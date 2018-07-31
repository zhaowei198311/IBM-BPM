package com.desmart.desmartbpm.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhOperLog;

@Repository
public interface OperLogMapper {

	int save(DhOperLog operLog);//@Param(value="operLog")

}
