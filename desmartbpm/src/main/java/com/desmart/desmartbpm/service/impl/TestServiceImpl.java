package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.enetity.Emp;
import com.desmart.desmartbpm.dao.EmpDao;
import com.desmart.desmartbpm.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TestServiceImpl implements TestService {
    private static final Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private EmpDao empMapper;

    public void method1() {
        Emp emp = empMapper.queryByPrimaryKey(7369);
        log.info("this is a log.... {}", emp);
    }

}