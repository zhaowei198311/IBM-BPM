/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.entity.DhTaskInstance;
import org.apache.commons.lang3.StringUtils;
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
    private Logger logger = Logger.getLogger(DhDraftsServiceImpl.class);

	@Autowired
	private DhDraftsMapper dhDraftsMapper;
	@Autowired
	private DhProcessFormService dhProcessFormService;
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	@Autowired
    private DhProcessInstanceMapper dhProcessInstanceMapper;
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	@Autowired
    private DhTaskInstanceMapper dhTaskInstanceMapper;
	

	
	/**
	 * 根据草稿id 删除草稿箱数据
	 */
	@Override
	public int deleteDraftsBydfsId(String dfsId) {
		logger.info("删除草稿箱数据开始...");
		int result = 0;
		try {
			result = dhDraftsMapper.deleteBydfsId(dfsId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("删除草稿箱数据结束...");
		return result;
	}

	/**
	 * 查询所有草稿数据
	 */
	@Override
	public ServerResponse<PageInfo<List<DhDrafts>>> selectDraftsList(Integer pageNum, Integer pageSize, String insTitle, String proName) {
		try {
			DhDrafts draftSelective = new DhDrafts();
			String dfsCreator = getCurrentUserUid();
			draftSelective.setDfsCreator(dfsCreator);
			draftSelective.setDfsTitle(insTitle);
			draftSelective.setProName(proName);
			PageHelper.startPage(pageNum, pageSize);
			PageHelper.orderBy("DFS_CREATEDATE desc");
			List<DhDrafts> resultList = dhDraftsMapper.listDraftsToShow(draftSelective);
			PageInfo<List<DhDrafts>> pageInfo = new PageInfo(resultList);
			for (DhDrafts draft : resultList) {
				draft.setDfsData(null);
			}
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.desmart.desmartportal.service.DraftsService#saveDrafts(com.desmart.desmartportal.entity.Drafts)
	 */
	@Override
	public int saveDrafts(DhDrafts drafts) {
		if(dhDraftsMapper.queryDraftsByInsUid(drafts.getInsUid())!=null) {
			return dhDraftsMapper.updateByPrimaryKeySelective(drafts);
		}else {
			String creator = getCurrentUserUid();
			if(creator!=null) {
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
		logger.info("根据草稿dfsid查询草稿数据开始...");
		try {
			return dhDraftsMapper.selectBydfsId(dfsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("根据草稿dfsid查询草稿数据结束...");
		return null;
	}

	@Override
	public Map<String, Object> selectDraftsAndFromInfo(String dfsId, String insUid) {
		logger.info("根据草稿id和流程id查询数据开始...");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 查询草稿数据
			DhDrafts drafts = dhDraftsMapper.selectBydfsId(dfsId);
			resultMap.put("drafts", drafts);
			// 表单详细信息设置
			DhProcessInstance dhProcessInstance = (DhProcessInstance) dhProcessInstanceService.selectByPrimaryKey(insUid).getData();
			Map<String, Object> formMap = dhProcessFormService.queryProcessForm(dhProcessInstance.getProAppId(), dhProcessInstance.getProUid(), dhProcessInstance.getProVerUid());
			resultMap.put("formMap", formMap);
			logger.info("根据草稿id和流程id查询数据结束...");
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询异常");
			return null;
		}
	}

	@Override
	public ServerResponse saveIfNotExists(DhDrafts dhDraft) {
        String insUid = dhDraft.getInsUid(); // 流程实例主键
        if (StringUtils.isBlank(insUid)) {
            return ServerResponse.createByErrorMessage("保存草稿失败，缺少流程实例主键信息");
        }
        DhDrafts draftInDb = dhDraftsMapper.queryDraftsByInsUid(insUid);
        if (draftInDb != null) {
            return ServerResponse.createBySuccess();
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (dhProcessInstance == null || (dhProcessInstance.getInsStatusId() != DhProcessInstance.STATUS_ID_DRAFT)) {
            return ServerResponse.createByErrorMessage("保存草稿失败，流程实例状态异常");
        }
        dhDraft.setDfsId(EntityIdPrefix.DH_DRAFTS_META + UUID.randomUUID().toString());
        dhDraft.setDfsCreator(getCurrentUserUid());
        if (StringUtils.isBlank(dhDraft.getDfsTitle())) {
            dhDraft.setDfsTitle("  ");
        }
        dhDraftsMapper.save(dhDraft);
		return ServerResponse.createBySuccess();
	}

	private String getCurrentUserUid() {
        return String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
    }

	@Override
	public ServerResponse saveProcessDraft(DhDrafts dhDraft) {
        String insUid = dhDraft.getInsUid(); // 流程实例主键
        if (StringUtils.isBlank(insUid)) {
            return ServerResponse.createByErrorMessage("保存草稿失败，缺少流程实例主键信息");
        }
        String dfsDataStr = dhDraft.getDfsData();
        try {
            JSON.parseObject(dfsDataStr);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("保存草稿失败，数据异常");
        }

        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (dhProcessInstance == null || (dhProcessInstance.getInsStatusId() != DhProcessInstance.STATUS_ID_DRAFT)) {
            return ServerResponse.createByErrorMessage("保存草稿失败，流程实例状态异常");
        }
        DhDrafts draftInDb = dhDraftsMapper.queryDraftsByInsUid(insUid);
        if (draftInDb != null) {
            draftInDb.setDfsTitle(dhDraft.getDfsTitle());
            draftInDb.setDfsData(dhDraft.getDfsData());
            dhDraftsMapper.updateByPrimaryKeySelective(draftInDb);
        } else {
            dhDraft.setDfsId(EntityIdPrefix.DH_DRAFTS_META + UUID.randomUUID().toString());
            dhDraft.setDfsCreator(getCurrentUserUid());
            if (StringUtils.isBlank(dhDraft.getDfsTitle())) {
                dhDraft.setDfsTitle("  ");
            }
            dhDraftsMapper.save(dhDraft);
        }
        return ServerResponse.createBySuccess();
	}

    @Override
    public ServerResponse saveTaskDraft(DhDrafts dhDraft) {
        String insUid = dhDraft.getInsUid(); // 流程实例主键
        String dfsDataStr = dhDraft.getDfsData();
        try {
            JSON.parseObject(dfsDataStr);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("保存草稿失败，数据异常");
        }
        if (StringUtils.isBlank(insUid)) {
            return ServerResponse.createByErrorMessage("保存草稿失败，缺少流程实例主键信息");
        }
        DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (currProcessInstance == null || (currProcessInstance.getInsStatusId() == DhProcessInstance.STATUS_ID_COMPLETED)) {
            return ServerResponse.createByErrorMessage("保存草稿失败，流程实例状态异常");
        }
        String taskUid = dhDraft.getTaskUid();
        DhTaskInstance currTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
        if (currTaskInstance == null) {
            return ServerResponse.createByErrorMessage("保存失败，当前任务不存在");
        }
        if (!DhTaskInstance.STATUS_RECEIVED.equals(currTaskInstance.getTaskStatus())) {
            return ServerResponse.createByErrorMessage("保存失败，当前任务不是可提交状态");
        }
        dhDraft.setDfsTitle(currProcessInstance.getInsTitle());
        // 根据任务主键获得指定的草稿
        DhDrafts draftInDb = dhDraftsMapper.queryDraftsByTaskUid(taskUid);
        if (draftInDb == null) {
            dhDraft.setDfsId(EntityIdPrefix.DH_DRAFTS_META + String.valueOf(UUID.randomUUID()));
            dhDraft.setDfsCreator(getCurrentUserUid());
            dhDraftsMapper.save(dhDraft);
        } else {
            draftInDb.setDfsData(dhDraft.getDfsData());
            dhDraftsMapper.updateByPrimaryKeySelective(draftInDb);
        }
        return ServerResponse.createBySuccess();
    }

	@Override
	public ServerResponse checkProcessDraftStatus(String dfsId) {
		if (StringUtils.isBlank(dfsId)) {
			return ServerResponse.createByErrorMessage("缺少必要的参数");
		}
        DhDrafts dhDraft = dhDraftsMapper.selectBydfsId(dfsId);
		if (dhDraft == null) {
            return ServerResponse.createByErrorMessage("此草稿数据不存在");
        }
        if (dhDraft.getTaskUid() != null) {
            return ServerResponse.createByErrorMessage("此草稿不是起草的草稿");
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(dhDraft.getInsUid());
		if (dhProcessInstance == null || dhProcessInstance.getInsStatusId() != DhProcessInstance.STATUS_ID_DRAFT) {
            return ServerResponse.createByErrorMessage("草稿状态异常");
        }
        // 草稿对应的流程定义是否是启用的版本
        DhProcessDefinition startAbleProcessDefinition = dhProcessDefinitionService.getStartAbleProcessDefinition(dhProcessInstance.getProAppId(),
                dhProcessInstance.getProUid());
		if (!startAbleProcessDefinition.getProVerUid().equals(dhProcessInstance.getProVerUid())) {
            return ServerResponse.createByErrorMessage("此草稿对应的流程版本已过时，请重新起草");
        }
        // 查看用户是否有发起此流程的权限
        if (!dhProcessInstanceService.checkPermissionStart(startAbleProcessDefinition)) {
            return ServerResponse.createByErrorMessage("您没有发起该流程的权限");
        }
        return ServerResponse.createBySuccess(dhDraft);
	}




}
