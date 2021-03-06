package com.desmart.desmartbpm.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.github.pagehelper.PageInfo;

public interface DhProcessMetaService {
    
    /** 获得所有公开的流程 */ 
    ServerResponse getAllExposedProcess(Integer pageNum, Integer pageSize);
    
    /** 根据条件分页获得公开的流程, 其中没有绑定的流程 */
    ServerResponse getExposedProcess(Integer pageNum, Integer pageSize, String processAppName, 
            String processAppAcronym, String display); 

    /**
     * 根据分类id列出所有此分类和其子分类下的流程元数据
     * @return
     */
    ServerResponse<PageInfo<List<DhProcessMeta>>> listDhProcessMetaByCategoryList(List<DhProcessCategory> categoryList, String proName, Integer pageNum, Integer pageSize);
    
    /**
     * 创建流程元数据
     * @param dhProcessMeta
     * @return
     */
    ServerResponse createDhProcessMeta(DhProcessMeta dhProcessMeta);
    
    /**
     * 重命名流程元数据
     * @param metaUid 元数据主键
     * @param newName 新名
     * @return
     */
    ServerResponse renameDhProcessMeta(String metaUid, String newName);
    
    /**
     * 删除指定的流程元数据
     * @param uids 要删除的一条或多条流程元数据
     * @return
     */
    ServerResponse removeProcessMeta(String uids);
    
    List<DhProcessMeta> listAll();
    
}
