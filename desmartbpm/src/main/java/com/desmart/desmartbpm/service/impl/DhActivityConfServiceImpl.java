package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.service.DhActivityConfService;

@Service
public class DhActivityConfServiceImpl implements DhActivityConfService {
    private static final Logger LOG = LoggerFactory.getLogger(DhActivityConfServiceImpl.class);

    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    
    
    public List<DhActivityConf> listHumanActivity(String proAppId, String proUid, String proVerUid) {
        //dhActivityConfMapper.
        
        return null;
    }
    
}
