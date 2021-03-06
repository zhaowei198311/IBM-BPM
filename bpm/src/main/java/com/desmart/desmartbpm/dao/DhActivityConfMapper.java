package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhActivityConf;

@Repository
public interface DhActivityConfMapper {
    int deleteByPrimaryKey(String actcUid);

    int deleteByPrimaryKeyList(List<String> actcUidList);

    int insert(DhActivityConf dhActivityConf);

    int insertBatch(List<DhActivityConf> dhActivityConfList);

    DhActivityConf selectByPrimaryKey(String actcUid);

    int updateByPrimaryKeySelective(DhActivityConf selective);

    int updateByPrimaryKey(DhActivityConf dhActivityConf);
    
    DhActivityConf getByActivityId(String activityId);
    
    /**
     * 
     * @Title: deleteByActivityIds  
     * @Description: 根据activityIds批量删除  
     * @param @param activityIds
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteByActivityIds(List<String> activityIds);

    /**
     * 根据主键批量搜索
     * @param actcUidList
     * @return
     */
    List<DhActivityConf> listByPrimayKeyList(List<String> actcUidList);
}