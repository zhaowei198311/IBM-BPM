package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.service.DhActivityAssignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public int removeByActivityIdList(List<String> activityIdList) {
        if (CollectionUtils.isEmpty(activityIdList)) {
            return 0;
        }
        return dhActivityAssignMapper.deleteByActivityIds(activityIdList);
    }

    @Override
    public int insertBatch(List<DhActivityAssign> dhActivityAssignList) {
        if (CollectionUtils.isEmpty(dhActivityAssignList)) {
            return 0;
        }
        return dhActivityAssignMapper.insertBatch(dhActivityAssignList);
    }


}