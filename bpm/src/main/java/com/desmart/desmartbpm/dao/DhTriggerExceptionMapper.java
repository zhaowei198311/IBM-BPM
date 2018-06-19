package com.desmart.desmartbpm.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhTriggerException;

@Repository
public interface DhTriggerExceptionMapper {

	int save(DhTriggerException dhTriggerException);

}
