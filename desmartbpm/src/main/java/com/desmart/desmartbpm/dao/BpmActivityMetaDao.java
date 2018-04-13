package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessCategory;

@Repository
public interface BpmActivityMetaDao {
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
    

}
