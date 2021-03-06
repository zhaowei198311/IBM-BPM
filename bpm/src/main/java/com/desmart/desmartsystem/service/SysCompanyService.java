package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.entity.SysCompany;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsf
 * @since 2018-05-22
 */
public interface SysCompanyService extends BaseService<SysCompany> {
	
	/**
     * 查询所有公司
     * @param selectAll
     * @param sysCompany
     */
	public List<SysCompany> selectAll(SysCompany sysCompany);
}
