package com.desmart.desmartsystem.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.dao.SysDictionaryMapper;
import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.service.SysDictionaryService;
/**
 * 
 * @ClassName: SysDictionaryServiceImpl  
 * @Description: 数据字典业务逻辑处理  
 * @author WUZHUANG  
 * @date 2018年5月3日  
 *
 */
@Service
public class SysDictionaryServiceImpl implements SysDictionaryService{
	
	@Autowired
	private SysDictionaryMapper sysDictionaryMapper;

	@Override
	public ServerResponse<List<SysDictionary>> listSysDictionary(String dicName) {
		List<SysDictionary> list = sysDictionaryMapper.selectSysDictionaryByName(dicName);
		if (list.size() > 0) {
			return ServerResponse.createBySuccess(list);
		}else {
			return ServerResponse.createByErrorMessage("查询结果为空！");
		}		
	}

	@Override
	public ServerResponse save(SysDictionary sysDictionary) {
		sysDictionary.setDicUid("dic"+UUID.randomUUID().toString());
		int count = sysDictionaryMapper.save(sysDictionary);
		if (count > 0) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}	
	}

	@Override
	public ServerResponse delete(String[] dicUids) {
		int count = sysDictionaryMapper.deleteSysDictionarys(dicUids);
		if (count > 0) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

	@Override
	public ServerResponse update(SysDictionary sysDictionary) {
		int count = sysDictionaryMapper.update(sysDictionary);
		if (count > 0) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}
	
}
