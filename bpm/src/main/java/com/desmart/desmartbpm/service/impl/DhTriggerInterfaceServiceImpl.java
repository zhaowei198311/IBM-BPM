/**
 * 
 */
package com.desmart.desmartbpm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DhTriggerInterfaceMapper;
import com.desmart.desmartbpm.entity.DhTriggerInterface;
import com.desmart.desmartbpm.service.DhTriggerInterfaceService;

/**  
* <p>Title: DhTriggerInterfaceServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年6月13日  
*/
@Service
public class DhTriggerInterfaceServiceImpl implements DhTriggerInterfaceService {
	
	@Autowired
	private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
	
	@Override
	public ServerResponse insertBatch(List<DhTriggerInterface> tinList) {
		if(tinList!=null) {
			dhTriggerInterfaceMapper.insertBatch(tinList);
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}

	@Override
	public ServerResponse<List<DhTriggerInterface>> selectByTriggerActivity(DhTriggerInterface dhTriggerInterface) {
		if(dhTriggerInterface == null) {
			return ServerResponse.createByErrorMessage("查询对象不能为空");
		}
		List<DhTriggerInterface> resultList = dhTriggerInterfaceMapper.selectByTriggerActivity(dhTriggerInterface);
		return ServerResponse.createBySuccess(resultList);
	}

	@Override
	public ServerResponse updateBatch(List<DhTriggerInterface> tinList) {
		if(tinList == null) {
			return ServerResponse.createByErrorMessage("修改对象集合不能为空");
		}
		dhTriggerInterfaceMapper.updateBatch(tinList);
		return ServerResponse.createBySuccess();
	}
}
