/**
 * 
 */
package com.desmart.desmartsystem.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.DhInterface;

/**  
* <p>Title: DhInterfaceDao</p>  
* <p>Description: 接口管理数据访问层</p>  
* @author zhaowei  
* @date 2018年4月12日  
*/
@Repository
public interface DhInterfaceMapper {
	
	/**
	 * 添加新的接口
	 * @param dhInterface 接口实体类
	 * @return
	 */
	int save(DhInterface dhInterface);
	
	/**
	 * 删除接口
	 * @param Interfaceid 接口id
	 * @return
	 */
	int delete(String Interfaceid);
	
	/**
	 * 查询所有接口
	 * @return list返回界面
	 */
	List <DhInterface> listAll();
	
	/**
	 * 根据状态查询所有接口
	 */
	List <DhInterface> listAllByStatus(String intStatus);
	
	/**
	 * 根据条件模糊查询 
	 * @return list返回界面
	 */
	List<DhInterface> selectByCondition(Map<String, Object> params);

	/**
	 * 修改接口数据
	 * @param dhInterface
	 * @return
	 */
	int update (DhInterface dhInterface);
	
	
	/**
	 * 根据id查询接口
	 * @param dhInterface
	 * @return
	 */
	DhInterface selectByintUid (String intUid);

	/**
	 * 根据id集合，查找多个接口
	 * @param intUidList
	 * @return
	 */
	List<DhInterface> listByIntUidList(List<String> intUidList);

}
