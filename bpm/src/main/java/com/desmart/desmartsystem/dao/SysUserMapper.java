package com.desmart.desmartsystem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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
	int insertBatch(@Param("lists")List<SysUser> lists);
	SysUser queryByPrimaryKey(String userUid);
	public List<SysUser> login(@Param(value="username")String username, @Param(value="password")String password);
}