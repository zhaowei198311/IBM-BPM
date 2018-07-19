/**
 * 
 */
package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.DhInterface;
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
	 * 批量插入
	 * @param dhInterfaceParameterList
	 * @return
	 */
	int insertBatch(List<DhInterfaceParameter> dhInterfaceParameterList);
	
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
	
	/**
	 * 根据指定参数 查询接口参数
	 * @param paraUid
	 * @return
	 */
	List<DhInterfaceParameter> byQueryParameter(DhInterfaceParameter dhInterfaceParameter);
	
	/**
	 * 删除Array的子参数
	 * @param paraUid
	 * @return
	 */
	int deleteArrayParameter(DhInterfaceParameter dhInterfaceParameter);

	/**
	 * 根据接口主键，查询多个接口的参数
	 * @param intUidList 接口主键集合
	 * @return
	 */
    List<DhInterfaceParameter> listByIntUidList(List<String> intUidList);

	/**
	 * 删除指定接口的所有参数
	 * @param intUid 接口主键
	 * @return
	 */
	int deleteByIntUid(String intUid);

}
