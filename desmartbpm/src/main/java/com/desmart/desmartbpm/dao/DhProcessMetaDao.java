package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;

@Repository
public interface DhProcessMetaDao {
    
    int save(DhProcessMeta dhProcessMeta);
    
    /**
     * 根据应用库id和流程图id查看是否有这样的元数据
     * @param proAppId 应用库id
     * @param proUid 流程图id
     * @return
     */
    int countByProAppIdAndProUid(@Param("proAppId")String proAppId, @Param("proUid")String proUid);
    
    /**
     * 根据应用库id和流程图id找数据源
     * @param proAppId 应用库id
     * @param proUid 流程图id
     * @return
     */
    DhProcessMeta queryByProAppIdAndProUid(@Param("proAppId")String proAppId, @Param("proUid")String proUid);
    
    DhProcessMeta queryByProMetaUid(String proMetaUid);
    
    List<DhProcessMeta> listAll();
    
    /**
     * 列出属于分类列表的所有流程元数据
     * @param categoryList
     * @return
     */
    List<DhProcessMeta> listByCategoryList(List<DhProcessCategory> categoryList);
    
    /**
     * 根据分类列表和流程名模糊匹配对应的分类
     * @param categoryList  分类列表
     * @param proName 模糊搜索的流程名
     * @return
     */
    List<DhProcessMeta> listByCategoryListAndProName(@Param("categoryList")List<DhProcessCategory> categoryList, @Param("proName")String proName);
}
