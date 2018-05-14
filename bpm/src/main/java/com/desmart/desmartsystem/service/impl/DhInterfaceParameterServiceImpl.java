/**
 * 
 */
package com.desmart.desmartsystem.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartsystem.common.EntityIdPrefix;
import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.dao.DhInterfaceParameterMapper;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: DhInterfaceParameterImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月17日  
*/
@Service
public class DhInterfaceParameterServiceImpl implements DhInterfaceParameterService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DhInterfaceParameterServiceImpl.class);
	
	@Autowired
	private DhInterfaceParameterMapper dhInterfaceParameterMapper;
	
	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#listDhInterfaceParameter(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServerResponse<PageInfo<List<DhInterfaceParameter>>> listDhInterfaceParameter(String intUid, Integer pageNum,
			Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DhInterfaceParameter> interfaceParameterlist = dhInterfaceParameterMapper.listAll(intUid);
		PageInfo<List<DhInterfaceParameter>> pageInfo = new PageInfo(interfaceParameterlist);
		return ServerResponse.createBySuccess(pageInfo);
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#delDhInterfaceParameter(java.lang.String)
	 */
	@Override
	public void delDhInterfaceParameter(String paraUid) {
		if(paraUid!=null) {
			dhInterfaceParameterMapper.delete(paraUid);
		}
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#saveDhInterfaceParametere(com.desmart.desmartbpm.entity.DhInterfaceParameter)
	 */
	@Override
	public ServerResponse saveDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter) {
		dhInterfaceParameter.setParaUid(EntityIdPrefix.DH_INTERFACE_PARAMETER + UUID.randomUUID().toString());
		return ServerResponse.createBySuccess(dhInterfaceParameterMapper.save(dhInterfaceParameter));
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartsystem.service.DhInterfaceParameterService#updateDhInterfaceParametere(com.desmart.desmartsystem.entity.DhInterfaceParameter)
	 */
	@Override
	public ServerResponse updateDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter) {
		return ServerResponse.createBySuccess(dhInterfaceParameterMapper.update(dhInterfaceParameter));
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartsystem.service.DhInterfaceParameterService#selectByparaUid(java.lang.String)
	 */
	@Override
	public DhInterfaceParameter selectByparaUid(String paraUid) {
		return dhInterfaceParameterMapper.selectByparaUid(paraUid);
	}
	
}
