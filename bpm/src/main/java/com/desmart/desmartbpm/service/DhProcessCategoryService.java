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
    
    /**
     * 
     * @Title: changeThePosition  
     * @Description: 改变源数据所在分类的位置  
     * @param: @param id
     * @param: @return  
     * @return: ServerResponse<?>
     * @throws
     */
    ServerResponse<?> changeTheCategoryOfProcessMeta(String metaUid, String categoryUid);
    
    /**
     * 
     * @Title: changeStatus  
     * @Description: 更改流程状态  
     * @param: @param metaUid
     * @param: @return  
     * @return: ServerResponse<?>
     * @throws
     */
    ServerResponse<?> changeStatus(String metaUid);
    
    /**
     * 
     * @Title: closeCategory  
     * @Description: 关闭流程  
     * @param: @param metaUid
     * @param: @return  
     * @return: ServerResponse<?>
     * @throws
     */
    ServerResponse<?> closeCategory(String metaUid);
    
    /**
     * 
     * @Title: enableCategory  
     * @Description: 启用流程  
     * @param @param metaUid
     * @param @return  
     * @return ServerResponse<?>  
     * @throws
     */
    ServerResponse<?> enableCategory(String metaUid);
    
    /**
     * @throws
     * @Title: enableCategory  
     * @Description: 启用流程  
     * @param @param metaUid
     * @param @return  
     * @return ServerResponse<?>  
     */
    ServerResponse<?> listByCategoryParent(String categoryParent);

    /**
     * 根据分类id获得一个分类集合，包含这个uid对应的分类与它所有的父分类，不包含根分类
     * @param categoryUid
     * @return
     */
    List<DhProcessCategory> getCategoryAndAllParentCategory(String categoryUid);

    /**
     * 根据分类主键列表获得多个的分类信息
     * @param categoryUidList  分类的主键列表
     * @return
     */
    List<DhProcessCategory> listByCategoryUidList(List<String> categoryUidList);

    /**
     * 批量插入分类
     * @param categoryUidList
     * @return
     */
    int insertBatch(List<DhProcessCategory> categoryUidList);

}
