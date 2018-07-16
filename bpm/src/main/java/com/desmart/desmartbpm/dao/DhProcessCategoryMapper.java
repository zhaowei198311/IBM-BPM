package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhProcessCategory;

@Repository
public interface DhProcessCategoryMapper {
    
    DhProcessCategory queryByCategoryUid(String categoryUid);
    
    int save(DhProcessCategory dhProcessCategory);
    
    /**
     * 根据父分类列出子分类
     * @param categoryParent 指定父分类id
     */
    List<DhProcessCategory> listByCategoryParent(String categoryParent);
    
    int countByCategoryParentAndCategoryName(@Param("categoryParent")String categoryParent, @Param("categoryName")String categoryName);

    List<DhProcessCategory> listAll();
    
    int updateByCategoryUidSelective(DhProcessCategory dhProcessCategory);
    
    int removeBatchByCategoryList(List<DhProcessCategory> categoryList);

    /**
     * 根据分类id集合获得多个分类
     * @param categoryUidList
     * @return
     */
    List<DhProcessCategory> listByCategoryUidList(List<String> categoryUidList);

    /**
     * 批量插入分类
     * @param categoryList
     * @return
     */
    int insertBatch(List<DhProcessCategory> categoryList);
}
