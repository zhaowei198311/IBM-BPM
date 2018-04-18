/**
 * 
 */
package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterface;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: DhInterfaceService</p>  
* <p>Description: 接口管理Service接口层</p>  
* @author zhaowei  
* @date 2018年4月12日  
*/
public interface DhInterfaceService {
	
	/**
	 * 
	 * @param intTitle 
	 * @return 返回一个list 所有的接口数据
	 */
	ServerResponse<PageInfo<List<DhInterface>>> listDhInterface(Integer pageNum, Integer pageSize);

	/**
	 * @param Interfaceid 需要删除的接口类
	 */
	void delDhInterface(String Interfaceid);

	/**
	 * @param dhInterface 接口实体
	 * @return
	 */
	int saveDhInterface(DhInterface dhInterface);

	/**
	 * 根据接口名称 进行模糊查询
	 * @param dhProcessList
	 * @param formTitle
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhInterface>>> listDhInterfaceByTitle(String intTitle,Integer pageNum, Integer pageSize);
}
