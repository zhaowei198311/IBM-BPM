/**
 * 
 */
package com.desmart.desmartsystem.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.common.EntityIdPrefix;
import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.dao.DhInterfaceMapper;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.service.DhInterfaceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * Title: DhInterfaceServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author shenlan
 * @date 2018年4月12日
 */
@Service
public class DhInterfaceServiceImpl implements DhInterfaceService {

	private static final Logger LOG = LoggerFactory.getLogger(DhInterfaceServiceImpl.class);

	@Autowired
	private DhInterfaceMapper dhInterfaceMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#listDhInterface()
	 */
	@Override
	public ServerResponse<PageInfo<List<DhInterface>>> listDhInterface(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DhInterface> interfacelist = dhInterfaceMapper.listAll();
		if (null == interfacelist || interfacelist.size() == 0) {
			LOG.info("查询接口出错,出错类为{}", DhInterfaceServiceImpl.class);
		}
		PageInfo<List<DhInterface>> pageInfo = new PageInfo(interfacelist);
		return  ServerResponse.createBySuccess(pageInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#saveDhInterface()
	 */
	@Override
	public int saveDhInterface(DhInterface dhInterface) {
		dhInterface.setIntUid(EntityIdPrefix.DH_INTERFACE_META + UUID.randomUUID().toString());
		int resultCount = dhInterfaceMapper.save(dhInterface);
		return resultCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#delDhInterface()
	 */
	@Override
	public void delDhInterface(String Interfaceid) {
		if (Interfaceid != null) {
			dhInterfaceMapper.delete(Interfaceid);
		}else {
			LOG.info("删除接口模块出错，出错类为"+DhInterfaceServiceImpl.class);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#listDhInterfaceById()
	 */
	@Override
	public ServerResponse<PageInfo<List<DhInterface>>> listDhInterfaceByTitle(Map<String, Object> params, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DhInterface> DhInterfaceList = dhInterfaceMapper.selectByCondition(params);
		PageInfo<List<DhInterface>> pageInfo = new PageInfo(DhInterfaceList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartsystem.service.DhInterfaceService#updateDhInterface(com.desmart.desmartsystem.entity.DhInterface)
	 */
	@Override
	public int updateDhInterface(DhInterface dhInterface) {
		int result = dhInterfaceMapper.update(dhInterface);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartsystem.service.DhInterfaceService#selectDhInterfaceByid(com.desmart.desmartsystem.entity.DhInterface)
	 */
	@Override
	public DhInterface selectDhInterfaceByid(String intUid) {
		return dhInterfaceMapper.selectByintUid(intUid);
	}
}
