package com.desmart.desmartbpm.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhProcessCategoryDao;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.service.DhProcessCategoryService;

@Service
public class DhProcessCategoryServiceImpl implements DhProcessCategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessCategoryServiceImpl.class);
    
    @Autowired
    private DhProcessCategoryDao dhProcessCategoryDao; 
    
    
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
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("创建失败");
        }
    }
    
    
}
