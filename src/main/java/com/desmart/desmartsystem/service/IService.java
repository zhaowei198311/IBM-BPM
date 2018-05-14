package com.desmart.desmartsystem.service;

public interface IService<T,ID> {
	
	public  int deleteByPrimaryKey(ID id);

	public   int insert(T entity);

	public   int insertSelective(T entity);

	public   T selectByPrimaryKey(ID id);

	public   int updateByPrimaryKeySelective(T entity);

	public   int updateByPrimaryKey(T entity);

}
