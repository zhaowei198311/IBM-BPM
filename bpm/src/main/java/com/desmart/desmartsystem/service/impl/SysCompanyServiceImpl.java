package com.desmart.desmartsystem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.dao.SysCompanyMapper;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.entity.SysCompany;
import com.desmart.desmartsystem.entity.SysCompany;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.service.SysCompanyService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-05-22
 */
@Service
public class SysCompanyServiceImpl  implements SysCompanyService {

	
	@Autowired
	private SysCompanyMapper sysCompanyMapper;

	@Override
	public int insert(SysCompany entity) throws Exception {
		// TODO Auto-generated method stub
		return sysCompanyMapper.insert(entity);
	}

	@Override
	public int update(SysCompany entity) throws Exception {
		// TODO Auto-generated method stub
		return sysCompanyMapper.update(entity);
	}

	@Override
	public int delete(SysCompany entity) throws Exception {
		// TODO Auto-generated method stub
		return sysCompanyMapper.delete(entity);
	}

	@Override
	public SysCompany select(SysCompany entity) {
		// TODO Auto-generated method stub
		return sysCompanyMapper.select(entity);
	}

	@Override
	public SysCompany findById(SysCompany id) {
		// TODO Auto-generated method stub
		return sysCompanyMapper.findById(id);
	}
	
	public List<SysCompany> selectAll(SysCompany entity) {
		return sysCompanyMapper.selectAll(entity);
	}
	

//	@Override
//	public PagedResult<SysCompany> queryByPage(SysCompany entity, Integer pageNo, Integer pageSize) {
//		// TODO Auto-generated method stub
//		pageNo = pageNo == null?1:pageNo;
//		pageSize = pageSize == null?10:pageSize;
//		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
//		return BeanUtil.toPagedResult(sysCompanyMapper.selectAll(entity));
//	}

	
}
