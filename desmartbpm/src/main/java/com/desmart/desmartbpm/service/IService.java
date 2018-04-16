package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.entity.SysRoleUser;

public interface IService<T,ID> {
	
	public  int deleteByPrimaryKey(ID id);

	public   int insert(T entity);

	public   int insertSelective(T entity);

	public   T selectByPrimaryKey(ID id);

	public   int updateByPrimaryKeySelective(T entity);

	public   int updateByPrimaryKey(T entity);

}
