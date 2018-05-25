/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.service.DhDraftsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 草稿箱实现类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月28日  
*/
@Service
public class DhDraftsServiceImpl implements DhDraftsService {
	
	@Autowired
	private DhDraftsMapper dhDraftsMapper;
	
	private Logger log = Logger.getLogger(DhDraftsServiceImpl.class);
	
	/**
	 * 根据草稿id 删除草稿箱数据
	 */
	@Override
	public int deleteDraftsBydfsId(String dfsId) {
		log.info("删除草稿箱数据开始...");
		int result = 0;
		try {
			result = dhDraftsMapper.deleteBydfsId(dfsId);
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
	public ServerResponse<PageInfo<List<DhDrafts>>> selectDraftsList(Integer pageNum, Integer pageSize) {
		log.info("查询所有草稿数据开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhDrafts> resultList = dhDraftsMapper.select();
			if(null == resultList || resultList.size() == 0) {
				log.info("查询所有草稿数据出错,出错类为{}"+DhDraftsServiceImpl.class);
			}
			PageInfo<List<DhDrafts>> pageInfo = new PageInfo(resultList);
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
	public ServerResponse<PageInfo<List<DhDrafts>>> selectDraftsBydfsTitle(String title, Integer pageNum, Integer pageSize) {
		log.info("根据草稿名称查询草稿数据开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhDrafts> resultList = dhDraftsMapper.selectBydfsTitle(title);
			PageInfo<List<DhDrafts>> pageInfo = new PageInfo(resultList);
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
	public int saveDrafts(DhDrafts drafts) {
		drafts.setDfsId(EntityIdPrefix.DH_DRAFTS_META+ UUID.randomUUID().toString());
		return dhDraftsMapper.save(drafts);
	}

	/**
	 * 根据dfsId查询draft信息
	 */
	@Override
	public DhDrafts selectBydfsId(String dfsId) {
		log.info("根据草稿dfsid查询草稿数据开始...");
		try {
			return dhDraftsMapper.selectBydfsId(dfsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据草稿dfsid查询草稿数据结束...");
		return null;
	}
	
}
