package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DhTaskHandlerMapper;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartbpm.service.TeamFilterService;

@Service
public class TeamFilterServiceImpl implements TeamFilterService {
    private static final Logger LOG = LoggerFactory.getLogger(TeamFilterServiceImpl.class);
    @Autowired
    private DhTaskHandlerMapper dhTaskHandlerMapper;
    
    public ServerResponse<String> getHandler(Integer insId, String activityBpdId) {
        if (insId == null || insId.intValue() <= 0 || StringUtils.isBlank(activityBpdId)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        
        // 获得处理人信息
        List<DhTaskHandler> list = dhTaskHandlerMapper.listActiveRecordsByInsIdAndActivityBpdId(insId, activityBpdId);
        if (list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0 ; i < list.size(); i++) {
                builder.append(list.get(i).getUserUid());
                if (i != list.size() - 1) {
                    builder.append(";");
                }
            }
            // 设置会签变量的值，传递有几个人会签
            
            return ServerResponse.createBySuccess(builder.toString());
        } else {
            return ServerResponse.createByErrorMessage("找不到处理人");
        }
        
    }

}
