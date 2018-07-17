package com.desmart.desmartbpm.service;


import com.desmart.desmartbpm.entity.DhActivityAssign;

import java.util.List;

public interface DhActivityAssignService {

    /**
     * 根据环节id集合获得所有的 分配信息：（默认处理人，可选处理人，超时通知人）
     * @param activityIdList
     * @return
     */
    List<DhActivityAssign> listByActivityIdList(List<String> activityIdList);

    /**
     * 根据环节id集合，批量删除
     * @param activityIdList
     * @return
     */
    int removeByActivityIdList(List<String> activityIdList);

    /**
     * 批量插入
     * @param dhActivityAssignList
     * @return
     */
    int insertBatch(List<DhActivityAssign> dhActivityAssignList);

}