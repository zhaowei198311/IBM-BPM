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
	 * @param intUid 接口定义id
	 * @return 根据 定义接口id  查询 返回一个list 接口参数数据
	 */
	ServerResponse<PageInfo<List<DhInterfaceParameter>>> listDhInterfaceParameter(String intUid,Integer pageNum, Integer pageSize);

	
	
	/**
	 * @param intUid 接口定义id
	 * @return 根据 定义接口id  查询 返回一个list 接口参数数据
	 */
	List<DhInterfaceParameter> querybyintUid(String intUid);

	
	/**
	 * @param paraUid 需要删除的接口参数
	 */
	void delDhInterfaceParameter(String paraUid);

	/**
	 * @param DhInterfaceParameter 接口参数实体
	 * @param
	 * @return 
	 */
	ServerResponse saveDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter);
	
	/**
	 * 修改接口参数
	 * @param dhInterfaceParameter
	 * @return
	 */
	ServerResponse updateDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter);
	
	/**
	 * 根据id查询参数
	 * @param paraUid 参数id
	 * @return
	 */
	DhInterfaceParameter selectByparaUid(String paraUid);
}
