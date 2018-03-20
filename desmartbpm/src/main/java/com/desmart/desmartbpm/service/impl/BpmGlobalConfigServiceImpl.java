package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.BpmGlobalConfigDao;
import com.desmart.desmartbpm.enetity.BpmGlobalConfig;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BpmGlobalConfigServiceImpl implements BpmGlobalConfigService {
    @Autowired
    private BpmGlobalConfigDao bpmGlobalConfigDao;

    public BpmGlobalConfig getFirstActConfig() {
        List<BpmGlobalConfig> configList = bpmGlobalConfigDao.queryActiveConfig();
        return configList.size() > 0 ? configList.get(0) : null;
    }
}