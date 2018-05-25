package com.desmart.desmartsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.dao.SysDictTpMapper;
import com.desmart.desmartsystem.entity.SysDictTp;
import com.desmart.desmartsystem.service.SysDictTpService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-05-24
 */
@Service
public class SysDictTpServiceImpl implements SysDictTpService {
	@Autowired
	SysDictTpMapper sysDictTpMapper;
	
	@Override
	public int insert(SysDictTp entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDictTpMapper.insert(entity);
	}

	@Override
	public int update(SysDictTp entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDictTpMapper.update(entity);
	} 

	@Override
	public int delete(SysDictTp entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDictTpMapper.delete(entity);
	}

	@Override 
	public SysDictTp select(SysDictTp entity) {
		// TODO Auto-generated method stub
		return sysDictTpMapper.select(entity);
	}

	@Override
	public SysDictTp findById(SysDictTp id) {
		// TODO Auto-generated method stub
		return sysDictTpMapper.findById(id);
	}


	@Override
	public PagedResult<SysDictTp> queryByPage(SysDictTp entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysDictTpMapper.selectAll(entity));
	}
}
