/**
 * 
 */
package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhInterfaceParameterDao;
import com.desmart.desmartbpm.entity.DhInterfaceParameter;
import com.desmart.desmartbpm.service.DhInterfaceParameterService;
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
	private DhInterfaceParameterDao dhInterfaceParameterDao;
	
	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#listDhInterfaceParameter(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServerResponse<PageInfo<List<DhInterfaceParameter>>> listDhInterfaceParameter(String intUid, Integer pageNum,
			Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DhInterfaceParameter> interfaceParameterlist = dhInterfaceParameterDao.listAll(intUid);
		PageInfo<List<DhInterfaceParameter>> pageInfo = new PageInfo(interfaceParameterlist);
		return ServerResponse.createBySuccess(pageInfo);
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#delDhInterfaceParameter(java.lang.String)
	 */
	@Override
	public void delDhInterfaceParameter(String paraUid) {
		if(paraUid!=null) {
			dhInterfaceParameterDao.delete(paraUid);
		}
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceParameterService#saveDhInterfaceParametere(com.desmart.desmartbpm.entity.DhInterfaceParameter)
	 */
	@Override
	public int saveDhInterfaceParametere(DhInterfaceParameter dhInterfaceParameter,String intUid) {
		dhInterfaceParameter.setParaUid(EntityIdPrefix.DH_INTERFACE_PARAMETER + UUID.randomUUID().toString());
		dhInterfaceParameter.setIntUid(intUid);
		int result = dhInterfaceParameterDao.save(dhInterfaceParameter);
		return result;
	}
	
}
