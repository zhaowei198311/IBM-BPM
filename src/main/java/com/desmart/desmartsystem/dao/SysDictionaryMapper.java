package com.desmart.desmartsystem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.entity.SysDictionaryData;
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
	List<SysDictionary> selectSysDictionaryByName(SysDictionary sysDictionary);
	/**
	 * 
	 * @Title: selectSysDictionaryById  
	 * @Description: 根据id查询  
	 * @param @return    参数  
	 * @return List<Map<String,Object>>    返回类型  
	 * @throws
	 */
	SysDictionary selectSysDictionaryById(@Param("dicUid")String dicUid);
	/**
	 * 
	 * @Title: selectSysDictionaryByCode  
	 * @Description: 查询dicCode是否唯一  
	 * @param @param sysDictionary
	 * @param @return  
	 * @return SysDictionary
	 * @throws
	 */
	List<SysDictionary> selectSysDictionaryByCode(SysDictionary sysDictionary);
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
	int delete(String dicUid);
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
	/**
	 * 
	 * @Title: selectSysDictionaryDataById  
	 * @Description: 根据id查找详情  
	 * @param @param dicUid
	 * @param @return  
	 * @return List<SysDictionary>
	 * @throws
	 */
	List<SysDictionaryData> selectSysDictionaryDataListById(SysDictionaryData sysDictionaryData);
	/**
	 * 
	 * @Title: selectSysDictionaryDataById  
	 * @Description: 根据id查询单个对象  
	 * @param @param dicDataUid
	 * @param @return  
	 * @return SysDictionaryData
	 * @throws
	 */
	SysDictionaryData selectSysDictionaryDataById(@Param("dicDataUid")String dicDataUid);
	/**
	 * 
	 * @Title: selectSysDictionaryDataByCode  
	 * @Description: 查询dicDataCode是否唯一  
	 * @param @param sysDictionaryData
	 * @param @return  
	 * @return SysDictionaryData
	 * @throws
	 */
	List<SysDictionaryData> selectSysDictionaryDataByCode(SysDictionaryData sysDictionaryData);
	/**
	 * 
	 * @Title: saveSysDictionaryData  
	 * @Description: 新增详情  
	 * @param @param sysDictionary
	 * @param @return  
	 * @return int
	 * @throws
	 */
	int saveSysDictionaryData(SysDictionaryData sysDictionaryData);
	/**
	 * 
	 * @Title: deleteSysDictionaryData  
	 * @Description: 删除详情  
	 * @param @param dicDataUid
	 * @param @return  
	 * @return int
	 * @throws
	 */
	int deleteSysDictionaryData(String dicDataUid);
	/**
	 * 
	 * @Title: updateSysDictionaryData  
	 * @Description: 修改详情  
	 * @param @param sysDictionaryData
	 * @param @return  
	 * @return int
	 * @throws
	 */
	int updateSysDictionaryData(SysDictionaryData sysDictionaryData);
}
