package com.desmart.desmartbpm.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmGlobalConfig;

import java.util.List;

@Repository
public interface BpmGlobalConfigDao {

    List<BpmGlobalConfig> queryActiveConfig();

}