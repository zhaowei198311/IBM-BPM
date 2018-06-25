package com.desmart.desmartbpm.enginedao;

import com.desmart.desmartbpm.entity.engine.LswUsrXref;
import org.springframework.stereotype.Repository;

@Repository
public interface LswUsrXrefMapper {

    /**
     * 根据用户主键，查询引擎中用户信息
     * @param userUid
     * @return
     */
    LswUsrXref queryByUserUid(String userUid);


}