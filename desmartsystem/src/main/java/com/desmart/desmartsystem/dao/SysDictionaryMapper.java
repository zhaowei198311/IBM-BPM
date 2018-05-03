package com.desmart.desmartsystem.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysDictionary;
/**
 * 
 * @ClassName: SysDictionaryMapper  
 * @Description: 数据字典接口类  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
@Repository
public interface SysDictionaryMapper {
	/**
	 * 
	 * @Title: selectSysDictionaryByName  
	 * @Description: 检索数据字典  
	 * @param @param name
	 * @param @return    参数  
	 * @return List<Map<String,Object>>    返回类型  
	 * @throws
	 */
	List<SysDictionary> selectSysDictionaryByName(String dicName);
//	/**
//	 * 
//	 * @Title: QuerySysDictionary  
//	 * @Description: 查询数据字典  
//	 * @param @return    参数  
//	 * @return List<Map<String,Object>>    返回类型  
//	 * @throws
//	 */
//	List<Map<String, Object>> selectSysDictionary();
	/**
	 * 
	 * @Title: save  
	 * @Description: 新增  
	 * @param @param sysDictionary
	 * @param @return    参数  
	 * @return int    返回类型  
	 * @throws
	 */
	int save(SysDictionary sysDictionary);
	/**
	 * 
	 * @Title: delete  
	 * @Description: 批量删除  
	 * @param @param sysDictionary
	 * @param @return    参数  
	 * @return int    返回类型  
	 * @throws
	 */
	int deleteSysDictionarys(String[] dicUids);
	/**
	 * 
	 * @Title: update  
	 * @Description: 更新  
	 * @param @param sysDictionary
	 * @param @return    参数  
	 * @return int    返回类型  
	 * @throws
	 */
	int update(SysDictionary sysDictionary);
}
