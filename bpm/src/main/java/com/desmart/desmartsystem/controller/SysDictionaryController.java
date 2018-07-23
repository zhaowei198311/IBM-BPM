package com.desmart.desmartsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.entity.SysDictionaryData;
import com.desmart.desmartsystem.service.SysDictionaryService;
import com.desmart.desmartsystem.util.PagedResult;
import com.desmart.desmartsystem.util.UUIDTool;

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
		return "desmartsystem/usermanagement/dictTpList";
	}
	
	@RequestMapping(value = "/dictionary1")
	public String dictionary1(){		
		return "desmartsystem/usermanagement/sys_dictionary";
	}
	
	
	@RequestMapping(value = "/dictCdList")
	public String dictCdList(){		
		return "desmartsystem/usermanagement/dictCdList";
	}
	
	@RequestMapping(value = "/selectDictionary")
	public ModelAndView toSelectDictionary(String elementId) {
		ModelAndView mv = new ModelAndView("desmartsystem/usermanagement/selectDictionary");
		mv.addObject("elementId",elementId);
		return mv;
	}
	
	@RequestMapping(value = "/selectDicData")
	public ModelAndView toSelectDicData(String elementId,String dicUid) {
		ModelAndView mv = new ModelAndView("desmartsystem/usermanagement/selectDicData");
		mv.addObject("elementId",elementId);
		mv.addObject("dicUid",dicUid);
		return mv;
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
	 * 批量删除数据字典
	 */
	@RequestMapping(value="/deleteSysDictionaryDataList")
	@ResponseBody
	public ServerResponse<?> deleteSysDictionaryDataList(@RequestParam("dicDataUidArr")String[] dicDataUidArr){
		try{
			return sysDictionaryService.deleteSysDictionaryDataList(dicDataUidArr);
		}catch(Exception e) {
			return ServerResponse.createByError();
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
	
	/**
	 * 查询所有启用的数据字典分类(不分页)
	 */
	@RequestMapping(value = "/listAllOnSysDictionary")
	@ResponseBody
	public ServerResponse listAllOnSysDictionary(String dicName) {
		return sysDictionaryService.listAllOnSysDictitonary(dicName);
	}
	
	/**
	 * 根据分类名模糊查询所有启用的数据字典分类(分页)
	 */
	@RequestMapping(value = "/getOnSysDictionaryList")
	@ResponseBody
	public ServerResponse getOnSysDictionaryList(@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize,String dicName) {
		return sysDictionaryService.getOnSysDictionaryList(pageNum,pageSize,dicName);
	}
	
	/**
	 * 根据数据字典id获得启用的数据字典内容(不分页)
	 */
	@RequestMapping(value = "/listOnDicDataBydicUid")
	@ResponseBody
	public ServerResponse listOnDicDataBydicUid(String dicUid,String dicDataName) {
		return sysDictionaryService.listOnDicDataBydicUid(dicUid,dicDataName);
	}
	
	/**
	 * 移动端根据数据字典id获得除已选外的数据字典内容（不分页）
	 */
	@RequestMapping(value = "/listOnDicDataBydicUidMove")
	@ResponseBody
	public ServerResponse listOnDicDataBydicUidMove(String dicUid,String dicDataUid,String condition) {
		return sysDictionaryService.listOnDicDataBydicUidMove(dicUid,dicDataUid,condition);
	}
	
	/**
	 * 根据数据字典id获得启用的数据字典详细信息(分页)
	 */
	@RequestMapping(value = "/getOnSysDictionaryDataList")
	@ResponseBody
	public ServerResponse getOnSysDictionaryDataList(@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize,String dicUid) {
		return sysDictionaryService.getOnSysDictionaryDataList(pageNum,pageSize,dicUid);
	}
}
