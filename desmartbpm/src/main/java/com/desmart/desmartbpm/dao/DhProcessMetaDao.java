package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhProcessMeta;

@Repository
public interface DhProcessMetaDao {
    
    int save(DhProcessMeta dhProcessMeta);
    
    int countByProAppIdAndProUid(@Param("proAppId")String proAppId, @Param("proUid")String proUid);
    
    DhProcessMeta queryByProMetaUid(String proMetaUid);
    
    List<DhProcessMeta> listAll();
}
