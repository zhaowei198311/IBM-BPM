package com.desmart.desmartsystem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.desmartsystem.entity.SysCompany;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-05-22
 */
public interface SysCompanyMapper extends BaseMapper<SysCompany> {
	int insertBatch(@Param("lists")List<SysCompany> lists);
}