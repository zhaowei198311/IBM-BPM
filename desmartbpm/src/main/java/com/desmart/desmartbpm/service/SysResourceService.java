package com.desmart.desmartbpm.service;

import com.desmart.desmartbpm.entity.SysResource;
import com.desmart.desmartbpm.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public interface SysResourceService extends BaseService<SysResource> {
	PagedResult<SysResource> queryByPage(SysResource entity,Integer pageNo,Integer pageSize);
}
