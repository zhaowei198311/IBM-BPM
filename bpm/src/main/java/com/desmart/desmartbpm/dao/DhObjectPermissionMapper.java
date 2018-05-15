package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhObjectPermission;

@Repository
public interface DhObjectPermissionMapper {
    int save(DhObjectPermission dhObjectPermission);
    
    int saveBatch(List<DhObjectPermission> list);
    
    /**
     * 根据传入的条件删除记录
     * @param selective
     * @return
     */
    int delectByDhObjectPermissionSelective(DhObjectPermission selective);
    
    /**
     * 查询符合条件的权限记录
     * @param selective
     * @return
     */
    List<DhObjectPermission> listByDhObjectPermissionSelective(DhObjectPermission selective);
}
