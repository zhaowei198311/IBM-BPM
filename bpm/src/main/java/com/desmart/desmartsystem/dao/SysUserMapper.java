package com.desmart.desmartsystem.dao;

import java.util.Collection;
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
	
	List<SysUser> listByPrimaryKeyList(Collection<String> uidList);

	int insertBatch(@Param("lists")List<SysUser> lists);

	SysUser queryByPrimaryKey(String userUid);

	List<SysUser> login(@Param(value="username")String username, @Param(value="password")String password);

	List<SysUser> allSysUserMove(@Param("userUidArr")String[] userUidArr, @Param("condition")String condition);

}