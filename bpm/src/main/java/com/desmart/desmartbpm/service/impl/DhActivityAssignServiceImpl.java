package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.service.DhActivityAssignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DhActivityAssignServiceImpl implements DhActivityAssignService {
    private static final Logger LOG = LoggerFactory.getLogger(DhActivityAssignServiceImpl.class);

    @Autowired
    private DhActivityAssignMapper dhActivityAssignMapper;

    @Override
    public List<DhActivityAssign> listByActivityIdList(List<String> activityIdList) {
        if (activityIdList == null || activityIdList.isEmpty()) {
            return new ArrayList<>();
        }
        return dhActivityAssignMapper.listByActivityIdList(activityIdList);
    }
}