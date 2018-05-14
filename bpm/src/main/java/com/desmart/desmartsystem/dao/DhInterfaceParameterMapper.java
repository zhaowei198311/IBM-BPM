/**
 * 
 */
package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.DhInterfaceParameter;

/**  
* <p>Title: DhInterfaceParameterDao</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月17日  
*/
@Repository
public interface DhInterfaceParameterMapper {
	
	/**
	 * 添加新的接口参数
	 * @param DhInterfaceParameter 接口实体类
	 * @return
	 */
	int save(DhInterfaceParameter dhInterfaceParameter);
	
	/**
	 * 删除接口参数
	 * @param paraUid 接口参数id
	 * @return
	 */
	int delete(String paraUid);
	
	/**
	 * 根据接口id查询当前接口参数
	 * @param intUid 接口定义id
	 * @return list返回界面
	 */
	List <DhInterfaceParameter> listAll(String intUid);
	
	/**
	 * 修改接口参数
	 * @param dhInterfaceParameter
	 * @return
	 */
	int update(DhInterfaceParameter dhInterfaceParameter);
	
	/**
	 * 根据参数id 查询接口参数
	 * @param paraUid
	 * @return
	 */
	DhInterfaceParameter selectByparaUid(String paraUid);
}
