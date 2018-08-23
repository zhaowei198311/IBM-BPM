package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionObjType;
import com.desmart.desmartbpm.enums.DhObjectPermissionParticipateType;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartsystem.dao.SysRoleMapper;
import com.desmart.desmartsystem.dao.SysTeamMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysRole;
import com.desmart.desmartsystem.entity.SysTeam;
import com.desmart.desmartsystem.entity.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class DhObjectPermissionServiceImpl implements DhObjectPermissionService {
    private static final Logger LOG = LoggerFactory.getLogger(DhObjectPermissionServiceImpl.class);
    
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private DhObjectPermissionMapper dhObjectPermissionMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysTeamMapper sysTeamMapper;
    @Autowired
    private DhProcessDefinitionMapper dhProcessDefinitionMapper;
    
    @Transactional
	@Override
    public ServerResponse updatePermissionOfProcess(DhProcessDefinition definition, String ids, String participateType, String action) {
        if (StringUtils.isBlank(action) || definition == null || DhObjectPermissionParticipateType.codeOf(participateType) == null
                || DhObjectPermissionAction.codeOf(action) == null || StringUtils.isBlank(definition.getProAppId())
                || StringUtils.isBlank(definition.getProUid()) || StringUtils.isBlank(definition.getProVerUid())) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        // 先把现有的权限删除
        DhObjectPermission selective = new DhObjectPermission();
        selective.setProAppId(definition.getProAppId());
        selective.setProUid(definition.getProUid());
        selective.setProVerUid(definition.getProVerUid());
        selective.setOpObjType(DhObjectPermissionObjType.PROCESS.getCode());
        selective.setOpAction(action);
        selective.setOpParticipateType(participateType);
        int countRow = dhObjectPermissionMapper.delectByDhObjectPermissionSelective(selective);
        
        if (StringUtils.isBlank(ids)) {
            return ServerResponse.createBySuccess();
        }
        
        String[] objIdArr = ids.split(";");
        List<DhObjectPermission> permissionList = new ArrayList<>();
        
        for (String objId : objIdArr) {
            DhObjectPermission permission = new DhObjectPermission();
            
            if (DhObjectPermissionParticipateType.USER.getCode().equals(participateType)) {
                SysUser sysUser = new SysUser();
                sysUser.setUserUid(objId);
                SysUser user = sysUserMapper.findById(sysUser);
                if (user == null) {
                    return ServerResponse.createByErrorMessage("设置权限失败，用户不存在");
                }
                permission.setOpParticipateType(DhObjectPermissionParticipateType.USER.getCode());
            } else if (DhObjectPermissionParticipateType.ROLE.getCode().equals(participateType)) {
                SysRole sysRole = sysRoleMapper.selectByPrimaryKey(objId);
                if (sysRole == null) {
                    return ServerResponse.createByErrorMessage("设置权限失败，角色不存在");
                }
                permission.setOpParticipateType(DhObjectPermissionParticipateType.ROLE.getCode());
            } else if (DhObjectPermissionParticipateType.TEAM.getCode().equals(participateType)) {
                SysTeam sysTeam = sysTeamMapper.selectByPrimaryKey(objId);
                if (sysTeam == null) {
                    return ServerResponse.createByErrorMessage("设置权限失败，角色组不存在");
                }
                permission.setOpParticipateType(DhObjectPermissionParticipateType.TEAM.getCode());
            }
            
            permission.setOpUid(EntityIdPrefix.DH_OBJECT_PERMISSION + UUID.randomUUID().toString());
            permission.setProAppId(definition.getProAppId());
            permission.setProUid(definition.getProUid());
            permission.setProVerUid(definition.getProVerUid());
            permission.setOpObjType(DhObjectPermissionObjType.PROCESS.getCode());
            permission.setOpAction(action);
            permission.setOpParticipateUid(objId);
            permissionList.add(permission);
        }
        countRow = dhObjectPermissionMapper.saveBatch(permissionList);
        if (countRow == permissionList.size()) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("编辑权限失败");
        }
    }

    @Override
    public List<DhObjectPermission> getPermissionListOfStartProcess(String proAppId, String proUid, String proVerUid) {
        DhObjectPermission selective = new DhObjectPermission();
        selective.setProAppId(proAppId);
        selective.setProUid(proUid);
        selective.setProVerUid(proVerUid);
        selective.setOpObjType(DhObjectPermissionObjType.PROCESS.getCode());
        selective.setOpAction(DhObjectPermissionAction.START.getCode());
        return dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
    }

    @Override
    public int deletePermissionListOfStartProcess(String proAppId, String proUid, String proVerUid) {
        DhObjectPermission selective = new DhObjectPermission();
        selective.setProAppId(proAppId);
        selective.setProUid(proUid);
        selective.setProVerUid(proVerUid);
        selective.setOpObjType(DhObjectPermissionObjType.PROCESS.getCode());
        selective.setOpAction(DhObjectPermissionAction.START.getCode());
        return dhObjectPermissionMapper.delectByDhObjectPermissionSelective(selective);
    }


	@Override
    public ServerResponse getPermissionStartOfProcess(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        Map<String, String> map = new HashMap<String, String>();
        DhObjectPermission selective = new DhObjectPermission();
        
        selective.setProAppId(proAppId);
        selective.setProUid(proUid);
        selective.setProVerUid(proVerUid);
        selective.setOpObjType(DhObjectPermissionObjType.PROCESS.getCode());
        selective.setOpAction(DhObjectPermissionAction.START.getCode());
        // 查人员
        selective.setOpParticipateType(DhObjectPermissionParticipateType.USER.getCode());
        List<DhObjectPermission> permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
        String userStr = "";
        String userViewStr = "";
        if (permissionList.size() > 0) {
            List<String> idList = getIdListByPermissionList(permissionList);
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList); 
            for (SysUser user : userList) {
                userStr += user.getUserUid() + ";";
                userViewStr += user.getUserName() + ";";
            }
        }
        map.put("permissionStartUser", userStr);
        map.put("permissionStartUserView", userViewStr);
        
        // 查角色
        selective.setOpParticipateType(DhObjectPermissionParticipateType.ROLE.getCode());
        permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
        String roleStr = "";
        String roleViewStr = "";
        if (permissionList.size() > 0) {
            List<String> idList = getIdListByPermissionList(permissionList);
            List<SysRole> roleList = sysRoleMapper.listByPrimaryKeyList(idList); 
            for (SysRole role : roleList) {
                roleStr += role.getRoleUid() + ";";
                roleViewStr += role.getRoleName() + ";";
            }
        }
        map.put("permissionStartRole", roleStr);
        map.put("permissionStartRoleView", roleViewStr);
        
        // 查角色组
        selective.setOpParticipateType(DhObjectPermissionParticipateType.TEAM.getCode());
        permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
        String teamStr = "";
        String teamViewStr = "";
        if (permissionList.size() > 0) {
            List<String> idList = getIdListByPermissionList(permissionList);
            List<SysTeam> sysTeamList = sysTeamMapper.listByPrimaryKeyList(idList); 
            for (SysTeam team : sysTeamList) {
                teamStr += team.getTeamUid() + ";";
                teamViewStr += team.getTeamName() + ";";
            }
        }
        map.put("permissionStartTeam", teamStr);
        map.put("permissionStartTeamView", teamViewStr);
        
        return ServerResponse.createBySuccess(map);
    }
    
    private List<String> getIdListByPermissionList(List<DhObjectPermission> permissionList) {
        List<String> idList = new ArrayList<>();
        for (DhObjectPermission item : permissionList) {
            idList.add(item.getOpParticipateUid());
        }
        return idList;
    }

	@Override
	public List<DhObjectPermission> getDhObjectPermissionInfo(DhObjectPermission dhObjectPermission) {
		return dhObjectPermissionMapper.listByDhObjectPermissionSelective(dhObjectPermission);
	}

	@Override
	public List<DhObjectPermission> getFieldPermissionByStepUidNotPrint(String stepUid) {
		return dhObjectPermissionMapper.getFieldPermissionByStepUidNotPrint(stepUid);
	}

	@Override
	public DhObjectPermission getFieldPermissionByStepUidAndFldUidNotPrint(String stepUid, String fldUid) {
		return dhObjectPermissionMapper.getFieldPermissionByStepUidAndFldUidNotPrint(stepUid,fldUid);
	}

	@Override
	public DhObjectPermission getFieldPrintPermissionByStepUidAndFldUid(String stepUid, String fldUid) {
		return dhObjectPermissionMapper.getFieldPrintPermissionByStepUidAndFldUid(stepUid,fldUid);
	}

	@Override
    public List<DhObjectPermission> listByStepUidList(List<String> stepUidList) {
        if (stepUidList == null || stepUidList.isEmpty()) {
            return new ArrayList<>();
        }
        return dhObjectPermissionMapper.listByStepUidList(stepUidList);
    }

    @Override
    public int removeByStepUidList(List<String> stepUidList) {
        if (stepUidList == null || stepUidList.isEmpty()) {
            return 0;
        }
        return dhObjectPermissionMapper.removeByStepUidList(stepUidList);
    }

    @Override
    public int removeByProAppIdAndProUidAndProVerUid(String proAppId, String proUid, String proVerUid) {
        DhObjectPermission permissionSelective = new DhObjectPermission();
        permissionSelective.setProAppId(proAppId);
        permissionSelective.setProUid(proUid);
        permissionSelective.setProVerUid(proVerUid);
        return dhObjectPermissionMapper.delectByDhObjectPermissionSelective(permissionSelective);
    }

    @Transactional
	@Override
	public ServerResponse updatePermissionOfMeta(DhProcessMeta dhProcessMeta, String ids, String participateType,
			String action) {
    	if (StringUtils.isBlank(action) || dhProcessMeta == null || DhObjectPermissionParticipateType.codeOf(participateType) == null
                || DhObjectPermissionAction.codeOf(action) == null || StringUtils.isBlank(dhProcessMeta.getProAppId())
                || StringUtils.isBlank(dhProcessMeta.getProUid())) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        // 先把现有的权限删除
        DhObjectPermission selective = new DhObjectPermission();
        selective.setProAppId(dhProcessMeta.getProAppId());
        selective.setProUid(dhProcessMeta.getProUid());
        selective.setOpObjType(DhObjectPermissionObjType.META.getCode());
        selective.setOpAction(action);
        selective.setOpParticipateType(participateType);
        int countRow = dhObjectPermissionMapper.delectByDhObjectPermissionSelective(selective);
        
        if (StringUtils.isBlank(ids)) {
            return ServerResponse.createBySuccess();
        }
        
        String[] objIdArr = ids.split(";");
        List<DhObjectPermission> permissionList = new ArrayList<>();
        
        for (String objId : objIdArr) {
            DhObjectPermission permission = new DhObjectPermission();
            
            if (DhObjectPermissionParticipateType.USER.getCode().equals(participateType)) {
                SysUser sysUser = new SysUser();
                sysUser.setUserUid(objId);
                SysUser user = sysUserMapper.findById(sysUser);
                if (user == null) {
                    return ServerResponse.createByErrorMessage("设置权限失败，用户不存在");
                }
                permission.setOpParticipateType(DhObjectPermissionParticipateType.USER.getCode());
            } else if (DhObjectPermissionParticipateType.ROLE.getCode().equals(participateType)) {
                SysRole sysRole = sysRoleMapper.selectByPrimaryKey(objId);
                if (sysRole == null) {
                    return ServerResponse.createByErrorMessage("设置权限失败，角色不存在");
                }
                permission.setOpParticipateType(DhObjectPermissionParticipateType.ROLE.getCode());
            } else if (DhObjectPermissionParticipateType.TEAM.getCode().equals(participateType)) {
                SysTeam sysTeam = sysTeamMapper.selectByPrimaryKey(objId);
                if (sysTeam == null) {
                    return ServerResponse.createByErrorMessage("设置权限失败，角色组不存在");
                }
                permission.setOpParticipateType(DhObjectPermissionParticipateType.TEAM.getCode());
            }
            
            permission.setOpUid(EntityIdPrefix.DH_OBJECT_PERMISSION + UUID.randomUUID().toString());
            permission.setProAppId(dhProcessMeta.getProAppId());
            permission.setProUid(dhProcessMeta.getProUid());
            permission.setOpObjType(DhObjectPermissionObjType.META.getCode());
            permission.setOpAction(action);
            permission.setOpParticipateUid(objId);
            permission.setOpObjUid(dhProcessMeta.getCompanyCode());//保存权限对应的公司编码
            permissionList.add(permission);
        }
        countRow = dhObjectPermissionMapper.saveBatch(permissionList);
        if (countRow == permissionList.size()) {
            return ServerResponse.createBySuccessMessage("设置权限成功");
        } else {
            return ServerResponse.createByErrorMessage("设置权限失败");
        }
	}

	@Override
	public ServerResponse getPermissionReadOfMeta(String opUid) {
		if (StringUtils.isBlank(opUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        
        DhObjectPermission selective = new DhObjectPermission();
        
        selective.setOpUid(opUid);
        selective.setOpObjType(DhObjectPermissionObjType.META.getCode());
        selective.setOpAction(DhObjectPermissionAction.READ.getCode());
        return this.searcherPermissionView(selective);
	}
	/**
	 * 根据条件查询组装界面显示的权限数据
	 * @param selective
	 * @return
	 */
	public ServerResponse searcherPermissionView(DhObjectPermission selective) {
		Map<String, String> map = new HashMap<String, String>();
		// 查人员
        selective.setOpParticipateType(DhObjectPermissionParticipateType.USER.getCode());
        List<DhObjectPermission> permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
        String userStr = "";
        String userViewStr = "";
        if (permissionList.size() > 0) {
            List<String> idList = getIdListByPermissionList(permissionList);
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList); 
            for (SysUser user : userList) {
                userStr += user.getUserUid() + ";";
                userViewStr += user.getUserName() + ";";
            }
        }
        map.put("permissionUser", userStr);
        map.put("permissionUserView", userViewStr);
        
        // 查角色
        selective.setOpParticipateType(DhObjectPermissionParticipateType.ROLE.getCode());
        permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
        String roleStr = "";
        String roleViewStr = "";
        if (permissionList.size() > 0) {
            List<String> idList = getIdListByPermissionList(permissionList);
            List<SysRole> roleList = sysRoleMapper.listByPrimaryKeyList(idList); 
            for (SysRole role : roleList) {
                roleStr += role.getRoleUid() + ";";
                roleViewStr += role.getRoleName() + ";";
            }
        }
        map.put("permissionRole", roleStr);
        map.put("permissionRoleView", roleViewStr);
        
        // 查角色组
        selective.setOpParticipateType(DhObjectPermissionParticipateType.TEAM.getCode());
        permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelective(selective);
        String teamStr = "";
        String teamViewStr = "";
        if (permissionList.size() > 0) {
            List<String> idList = getIdListByPermissionList(permissionList);
            List<SysTeam> sysTeamList = sysTeamMapper.listByPrimaryKeyList(idList); 
            for (SysTeam team : sysTeamList) {
                teamStr += team.getTeamUid() + ";";
                teamViewStr += team.getTeamName() + ";";
            }
        }
        map.put("permissionTeam", teamStr);
        map.put("permissionTeamView", teamViewStr);
        
        return ServerResponse.createBySuccess(map);
	}

	@Override
	public ServerResponse<PageInfo<List<DhObjectPermission>>> getPermissionReadOfMetaByPage(Integer pageNum,
			Integer pageSize, String proAppId, String proUid) {
		if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
		}
        DhObjectPermission selective = new DhObjectPermission();
        
        selective.setProAppId(proAppId);
        selective.setProUid(proUid);
        selective.setOpObjType(DhObjectPermissionObjType.META.getCode());
        selective.setOpAction(DhObjectPermissionAction.READ.getCode());
        PageHelper.startPage(pageNum, pageSize);
        List<DhObjectPermission> permissionList = dhObjectPermissionMapper.listByDhObjectPermissionSelectiveOfRelation(selective);
        PageInfo<List<DhObjectPermission>> pageInfo = new PageInfo(permissionList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse deleteBatchByPrimaryKeys(List<String> primaryKeys) {
		List<DhObjectPermission> list = new ArrayList<>();
		for (String opUid : primaryKeys) {
			DhObjectPermission selective = new DhObjectPermission();
			selective.setOpUid(opUid);
			list.add(selective);
		}
		if(list!=null&&list.size()>0) {
			dhObjectPermissionMapper.deleteBatchSelective(list);
		}
		return ServerResponse.createBySuccessMessage("删除成功");
	}

}
