package com.desmart.desmartsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.entity.SysDictionaryData;
import com.desmart.desmartsystem.service.SysDictionaryService;
import com.desmart.desmartsystem.util.PagedResult;

/**
 * 
 * @ClassName: SysDictionaryController  
 * @Description: 数据字典  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
@Controller
@RequestMapping(value = "/sysDictionary")
public class SysDictionaryController {
	
	@Autowired
	private SysDictionaryService sysDictionaryService;
	
	@RequestMapping(value = "/dictionary")
	public String dictionary(){		
		return "usermanagement/sys_dictionary";
	}
	
	/**
	 * 
	 * @Title: getSysDictionaryList  
	 * @Description: 获取数据字典信息，如name为空则查询所有  
	 * @param @param dicName
	 * @param @return
	 * @return ServerResponse
	 * @throws
	 */
	@RequestMapping(value = "/getSysDictionaryList")
	@ResponseBody
	public PagedResult<SysDictionary> getSysDictionaryList(SysDictionary sysDictionary, Integer pageNo, Integer pageSize){
		return sysDictionaryService.listSysDictionary(sysDictionary, pageNo, pageSize);
	}
	/**
	 * 
	 * @Title: getSysDictionaryListById  
	 * @Description: 根据id查找  
	 * @param @param dicUid
	 * @param @return  
	 * @return SysDictionary
	 * @throws
	 */
	@RequestMapping(value = "/getSysDictionaryById")
	@ResponseBody
	public SysDictionary getSysDictionaryById(String dicUid){
		return sysDictionaryService.getSysDictionaryById(dicUid);
	}
	/**
	 * 
	 * @Title: saveSysDictionary  
	 * @Description: 新增  
	 * @param @param sysDictionary
	 * @param @return
	 * @return ServerResponse
	 * @throws
	 */
	@RequestMapping(value = "/saveSysDictionary")
	@ResponseBody
	public String saveSysDictionary(SysDictionary sysDictionary){
		try {
			int count = sysDictionaryService.save(sysDictionary);
			if (count > 0) {
				return "{\"msg\":\"success\"}";
			}else{
				if (count == -1) {
					return "{\"msg\":\"编码不能重复！\"}";
				}
				return "{\"msg\":\"error\"}";
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}		
	}
	
	/**
	 * 
	 * @Title: deleteSysDictionary  
	 * @Description: 批量删除  
	 * @param @param dicUids
	 * @param @return
	 * @return ServerResponse
	 * @throws
	 */
	@RequestMapping(value = "/deleteSysDictionary")
	@ResponseBody
	public String deleteSysDictionary(String dicUid){
		try {
			int count = sysDictionaryService.delete(dicUid);
			if (count > 0) {
				return "{\"msg\":\"success\"}";
			}else{
				return "{\"msg\":\"error\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	/**
	 * 
	 * @Title: updateSysDictionary  
	 * @Description: 更改  
	 * @param @param sysDictionary
	 * @param @return      
	 * @return ServerResponse      
	 * @throws
	 */
	@RequestMapping(value = "/updateSysDictionary")
	@ResponseBody
	public String updateSysDictionary(SysDictionary sysDictionary){
		try {
			int count = sysDictionaryService.update(sysDictionary);
			if (count > 0) {
				return "{\"msg\":\"success\"}";
			}else{
				if (count == -1) {
					return "{\"msg\":\"编码不能重复！\"}";
				}
				return "{\"msg\":\"error\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * @Title: getSysDictionaryDataList  
	 * @Description: 根据dicUid查询详情内容  
	 * @param @param dicUid
	 * @param @param pageNo
	 * @param @param pageSize
	 * @param @return  
	 * @return PagedResult<SysDictionaryData>
	 * @throws
	 */
	@RequestMapping(value = "/getSysDictionaryDataList")
	@ResponseBody
	public PagedResult<SysDictionaryData> getSysDictionaryDataList(SysDictionaryData sysDictionaryData, Integer pageNo, Integer pageSize){
		return sysDictionaryService.listSysDictionaryDataById(sysDictionaryData, pageNo, pageSize);
	}
	
	/**
	 * 
	 * @Title: getSysDictionaryDataById  
	 * @Description: 根据dicDataUid返回单个对象  
	 * @param @param dicDataUid
	 * @param @return  
	 * @return SysDictionaryData
	 * @throws
	 */
	@RequestMapping(value = "/getSysDictionaryDataById")
	@ResponseBody
	public SysDictionaryData getSysDictionaryDataById(String dicDataUid){
		return sysDictionaryService.getSysDictionaryDataById(dicDataUid);
	}
	
	/**
	 * 
	 * @Title: saveSysDictionaryData  
	 * @Description: 新增详情  
	 * @param @param sysDictionaryData
	 * @param @return  
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/saveSysDictionaryData")
	@ResponseBody
	public String saveSysDictionaryData(SysDictionaryData sysDictionaryData){
		try {
			int count = sysDictionaryService.saveSysDictionaryData(sysDictionaryData);
			if (count > 0) {
				return "{\"msg\":\"success\"}";
			}else {
				if (count == -1) {
					return "{\"msg\":\"编码不能重复！\"}";
				}
				return "{\"msg\":\"error\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * @Title: deleteSysDictionaryData  
	 * @Description: 删除详情  
	 * @param @param dicDataUid
	 * @param @return  
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/deleteSysDictionaryData")
	@ResponseBody
	public String deleteSysDictionaryData(String dicDataUid){
		try {
			int count = sysDictionaryService.deleteSysDictionaryData(dicDataUid);
			if (count > 0) {
				return "{\"msg\":\"success\"}";
			}else {
				return "{\"msg\":\"error\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * @Title: updateSysDictionaryData  
	 * @Description: 修改详情  
	 * @param @param sysDictionaryData
	 * @param @return  
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/updateSysDictionaryData")
	@ResponseBody
	public String updateSysDictionaryData(SysDictionaryData sysDictionaryData){
		try {
			int count = sysDictionaryService.updateSysDictionaryData(sysDictionaryData);
			if (count > 0) {
				return "{\"msg\":\"success\"}";
			}else {
				if (count == -1) {
					return "{\"msg\":\"编码不能重复！\"}";
				}
				return "{\"msg\":\"error\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
