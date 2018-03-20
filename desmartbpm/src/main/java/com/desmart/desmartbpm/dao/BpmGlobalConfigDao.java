package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.enetity.BpmGlobalConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BpmGlobalConfigDao {

    List<BpmGlobalConfig> queryActiveConfig();

}