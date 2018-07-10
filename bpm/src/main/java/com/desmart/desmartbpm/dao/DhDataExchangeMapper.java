package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhDataExchange;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DhDataExchangeMapper {

    int insert(DhDataExchange dataExchange);

    String getInsUidByInsIdAndIdentity(@Param("insId") Integer insId, @Param("identity") String identify);

    int updateByInsUidByInsIdAndIdentity(DhDataExchange dataExchange);

}