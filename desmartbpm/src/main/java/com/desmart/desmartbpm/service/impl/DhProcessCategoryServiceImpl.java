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

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhProcessCategoryDao;
import com.desmart.desmartbpm.dao.DhProcessMetaDao;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhProcessCategoryService;

@Service
public class DhProcessCategoryServiceImpl implements DhProcessCategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessCategoryServiceImpl.class);
    
    @Autowired
    private DhProcessCategoryDao dhProcessCategoryDao; 
    @Autowired
    private DhProcessMetaDao dhProcessMetaDao;
    
    
    public ServerResponse save(DhProcessCategory dhProcessCategory) {
        if (StringUtils.isBlank(dhProcessCategory.getCategoryName()) || StringUtils.isBlank(dhProcessCategory.getCategoryParent())) {
            return ServerResponse.createByErrorMessage("缺少必须要的参数");
        }
        int count = dhProcessCategoryDao.countByCategoryParentAndCategoryName(dhProcessCategory.getCategoryParent(), dhProcessCategory.getCategoryName());
        if (count > 0) {
            return ServerResponse.createByErrorMessage("父分类下已存在同名的分类");
        }
        
        dhProcessCategory.setCategoryUid(EntityIdPrefix.BPM_PROCESS_CATEGORY + UUID.randomUUID().toString());
        int resultCount = dhProcessCategoryDao.save(dhProcessCategory);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess(dhProcessCategory);
        } else {
            return ServerResponse.createByErrorMessage("创建失败");
        }
    }
    
    public List<DhProcessCategory> listAll() {
        List<DhProcessCategory> categoryList = dhProcessCategoryDao.listAll();
        return categoryList;
    }
    
    // 根据父分类查询所有的子分类
    public List<DhProcessCategory> getChildrenCategory(String categoryUid) {
        List<DhProcessCategory> result = new ArrayList<>();
        List<DhProcessCategory> childrens = dhProcessCategoryDao.listByCategoryParent(categoryUid);
        
        result.addAll(childrens);
        Iterator<DhProcessCategory> it = childrens.iterator();
        while (it.hasNext()) {
            DhProcessCategory dhProcessCategory = it.next();
            List<DhProcessCategory> tmp = dhProcessCategoryDao.listByCategoryParent(dhProcessCategory.getCategoryUid());
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
        DhProcessCategory dhProcessCategory = dhProcessCategoryDao.queryByCategoryUid(categoryUid);
        if (dhProcessCategory == null) {
            return ServerResponse.createByErrorMessage("该分类不存在");
        } 
        // 与原名相同
        if (dhProcessCategory.getCategoryName().equals(newName)) {
            return ServerResponse.createBySuccess();
        }
        // 父分类下有没有同名的分类
        int count = dhProcessCategoryDao.countByCategoryParentAndCategoryName(dhProcessCategory.getCategoryParent(), newName);
        if (count > 0) {
            return ServerResponse.createByErrorMessage("父分类下已经有此名称的分类");
        }
        dhProcessCategory.setCategoryName(newName);
        int countRow = dhProcessCategoryDao.updateByCategoryUidSelective(dhProcessCategory);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("修改失败，找不到该分类");
        }
    }

    @Override
    public ServerResponse removeDhProcessCategory(String categoryUid) {
        DhProcessCategory dhProcessCategory = dhProcessCategoryDao.queryByCategoryUid(categoryUid);
        if (dhProcessCategory == null) {
            return ServerResponse.createByErrorMessage("该分类不存在");
        }
        if (dhProcessCategory.getCategoryUid().equals("rootCategory")) {
            return ServerResponse.createByErrorMessage("不能删除根分类");
        }
        List<DhProcessCategory> list = getChildrenCategory(categoryUid);
        list.add(dhProcessCategory);
        
        List<DhProcessMeta> metaList = dhProcessMetaDao.listByCategoryList(list);
        if (metaList.size() > 0) {
            return ServerResponse.createByErrorMessage("此分类或其子分类下绑定了流程，请先解除绑定的流程");
        }
        int countRow = dhProcessCategoryDao.removeBatchByCategoryList(list);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }
    
}
