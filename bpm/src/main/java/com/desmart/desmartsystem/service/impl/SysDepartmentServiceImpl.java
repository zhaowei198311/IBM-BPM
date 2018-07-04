package com.desmart.desmartsystem.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.TreeNode;
import com.desmart.desmartsystem.service.SysDepartmentService;
import com.desmart.desmartsystem.util.BeanUtil;
import com.desmart.desmartsystem.util.PagedResult;
import com.github.pagehelper.PageHelper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsf
 * @since 2018-04-09
 */
@Service
public class SysDepartmentServiceImpl   implements SysDepartmentService {

	@Autowired
	public SysDepartmentMapper sysDepartmentDao;
	
	@Override
	public int insert(SysDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(entity.getDepartAdmins())) {
			entity.setDepartAdmins("00000000");
		};
		return sysDepartmentDao.insert(entity);
	}

	@Override
	public int update(SysDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(entity.getDepartAdmins())) {
			entity.setDepartAdmins("00000000");
		};
		return sysDepartmentDao.update(entity);
	}

	@Override
	public int delete(SysDepartment entity) throws Exception {
		// TODO Auto-generated method stub
		return sysDepartmentDao.delete(entity);
	}

	@Override
	public SysDepartment select(SysDepartment entity) {
		// TODO Auto-generated method stub
		return sysDepartmentDao.select(entity);
	}

	@Override
	public SysDepartment findById(SysDepartment id) {
		// TODO Auto-generated method stub
		return sysDepartmentDao.findById(id);
	}

	@Override
	public PagedResult<SysDepartment> queryByPage(SysDepartment entity, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(sysDepartmentDao.selectAll(entity));
	}

	@Override
	public List<TreeNode> selectTree(TreeNode entity) {
		// TODO Auto-generated method stub
		return sysDepartmentDao.selectTree(entity);
	}

	@Override
	public ServerResponse queryDepartByNoAndName(SysDepartment sysDepartment) {
		SysDepartment depart = sysDepartmentDao.queryDepartByNoAndName(sysDepartment);
		if(null == depart) {
			throw new PlatformException("未找到指定的部门对象");
		}
		return ServerResponse.createBySuccess(depart);
	}

	@Override
	public ServerResponse<List<SysDepartment>> queryDepartParentsByDepartId(String departUid) {
		List<SysDepartment> parentDepartList = new ArrayList<>();
		parentDepartList = recurrenceQueryParentDeparts(departUid,parentDepartList);
		return ServerResponse.createBySuccess(parentDepartList);
	}
	
	private List<SysDepartment> recurrenceQueryParentDeparts(String departUid,List<SysDepartment> parentDepartList){
		SysDepartment parentDepart = sysDepartmentDao.queryParentDepartByDepartId(departUid);
		if(null == parentDepart) {
			return parentDepartList;
		}else {
			parentDepartList.add(parentDepart);
			return recurrenceQueryParentDeparts(parentDepart.getDepartUid(),parentDepartList);
		}
	}
}
