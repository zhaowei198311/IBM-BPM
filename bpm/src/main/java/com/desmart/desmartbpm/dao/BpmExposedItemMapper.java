package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.service.BpmExposedItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BpmExposedItemMapper {

    /**
     * 批量插入公开的流程
     * @param bpmExposedItems
     * @return
     */
    int insertBatch(List<BpmExposedItem> bpmExposedItems);

    /**
     * 删除所有公开的流程
     * @return
     */
    int removeAll();

    /**
     * 根据应用库id与流程图id查询符合条件的公开流程
     * @param proAppId
     * @param bpdId
     * @return
     */
    List<BpmExposedItem> listUnSynchronizedByProAppIdAndBpdId(@Param("proAppId") String proAppId, @Param("bpdId") String bpdId);

}