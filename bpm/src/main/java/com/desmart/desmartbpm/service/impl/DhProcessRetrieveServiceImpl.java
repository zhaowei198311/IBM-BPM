package com.desmart.desmartbpm.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.DateUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.dao.DhProcessRetrieveMapper;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhProcessRetrieve;
import com.desmart.desmartbpm.service.DhProcessRetrieveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author liyangsa
 *
 */
@Service
public class DhProcessRetrieveServiceImpl implements DhProcessRetrieveService {
	
	@Autowired
	private DhProcessRetrieveMapper dhProcessRetrieveMapper;
	
	@Autowired
	private DhProcessMetaMapper dhProcessMetaMapper;
	
	@Autowired
	private DhProcessRetrieveMapper dhPorcessRetrieveMapper;
	
	@Override
	public Integer insert(DhProcessRetrieve dhProcessRetrieve) {
		return dhPorcessRetrieveMapper.insert(dhProcessRetrieve);
	}

	@Override
	public Integer delete(DhProcessRetrieve dhProcessRetrieve) {
		return dhPorcessRetrieveMapper.delete(dhProcessRetrieve);
	}

	@Override
	public Integer update(DhProcessRetrieve dhProcessRetrieve) {
		return dhPorcessRetrieveMapper.update(dhProcessRetrieve);
	}

	@Override
	public ServerResponse queryProcessRetrieve(String metaUid, Integer pageNum, Integer pageSize) {
		DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(metaUid);
		DhProcessRetrieve dhProcessRetrieve = new DhProcessRetrieve();
		dhProcessRetrieve.setProAppId(dhProcessMeta.getProAppId());
		dhProcessRetrieve.setProUid(dhProcessMeta.getProUid());
		PageHelper.startPage(pageNum, pageSize);
		List<DhProcessRetrieve> list = dhProcessRetrieveMapper.getDhprocessRetrievesByCondition(dhProcessRetrieve);
		PageInfo<List<DhProcessRetrieve>> pageInfo = new PageInfo(list);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	@Transactional
	public ServerResponse addProcessRetrieve(DhProcessRetrieve dhProcessRetrieve,String metaUid) {
		String currUserUid = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		Date currDate = DateUtil.format(new Date());
		dhProcessRetrieve.setRetrieveUid(EntityIdPrefix.DH_PROCESS_RETRIEVE+UUID.randomUUID().toString());
		dhProcessRetrieve.setCreateUserUid(currUserUid);
		dhProcessRetrieve.setCreateTime(currDate);
		dhProcessRetrieve.setDataSource(DhProcessRetrieve.SOURCE_BY_DICTIONARIES);
		DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(metaUid);
		dhProcessRetrieve.setProAppId(dhProcessMeta.getProAppId());
		dhProcessRetrieve.setProUid(dhProcessMeta.getProUid());
		Integer count = this.insert(dhProcessRetrieve);
		if(count>0) {
			return ServerResponse.createBySuccessMessage("新增成功");
		}else {
			return ServerResponse.createByErrorMessage("新增失败");
		}
	}
	
	
}
