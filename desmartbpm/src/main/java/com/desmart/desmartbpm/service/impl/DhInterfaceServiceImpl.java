/**
 * 
 */
package com.desmart.desmartbpm.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhInterfaceDao;
import com.desmart.desmartbpm.entity.DhInterface;
import com.desmart.desmartbpm.service.DhInterfaceService;

/**  
* <p>Title: DhInterfaceServiceImpl</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月12日  
*/
@Service
public class DhInterfaceServiceImpl implements DhInterfaceService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DhInterfaceServiceImpl.class);
	
	@Autowired
	private DhInterfaceDao dhInterfaceDao;
	
	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#listDhInterface()
	 */
	@Override
	public List<DhInterface> listDhInterface() {
		List<DhInterface> interfacelist = dhInterfaceDao.listAll();
		if(null == interfacelist || interfacelist.size() == 0) {
			LOG.info("查询所有接口出错,出错类为{}",DhInterfaceServiceImpl.class);
		}
		return interfacelist;
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#saveDhInterface()
	 */
	@Override
	public int saveDhInterface(DhInterface dhInterface) {
		dhInterface.setInterfaceId(EntityIdPrefix.DH_INTERFACE_META + UUID.randomUUID().toString());
	    int resultCount = dhInterfaceDao.save(dhInterface);
		return resultCount;
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhInterfaceService#delDhInterface()
	 */
	@Override
	public void delDhInterface(int Interfaceid) {
		dhInterfaceDao.delete(Interfaceid);
	}

}
