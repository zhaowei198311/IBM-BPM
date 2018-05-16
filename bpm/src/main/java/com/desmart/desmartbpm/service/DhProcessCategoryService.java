package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;

public interface DhProcessCategoryService {
    
    /** 创建分类  */
    ServerResponse save(DhProcessCategory dhProcessCategory);
    
    List<DhProcessCategory> listAll();

    /**
     * 根据父分类查询所有的子分类(不包含作为参数的那个分类)
     * @param categoryUid
     * @return
     */
    List<DhProcessCategory> getChildrenCategory(String categoryUid);
    
    /**
     * 列出此分类和所有子分类
     * @param categoryUid
     * @return
     */
    ServerResponse<List<DhProcessCategory>> listChildrenCategoryAndThisCategory(String categoryUid);
    
    /** 删除指定分类 */
    ServerResponse removeDhProcessCategory(String categoryUid);
    
    /** 重命名分类*/
    ServerResponse renameDhProcessCategory(String categoryUid, String newName);
}
