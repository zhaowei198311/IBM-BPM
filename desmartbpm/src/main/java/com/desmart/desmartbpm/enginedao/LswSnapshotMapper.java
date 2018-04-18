package com.desmart.desmartbpm.enginedao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.engine.LswSnapshot;

@Repository
public interface LswSnapshotMapper {
    
    List<LswSnapshot> listAll();
    
}
