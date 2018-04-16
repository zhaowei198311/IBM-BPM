/**
 * 
 */
package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhInterface;

/**  
* <p>Title: DhInterfaceDao</p>  
* <p>Description: 接口管理数据访问层</p>  
* @author zhaowei  
* @date 2018年4月12日  
*/
@Repository
public interface DhInterfaceDao {
	
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
	 * 根据id模糊查询 
	 * @return list返回界面
	 */
	List<DhInterface> listById(String Interfaceid);

	
}
