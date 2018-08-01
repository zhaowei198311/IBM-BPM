package com.desmart.desmartbpm.enginedao;

import com.desmart.desmartbpm.entity.engine.LswProject;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 对应引擎中的应用库
 */
@Repository
public interface LswProjectMapper {

    /**
     * 根据projectId集合进行查询
     * @param projectIds 没有"2066."前缀的应用库id
     * @return
     */
    List<LswProject> listByProjectIdList(Collection<String> projectIds);


}