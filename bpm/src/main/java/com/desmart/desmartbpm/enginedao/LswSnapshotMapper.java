package com.desmart.desmartbpm.enginedao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.engine.LswSnapshot;

@Repository
public interface LswSnapshotMapper {
    
    List<LswSnapshot> listAll();

    /**
     * 根据快照版本id列表查询多个快照
     * @param snapshotIdList  没有"25."前缀的快照id
     * @return
     */
    List<LswSnapshot> listBySnapshotIdList(List<String> snapshotIdList);
    
    LswSnapshot queryBySnapshotId(String snapshotId);


    
}
