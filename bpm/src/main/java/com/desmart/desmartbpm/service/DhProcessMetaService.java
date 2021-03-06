package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.github.pagehelper.PageInfo;

public interface DhProcessMetaService {


    /**
     * 分页查询没有被纳入平台的流程元数据
     * @param pageNum
     * @param pageSize
     * @param processAppName
     * @param processAppAcronym
     * @param display
     * @return
     */
    ServerResponse getUnSynchronizedProcessMeta(Integer pageNum, Integer pageSize, String processAppName,
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
     * 更新流程元数据
     * @param metaUid 元数据主键
     * @param newName 新名
     * @param proNo
     * @return
     */
    ServerResponse updateDhProcessMeta(String metaUid, String newName, String proNo);
    
    /**
     * 删除指定的流程元数据
     * @param uid
     * @return
     */
    ServerResponse removeProcessMeta(String uid);

    /**
     * 获得所有流程元数据
     * @return
     */
    List<DhProcessMeta> listAll();

    /**
     * 根据流程名模糊查询匹配的流程元数据
     * @param proName
     * @return
     */
    List<DhProcessMeta> searchByProName(String proName);
    
    /**
     * 根据分类id 查询流程实例
     */
    List<DhProcessMeta> searchByCategory(String categoryUid);

    /**
     * 根据流程应用库id与流程图id查找元数据
     * @param proAppId
     * @param proUid
     * @return
     */
    DhProcessMeta getByProAppIdAndProUid(String proAppId, String proUid);
    /**
     * 更新流程元数据查看权限
     * @param dhProcessMeta
     * @return
     */
    public ServerResponse updateDhProcessMetaPower(DhProcessMeta dhProcessMeta);
}
