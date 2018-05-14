package com.desmart.desmartsystem.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.dao.SysDictionaryMapper;
import com.desmart.desmartsystem.entity.SysDictionary;
import com.desmart.desmartsystem.entity.SysDictionaryData;
import com.desmart.desmartsystem.service.SysDictionaryService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;
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
	public PagedResult<SysDictionary> listSysDictionary(SysDictionary sysDictionary, Integer pageNo, Integer pageSize) {
		try {
			pageNo = pageNo == null?1:pageNo;
			pageSize = pageSize == null?10:pageSize;
			PageHelper.startPage(pageNo,pageSize);
			return BeanUtil.toPagedResult(sysDictionaryMapper.selectSysDictionaryByName(sysDictionary));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int save(SysDictionary sysDictionary) {
		// 验证，dicCode唯一
		List<SysDictionary> checkList = sysDictionaryMapper.selectSysDictionaryByCode(sysDictionary);
		if (checkList.size() > 0) {
			return -1;
		}
		sysDictionary.setDicUid("dic"+UUID.randomUUID().toString());
		return sysDictionaryMapper.save(sysDictionary);
	}

	@Override
	public int delete(String dicUid) {
		return sysDictionaryMapper.delete(dicUid);
	}

	@Override
	public int update(SysDictionary sysDictionary) {
		// 验证，dicCode唯一
		List<SysDictionary> checkList = sysDictionaryMapper.selectSysDictionaryByCode(sysDictionary);
		SysDictionary checkSysDictionary = sysDictionaryMapper.selectSysDictionaryById(sysDictionary.getDicUid());
		if (checkSysDictionary.getDicCode().equals(sysDictionary.getDicCode())) {
			return sysDictionaryMapper.update(sysDictionary);
		}else {
			if (checkList.size() > 0) {
				return -1;
			}else {
				return sysDictionaryMapper.update(sysDictionary);
			}
		}			
	}

	@Override
	public SysDictionary getSysDictionaryById(String dicUid) {
		return sysDictionaryMapper.selectSysDictionaryById(dicUid);
	}

	@Override
	public PagedResult<SysDictionaryData> listSysDictionaryDataById(SysDictionaryData sysDictionaryData, Integer pageNo, Integer pageSize) {
		try {
			pageNo = pageNo == null?1:pageNo;
			pageSize = pageSize == null?10:pageSize;
			PageHelper.startPage(pageNo,pageSize);
			return BeanUtil.toPagedResult(sysDictionaryMapper.selectSysDictionaryDataListById(sysDictionaryData));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SysDictionaryData getSysDictionaryDataById(String dicDataUid) {
		return sysDictionaryMapper.selectSysDictionaryDataById(dicDataUid);
	}

	@Override
	public int saveSysDictionaryData(SysDictionaryData sysDictionaryData) {
		// 验证，dicDataCode唯一
		List<SysDictionaryData> checkList = sysDictionaryMapper.selectSysDictionaryDataByCode(sysDictionaryData);
		if (checkList.size() > 0) {
			return -1;
		}
		sysDictionaryData.setDicDataUid("dic_data"+UUID.randomUUID().toString());
		return sysDictionaryMapper.saveSysDictionaryData(sysDictionaryData);
	}

	@Override
	public int deleteSysDictionaryData(String dicDataUid) {
		return sysDictionaryMapper.deleteSysDictionaryData(dicDataUid);
	}

	@Override
	public int updateSysDictionaryData(SysDictionaryData sysDictionaryData) {
		// 验证，dicDataCode唯一
		List<SysDictionaryData> checkList = sysDictionaryMapper.selectSysDictionaryDataByCode(sysDictionaryData);
		SysDictionaryData checkSysDictionaryData = sysDictionaryMapper.selectSysDictionaryDataById(sysDictionaryData.getDicDataUid());
		if (checkSysDictionaryData.getDicDataCode().equals(sysDictionaryData.getDicDataCode())) {
			return sysDictionaryMapper.updateSysDictionaryData(sysDictionaryData);
		}else {
			if (checkList.size() > 0) {
				return -1;
			}else {
				return sysDictionaryMapper.updateSysDictionaryData(sysDictionaryData);
			}
		}	
	}
	
}
