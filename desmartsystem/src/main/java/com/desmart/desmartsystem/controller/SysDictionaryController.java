package com.desmart.desmartsystem.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.service.SysDictionaryService;

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
	public ServerResponse getSysDictionaryList(@RequestParam String dicName){
		return sysDictionaryService.listSysDictionary(dicName);
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
	public ServerResponse saveSysDictionary(@RequestBody SysDictionary sysDictionary){
		return sysDictionaryService.save(sysDictionary);
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
	public ServerResponse deleteSysDictionary(@RequestBody String[] dicUids){
		return sysDictionaryService.delete(dicUids);
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
	public ServerResponse updateSysDictionary(@RequestBody SysDictionary sysDictionary){
		return sysDictionaryService.update(sysDictionary);
	}
}
