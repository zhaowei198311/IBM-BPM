package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 触发器服务
 */
@Service
public class DhTriggerServiceImpl implements DhTriggerService {
    private static final Logger LOG = LoggerFactory.getLogger(DhTriggerServiceImpl.class);

    @Autowired
    private DhTriggerMapper dhTriggerMapper;


    @Override
    public ServerResponse searchTrigger(DhTrigger dhTrigger, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("update_time desc");
        List<DhTrigger> list = dhTriggerMapper.searchBySelective(dhTrigger);
        PageInfo pageInfo = new PageInfo(list);
        return ServerResponse.createBySuccess(pageInfo);
    }


	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhTriggerService#deleteTrigger(java.lang.String)
	 */
	@Override
	public int deleteTrigger(String triUid) {
		return dhTriggerMapper.delete(triUid);
	}


	/* (non-Javadoc)
	 * @see com.desmart.desmartbpm.service.DhTriggerService#saveTrigger(com.desmart.desmartbpm.entity.DhTrigger)
	 */
	@Override
	public int saveTrigger(DhTrigger dhTrigger) {
		// 触发器id
		dhTrigger.setTriUid(EntityIdPrefix.DH_TRIGGER + UUID.randomUUID().toString());
    	// 获得当前登录用户的身份
    	String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
    	dhTrigger.setCreator(creator);
    	dhTrigger.setUpdator(creator);
    	// 保存数据
		return dhTriggerMapper.save(dhTrigger);
	}
}