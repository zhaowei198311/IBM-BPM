package com.desmart.desmartsystem.service;


public interface BaseService<T> {
	public int insert(T entity) throws Exception;
	public int update(T entity) throws Exception;
	public int delete(T entity) throws Exception;
	public T select(T entity);
	public T findById(T id);
}
