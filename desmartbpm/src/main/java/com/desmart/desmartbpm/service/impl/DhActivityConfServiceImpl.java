package com.desmart.desmartbpm.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.service.DhActivityConfService;

@Service
public class DhActivityConfServiceImpl implements DhActivityConfService {
    private static final Logger LOG = LoggerFactory.getLogger(DhActivityConfServiceImpl.class);

    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    
    public ServerResponse getActivityConfData(String actcUid) {
        if(StringUtils.isBlank(actcUid)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        DhActivityConf dhActivityConf = dhActivityConfMapper.selectByPrimaryKey(actcUid);
        if (dhActivityConf == null) {
            return ServerResponse.createByErrorMessage("此环节不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("conf", dhActivityConf);
        return ServerResponse.createBySuccess(result);
    }
    
}
