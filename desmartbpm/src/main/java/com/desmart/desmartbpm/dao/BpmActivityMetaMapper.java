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
}
