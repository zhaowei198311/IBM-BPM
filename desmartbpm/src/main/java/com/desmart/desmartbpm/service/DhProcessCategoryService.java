package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;

public interface DhProcessCategoryService {
    
    /** 创建分类  */
    ServerResponse save(DhProcessCategory dhProcessCategory);

}
