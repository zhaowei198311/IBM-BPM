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
     * @param categoryUid 指定uid
     */
    List<DhProcessCategory> listByCategoryParent(String categoryParent);
    
    int countByCategoryParentAndCategoryName(@Param("categoryParent")String categoryParent, @Param("categoryName")String categoryName);

    List<DhProcessCategory> listAll();
    
    int updateByCategoryUidSelective(DhProcessCategory dhProcessCategory);
    
    int removeBatchByCategoryList(List<DhProcessCategory> categoryList);
    
    
}
