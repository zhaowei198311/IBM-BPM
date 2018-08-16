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
	 * @param bpmActivityMeta
	 * @return
	 */
	List<BpmActivityMeta> queryByBpmActivityMetaSelective(BpmActivityMeta bpmActivityMeta);


    /**
     * 新增一条记录
     * @param bpmActivityMeta
     * @return
     */
    int save(BpmActivityMeta bpmActivityMeta);

    /**
     * 批量插入
     * @param bpmActivityMetaList
     * @return
     */
    int saveBatch(List<BpmActivityMeta> bpmActivityMetaList);

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
     * 根据流程应用库id， 流程图id ， 流程版本查询流程源节点中的人工节点
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    List<BpmActivityMeta> queryByConditionToSource(@Param("proAppId")String proAppId, @Param("proUid")String proUid
    		, @Param("proVerUid")String proVerUid);
    /**
     * 根据activityId查询子级节点,可加其它条件
     * @param bpmActivityMeta
     * @return
     */
    List<BpmActivityMeta> queryChildrenMetaByCondition(BpmActivityMeta bpmActivityMeta);

    /**
     * 根据环节id批量删除
     * @param activityIdList
     * @return
     */
    int removeByActivityIdList(List<String> activityIdList);
    /**
     * 根据环节
     * @param activityIdList
     * @return
     */
    public List<BpmActivityMeta> queryPrimaryKeyByBatch(List<String> activityIdList);
}
