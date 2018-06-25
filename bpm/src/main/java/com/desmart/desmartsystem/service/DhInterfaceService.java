/**
 * 
 */
package com.desmart.desmartsystem.service;

import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
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
	int saveDhInterface(DhInterface dhInterface) throws Exception;

	/**
	 * 根据接口名称 进行模糊查询
	 * @param dhProcessList
	 * @param formTitle
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhInterface>>> listDhInterfaceByTitle(Map<String, Object> params,Integer pageNum, Integer pageSize);
	
	
	/**
	 * 修改接口
	 * @param dhInterface 接口实体类
	 * @return
	 */
	int updateDhInterface(DhInterface dhInterface);
	
	/**
	 * 根据id查询接口
	 * @param dhInterface
	 * @return
	 */
	DhInterface selectDhInterfaceByid (String intUid);
}
