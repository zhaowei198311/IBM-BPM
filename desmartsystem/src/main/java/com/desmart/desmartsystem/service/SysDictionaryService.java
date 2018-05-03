package com.desmart.desmartsystem.service;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.entity.SysDictionary;
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
	 * @Description: 根据分类名查询数据字典，如为空则查询所有  
	 * @param @param dicName
	 * @param @return    参数  
	 * @return ServerResponse<List<SysDictionary>>    返回类型  
	 * @throws
	 */
	ServerResponse<List<SysDictionary>> listSysDictionary(String dicName);
	/**
	 * 
	 * @Title: save  
	 * @Description: TODO  
	 * @param @param sysDictionary    参数  
	 * @return void    返回类型  
	 * @throws
	 */
	ServerResponse save(SysDictionary sysDictionary);
	/**
	 * 
	 * @Title: delete  
	 * @Description: 批量删除  
	 * @param @param sysDictionary    参数  
	 * @return void    返回类型  
	 * @throws
	 */
	ServerResponse delete(String[] dicUids);
	/**
	 * 
	 * @Title: update  
	 * @Description: 更改  
	 * @param @param sysDictionary
	 * @param @return    参数  
	 * @return ServerResponse    返回类型  
	 * @throws
	 */
	ServerResponse update(SysDictionary sysDictionary);
}
