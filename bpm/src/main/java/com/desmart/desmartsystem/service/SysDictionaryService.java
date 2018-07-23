package com.desmart.desmartsystem.service;

import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.util.PagedResult;
import com.desmart.common.constant.ServerResponse;
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
	
	/**
	 * 查询所有启用的数据字典分类(不分页)
	 */
	ServerResponse listAllOnSysDictitonary(String dicName);
	
	/**
	 * 根据数据字典id获得启用的数据字典内容(不分页)
	 */
	ServerResponse listOnDicDataBydicUid(String dicUid,String dicDataName);
	
	/**
	 * 根据分类名模糊查询所有启用的数据字典分类(分页)
	 */
	ServerResponse getOnSysDictionaryList(Integer pageNum,Integer pageSize,String dicName);
	
	/**
	 * 根据数据字典id获得启用的数据字典详细信息(分页)
	 */
	ServerResponse getOnSysDictionaryDataList(Integer pageNum,Integer pageSize,String dicUid);
	
	/**
	 * 移动端根据数据字典id获得除已选外的数据字典内容（不分页）
	 */
	ServerResponse listOnDicDataBydicUidMove(String dicUid, String dicDataUid, String condition);
	
	/**
	 * 批量删除数据字典数据
	 */
	ServerResponse<?> deleteSysDictionaryDataList(String[] dicDataUidArr);
}
