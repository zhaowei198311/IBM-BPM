package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.DhProcessDefinitionDao;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DhProcessDefinitionServiceImpl implements DhProcessDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessDefinitionServiceImpl.class);

    @Autowired
    private DhProcessDefinitionDao dhProcessDefinitionDao;






}