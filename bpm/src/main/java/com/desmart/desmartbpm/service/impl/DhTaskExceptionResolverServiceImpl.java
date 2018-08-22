package com.desmart.desmartbpm.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DhTaskExceptionMapper;
import com.desmart.desmartbpm.entity.DhTaskException;
import com.desmart.desmartbpm.service.AutoCommitSystemTaskService;
import com.desmart.desmartbpm.service.DhTaskExceptionResolverService;
import com.desmart.desmartbpm.service.DhTriggerStepService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhTaskInstanceService;

@Service
public class DhTaskExceptionResolverServiceImpl implements DhTaskExceptionResolverService {
    private static final Logger logger = LoggerFactory.getLogger(DhTaskExceptionResolverService.class);

    @Autowired
    private DhTaskExceptionMapper dhTaskExceptionMapper;
    @Autowired
    private DhTriggerStepService dhTriggerStepService;
    @Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
    @Autowired
    private DhTaskInstanceService dhTaskInstanceService;
    @Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
    @Autowired
    private AutoCommitSystemTaskService autoCommitSystemTaskService;



    public ServerResponse recoverTask(String taskUid) {
        if (StringUtils.isBlank(taskUid)) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        final DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
        if (dhTaskInstance == null) {
            return ServerResponse.createByErrorMessage("此任务不存在");
        }
        if (!dhTaskInstance.getTaskStatus().equals(DhTaskInstance.STATUS_ERROR)) {
            return ServerResponse.createByErrorMessage("此任务不是异常状态");
        }
        DhTaskException dhTaskException = findLastExceptionRecordOfTask(taskUid);
        if (dhTaskException == null) {
            return ServerResponse.createByErrorMessage("此任务没有异常记录");
        }
        if (DhTaskException.STATUS_DONE.equals(dhTaskException.getStatus())) {
            return ServerResponse.createByErrorMessage("该异常已经恢复");
        }
        dhTaskException.setDhTaskInstance(dhTaskInstance);

        ServerResponse retryResponse = null;
        switch (dhTaskException.getStatus()) {
            case DhTaskException.STATUS_STEP_EXCEPTION:
                // 重试步骤
                retryResponse = dhTriggerStepService.retryErrorStep(dhTaskException);
                break;
            case DhTaskException.STATUS_COMMIT_EXCEPTION:
                // 重试提交
                retryResponse = dhTaskInstanceService.retryCommitTask(dhTaskException);
                break;
            case DhTaskException.STATUS_PUSH_TO_MQ_EXCEPTION:
                // 重试推送
                retryResponse = dhTaskInstanceService.retryPushToMQ(dhTaskException);
                break;
            case DhTaskException.STATUS_CHECK_EXCEPTION:
                if (dhTaskInstance.getSynNumber() == -1) {
                    // 再次尝试执行

                } else if (dhTaskInstance.getSynNumber() == -2) {
                    // 再次尝试执行
                    retryResponse = autoCommitSystemTaskService.retrySubmitSystemTask(dhTaskException);
                } else if (dhTaskInstance.getSynNumber() == -3) {
                    // 再次验证时间，并执行
                    retryResponse = autoCommitSystemTaskService.retrySubmitSystemDelayTask(dhTaskException);
                }
                break;
            default:
                return ServerResponse.createByErrorMessage("异常类型非法");
        }

        if (retryResponse.isSuccess()) {
            setTaskExceptionDone(dhTaskException.getId());
            dhTaskInstanceMapper.updateTaskStatus(dhTaskException.getTaskUid(), DhTaskInstance.STATUS_CLOSED);
            tryRecoverProcessInstance(dhTaskException.getInsUid());
        } else {
            saveNewTaskException(dhTaskException, (DhTaskException) retryResponse.getData());
        }
        return retryResponse;

    }

    private DhTaskException findLastExceptionRecordOfTask(String taskUid) {
        List<DhTaskException> dhTaskExceptions = dhTaskExceptionMapper.listByTaskUid(taskUid);
        if (CollectionUtils.isEmpty(dhTaskExceptions)) {
            return null;
        }
        return dhTaskExceptions.get(dhTaskExceptions.size() - 1);

    }

    private void tryRecoverProcessInstance(String insUid) {
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (dhProcessInstance.getInsStatusId() != DhProcessInstance.STATUS_ID_FAILED) {
            return;
        }
        // 检查有没有其他异常的任务
        if (dhTaskInstanceService.listErrorTasksByInsUid(insUid).isEmpty()) {
            dhProcessInstanceMapper.updateInsStatusIdByInsUid(DhProcessInstance.STATUS_ID_ACTIVE, insUid);
        }
    }


    private void saveNewTaskException(DhTaskException oldTaskException, DhTaskException newTaskException) {
        newTaskException.setId(EntityIdPrefix.DH_TASK_EXCEPTION + String.valueOf(UUID.randomUUID()));
        if (newTaskException.isSameException(oldTaskException)) {
            newTaskException.setRetryCount(oldTaskException.getRetryCount() + 1);
        } else {
            oldTaskException.setRetryCount(0);
        }
        dhTaskExceptionMapper.save(newTaskException);
    }


    /**
     * 将指定异常记录的状态设为完成
     * @param id 异常记录的主键
     */
    private void setTaskExceptionDone(String id) {
        DhTaskException exceptionSelective = new DhTaskException();
        exceptionSelective.setId(id).setLastRetryTime(new Date()).setStatus(DhTaskException.STATUS_DONE);
        dhTaskExceptionMapper.updateByPrimaryKeySelective(exceptionSelective);
    }


}