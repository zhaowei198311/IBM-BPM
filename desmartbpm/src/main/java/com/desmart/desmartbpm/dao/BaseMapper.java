package com.desmart.desmartbpm.dao;

import java.util.List;


public interface BaseMapper<T> {
	//添加单个对象
	public int insert(T entity);
	
	//修改单个对象
	public int update(T entity);
	
	//删除单个对象
	public int delete(T entity);
	
	//查询单个对象
	public T select(T entity);
	
	public List<T> selectAll(T entity);
	
	//根据ID查询
	public T findById(T id);
}
