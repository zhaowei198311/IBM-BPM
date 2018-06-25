package com.desmart.desmartbpm.service.impl;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.enginedao.LswTaskMapper;
import com.desmart.desmartbpm.enginedao.LswUsrXrefMapper;
import com.desmart.desmartbpm.entity.engine.LswTask;
import com.desmart.desmartbpm.entity.engine.LswUsrXref;
import com.desmart.desmartbpm.service.LswTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LswTaskServiceImpl implements LswTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(LswTaskServiceImpl.class);

    @Autowired
    private LswTaskMapper lswTaskMapper;
    @Autowired
    private LswUsrXrefMapper lswUsrXrefMapper;


    public ServerResponse changeOwnerOfLswTask(int taskId, String userUid) {
        LswUsrXref lswUsrXref = lswUsrXrefMapper.queryByUserUid(userUid);
        if (lswUsrXref == null) {
            return ServerResponse.createByErrorMessage("引擎中没有此处理人");
        }
        LswTask lswTask = lswTaskMapper.queryTaskByTaskId(taskId);
        Long groupId = lswTask.getGroupId();
        Long userId = lswTask.getUserId();
        LswTask taskSelective = new LswTask();
        taskSelective.setTaskId(taskId);
        taskSelective.setGroupId(groupId > 0 ? -1 *groupId : groupId);
        taskSelective.setUserId(Long.valueOf(lswUsrXref.getUserId()));

        int countRow = lswTaskMapper.updateByPrimaryKeySelective(taskSelective);
        if (countRow > 0 ) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("更改处理人失败");
        }
    }

}