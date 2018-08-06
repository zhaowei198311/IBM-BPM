package com.desmart.desmartbpm.reflect;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.desmartbpm.dao.DhTriggerInvokeRecordMapper;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTriggerInvokeRecord;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

/**
 * 只执行一次的触发器需要继承此抽象类 实现doOneTime方法
 */
public abstract class DhOneTimeJavaClassTrigger implements DhJavaClassTriggerTemplate{

    /**
     * 检查此流程是否触发过此触发器
     */
    public final boolean beInvoked(WebApplicationContext ac, String insUid, DhStep dhStep) {
        DhTriggerInvokeRecordMapper dhTriggerInvokeRecordMapper = ac.getBean(DhTriggerInvokeRecordMapper.class);
        DhTriggerInvokeRecord dhTriggerInvokeRecord = dhTriggerInvokeRecordMapper.queryByInsUidAndStepUid(insUid, dhStep.getStepUid());
        return dhTriggerInvokeRecord != null;
    }

    /**
     * 调用触发器成功， 保存调用记录
     * @param ac
     * @param insUid
     * @param dhStep
     */
    public final void saveInvokeRecord(WebApplicationContext ac, String insUid, DhStep dhStep) {
        DhTriggerInvokeRecordMapper dhTriggerInvokeRecordMapper = ac.getBean(DhTriggerInvokeRecordMapper.class);
        DhTriggerInvokeRecord invokeRecord = new DhTriggerInvokeRecord();
        invokeRecord.setInvokeUid(EntityIdPrefix.DH_TRIGGER_INVOKE_RECORD + UUID.randomUUID().toString());
        invokeRecord.setInsUid(insUid);
        invokeRecord.setStepUid(dhStep.getStepUid());
        dhTriggerInvokeRecordMapper.save(invokeRecord);
    }

    public void execute(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep){
        if (!beInvoked(ac, insUid, dhStep)) {
            doOneTime(ac, insUid, jsonObject, dhStep);
            saveInvokeRecord(ac, insUid, dhStep);
        }
    }

    /**
     * 需要被执行的业务逻辑
     * @param ac wab容器
     * @param insUid  流程实例主键
     * @param jsonObject  触发器参数
     * @param dhStep  步骤
     */
    public abstract void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep);

}