/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.DraftsDao;
import com.desmart.desmartportal.entity.Drafts;
import com.desmart.desmartportal.service.DraftsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 草稿箱实现类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月28日  
*/
@Service
public class DraftsServiceImpl implements DraftsService {
	
	@Autowired
	private DraftsDao draftsDao;
	
	private Logger log = Logger.getLogger(DraftsServiceImpl.class);
	
	/**
	 * 根据草稿id 删除草稿箱数据
	 */
	@Override
	public int deleteDraftsBydfsId(String dfsId) {
		log.info("删除草稿箱数据开始...");
		int result = 0;
		try {
			result = draftsDao.deleteBydfsId(dfsId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("删除草稿箱数据结束...");
		return result;
	}

	/**
	 * 查询所有草稿数据
	 */
	@Override
	public ServerResponse<PageInfo<List<Drafts>>> selectDraftsList(Integer pageNum, Integer pageSize) {
		log.info("查询所有草稿数据开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<Drafts> resultList = draftsDao.select();
			if(null == resultList || resultList.size() == 0) {
				log.info("查询所有草稿数据出错,出错类为{}"+DraftsServiceImpl.class);
			}
			PageInfo<List<Drafts>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询所有草稿数据结束...");
		return null;
	}

	/**
	 * 根据草稿名称查询 草稿数据
	 */
	@Override
	public ServerResponse<PageInfo<List<Drafts>>> selectDraftsBydfsTitle(String title, Integer pageNum, Integer pageSize) {
		log.info("根据草稿名称查询草稿数据开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<Drafts> resultList = draftsDao.selectBydfsTitle(title);
			PageInfo<List<Drafts>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据草稿名称查询草稿数据结束...");
		return null;
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartportal.service.DraftsService#saveDrafts(com.desmart.desmartportal.entity.Drafts)
	 */
	@Override
	public int saveDrafts(Drafts drafts) {
		drafts.setDfsId(EntityIdPrefix.DH_DRAFTS_META+ UUID.randomUUID().toString());
		return draftsDao.save(drafts);
	}

	/**
	 * 根据dfsId查询draft信息
	 */
	@Override
	public ServerResponse<PageInfo<List<Drafts>>> selectBydfsId(String dfsId) {
		log.info("根据草稿dfsid查询草稿数据开始...");
		try {
			PageHelper.startPage(1, 10);
			List<Drafts> resultList = draftsDao.selectBydfsId(dfsId);
			PageInfo<List<Drafts>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据草稿dfsid查询草稿数据结束...");
		return null;
	}
	
}
