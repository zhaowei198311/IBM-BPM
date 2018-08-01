package com.desmart.desmartbpm.enginedao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.engine.LswBpd;

import java.util.List;
import java.util.Map;

@Repository
public interface LswBpdMapper {
    
    /**
     * 根据流程图ID和版本（非快照版本）查询LSW_BPD中的信息
     * @param bpdId
     * @param versionId
     * @return
     */
    LswBpd queryByBpdIdAndVersionId(@Param("bpdId")String bpdId, @Param("versionId")String versionId);

    /**
     * 根据流程图ID和versionId批量查询
     * @param paramListA
     * @return
     */
    List<LswBpd> queryByBpdIdAndVersionIdList(List<Map<String, String>> paramList);

}
