package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
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
    
    @Transactional
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
}
