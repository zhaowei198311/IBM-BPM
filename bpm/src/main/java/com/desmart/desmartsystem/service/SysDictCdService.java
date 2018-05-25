package com.desmart.desmartsystem.service;

import com.desmart.desmartsystem.entity.SysDictCd;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-05-24
 */
public interface SysDictCdService extends BaseService<SysDictCd> {
	PagedResult<SysDictCd> queryByPage(SysDictCd entity,Integer pageNo,Integer pageSize);
}
