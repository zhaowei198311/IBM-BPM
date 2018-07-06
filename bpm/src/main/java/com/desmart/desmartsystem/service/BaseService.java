package com.desmart.desmartsystem.service;


public interface BaseService<T> {
	
	/**
	 * 根据条件添加
	 * 
	 * @param T
	 */
	public int insert(T entity) throws Exception;
	
	/**
	 *  根据条件修改
	 * 
	 * @param T
	 */
	public int update(T entity) throws Exception;
	
	/**
	 * 根据条件删除
	 * 
	 * @param T
	 */
	public int delete(T entity) throws Exception;
	
	/**
	 * 根据指定条件查询
	 * 
	 * @param T
	 */
	public T select(T entity);
	
	/**
	 * 根据id查找
	 * 
	 * @param T
	 */
	public T findById(T id);
}
