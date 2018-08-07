package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTriggerInvokeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhTriggerInvokeRecordMapper {

    /**
     * 保存调用记录
     * @param invokeRecord
     * @return
     */
    int save(DhTriggerInvokeRecord invokeRecord);

    /**
     * 根据流程实例id与步骤id查找
     * @param insUid
     * @param stepUid
     * @return
     */
    List<DhTriggerInvokeRecord> queryByInsUidAndStepUid(@Param("insUid") String insUid, @Param("stepUid") String stepUid);


}