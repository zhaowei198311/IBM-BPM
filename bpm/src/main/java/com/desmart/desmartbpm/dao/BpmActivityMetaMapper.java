package com.desmart.desmartbpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmActivityMeta;

@Repository
public interface BpmActivityMetaMapper {
	/** 
	 * 查询出某流程版本的所有环节配置
	 * @param bpmProcessSnapshotId
	 * @return
	 */
	List<BpmActivityMeta> queryByBpmActivityMetaSelective(BpmActivityMeta bpmActivityMeta);

	/**
	 * 根据流程图上的元素id和快照版本
	 * @param activityBpdId
	 * @param snapshotUid
	 * @return
	 */
    List<BpmActivityMeta> queryByActivityBpdIdAndSnapshotUid(@Param("activityBpdId") String activityBpdId, @Param("snapshotUid") String snapshotUid);

    /**
     * 新增一条记录
     * @param bpmActivityMeta
     * @return
     */
    int save(BpmActivityMeta bpmActivityMeta);

    /** 根据activityId批量删除  */
    int batchRemoveByPrimaryKey(List<BpmActivityMeta> bpmActivityMetaList);
    
    /**
     * 根据主键选择性的更新字段
     * @param bpmActivityMeta
     * @return
     */
    int updateByPrimaryKeySelective(BpmActivityMeta bpmActivityMeta);
    /**
     * 
     * @Title: queryByPrimaryKey  
     * @Description: 根据ACTIVITY_ID查找  
     * @param @param activityId
     * @param @return  
     * @return BpmActivityMeta  
     * @throws
     */
    BpmActivityMeta queryByPrimaryKey(String activityId);
    
    /**
     * 
     * @Title: listSimilarActivityMetaById  
     * @Description: 查询新旧流程相同的环节元素  
     * @param @return  
     * @return List<BpmActivityMeta>
     * @throws
     */
    List<Map<String, Object>> listSimilarActivityMetaById(Map<String, Object> idS);
    
    /**
     * 
     * @Title: listCopyActivityMetaById  
     * @Description: 查询新流程没有的环节元素  
     * @param @return  
     * @return List<BpmActivityMeta>
     * @throws
     */
    BpmActivityMeta getActivityIdByIdAndName(Map<String, Object> param);
    
    /**
     * 
     * @Title: getActivityIdByIds  
     * @Description: 根据ids查找ACTIVITY_ID  
     * @param @param param
     * @param @return  
     * @return BpmActivityMeta  
     * @throws
     */
    BpmActivityMeta getActivityIdByIds(Map<String, Object> param);
    /**
     * 
     * @Title: 根据BPD_ID,PRO_APP_ID删除流程节点元素  
     * @Description: TODO  
     * @param @param bpmActivityMeta
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteByIds(BpmActivityMeta bpmActivityMeta);
    
    /**
     * 根据流程应用库id， 流程图id，版本id， 元素节点id查出唯一的一个节点
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @param activityBpdId
     * @return
     */
    BpmActivityMeta queryByFourElement(@Param("proAppId")String proAppId, @Param("proUid")String proUid,
            @Param("proVerUid")String proVerUid, @Param("activityBpdId")String activityBpdId);
}
