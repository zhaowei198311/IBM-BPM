package com.desmart.desmartsystem.service;

import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.util.PagedResult;

import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.entity.SysDictionaryData;
/**
 *
 * @ClassName: SysDictionaryService  
 * @Description: 数据字典业务接口  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
@Service
public interface SysDictionaryService {
	/**
	 * 
	 * @Title: ListSysDictionary  
	 * @Description: 初始化查询  
	 * @param @param dicName
	 * @param @return      
	 * @return ServerResponse<List<SysDictionary>>      
	 * @throws
	 */
	PagedResult<SysDictionary> listSysDictionary(SysDictionary sysDictionary, Integer pageNo, Integer pageSize);
	/**
	 * 
	 * @Title: listSysDictionaryById  
	 * @Description: 根据id查找  
	 * @param @param dicUid
	 * @param @return  
	 * @return SysDictionary
	 * @throws
	 */
	SysDictionary getSysDictionaryById(String dicUid);
	/**
	 * 
	 * @Title: save  
	 * @Description: 新增  
	 * @param @param sysDictionary      
	 * @return void      
	 * @throws
	 */
	int save(SysDictionary sysDictionary);
	/**
	 * 
	 * @Title: delete  
	 * @Description: 删除  
	 * @param @param sysDictionary      
	 * @return void      
	 * @throws
	 */
	int delete(String dicUid);
	/**
	 * 
	 * @Title: update  
	 * @Description: 更改  
	 * @param @param sysDictionary
	 * @param @return      
	 * @return ServerResponse      
	 * @throws
	 */
	int update(SysDictionary sysDictionary);
	/**
	 * 
	 * @Title: listSysDictionaryData  
	 * @Description: 根据dicUid查询  
	 * @param @param dicName
	 * @param @param pageNo
	 * @param @param pageSize
	 * @param @return  
	 * @return PagedResult<SysDictionary>
	 * @throws
	 */
	PagedResult<SysDictionaryData> listSysDictionaryDataById(SysDictionaryData sysDictionaryData, Integer pageNo, Integer pageSize);
	/**
	 * 
	 * @Title: getSysDictionaryDataById  
	 * @Description: 根据dicDataUid返回单个对象  
	 * @param @param dicDataUid
	 * @param @return  
	 * @return SysDictionaryData
	 * @throws
	 */
	SysDictionaryData getSysDictionaryDataById(String dicDataUid);
	/**
	 * 
	 * @Title: saveSysDictionaryData  
	 * @Description: 新增详情  
	 * @param @param sysDictionaryData
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
