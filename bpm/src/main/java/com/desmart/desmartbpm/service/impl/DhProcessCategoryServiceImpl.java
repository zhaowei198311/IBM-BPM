package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.DhProcessCategoryMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhProcessCategoryService;

@Service
public class DhProcessCategoryServiceImpl implements DhProcessCategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessCategoryServiceImpl.class);
    
    @Autowired
    private DhProcessCategoryMapper dhProcessCategoryMapper;
    @Autowired
    private DhProcessMetaMapper dhProcessMetaMapper;
    
    
    public ServerResponse save(DhProcessCategory dhProcessCategory) {
        if (StringUtils.isBlank(dhProcessCategory.getCategoryName()) || StringUtils.isBlank(dhProcessCategory.getCategoryParent())) {
            return ServerResponse.createByErrorMessage("缺少必须要的参数");
        }
        int count = dhProcessCategoryMapper.countByCategoryParentAndCategoryName(dhProcessCategory.getCategoryParent(), dhProcessCategory.getCategoryName());
        if (count > 0) {
            return ServerResponse.createByErrorMessage("父分类下已存在同名的分类");
        }
        
        dhProcessCategory.setCategoryUid(EntityIdPrefix.BPM_PROCESS_CATEGORY + UUID.randomUUID().toString());
        int resultCount = dhProcessCategoryMapper.save(dhProcessCategory);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess(dhProcessCategory);
        } else {
            return ServerResponse.createByErrorMessage("创建失败");
        }
    }
    
    public List<DhProcessCategory> listAll() {
        List<DhProcessCategory> categoryList = dhProcessCategoryMapper.listAll();
        for (DhProcessCategory dhProcessCategory : categoryList) {
            dhProcessCategory.setCategoryIcon("../resources/images/1.png");
        }
        return categoryList;
    }
    
    public ServerResponse<List<DhProcessCategory>> listChildrenCategoryAndThisCategory(String categoryUid) {
        if (StringUtils.isBlank(categoryUid)) {
            return ServerResponse.createByErrorMessage("此分类不存在");
        }
        
        DhProcessCategory dhProcessCategory = dhProcessCategoryMapper.queryByCategoryUid(categoryUid);
        if (dhProcessCategory == null && !"rootCategory".equalsIgnoreCase(categoryUid)) {
            return ServerResponse.createByErrorMessage("此分类不存在");
        }
        
        List<DhProcessCategory> list = getChildrenCategory(categoryUid);
        list.add(dhProcessCategory);
        return ServerResponse.createBySuccess(list);
    }
    
    
    public List<DhProcessCategory> getChildrenCategory(String categoryUid) {
        List<DhProcessCategory> result = new ArrayList<>();
        List<DhProcessCategory> childrens = dhProcessCategoryMapper.listByCategoryParent(categoryUid);
        
        result.addAll(childrens);
        Iterator<DhProcessCategory> it = childrens.iterator();
        while (it.hasNext()) {
            DhProcessCategory dhProcessCategory = it.next();
            List<DhProcessCategory> tmp = dhProcessCategoryMapper.listByCategoryParent(dhProcessCategory.getCategoryUid());
            if (!tmp.isEmpty()) {
                result.addAll(getChildrenCategory(dhProcessCategory.getCategoryUid()));
            }
        }
        return result;
    }
    

    @Override
    public ServerResponse renameDhProcessCategory(String categoryUid,
            String newName) {
        if (StringUtils.isBlank(categoryUid) || StringUtils.isBlank(newName) || newName.length() > 60) {
            return ServerResponse.createByErrorMessage("缺少必要的参数");
        }
        newName = newName.trim();
        DhProcessCategory dhProcessCategory = dhProcessCategoryMapper.queryByCategoryUid(categoryUid);
        if (dhProcessCategory == null) {
            return ServerResponse.createByErrorMessage("该分类不存在");
        } 
        // 与原名相同
        if (dhProcessCategory.getCategoryName().equals(newName)) {
            return ServerResponse.createBySuccess();
        }
        // 父分类下有没有同名的分类
        int count = dhProcessCategoryMapper.countByCategoryParentAndCategoryName(dhProcessCategory.getCategoryParent(), newName);
        if (count > 0) {
            return ServerResponse.createByErrorMessage("父分类下已经有此名称的分类");
        }
        dhProcessCategory.setCategoryName(newName);
        int countRow = dhProcessCategoryMapper.updateByCategoryUidSelective(dhProcessCategory);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("修改失败，找不到该分类");
        }
    }

    @Override
    public ServerResponse removeDhProcessCategory(String categoryUid) {
        DhProcessCategory dhProcessCategory = dhProcessCategoryMapper.queryByCategoryUid(categoryUid);
        if (dhProcessCategory == null) {
            return ServerResponse.createByErrorMessage("该分类不存在");
        }
        if (dhProcessCategory.getCategoryUid().equals("rootCategory")) {
            return ServerResponse.createByErrorMessage("不能删除根分类");
        }
        List<DhProcessCategory> list = getChildrenCategory(categoryUid);
        list.add(dhProcessCategory);
        
        List<DhProcessMeta> metaList = dhProcessMetaMapper.listByCategoryList(list);
        if (metaList.size() > 0) {
            return ServerResponse.createByErrorMessage("此分类或其子分类下有流程元数据，请先删除流程元数据");
        }
        int countRow = dhProcessCategoryMapper.removeBatchByCategoryList(list);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }
    
}
