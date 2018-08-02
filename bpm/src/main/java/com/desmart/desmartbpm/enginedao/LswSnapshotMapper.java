package com.desmart.desmartbpm.enginedao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.engine.LswSnapshot;

@Repository
public interface LswSnapshotMapper {
    
    List<LswSnapshot> listAll();

    /**
     * 根据快照版本id列表查询多个快照
     * @param snapshotIdList  没有"2064."前缀的快照id
     * @return
     */
    List<LswSnapshot> listBySnapshotIdList(List<String> snapshotIdList);

    /**
     * 根据快照id批量查询，结果按照快照创建时间倒序排列
     * @param snapshotId  没有"2064."前缀的快照id
     * @return
     */
    LswSnapshot queryBySnapshotId(String snapshotId);


    
}
