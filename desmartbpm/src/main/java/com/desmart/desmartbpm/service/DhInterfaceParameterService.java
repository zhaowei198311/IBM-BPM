/**
 * 
 */
package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhInterfaceParameter;
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
	 * @param paraUid 需要删除的接口参数
	 */
	void delDhInterfaceParameter(String paraUid);

	/**
	 * @param DhInterfaceParameter 接口参数实体
	 * @param intUid 接口定义id
	 * @return 
	 */
	int saveDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter,String intUid);
}
