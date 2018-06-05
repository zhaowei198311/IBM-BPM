/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhDraftsMapper;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhDraftsService;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhProcessInstanceService;
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
	
	@Autowired
	private DhProcessFormService dhProcessFormService;
	
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	
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
		if(dhDraftsMapper.queryDraftsByInsUid(drafts.getInsUid())!=null) {
			return dhDraftsMapper.updateByInsUid(drafts);
		}else {
			String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
			if(creator==null) {
			drafts.setDfsCreator(creator);
			drafts.setDfsId(EntityIdPrefix.DH_DRAFTS_META+ UUID.randomUUID().toString());
			return dhDraftsMapper.save(drafts);
			}else {
				return 0;
			}
		}
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

	@Override
	public Map<String, Object> selectDraftsAndFromInfo(String dfsId, String insUid) {
		log.info("根据草稿id和流程id查询数据开始...");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 查询草稿数据
			DhDrafts drafts = dhDraftsMapper.selectBydfsId(dfsId);
			resultMap.put("drafts", drafts);
			// 表单详细信息设置
			DhProcessInstance dhProcessInstance = (DhProcessInstance) dhProcessInstanceService.selectByPrimaryKey(insUid).getData();
			Map<String, Object> formMap = dhProcessFormService.queryProcessForm(dhProcessInstance.getProAppId(), dhProcessInstance.getProUid(), dhProcessInstance.getProVerUid());
			resultMap.put("formMap", formMap);
			log.info("根据草稿id和流程id查询数据结束...");	
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("查询异常");
			return null;
		}
	}
	
}
