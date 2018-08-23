package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.desmart.common.exception.PermissionException;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.*;
import com.desmart.desmartbpm.service.BpmExposedItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionParticipateType;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class DhProcessMetaServiceImpl implements DhProcessMetaService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessMetaServiceImpl.class);
    
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhProcessMetaMapper dhProcessMetaMapper;
    @Autowired
    private DhProcessCategoryMapper dhProcessCategoryMapper;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhActivityAssignMapper dhActivityAssignMapper;
    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    @Autowired
    private DhActivityRejectMapper dhActivityRejectMapper;
    @Autowired
    private DhObjectPermissionMapper dhObjectPermissionMapper; 
    @Autowired
    private DhProcessDefinitionMapper dhProcessDefinitionMapper;
    @Autowired
    private DhStepMapper dhStepMapper;
    @Autowired
    private BpmFormManageService bpmFormManageService;
    @Autowired
    private DhGatewayLineMapper dhGatewayLineMapper;
    @Autowired
    private DatRuleMapper datRuleMapper;
    @Autowired
    private DatRuleConditionMapper datRuleConditionMapper;
    @Autowired
    private BpmExposedItemMapper bpmExposedItemMapper;
    @Autowired
    private DhObjectPermissionService dhObjectPermissionService;

    
    public ServerResponse getUnSynchronizedProcessMeta(Integer pageNum, Integer pageSize, String processAppName,
                                                       String processAppAcronym, String display) {
        List<Map<String, String>> maps = bpmExposedItemMapper.listUnSynchronizedProcessMeta();
        PageInfo<BpmExposedItem> pageInfo = null;
        if (maps.isEmpty()) {
            pageInfo = new PageInfo<>(new ArrayList());
            pageInfo.setPageNum(1);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotal(0);
        } else {
            PageHelper.startPage(pageNum, pageSize);
            List<BpmExposedItem> exposedItems = bpmExposedItemMapper.listByProAppIdAndBpdId(maps);
            pageInfo = new PageInfo(exposedItems);
        }
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse<PageInfo<List<DhProcessMeta>>> listDhProcessMetaByCategoryList(List<DhProcessCategory> categoryList, String proName, 
            Integer pageNum, Integer pageSize) {
        if (categoryList == null || categoryList.isEmpty()) {
            return ServerResponse.createByErrorMessage("列表参数错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        
        // 获取所有的子分类
        List<DhProcessMeta> metalist = dhProcessMetaMapper.listByCategoryListAndProName(categoryList, proName);
        PageInfo<List<DhProcessMeta>> pageInfo = new PageInfo(metalist);
        return ServerResponse.createBySuccess(pageInfo);
    }
    
    
    public ServerResponse createDhProcessMeta(DhProcessMeta dhProcessMeta) {
        if (StringUtils.isBlank(dhProcessMeta.getCategoryUid()) || StringUtils.isBlank(dhProcessMeta.getProAppId())
                || StringUtils.isBlank(dhProcessMeta.getProUid()) || StringUtils.isBlank(dhProcessMeta.getProName())) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        
        DhProcessCategory category = dhProcessCategoryMapper.queryByCategoryUid(dhProcessMeta.getCategoryUid());
        if (category == null) {
            return ServerResponse.createByErrorMessage("此分类不存在");
        }
        
        int countRow = dhProcessMetaMapper.countByProAppIdAndProUid(dhProcessMeta.getProAppId(), dhProcessMeta.getProUid());
        if (countRow > 0) {
            return ServerResponse.createByErrorMessage("流程元数据已经设置了分类，不能重复配置");
        }
        if (dhProcessMeta.getProName().length() > 50) {
            return ServerResponse.createByErrorMessage("流程名过长");
        }
        
        // 查找分类下有没有重名的元数据
        DhProcessMeta metaSelective = new DhProcessMeta();
        metaSelective.setProName(dhProcessMeta.getProName());
        //metaSelective.setCategoryUid(dhProcessMeta.getCategoryUid());
        List<DhProcessMeta> checkList = dhProcessMetaMapper.queryByProName(metaSelective);
        if (checkList!=null&&checkList.size() > 0) {
            return ServerResponse.createByErrorMessage("存在同名的元数据，请重新命名");
        }
        
        dhProcessMeta.setProMetaUid(EntityIdPrefix.DH_PROCESS_META + UUID.randomUUID().toString());
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhProcessMeta.setCreator(currUser);
        dhProcessMeta.setProMetaStatus(DhProcessMeta.STATUS_ON);
        countRow = dhProcessMetaMapper.save(dhProcessMeta);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("添加失败");
        }
        
    }
    
    public ServerResponse updateDhProcessMeta(String metaUid, String newName, String proNo) {
        if (StringUtils.isBlank(metaUid) || StringUtils.isBlank(newName) || StringUtils.isBlank(proNo)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        newName = newName.trim();
        
        DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(metaUid);
        if (dhProcessMeta == null) {
            return ServerResponse.createByErrorMessage("此流程元数据不存在");
        } 
        
        if (newName.equals(dhProcessMeta.getProName()) && StringUtils.equals(dhProcessMeta.getProNo(), proNo)) {
            return ServerResponse.createBySuccess();
        }
        
        DhProcessMeta metaSelective = new DhProcessMeta();
        metaSelective.setProName(newName);
        // metaSelective.setCategoryUid(dhProcessMeta.getCategoryUid());
        // 指定元数据名的元数据
        List<DhProcessMeta> metaListExists = dhProcessMetaMapper.queryByProName(metaSelective);
        for (DhProcessMeta metaListExist : metaListExists) {
            // 如果库中的元数据主键与当前元数据不同，说明有同名的元数据存在
            if (!metaListExist.getProMetaUid().equals(metaUid)) {
                return ServerResponse.createByErrorMessage("存在同名的元数据，请重新命名");
            }
        }

        String updator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhProcessMeta.setUpdateUser(updator);
        dhProcessMeta.setProName(newName.trim());
        dhProcessMeta.setProNo(proNo.trim());
        int countRow = dhProcessMetaMapper.updateByProMetaUidSelective(dhProcessMeta);
        
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("修改失败");
        }
    }
    
    @Transactional
    public ServerResponse removeProcessMeta(String uid) {
        DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(uid);
        if (dhProcessMeta == null) {
            throw new PlatformException("找不到指定的元数据");
        }
        String proUid = dhProcessMeta.getProUid();
        String proAppId = dhProcessMeta.getProAppId();
//        String proName = dhProcessMeta.getProName();
        
        // BPM_ACTIVITY_META
        BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
        bpmActivityMeta.setBpdId(proUid);
        bpmActivityMeta.setProAppId(proAppId);
        List<BpmActivityMeta> bpmActivityMetaList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(bpmActivityMeta);
        // ACTIVITY_ID集合
        List<String> activityIds = new ArrayList<>();
        for (BpmActivityMeta bam : bpmActivityMetaList) {
			activityIds.add(bam.getActivityId());
		} 
        if (!activityIds.isEmpty()) {
        	// 根据ACTIVITY_ID集合批量删除DH_ACTIVITY_ASSIGN表
            dhActivityAssignMapper.deleteByActivityIds(activityIds);
            // 根据ACTIVITY_ID集合批量删除DH_ACTIVITY_CONF表
            dhActivityConfMapper.deleteByActivityIds(activityIds);
            // 根据ACTIVITY_ID集合批量删除DH_ACTIVITY_REJECT表
            dhActivityRejectMapper.deleteByActivityIds(activityIds);
		}
        
        // 根据PRO_UID,PRO_APP_ID删除DH_OBJECT_PERMISSION
        DhObjectPermission dhObjectPermission = new DhObjectPermission();
        dhObjectPermission.setProUid(proUid);
        dhObjectPermission.setProAppId(proAppId);
        dhObjectPermissionMapper.delectByDhObjectPermissionSelective(dhObjectPermission);
        // 根据PRO_UID,PRO_APP_ID删除DH_PROCESS_DEFINITION
        DhProcessDefinition dhProcessDefinition = new DhProcessDefinition();
        dhProcessDefinition.setProUid(proUid);
        dhProcessDefinition.setProAppId(proAppId);
        dhProcessDefinitionMapper.deleteBySelective(dhProcessDefinition);
        // 
        DhStep dhStep = new DhStep();
        dhStep.setProUid(proUid);
        dhStep.setProAppId(proAppId);
        List<DhStep> dhStepList = dhStepMapper.queryDhStepByProUidAndProAppId(dhStep);

        // formId集合
        String[] forms = new String[dhStepList.size()];
        for (int j = 0; j < forms.length; j++) {
    		forms[j] = dhStepList.get(j).getStepObjectUid();	
		}
        // 批量删除表单
        bpmFormManageService.deleteForm(forms);
        // 根据PRO_UID,PRO_APP_ID删除DH_STEP
        dhStepMapper.deleteBySelective(dhStep);
        // 根据PRO_UID,PRO_APP_ID删除BPM_ACTIVITY_META
        bpmActivityMetaMapper.deleteByIds(bpmActivityMeta);
        
        // 删除流程网关信息
        if (!activityIds.isEmpty()) {
	        List<DhGatewayLine> dhGatewayLineList = dhGatewayLineMapper.listByActivityIds(activityIds);
	        List<String> ruleIds = new ArrayList<>();
	        for (DhGatewayLine dhGatewayLine : dhGatewayLineList) {
	        	if (dhGatewayLine.getRuleId() != null) {
	        		ruleIds.add(dhGatewayLine.getRuleId());
				}
			}
	        if (!ruleIds.isEmpty()) {
	        	datRuleMapper.deleteByRuleIds(ruleIds);
	        	datRuleConditionMapper.deleteByRuleIds(ruleIds);
			}       
        
			dhGatewayLineMapper.deleteByActivityIds(activityIds);
		}
        
        // DH_PROCESS_META
        dhProcessMetaMapper.removeByProMetaUid(uid);

        return ServerResponse.createBySuccess();

    }

    @Override
    public List<DhProcessMeta> listAll() {
        return dhProcessMetaMapper.listAll();
    }

    @Override
    public List<DhProcessMeta> searchByProName(String proName) {
        DhProcessMeta selective = new DhProcessMeta();
        selective.setProName(proName);
        return dhProcessMetaMapper.listByDhProcessMetaSelective(selective);
    }

	@Override
	public List<DhProcessMeta> searchByCategory(String categoryUid) {
		return dhProcessMetaMapper.listByCategoryUid(categoryUid);
	}

	@Override
    public DhProcessMeta getByProAppIdAndProUid(String proAppId, String proUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid)) {
            return null;
        }
        DhProcessMeta processMetaSelective = new DhProcessMeta();
        processMetaSelective.setProAppId(proAppId);
        processMetaSelective.setProUid(proUid);
        List<DhProcessMeta> processMetaList = dhProcessMetaMapper.listByDhProcessMetaSelective(processMetaSelective);
        if (processMetaList.isEmpty()) {
            return null;
        }
        if (processMetaList.size() > 1) {
            throw new PlatformException("流程元数据重复");
        }
        return processMetaList.get(0);
    }

	@Override
	public ServerResponse updateDhProcessMetaPower(DhProcessMeta dhProcessMeta) {
		if(StringUtils.isBlank(dhProcessMeta.getProAppId())||StringUtils.isBlank(dhProcessMeta.getProUid())) {
			return ServerResponse.createByErrorMessage("参数异常");
		}
		DhProcessMeta checkDhProcessMeta = this.getByProAppIdAndProUid(dhProcessMeta.getProAppId(), dhProcessMeta.getProUid());
		if(checkDhProcessMeta==null) {
			return ServerResponse.createByErrorMessage("流程元数据不存在");
		}
		 // 人员权限
        String permissionUser = dhProcessMeta.getPermissionUser();
        ServerResponse response = dhObjectPermissionService.updatePermissionOfMeta(dhProcessMeta, permissionUser, 
                DhObjectPermissionParticipateType.USER.getCode(), DhObjectPermissionAction.READ.getCode());
        if (!response.isSuccess()) {
            throw new PermissionException(response.getMsg());
        }
       
        // 角色权限
        String permissionRole = dhProcessMeta.getPermissionRole();
        response = dhObjectPermissionService.updatePermissionOfMeta(dhProcessMeta, permissionRole, 
                DhObjectPermissionParticipateType.ROLE.getCode(), DhObjectPermissionAction.READ.getCode());
        if (!response.isSuccess()) {
            throw new PermissionException(response.getMsg());
        }
        
        // 角色组权限
        String permissionTeam = dhProcessMeta.getPermissionTeam();
        response = dhObjectPermissionService.updatePermissionOfMeta(dhProcessMeta, permissionTeam, 
                DhObjectPermissionParticipateType.TEAM.getCode(), DhObjectPermissionAction.READ.getCode());
        if (!response.isSuccess()) {
            throw new PermissionException(response.getMsg());
        }
        return ServerResponse.createBySuccessMessage("更新流程元数据权限成功");
	}

}
