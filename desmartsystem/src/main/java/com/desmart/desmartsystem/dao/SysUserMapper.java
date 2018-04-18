package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysUser;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
	
	public List<SysUser> listByPrimaryKeyList(List<String> list);
	
}