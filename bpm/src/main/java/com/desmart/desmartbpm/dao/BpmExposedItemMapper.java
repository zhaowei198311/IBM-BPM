package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.service.BpmExposedItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
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
    List<BpmExposedItem> listUnsynItemByProAppIdAndBpdId(@Param("proAppId") String proAppId, @Param("bpdId") String bpdId);


    /**
     * 根据应用库id与版本id查询
     * @param proAppId
     * @param snapshotId
     * @return
     */
    List<BpmExposedItem> listByProAppIdAndSnapshotId(@Param("proAppId") String proAppId, @Param("snapshotId") String snapshotId);

    /**
     * 查询去重的应用库id集合，包含流程定义表中的和公开的流程中的
     * @return
     */
    List<String> listDistinctProAppId();

    /**
     * 根据应用库id，列出没有同步过的快照版本
     * @param proAppId
     * @return
     */
    List<LswSnapshot> listUnsynchronizedSnapshotByProAppId(String proAppId);

    /**
     * 列出没有同步的元数据
     * @return
     */
    List<Map<String, String>> listUnSynchronizedProcessMeta();


    /**
     * 根据proAppId， bpdId批量查询
     * @param mapList
     * @return
     */
    List<BpmExposedItem> listByProAppIdAndBpdId(List<Map<String, String>> mapList);

}