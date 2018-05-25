package com.desmart.desmartsystem.service;

import com.desmart.desmartsystem.entity.SysDictTp;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-05-24
 */
public interface SysDictTpService extends BaseService<SysDictTp> {
	PagedResult<SysDictTp> queryByPage(SysDictTp entity,Integer pageNo,Integer pageSize);
}
