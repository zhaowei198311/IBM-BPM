/**
 * 
 */
package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhInterface;
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
	 * @return 返回一个list 所有的接口数据
	 */
	List<DhInterface> listDhInterface();

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
	 * 根据id 进行模糊查询
	 * @param dhProcessList
	 * @param formTitle
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhInterface>>> listDhInterfaceById(String id,
			Integer pageNum, Integer pageSize);
}
