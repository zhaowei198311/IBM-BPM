package com.desmart.desmartsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.dao.SysDictCdMapper;
import com.desmart.desmartsystem.entity.SysDictCd;
import com.desmart.desmartsystem.service.SysDictCdService;
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
public class SysDictCdServiceImpl implements SysDictCdService {

	@Autowired
	SysDictCdMapper sysDictCdMapper;
	
	@Override
	public int insert(SysDictCd entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDictCdMapper.insert(entity);
	}

	@Override
	public int update(SysDictCd entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDictCdMapper.update(entity);
	} 

	@Override
	public int delete(SysDictCd entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDictCdMapper.delete(entity);
	}

	@Override 
	public SysDictCd select(SysDictCd entity) {
		// TODO Auto-generated method stub
		return sysDictCdMapper.select(entity);
	}

	@Override
	public SysDictCd findById(SysDictCd id) {
		// TODO Auto-generated method stub
		return sysDictCdMapper.findById(id);
	}


	@Override
	public PagedResult<SysDictCd> queryByPage(SysDictCd entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysDictCdMapper.selectAll(entity));
	}

}
