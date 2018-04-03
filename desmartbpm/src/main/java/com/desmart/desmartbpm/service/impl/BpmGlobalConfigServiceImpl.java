package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.BpmGlobalConfigDao;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;

@Service
public class BpmGlobalConfigServiceImpl implements BpmGlobalConfigService {
	private static final Logger LOG = LoggerFactory.getLogger(BpmGlobalConfigServiceImpl.class);
	
    @Autowired
    private BpmGlobalConfigDao bpmGlobalConfigDao;

    public BpmGlobalConfig getFirstActConfig() {
        List<BpmGlobalConfig> configList = bpmGlobalConfigDao.queryActiveConfig();
        return configList.size() > 0 ? configList.get(0) : null;
    }
}