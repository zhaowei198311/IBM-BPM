package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTaskException;

public interface DhTaskExceptionMapper {
    int deleteByPrimaryKey(String taskUid);

    int insert(DhTaskException record);

    int insertSelective(DhTaskException record);

    DhTaskException selectByPrimaryKey(String taskUid);

    int updateByPrimaryKeySelective(DhTaskException record);

    int updateByPrimaryKeyWithBLOBs(DhTaskException record);

    int updateByPrimaryKey(DhTaskException record);
}