/**
 * 
 */
package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: DhInterfaceParameterService</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月17日  
*/
public interface DhInterfaceParameterService {
	
	/**
	 * 根据 定义接口id  查询 返回一个list 接口参数数据
	 * @param intUid 接口定义id
	 * @return 根据 定义接口id  查询 返回一个list 接口参数数据
	 */
	ServerResponse<PageInfo<List<DhInterfaceParameter>>> listDhInterfaceParameter(String intUid,Integer pageNum, Integer pageSize);

	/**
	 * 根据 定义接口id  查询 返回一个list 接口参数数据
	 * @param intUid 接口定义id
	 * @return 根据 定义接口id  查询 返回一个list 接口参数数据
	 */
	List<DhInterfaceParameter> querybyintUid(String intUid);

	/**
	 * 根据接口主键，查询多个接口的参数
	 * @param intUidList 接口主键集合
	 * @return
	 */
	List<DhInterfaceParameter> listByIntUidList(List<String> intUidList);
	
	/**
	 * @param paraUid 需要删除的接口参数
	 */
	void delDhInterfaceParameter(String paraUid);

	/**
	 * @param DhInterfaceParameter 接口参数实体
	 * @param
	 * @return 
	 */
	ServerResponse saveDhInterfaceParametere(List<DhInterfaceParameter> dhInterfaceParameterList);
	
	/**
	 * 修改接口参数
	 * @param dhInterfaceParameter
	 * @return
	 */
	ServerResponse updateDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter);
	
	
	/**
	 * 修改或添加参数
	 * @param 
	 * @return 
	 */
	ServerResponse	saveOrUpdate(List<DhInterfaceParameter> dhInterfaceParameterList) throws Exception;
	
	/**
	 * 根据id查询参数
	 * @param paraUid 参数id
	 * @return
	 */
	DhInterfaceParameter selectByparaUid(String paraUid);
	
	
	
	/**
	 * @param 
	 * @return  根据指定参数查询集合
	 */
	List<DhInterfaceParameter> byQueryParameter(DhInterfaceParameter dhInterfaceParameter);
	
	/**
	 * @param 
	 * @return 查询所有的参数
	 */
	List<DhInterfaceParameter> interfaceParameterList(DhInterfaceParameter dhInterfaceParameter);
}
