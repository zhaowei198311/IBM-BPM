package com.desmart.desmartbpm.enginedao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.engine.LswBpd;

@Repository
public interface LswBpdMapper {
    
    /**
     * 根据流程图ID和版本（非快照版本）查询LSW_BPD中的信息
     * @param bpdId
     * @param versionId
     * @return
     */
    LswBpd queryByBpdIdAndVersionId(@Param("bpdId")String bpdId, @Param("versionId")String versionId);
    
}
